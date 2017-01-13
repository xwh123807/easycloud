package org.easycloud.platform.metadata.internal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easycloud.platform.core.domain.FieldDataType;
import org.easycloud.platform.core.domain.FieldDataType.FieldAttr;
import org.easycloud.platform.core.utils.AssertUtil;
import org.easycloud.platform.core.utils.ClassUtil;
import org.easycloud.platform.core.utils.StringUtil;
import org.easycloud.platform.metadata.annotation.FieldView;
import org.easycloud.platform.metadata.annotation.FlyEnum;
import org.easycloud.platform.metadata.annotation.FlySearchRelation;
import org.easycloud.platform.metadata.annotation.MetaDataView;
import org.easycloud.platform.metadata.annotation.TableView;
import org.easycloud.platform.metadata.define.AssociationSetFieldValueHandler;
import org.easycloud.platform.metadata.define.CommonSubTableFieldDenifition;
import org.easycloud.platform.metadata.define.CommonSubTableType;
import org.easycloud.platform.metadata.define.ComplexGetPKFieldValueHandler;
import org.easycloud.platform.metadata.define.ComplexSetPKFieldValueHandler;
import org.easycloud.platform.metadata.define.DefaultGetFieldValueHandler;
import org.easycloud.platform.metadata.define.DefaultSetFieldValueHandler;
import org.easycloud.platform.metadata.define.FKFieldDefinition;
import org.easycloud.platform.metadata.define.FieldDefinition;
import org.easycloud.platform.metadata.define.FlySearchRelationGetFieldValueHandler;
import org.easycloud.platform.metadata.define.PKFieldDefinition;
import org.easycloud.platform.metadata.define.SearchRelationGetFieldValueHandler;
import org.easycloud.platform.metadata.define.TableDefinition;
import org.easycloud.platform.metadata.service.IEntityMetaDataService;
import org.easycloud.platform.metadata.service.IMetaDataRegister;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.jpa.mapping.JpaPersistentEntity;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.SimpleAssociationHandler;
import org.springframework.data.mapping.SimplePropertyHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * 实体元模型服务
 * 
 * @author xiangwanhong
 *
 */
@Service
@CacheConfig(cacheNames = "entity-metadata-service")
public class EntityMetaDataService implements IEntityMetaDataService {
	private static Log log = LogFactory.getLog(EntityMetaDataService.class);
	@Autowired
	private JpaMetamodelMappingContext mappingContext;
	@Autowired
	@Qualifier("defaultConversionService")
	private ConversionService conversionService;
	/**
	 * 存储扩展注册的元模型
	 */
	@Autowired
	public List<IMetaDataRegister> metaDataRegisters;

	/**
	 * 缓存所有已经加载的元模型
	 */
	private static Map<String, EntityMetaData> cachedEntityMetaDatas = new ConcurrentHashMap<>();

	/*
	 * (non-Javadoc)ø
	 * 
	 * @see
	 * org.myfly.platform.system.service.IEntityMetaDataService#getEntityClass(
	 * java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Cacheable(key = "'getEntityClass-' + #entityNameOrClassName")
	public <T> T getEntityClass(final String entityNameOrClassName) {
		AssertUtil.parameterEmpty(entityNameOrClassName, "entityNameOrClassName");
		Class<?> entityClass = null;
		for (JpaPersistentEntity<?> entity : mappingContext.getPersistentEntities()) {
			if (entity.getName().equals(entityNameOrClassName)) {
				entityClass = entity.getType();
				break;
			} else {
				String shortName = ClassUtils.getShortName(entity.getName());
				if (shortName.equalsIgnoreCase(entityNameOrClassName)) {
					entityClass = entity.getType();
					break;
				}
			}
		}
		return (T) entityClass;
	}

	/**
	 * 根据子表属性，获取对应子表名称
	 * 
	 * @param table
	 * @param subTableAttr
	 * @return
	 */
	@Override
	public EntityMetaData getSubEntityMetaData(String table, String subTableAttr) {
		String subTableClass = getEntityMetaData(table).getField(subTableAttr).getRelationClass();
		return getEntityMetaData(subTableClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.myfly.platform.system.service.IEntityMetaDataService#
	 * getAllEntityClasses()
	 */
	@Override
	public List<Class<?>> getAllEntityClasses() {
		List<Class<?>> list = new ArrayList<>();
		for (JpaPersistentEntity<?> entity : mappingContext.getPersistentEntities()) {
			list.add(entity.getType());
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.myfly.platform.system.service.IEntityMetaDataService#
	 * getEntityMetaData(java.lang.Object)
	 */
	@Override
	// @Cacheable(key = "'getEntityMetaData-' + #entityNameOrClassName")
	public EntityMetaData getEntityMetaData(final String entityNameOrClassName) {
		AssertUtil.parameterEmpty(entityNameOrClassName, "entityNameOrClassName");
		if (cachedEntityMetaDatas.containsKey(entityNameOrClassName)) {
			return cachedEntityMetaDatas.get(entityNameOrClassName);
		}
		Class<?> entityClass = getEntityClass(entityNameOrClassName);
		EntityMetaData metaData = null;
		if (entityClass == null) {
			// 非实体类，从扩展元模型中获取
			metaData = getExternalMetaData(entityNameOrClassName);
			if (metaData != null) {
				return metaData;
			}
			String entityName = "";
			if (entityNameOrClassName.indexOf(".") != -1) {
				entityName = StringUtils.substringAfter(entityNameOrClassName, ".");
				entityClass = getEntityClass(entityName);
				if (entityClass == null && entityName.contains("_")) {
					entityName = entityName.replaceAll("_", "");
					entityClass = getEntityClass(entityName);
				}
			}
		}
		if (entityClass != null) {
			log.info("加载实体[" + entityNameOrClassName + "]元模型.");
			metaData = new EntityMetaData(entityClass);
			updateMetaDataTableDefinitions(metaData);
			updateMetaDataFieldDefinitions(metaData);
			updateMetaDataPKFieldDefinitions(metaData);
			metaData.buildListDefinition();
			metaData.buildFormDefinitions();
		}
		cachedEntityMetaDatas.put(entityNameOrClassName, metaData);
		return metaData;
	}

	/**
	 * 从扩展元模型中获取
	 * 
	 * @param tableName
	 * @return
	 */
	public EntityMetaData getExternalMetaData(final String entityName) {
		for (IMetaDataRegister register : metaDataRegisters) {
			if (register.contains(entityName)) {
				return register.getEntityMetaData(entityName);
			}
		}
		return null;
	}

	/**
	 * 返回可序列化和反序列化的元模型
	 * 
	 * @param entityNameOrClassName
	 * @return
	 */
	public JsonEntityMetaData getJsonEntityMetaData(final String entityNameOrClassName) {
		AssertUtil.parameterEmpty(entityNameOrClassName, "entityNameOrClassName");
		EntityMetaData entityMetaData = getEntityMetaData(entityNameOrClassName);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData", "找不到名称为[" + entityNameOrClassName + "]的实体定义.");
		JsonEntityMetaData metaData = new JsonEntityMetaData(entityMetaData);
		return metaData;
	}

	/**
	 * 更新实体主键定义信息
	 * 
	 * @param metaData
	 */
	private void updateMetaDataPKFieldDefinitions(EntityMetaData metaData) {
		PKFieldDefinition pkFieldDefinition = new PKFieldDefinition(metaData);
		IdClass idClass = metaData.getEntityClass().getAnnotation(IdClass.class);
		if (idClass != null) {
			pkFieldDefinition.setIdClass(idClass.value());
		}
		List<FieldDefinition> pkFields = new ArrayList<>();
		for (FieldDefinition field : metaData.getFieldMap().values()) {
			if (field.isIdField()) {
				pkFields.add(field);
				if (field.getGetter().getAnnotation(Embeddable.class) != null) {
					pkFieldDefinition.setIdClass(field.getType());
				}
			}
		}
		pkFieldDefinition.setIdFields(pkFields.toArray(new FieldDefinition[] {}));
		if (pkFieldDefinition.isComplexKey()) {
			// 复合主键
			pkFieldDefinition.setGetValueHandler(new ComplexGetPKFieldValueHandler(pkFieldDefinition));
			pkFieldDefinition.setSetValueHandler(new ComplexSetPKFieldValueHandler(pkFieldDefinition));
		} else {
			// 单个主键
			FieldDefinition pkField = pkFieldDefinition.getIdFields()[0];
			pkFieldDefinition.setGetValueHandler(new DefaultGetFieldValueHandler(pkField));
			pkFieldDefinition.setSetValueHandler(new DefaultSetFieldValueHandler(pkField));
		}
		pkFieldDefinition.updateKeyType();
		metaData.setPkFieldDefinition(pkFieldDefinition);
	}

	/**
	 * 更新元模型表定义信息
	 * 
	 * @param metaData
	 */
	private void updateMetaDataTableDefinitions(final EntityMetaData metaData) {
		MetaDataView metaDataView = metaData.getEntityClass().getAnnotation(MetaDataView.class);
		TableDefinition tableDefinition = new TableDefinition();
		if (metaDataView != null && metaDataView.tableView() != null) {
			// 有定义表定义
			TableView tableView = metaDataView.tableView();
			tableDefinition.setTitle(tableView.title());
			tableDefinition.setDescription(tableView.description());
			tableDefinition.setLabelField(tableView.labelField());
			tableDefinition.setCommonSubTables(tableView.commonSubTables());
			tableDefinition.setCreateIndex(tableView.createIndex());
			tableDefinition.setIndexName(tableView.indexName());
		} else {
			// 没有配置时，则支持所有通用子表
			tableDefinition.setCommonSubTables(CommonSubTableType.values());
		}
		Table table = metaData.getEntityClass().getAnnotation(Table.class);
		if (table != null) {
			tableDefinition.setSchema(table.schema());
			tableDefinition.setTableName(table.name());
		}
		if (StringUtils.isBlank(tableDefinition.getTableName())) {
			tableDefinition.setTableName(StringUtil.getHibernateName(metaData.getEntityName()));
		}
		if (StringUtils.isNotBlank(tableDefinition.getSchema())) {
			tableDefinition.setTableName(tableDefinition.getSchema() + "." + tableDefinition.getTableName());
		}
		if (StringUtils.isBlank(tableDefinition.getTitle())) {
			tableDefinition.setTitle(metaData.getEntityName());
		}
		if (StringUtils.isBlank(tableDefinition.getLabelField())) {
			tableDefinition.setLabelField(EntityMetaDataConstants.DEFAULT_LABEL_FIELD_NAME);
		}
		if (StringUtils.isBlank(tableDefinition.getIndexName())) {
			tableDefinition.setIndexName(metaData.getEntityName().toLowerCase());
		}
		metaData.setTableDefinition(tableDefinition);
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
