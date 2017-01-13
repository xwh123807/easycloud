package org.easycloud.platform.core.metadata.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easycloud.platform.core.metadata.annotation.FormView;
import org.easycloud.platform.core.metadata.annotation.ListView;
import org.easycloud.platform.core.metadata.annotation.MetaDataView;
import org.easycloud.platform.core.metadata.define.FKFieldDefinition;
import org.easycloud.platform.core.metadata.define.FieldDefinition;
import org.easycloud.platform.core.metadata.define.FormDefinition;
import org.easycloud.platform.core.metadata.define.ListDefinition;
import org.easycloud.platform.core.metadata.define.PKFieldDefinition;
import org.easycloud.platform.core.metadata.define.SearchRelationGetFieldValueHandler;
import org.easycloud.platform.core.metadata.define.TableDefinition;
import org.easycloud.platform.core.utils.AppUtil;
import org.easycloud.platform.core.utils.AssertUtil;
import org.easycloud.platform.metadata.annotation.FieldDataType;
import org.easycloud.platform.metadata.annotation.IKeyEntity;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 实体元模型数据结构
 * 
 * @author xiangwanhong
 *
 */
public class EntityMetaData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3928359002888888678L;
	private static Log log = LogFactory.getLog(EntityMetaData.class);
	/**
	 * 实体名称，实体的唯一标示，不能重复，如果是实体类，entityName为类名称
	 */
	private String entityName;
	/**
	 * 实体类名，为空时表示没有对应实体类
	 */
	private Class<?> entityClass;
	/**
	 * 当是扩展云模型时，需要指定，元模型所在的数据源
	 */
	private String dataSourceId;
	/**
	 * 实体是否继承SBaseEntity
	 */
	private boolean isFlyEntity;
	/**
	 * 字段列表
	 */
	private Map<String, FieldDefinition> fieldMap;
	/**
	 * 表基本信息
	 */
	private TableDefinition tableDefinition;
	/**
	 * 列表视图定义
	 */
	private Map<String, ListDefinition> listDefinitions;
	/**
	 * 表单实体定义
	 */
	private Map<String, FormDefinition> formDefinitions;
	/**
	 * 主键定义
	 */
	private PKFieldDefinition pkFieldDefinition;
	/**
	 * 外键定义,查找关系
	 */
	private Map<String, FKFieldDefinition> fkFieldDefinitions;

	public Map<String, FieldDefinition> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<String, FieldDefinition> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public EntityMetaData() {
		init();
	}

	private void init() {
		listDefinitions = new LinkedHashMap<>();
		formDefinitions = new LinkedHashMap<>();
		fkFieldDefinitions = new LinkedHashMap<>();
	}

	public EntityMetaData(Class<?> entityClass) {
		AssertUtil.parameterEmpty(entityClass, "entityClass");
		this.setEntityClass(entityClass);
		if (entityClass.getSuperclass() == IKeyEntity.class) {
			this.setFlyEntity(true);
		}
		this.setEntityName(ClassUtils.getShortClassName(entityClass));
		init();
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public TableDefinition getTableDefinition() {
		return tableDefinition;
	}

	public void setTableDefinition(TableDefinition tableDefinition) {
		this.tableDefinition = tableDefinition;
	}

	public FieldDefinition getField(String name) {
		return getFieldMap().get(name);
	}

	@JsonIgnore
	public Map<String, ListDefinition> getListDefinitions() {
		return listDefinitions;
	}

	public void setListDefinitions(Map<String, ListDefinition> listDefinitions) {
		this.listDefinitions = listDefinitions;
	}

	@JsonIgnore
	public Map<String, FormDefinition> getFormDefinitions() {
		return formDefinitions;
	}

	public void setFormDefinitions(Map<String, FormDefinition> formDefinitions) {
		this.formDefinitions = formDefinitions;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	/**
	 * 获取实体主键定义
	 * 
	 * @return
	 */
	public PKFieldDefinition getPKFieldDefinition() {
		return this.pkFieldDefinition;
	}

	public void setPkFieldDefinition(PKFieldDefinition pkFieldDefinition) {
		this.pkFieldDefinition = pkFieldDefinition;
	}

	/**
	 * 获取显示名称字段
	 * 
	 * @return
	 */
	@JsonIgnore
	public FieldDefinition getLabelField() {
		return getField(getTableDefinition().getLabelField());
	}

	/**
	 * 获取标签属性名称
	 * 
	 * @return
	 */
	public String getLabelFieldName() {
		if (getLabelField() != null) {
			return getLabelField().getName();
		} else {
			return null;
		}
	}

	public boolean isFlyEntity() {
		return isFlyEntity;
	}

	public boolean isJpaEntity() {
		return getEntityClass() != null;
	}

	public void setFlyEntity(boolean isFlyEntity) {
		this.isFlyEntity = isFlyEntity;
	}

	public Map<String, FKFieldDefinition> getFkFieldDefinitions() {
		return fkFieldDefinitions;
	}

	public void setFkFieldDefinitions(Map<String, FKFieldDefinition> fkFieldDefinitions) {
		this.fkFieldDefinitions = fkFieldDefinitions;
	}

	/**
	 * 根据字段名称获取字段定义信息
	 * 
	 * @param fields
	 * @param enableActions
	 *            为true时，增加实体操作列
	 * @return
	 */
	public FieldDefinition[] getFields(String[] fields) {
		if (ArrayUtils.isEmpty(fields)) {
			return new FieldDefinition[0];
		}
		FieldDefinition[] fieldDefinitions = new FieldDefinition[fields.length];
		for (int i = 0; i < fields.length; i++) {
			FieldDefinition field = getField(fields[i]);
			AssertUtil.parameterEmpty(field, fields[i]);
			fieldDefinitions[i] = field;
		}
		return fieldDefinitions;
	}

	/**
	 * 获取所有字段，不包含主从关系
	 * 
	 * @return
	 */
	@JsonIgnore
	public FieldDefinition[] getAllFields() {
		List<FieldDefinition> fieldDefinitions = new ArrayList<>();
		for (FieldDefinition field : getFieldMap().values()) {
			if (!(FieldDataType.MDRELATION.equals(field.getDataType())
					|| FieldDataType.FLYMDRELATION.equals(field.getDataType()))) {
				fieldDefinitions.add(field);
			}
		}
		return fieldDefinitions.toArray(new FieldDefinition[] {});
	}

	/**
	 * 获取指定字段类型的字段
	 * 
	 * @param mdrelation
	 * @return
	 */
	public FieldDefinition[] getFieldsByDataType(FieldDataType[] dataTypes) {
		AssertUtil.parameterEmpty(dataTypes, "dataType");
		List<FieldDefinition> fieldDefinitions = new ArrayList<>();
		for (FieldDefinition field : getFieldMap().values()) {
			for (int i = 0; i < dataTypes.length; i++) {
				if (field.getDataType().equals(dataTypes[i])) {
					fieldDefinitions.add(field);
				}
			}
		}
		return fieldDefinitions.toArray(new FieldDefinition[] {});
	}

	/**
	 * 获取字段名称指向的字段定义
	 * 
	 * @param dbFieldName
	 * @return
	 */
	public FieldDefinition getFieldByFieldName(String dbFieldName) {
		AssertUtil.parameterEmpty(dbFieldName, "dbFieldName");
		for (FieldDefinition field : getFieldMap().values()) {
			if (dbFieldName.equals(field.getFieldName())) {
				return field;
			}
		}
		return null;
	}

	/**
	 * 创建实体实例
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T newEntityInstance() {
		if (getEntityClass() == null) {
			return null;
		}
		try {
			return (T) getEntityClass().newInstance();
		} catch (Exception e) {
			AssertUtil.parameterInvalide(getEntityClass().getName(), "创建实体实例失败，" + e.getMessage());
		}
		return null;
	}

	/**
	 * 获取列表视图定义，如果不存在该视图则返回默认列表视图，如果还是没有则返回默认全部列表视图
	 * 
	 * @param name
	 * @return
	 */
	public ListDefinition getListDefinition(final String viewName) {
		ListDefinition listDefinition = getListDefinitions().get(viewName);
		if (listDefinition == null) {
			listDefinition = getListDefinitions().get(EntityMetaDataConstants.DEFAULT_NAME);
		}
		if (listDefinition == null) {
			listDefinition = getListDefinitions().get(EntityMetaDataConstants.DEFAULT_ALL_NAME);
		}
		if (!listDefinition.getName().equals(viewName)) {
			if (log.isWarnEnabled()) {
				log.warn("实体[" + getEntityName() + "]不存在名称为[" + viewName + "]的ListDefinition定义，将返回名称为["
						+ listDefinition.getName() + "]的。");
			}
		}
		return listDefinition;
	}

	/**
	 * 获取表单视图定义，如果不存在该视图则返回默认表单视图，如果还是没有则返回默认全部表单视图
	 * 
	 * @param name
	 * @return
	 */
	public FormDefinition getFormDefinition(final String viewName) {
		FormDefinition formDefinition = getFormDefinitions().get(viewName);
		if (formDefinition == null) {
			formDefinition = getFormDefinitions().get(EntityMetaDataConstants.DEFAULT_NAME);
		}
		if (formDefinition == null) {
			formDefinition = getFormDefinitions().get(EntityMetaDataConstants.DEFAULT_ALL_NAME);
		}
		if (!formDefinition.getName().equals(viewName)) {
			if (log.isWarnEnabled()) {
				log.warn("实体[" + getEntityName() + "]不存在名称为[" + viewName + "]的FormDefinition定义，将返回名称为["
						+ formDefinition.getName() + "]的。");
			}
		}
		return formDefinition;
	}

	/**
	 * 增加列表视图定义
	 * 
	 * @param listDefinition
	 */
	public void addListDenifition(final ListDefinition listDefinition) {
		AssertUtil.parameterEmpty(listDefinition, "listDefinition");
		AssertUtil.parameterEmpty(listDefinition.getName(), "listDefinition.getName()");
		if (getListDefinitions().containsKey(listDefinition.getName())) {
			AssertUtil.parameterInvalide("listDefinition",
					"名称为[" + listDefinition.getName() + "]ListDefinition已经存在，请检查实体[" + getEntityName() + "]元模型定义.");
		}
		getListDefinitions().put(listDefinition.getName(), listDefinition);
	}

	/**
	 * 增加表单视图定义
	 * 
	 * @param formDefinition
	 */
	public void addFormDefinition(FormDefinition formDefinition) {
		AssertUtil.parameterEmpty(formDefinition, "formDefinition");
		AssertUtil.parameterEmpty(formDefinition.getName(), "formDefinition.getName()");
		if (getFormDefinitions().containsKey(formDefinition.getName())) {
			AssertUtil.parameterInvalide("formDefinition",
					"名称为[" + formDefinition.getName() + "]FormDefinition已经存在，请检查实体[" + getEntityName() + "]元模型定义.");
		}
		getFormDefinitions().put(formDefinition.getName(), formDefinition);
	}

	/**
	 * 获取实体子表实体定义
	 * 
	 * @param subTableAttr
	 * @return
	 */
	public EntityMetaData getSubEntityMetaData(String subTableAttr) {
		// 获取子表主键
		FieldDefinition subField = getField(subTableAttr);
		AssertUtil.parameterNotEmpty("实体[" + getEntityName() + "]没有名称为[" + subTableAttr + "]的子表属性", subField);
		AssertUtil.parameterInvalide(
				!(FieldDataType.MDRELATION.equals(subField.getDataType())
						|| FieldDataType.FLYMDRELATION.equals(subField.getDataType())),
				"属性[" + subTableAttr + "]必须是主子表关系.");
		EntityMetaData subEntityMetaData = AppUtil.getEntityMataDataService()
				.getEntityMetaData(subField.getRelationTable());
		AssertUtil.parameterNotEmpty("实体[" + getEntityName() + "]没有名称为[" + subTableAttr + "]的子表实体定义",
				subEntityMetaData);
		return subEntityMetaData;
	}

	/**
	 * 获取实体子表主键定义
	 * 
	 * @param subTableAttr
	 * @return
	 */
	public PKFieldDefinition getSubEntityPKFieldDefinition(String subTableAttr) {
		return getSubEntityMetaData(subTableAttr).getPKFieldDefinition();
	}

	@Override
	public String toString() {
		return "entityName: " + getEntityName() + ", entityClass: " + getEntityClass() + ", tableName: "
				+ getTableDefinition().getTableName();
	}

	/**
	 * 构建自动关联属性字段
	 * 
	 * @param field
	 * @param relAttr
	 * @return
	 */
	private FieldDefinition buildAutoRelationField(FieldDefinition field, String relAttr) {
		String relLabel = AppUtil.getEntityMataDataService().getEntityMetaData(field.getRelationClass())
				.getField(relAttr).getLabel();
		FieldDefinition newField = new FieldDefinition(relLabel, field.getName() + "__" + relAttr,
				FieldDataType.AUTORELATION);
		newField.setFieldName(field.getFieldName());
		newField.setDescription(field.getDescription());
		newField.setGetter(field.getGetter());
		newField.setLabelField(relAttr);
		newField.setRelationTable(field.getRelationTable());
		newField.setRelationClass(field.getRelationClass());
		newField.setGetValueHandler(new SearchRelationGetFieldValueHandler(newField));
		return newField;
	}

	/**
	 * 根据字段名列表获取字段列表，支持自动关联属性。格式为"attr.relationattr"
	 * 
	 * @param fields
	 * @param enableActions
	 *            为true时，增加实体操作列
	 * @return
	 */
	public FieldDefinition[] getFields(EntityMetaData metaData, String[] fields) {
		FieldDefinition[] fieldDefinitions = new FieldDefinition[fields.length];
		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i];
			String relationAttr = null;
			String attr;
			if (fieldName.contains(".")) {
				// 取出关联属性
				relationAttr = StringUtils.substringAfter(fieldName, ".");
				AssertUtil.parameterEmpty(relationAttr, "relationAttr",
						"属性名[" + fieldName + "]不是有效的关联属性名，语法为attr.relationattr");
				attr = StringUtils.substringBefore(fieldName, ".");
			} else {
				attr = fieldName;
			}
			FieldDefinition field = metaData.getField(attr);
			AssertUtil.parameterEmpty(field, attr, "实体[" + metaData.getEntityName() + "]没有名称为[" + fieldName + "]的属性.");
			if (StringUtils.isNotBlank(relationAttr)) {
				// 表示自动关联属性, 取出关联属性
				field = buildAutoRelationField(field, relationAttr);
			}

			fieldDefinitions[i] = field;
		}
		return fieldDefinitions;
	}

	/**
	 * 构建列表查询定义
	 */
	public void buildListDefinition() {
		if (getEntityClass() != null) {
			// 实体类
			MetaDataView metaDataView = getEntityClass().getAnnotation(MetaDataView.class);
			ListView[] listViews = (metaDataView != null) ? metaDataView.listViews() : null;
			if (ArrayUtils.isNotEmpty(listViews)) {
				// 有定义列表视图
				for (ListView listView : listViews) {
					ListDefinition listDefinition = ListDefinition.buildListDefinition(this, listView);
					addListDenifition(listDefinition);
				}
			}
		}
		// 自动添加包含全部字段的ListDefinition
		if (!getListDefinitions().containsKey(EntityMetaDataConstants.DEFAULT_ALL_NAME)) {
			ListDefinition listDefinition = ListDefinition.buildDefaultListDefinition(this,
					EntityMetaDataConstants.DEFAULT_ALL_NAME);
			addListDenifition(listDefinition);
		}
	}

	/**
	 * 构建表单定义
	 */
	public void buildFormDefinitions() {
		if (getEntityClass() != null) {
			MetaDataView metaDataView = getEntityClass().getAnnotation(MetaDataView.class);
			FormView[] formViews = (metaDataView != null) ? metaDataView.formViews() : null;
			if (ArrayUtils.isNotEmpty(formViews)) {
				for (FormView formView : formViews) {
					FormDefinition formDefinition = FormDefinition.buildFormDefinition(this, formView);
					addFormDefinition(formDefinition);
				}
			}
		}
		// 自动增加All定义
		if (!getFormDefinitions().containsKey(EntityMetaDataConstants.DEFAULT_ALL_NAME)) {
			FormDefinition formDefinition = FormDefinition.buildDefaultFormViewDefinition(this,
					EntityMetaDataConstants.DEFAULT_ALL_NAME);
			addFormDefinition(formDefinition);
		}
	}

	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	/**
	 * 验证元模型正确性
	 */
	public void validate() {
		Assert.notNull(getEntityName(), "entityName不能为空");
		Assert.notNull(getTableDefinition(), "getTableDefinition不能为空");
		Assert.notNull(getPKFieldDefinition(), "getPKFieldDefinition不能为空");
		Assert.notEmpty(getFieldMap(), "getFieldMap不能为空");
		Assert.notEmpty(getListDefinitions(), "getListDefinitions不能为空");
		Assert.notEmpty(getFormDefinitions(), "getFormDefinitions不能为空");
		getTableDefinition().validate();
		getPKFieldDefinition().validate();
		getFieldMap().forEach(new BiConsumer<String, FieldDefinition>() {

			@Override
			public void accept(String name, FieldDefinition field) {
				field.validate();
			}
		});
		
		getFkFieldDefinitions().forEach(new BiConsumer<String, FKFieldDefinition>() {

			@Override
			public void accept(String t, FKFieldDefinition fkField) {
				fkField.validate();
			}
		});
		
		getListDefinitions().forEach(new BiConsumer<String, ListDefinition>() {

			@Override
			public void accept(String name, ListDefinition list) {
				list.validate();
			}
		});
		
		getFormDefinitions().forEach(new BiConsumer<String, FormDefinition>() {

			@Override
			public void accept(String name, FormDefinition form) {
				form.validate();
			}
		});
	}
}
