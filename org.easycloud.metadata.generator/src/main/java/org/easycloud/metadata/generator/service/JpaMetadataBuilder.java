package org.easycloud.metadata.generator.service;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Table;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.easycloud.common.utils.AssertUtil;
import org.easycloud.common.utils.StringUtil;
import org.easycloud.metadata.generator.annotation.entity.FieldView;
import org.easycloud.metadata.generator.annotation.entity.FlyEnum;
import org.easycloud.metadata.generator.annotation.entity.FlySearchRelation;
import org.easycloud.metadata.generator.annotation.entity.TableView;
import org.easycloud.metadata.generator.annotation.view.MetaDataView;
import org.easycloud.metadata.generator.define.entity.FieldDefinition;
import org.easycloud.metadata.generator.define.entity.TableDefinition;
import org.easycloud.metadata.service.model.CommonSubTableType;
import org.easycloud.metadata.service.model.Entity;
import org.easycloud.metadata.service.model.FieldDataType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.jpa.mapping.JpaPersistentEntity;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.SimpleAssociationHandler;
import org.springframework.data.mapping.SimplePropertyHandler;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * 从实体Jpa定义中获取元模型定义信息
 * @author xiangwanhong
 *
 */
public class JpaMetadataBuilder {
	private JpaMetamodelMappingContext mappingContext;

	public JpaMetamodelMappingContext getMappingContext() {
		return mappingContext;
	}

	public void setMappingContext(JpaMetamodelMappingContext mappingContext) {
		this.mappingContext = mappingContext;
	}
	
	public void buildAll(){
		for (JpaPersistentEntity<?> jpaEntity : mappingContext.getPersistentEntities()) {
			Entity entityMetaData = buildEntityMetaDataFromJpaEntity(jpaEntity);
		}
	}

	private Entity buildEntityMetaDataFromJpaEntity(JpaPersistentEntity<?> jpaEntity) {
		Class<?> entityClass = jpaEntity.getType();
		
		return null;
	}
	
	private void updateMetaDataTableDefinitions(Entity entity, Class<?> entityClass) {
		String shortName = ClassUtils.getShortName(entityClass.getName());
		entity.setName(shortName);
		MetaDataView metaDataView = entityClass.getAnnotation(MetaDataView.class);
		if (metaDataView != null && metaDataView.tableView() != null) {
			// 有定义表定义
			TableView tableView = metaDataView.tableView();
			entity.setTitle(tableView.title());
			entity.setDescription(tableView.description());
			entity.setLabelField(tableView.labelField());
			entity.setCommonSubTables(tableView.commonSubTables());
			entity.setCreateIndex(tableView.createIndex());
			entity.setIndexName(tableView.indexName());
		}else{
			// 没有配置时，则支持所有通用子表
			entity.setCommonSubTables(CommonSubTableType.values());
		}
		Table table = entityClass.getAnnotation(Table.class);
		if (table != null) {
			entity.setSchema(table.schema());
			entity.setDbName(table.name());
		}
		if (StringUtils.isBlank(entity.getDbName())) {
			entity.setDbName(StringUtil.getHibernateName(entity.getName()));
		}
		if (StringUtils.isNotBlank(entity.getSchema())) {
			entity.setDbName(entity.getSchema() + "." + entity.getDbName());
		}
		if (StringUtils.isBlank(entity.getTitle())) {
			entity.setTitle(entity.getDbName());
		}
		if (StringUtils.isBlank(entity.getLabelField())) {
		}
		if (StringUtils.isBlank(entity.getIndexName())) {
			entity.setIndexName(entity.getName().toLowerCase());
		}
	}
	
	/**
	 * 构建字段基本信息
	 * 
	 * @param property
	 * @param column
	 * @return
	 */
	private FieldDefinition buildFieldDefinition(PersistentProperty<?> property, Column column) {
		FieldView fieldView = property.findAnnotation(FieldView.class);
		String label = (fieldView != null && StringUtils.isNotBlank(fieldView.title())) ? fieldView.title()
				: property.getName();
		String description = (fieldView != null && StringUtils.isNotBlank(fieldView.description()))
				? fieldView.description() : property.getName();
		FieldDataType dataType = (fieldView != null) ? fieldView.dataType() : FieldDataType.NONE;
		FieldDefinition field = new FieldDefinition(label, property.getName());
		field.setDescription(description);
		field.setRequired(column != null && !column.nullable());
		if (column != null && StringUtils.isNotBlank(column.name())) {
			field.setFieldName(column.name());
		} else {
			// 没有指定字段名，自动将属性名转换为数据库字段名
			field.setFieldName(StringUtil.getHibernateName(field.getName()));
		}
		field.setDataType(dataType);
		if (fieldView != null) {
			field.setMinLength(fieldView.minLength());
		}
		return field;
	}

	/**
	 * 更新元模型字段信息
	 * 
	 * @param metaData
	 */
	private void updateMetaDataFieldDefinitions(final EntityMetaData metaData) {
		JpaPersistentEntity<?> model = mappingContext.getPersistentEntity(metaData.getEntityClass());
		AssertUtil.parameterEmpty(model, "udpateMetaDataFields.metaData");
		final Map<String, FieldDefinition> fieldsMap = new LinkedHashMap<>();
		model.doWithProperties(new SimplePropertyHandler() {
			@Override
			public void doWithPersistentProperty(final PersistentProperty<?> property) {
				Column column = property.findAnnotation(Column.class);
				final FieldDefinition field = buildFieldDefinition(property, column);
				// 判断是否扩展枚举类型
				FlyEnum enumView = property.findAnnotation(FlyEnum.class);
				if (enumView != null) {
					// 是扩展枚举类型
					field.setDataType(FieldDataType.FLYENUM);
					String entityName = "", attrName = "";
					if (StringUtils.isBlank(enumView.entityName()) && StringUtils.isBlank(enumView.attrName())) {
						// 在FlyEnum注解中，没有指定引用关系，表示需要新注册枚举类型
						entityName = metaData.getEntityName();
						attrName = field.getName();
						registerEnumType(entityName, attrName, field.getLabel());
					} else if (StringUtils.isNotBlank(enumView.entityName())
							&& StringUtils.isNotBlank(enumView.attrName())) {
						// 表示引用
						entityName = enumView.entityName();
						attrName = enumView.attrName();
					} else {
						Assert.isTrue(false, "实体[]属性[]，数据类型为SysEnum，注解@SysEnum的entityName和attrName的值，要不都为空，要不都不为空.");
					}
					field.setRelationClass(entityName);
					field.setRelationTable(attrName);
				}
				// 判断是否扩展查找关系
				FlySearchRelation searchRelation = property.findAnnotation(FlySearchRelation.class);
				if (searchRelation == null) {
					// 设置字段类型
					FieldAttr fieldAttr = FieldDataType.fromJavaType(property.getType(), column);
					if (FieldDataType.NONE.equals(field.getDataType())) {
						field.setDataType(fieldAttr.getDataType());
					}
					field.setMaxLength((int) fieldAttr.getLength());
					field.setPrecision((int) fieldAttr.getPrecision());
					// 是枚举类型时
					if (FieldDataType.SYSENUM.equals(field.getDataType())) {
						field.setRelationClass(property.getType().getName());
					}
					// 值类型
					if (property.getGetter() != null) {
						field.setType(property.getGetter().getReturnType());
					}
					// 设置
					field.setIdField(property.isIdProperty());
					// 设置getter、setter方法
					Assert.notNull(property.getGetter(),
							"实体[" + metaData.getEntityName() + "]属性[" + field.getName() + "]的Getter.");
					Assert.notNull(property.getSetter(),
							"实体[" + metaData.getEntityName() + "]属性[" + field.getName() + "]的Setter为空.");
					field.setGetter(property.getGetter());
					field.setSetter(property.getSetter());
					// 设置字段取值函数
					field.setGetValueHandler(new DefaultGetFieldValueHandler(field));
					// 设置实体赋值函数
					field.setSetValueHandler(new DefaultSetFieldValueHandler(field));
					field.setParent(metaData);
					// 设置值自动生成器
					GenericGenerator generator = property.findAnnotation(GenericGenerator.class);
					if (generator != null) {
						field.setGenerator(generator);
					}
				} else {
					// 平台扩展的查找关联类型
					field.setType(property.getType());
					AssertUtil.parameterInvalide(ArrayUtils.isEmpty(searchRelation.entityNames()),
							"searchRelation.entityNames()");
					if (searchRelation.entityNames().length == 1) {
						field.setRelationClass(searchRelation.entityNames()[0]);
						field.setRelationTable(searchRelation.entityNames()[0]);
					} else {
						AssertUtil.notSupport("多个关联实体还未支持");
					}
					field.setRequired(!searchRelation.optional());
					field.setDataType(FieldDataType.FLYSEARCHRELATION);
					// 设置显示字段
					field.setLabelField(EntityMetaDataConstants.DEFAULT_LABEL_FIELD_NAME);
					field.setGetter(property.getGetter());
					field.setSetter(property.getSetter());
					field.setGetValueHandler(new FlySearchRelationGetFieldValueHandler(field));
					field.setSetValueHandler(new AssociationSetFieldValueHandler(field));
					field.setParent(metaData);
				}
				fieldsMap.put(field.getName(), field);
			}

			private void registerEnumType(String entityName, String attrName, String title) {
//				EnumType entity = new EnumType();
//				entity.setEntityName(entityName);
//				entity.setAttrName(attrName);
//				entity.setName(title);
//				try {
//					AppUtil.getJpaFlyDataAccessService().saveEntity(entity);
//				} catch (Exception e) {
//					// 数据重复异常
//				}
			}
		});
		model.doWithAssociations(new SimpleAssociationHandler() {

			@Override
			public void doWithAssociation(final Association<? extends PersistentProperty<?>> association) {
				Column column = association.getInverse().findAnnotation(Column.class);
				final FieldDefinition field = buildFieldDefinition(association.getInverse(), column);
				if (association.getInverse().isCollectionLike()) {
					// 子表关系
					field.setType(association.getInverse().getComponentType());
					String relationClass = field.getType().getName();
					field.setRelationClass(relationClass);
					field.setRelationTable(ClassUtil.getClassShortName(relationClass).toLowerCase());
					field.setDataType(FieldDataType.MDRELATION);
					// 设置字段取值函数
					field.setGetValueHandler(new DefaultGetFieldValueHandler(field));

					// 获取子表字段
					OneToMany oneToMany = association.getInverse().findAnnotation(OneToMany.class);
					if (oneToMany != null && StringUtils.isNotBlank(oneToMany.mappedBy())) {
						FieldDefinition tmp = new FieldDefinition();
						tmp.setLabel("临时，读取时更新");
						tmp.setName(oneToMany.mappedBy());
						field.setRelationField(tmp);
					} else {
						AssertUtil.parameterEmpty("OneToMany.mappedBy",
								"主子表关系必须设置实体[" + metaData.getEntityClass().getName() + "]子表属性[" + field.getName()
										+ "]的OneToMany.mappedBy属性.");
					}

				} else {
					// 查找关系
					field.setType(association.getInverse().getType());
					String relationClass = field.getType().getName();
					field.setRelationClass(relationClass);
					field.setRelationTable(ClassUtil.getClassShortName(relationClass).toLowerCase());
					field.setDataType(FieldDataType.SEARCHRELATION);

					FKFieldDefinition fkFieldDefinition = new FKFieldDefinition(metaData);

					ManyToOne manyToOne = association.getInverse().findAnnotation(ManyToOne.class);
					if (manyToOne != null) {
						field.setRequired(!manyToOne.optional());
					}

					JoinColumn joinColumn = association.getInverse().findAnnotation(JoinColumn.class);
					if (joinColumn != null) {
						// 单个字段外键关联
						if (StringUtils.isNotBlank(joinColumn.name())) {
							field.setFieldName(StringUtil.getHibernateName(joinColumn.name()));
						} else {
							field.setFieldName(StringUtil.getHibernateName(field.getName() + "_uid"));
						}
						String[] fkFields = new String[] { field.getName() };
						String relationField = joinColumn.referencedColumnName();
						if (StringUtils.isBlank(relationField)) {
							relationField = "uid";
						}
						String[] relationFields = new String[] { relationField };
						fkFieldDefinition.setFkFields(fkFields);
						fkFieldDefinition.setRelationFields(relationFields);
					} else {
						// 多个字段外键关联
						JoinColumns joinColumns = association.getInverse().findAnnotation(JoinColumns.class);
						if (joinColumns != null) {
							// 复合外键关联，字段名称存放 name=refname;name2=refname2
							String fieldNames = "";
							String[] fkFields = new String[joinColumns.value().length];
							String[] relationFields = new String[joinColumns.value().length];
							int index = 0;
							for (JoinColumn joinColumn2 : joinColumns.value()) {
								fieldNames += ";" + joinColumn2.name() + "=" + joinColumn2.referencedColumnName();
								fkFields[index] = joinColumn2.name();
								relationFields[index] = joinColumn2.referencedColumnName();
								index++;
							}
							fieldNames = fieldNames.substring(1);
							// 标记为后续还要处理
							field.setFieldName("tmp|" + fieldNames);
							fkFieldDefinition.setFkFields(fkFields);
							fkFieldDefinition.setRelationFields(relationFields);
						}
					}
					// 设置显示字段
					MetaDataView metaDataView = field.getType().getAnnotation(MetaDataView.class);
					if (metaDataView != null && metaDataView.tableView() != null
							&& StringUtils.isNotBlank(metaDataView.tableView().labelField())) {
						field.setLabelField(metaDataView.tableView().labelField());
					} else {
						field.setLabelField(EntityMetaDataConstants.DEFAULT_LABEL_FIELD_NAME);
					}
					// 设置字段取值函数
					field.setGetValueHandler(new SearchRelationGetFieldValueHandler(field));
					//
					fkFieldDefinition.setName(field.getName());
					fkFieldDefinition.setField(field.getName());
					fkFieldDefinition.setRelationClass(field.getRelationClass());
					fkFieldDefinition.setRelationTable(field.getRelationTable());
					metaData.getFkFieldDefinitions().put(fkFieldDefinition.getField(), fkFieldDefinition);
				}
				// 设置值类型
				if (association.getInverse().getGetter() != null) {
					field.setType(association.getInverse().getGetter().getReturnType());
				}
				// 设置
				field.setIdField(association.getInverse().isIdProperty());
				// 设置getter、setter方法
				field.setGetter(association.getInverse().getGetter());
				field.setSetter(association.getInverse().getSetter());
				// 设置实体赋值函数
				field.setSetValueHandler(new AssociationSetFieldValueHandler(field));
				field.setParent(metaData);
				fieldsMap.put(field.getName(), field);
			}
		});

		// 增加通用子表字段，包括附件和备注
		for (CommonSubTableType subTableType : metaData.getTableDefinition().getCommonSubTables()) {
			try {
				ClassUtils.forName(subTableType.getTableClass(), getClass().getClassLoader());
				CommonSubTableFieldDenifition commonSubTableFieldDenifition = new CommonSubTableFieldDenifition(
						subTableType, metaData.getEntityName(), metaData.getEntityClass().getName());
				fieldsMap.put(commonSubTableFieldDenifition.getName(), commonSubTableFieldDenifition);
			} catch (ClassNotFoundException e) {
				log.warn("找不到子表实现类[" + subTableType.getTableClass() + "]，将忽略");
			} catch (LinkageError e) {
				e.printStackTrace();
			}
		}

		metaData.setFieldMap(fieldsMap);
	}
}
