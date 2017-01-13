package org.easycloud.platform.metadata.internal;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.common.utils.JSONUtil;
import org.easycloud.platform.metadata.annotation.FieldDataType;
import org.easycloud.platform.metadata.define.DefaultGetFieldValueHandler;
import org.easycloud.platform.metadata.define.FKFieldDefinition;
import org.easycloud.platform.metadata.define.FieldDefinition;
import org.easycloud.platform.metadata.define.PKFieldDefinition;
import org.easycloud.platform.metadata.define.PKFieldDefinition.KeyType;
import org.easycloud.platform.metadata.define.SearchRelationGetFieldValueHandler;
import org.easycloud.platform.metadata.define.TableDefinition;

public final class JsonEntityMetaData {
	/**
	 * 实体名称
	 */
	private String entityName;
	/**
	 * 实体类名
	 */
	private Class<?> entityClass;
	/**
	 * 当时外部加载实体时，需要指定所在数据源
	 */
	private String dataSourceId;
	/**
	 * 表信息
	 */
	private TableDefinition tableDefinition;
	/**
	 * 主键
	 */
	private JsonPKFieldDefinition pkFieldDefinition;
	/**
	 * 字段列表
	 */
	private Map<String, JsonFieldDefinition> fieldMap;
	/**
	 * 外键定义
	 */
	private Map<String, FKFieldDefinition> fkFieldsMap;

	public JsonEntityMetaData() {
	}

	public JsonEntityMetaData(EntityMetaData metaData) {
		setEntityName(metaData.getEntityName());
		setEntityClass(metaData.getEntityClass());
		setDataSourceId(metaData.getDataSourceId());
		setTableDefinition(metaData.getTableDefinition());
		if (metaData.getPKFieldDefinition() != null) {
			setPkFieldDefinition(new JsonPKFieldDefinition(metaData.getPKFieldDefinition()));
		}
		fieldMap = new LinkedHashMap<>();
		for (FieldDefinition field : metaData.getFieldMap().values()) {
			JsonFieldDefinition jsonField = new JsonFieldDefinition(field);
			fieldMap.put(jsonField.getName(), jsonField);
		}
		fkFieldsMap = metaData.getFkFieldDefinitions();
	}

	public EntityMetaData convertToEntityMetaData() {
		EntityMetaData metaData = new EntityMetaData();
		metaData.setEntityClass(getEntityClass());
		metaData.setEntityName(getEntityName());
		metaData.setDataSourceId(getDataSourceId());
		metaData.setTableDefinition(getTableDefinition());
		// fieldMap
		Map<String, FieldDefinition> fieldMap = new LinkedHashMap<>();
		for (JsonFieldDefinition jsonField : getFieldMap().values()) {
			FieldDefinition field = jsonField.convert(metaData);
			fieldMap.put(field.getName(), field);
		}
		metaData.setFieldMap(fieldMap);
		// PkFieldDefinition
		if (getPkFieldDefinition() != null) {
			metaData.setPkFieldDefinition(getPkFieldDefinition().convert(metaData));
		}
		// fk
		metaData.setFkFieldDefinitions(getFkFieldsMap());
		return metaData;
	}

	public String toJson() {
		return JSONUtil.toJSON(this);
	}

	public static EntityMetaData fromJson(String content) {
		JsonEntityMetaData jsonEntityMetaData = JSONUtil.fromJSON(content, JsonEntityMetaData.class);
		return jsonEntityMetaData.convertToEntityMetaData();
	}

	public static EntityMetaData fromJson(URL url) {
		JsonEntityMetaData jsonEntityMetaData = JSONUtil.fromJSON(url, JsonEntityMetaData.class);
		return jsonEntityMetaData.convertToEntityMetaData();
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
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

	public JsonPKFieldDefinition getPkFieldDefinition() {
		return pkFieldDefinition;
	}

	public void setPkFieldDefinition(JsonPKFieldDefinition pkFieldDefinition) {
		this.pkFieldDefinition = pkFieldDefinition;
	}

	public Map<String, JsonFieldDefinition> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<String, JsonFieldDefinition> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public static class JsonPKFieldDefinition {
		/**
		 * 复合主键类
		 */
		private String idClass;
		/**
		 * 主键字段，已经按名称排好了顺序
		 */
		private String[] idFields;
		/**
		 * 主键类型
		 */
		private KeyType keyType;

		public JsonPKFieldDefinition() {
		}

		public PKFieldDefinition convert(EntityMetaData parent) {
			PKFieldDefinition pkFieldDefinition = new PKFieldDefinition(parent);
			if (StringUtils.isNotBlank(getIdClass())){
				try {
					pkFieldDefinition.setIdClass(Class.forName(getIdClass()));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			pkFieldDefinition.setKeyType(getKeyType());
			List<FieldDefinition> fields = new ArrayList<>();
			if (ArrayUtils.isNotEmpty(getIdFields())) {
				for (String name : getIdFields()) {
					FieldDefinition field = parent.getField(name);
					fields.add(field);
				}
			}
			pkFieldDefinition.setIdFields(fields.toArray(new FieldDefinition[] {}));
			return pkFieldDefinition;
		}

		public JsonPKFieldDefinition(PKFieldDefinition pkFieldDefinition) {
			if (pkFieldDefinition.getIdClass() != null) {
				setIdClass(pkFieldDefinition.getIdClass().getName());
			}
			setIdFields(pkFieldDefinition.getIdFieldNames());
			setKeyType(pkFieldDefinition.getKeyType());
		}

		public String getIdClass() {
			return idClass;
		}

		public void setIdClass(String idClass) {
			this.idClass = idClass;
		}

		public String[] getIdFields() {
			return idFields;
		}

		public void setIdFields(String[] idFields) {
			this.idFields = idFields;
		}

		public KeyType getKeyType() {
			return keyType;
		}

		public void setKeyType(KeyType keyType) {
			this.keyType = keyType;
		}
	}

	public static class JsonFieldDefinition {
		private String name;
		/**
		 * 标签
		 */
		private String label;

		/**
		 * 字段名称
		 */
		private String fieldName;
		/**
		 * 简档
		 */
		private String description;
		/**
		 * 是否必填
		 */
		private boolean required;
		/**
		 * 数据类型
		 */
		private FieldDataType dataType;
		/**
		 * 控件值
		 */
		private String value;
		/**
		 * 如果是枚举类型，存放枚举类型名；如果是关联关系，存放关联类名
		 */
		private String relationClass;
		/**
		 * 如果是查找关系、主子表关系时，存放关联表名
		 */
		private String relationTable;
		/**
		 * 关联字段
		 */
		private String relationField;
		/**
		 * 最小长度
		 */
		private int minLength;
		/**
		 * 最大长度，如果是字符串类型时，取长度
		 */
		private int maxLength;
		/**
		 * 精度
		 */
		private int precision;
		/**
		 * 输入限制
		 */
		private String mask;
		/**
		 * 字段值类型
		 */
		private String type;
		/**
		 * 字段链接配置
		 */
		private String linkUrl;
		/**
		 * 只读，不能更改
		 */
		private boolean readonly;
		/**
		 * 为关联属性时，id转名称显示字段
		 */
		private String labelField;
		/**
		 * 是否为主键字段，如果是复合主键，则一个实体中有多个属性
		 */
		private boolean isIdField;

		public JsonFieldDefinition() {
		}

		public FieldDefinition convert(EntityMetaData parent) {
			FieldDefinition field = new FieldDefinition();
			field.setParent(parent);
			field.setDataType(getDataType());
			field.setDescription(getDescription());
			field.setFieldName(getFieldName());
			field.setIdField(isIdField());
			field.setLabel(getLabel());
			field.setLabelField(getLabelField());
			field.setLinkUrl(getLinkUrl());
			field.setMask(getMask());
			field.setMaxLength(getMaxLength());
			field.setMinLength(getMinLength());
			field.setName(getName());
			field.setPrecision(getPrecision());
			field.setReadonly(isReadonly());
			field.setRelationClass(getRelationClass());
			field.setRelationTable(getRelationTable());
			field.setRequired(isRequired());
			try {
				field.setType(Class.forName(getType()));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			if (FieldDataType.SEARCHRELATION.equals(getDataType())) {
				field.setGetValueHandler(new SearchRelationGetFieldValueHandler(field));
			} else {
				field.setGetValueHandler(new DefaultGetFieldValueHandler(field));
			}
			return field;
		}

		public JsonFieldDefinition(FieldDefinition field) {
			setDataType(field.getDataType());
			setFieldName(field.getFieldName());
			setIdField(field.isIdField());
			setLabel(field.getLabel());
			setLabelField(field.getLabelField());
			setLinkUrl(field.getLinkUrl());
			setMask(field.getMask());
			setMaxLength(field.getMaxLength());
			setMinLength(field.getMinLength());
			setName(field.getName());
			setPrecision(field.getPrecision());
			setReadonly(field.isReadonly());
			setRelationClass(field.getRelationClass());
			setRelationTable(field.getRelationTable());
			setRequired(field.isRequired());
			if (field.getType() != null) {
				setType(field.getType().getName());
			}
			setValue(field.getValue());
			if (field.getRelationField() != null) {
				setRelationField(field.getRelationField().getName());
			}
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean isRequired() {
			return required;
		}

		public void setRequired(boolean required) {
			this.required = required;
		}

		public FieldDataType getDataType() {
			return dataType;
		}

		public void setDataType(FieldDataType dataType) {
			this.dataType = dataType;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getRelationClass() {
			return relationClass;
		}

		public void setRelationClass(String relationClass) {
			this.relationClass = relationClass;
		}

		public String getRelationTable() {
			return relationTable;
		}

		public void setRelationTable(String relationTable) {
			this.relationTable = relationTable;
		}

		public int getMinLength() {
			return minLength;
		}

		public void setMinLength(int minLength) {
			this.minLength = minLength;
		}

		public int getMaxLength() {
			return maxLength;
		}

		public void setMaxLength(int maxLength) {
			this.maxLength = maxLength;
		}

		public int getPrecision() {
			return precision;
		}

		public void setPrecision(int precision) {
			this.precision = precision;
		}

		public String getMask() {
			return mask;
		}

		public void setMask(String mask) {
			this.mask = mask;
		}

		public String getLinkUrl() {
			return linkUrl;
		}

		public void setLinkUrl(String linkUrl) {
			this.linkUrl = linkUrl;
		}

		public boolean isReadonly() {
			return readonly;
		}

		public void setReadonly(boolean readonly) {
			this.readonly = readonly;
		}

		public String getLabelField() {
			return labelField;
		}

		public void setLabelField(String labelField) {
			this.labelField = labelField;
		}

		public boolean isIdField() {
			return isIdField;
		}

		public void setIdField(boolean isIdField) {
			this.isIdField = isIdField;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getRelationField() {
			return relationField;
		}

		public void setRelationField(String relationField) {
			this.relationField = relationField;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	}

	public static String[] getNamesFromFeildDefinitions(FieldDefinition[] fieldDefinitions) {
		List<String> names = new ArrayList<>();
		if (ArrayUtils.isNotEmpty(fieldDefinitions)) {
			for (FieldDefinition fieldDefinition : fieldDefinitions) {
				names.add(fieldDefinition.getName());
			}
		}
		return names.toArray(new String[] {});
	}

	public static FieldDefinition[] buildFieldDefinitionsFromNames(EntityMetaData metaData, String[] fields) {
		List<FieldDefinition> fieldDefinitions = new ArrayList<>();
		if (ArrayUtils.isNotEmpty(fields)) {
			for (String name : fields) {
				FieldDefinition field = metaData.getField(name);
				if (field == null) {
					field = new FieldDefinition(null, name);
				} else {
					// TODO，操作字段和自动关联属性字段
				}
				fieldDefinitions.add(field);
			}
		}
		return fieldDefinitions.toArray(new FieldDefinition[] {});
	}

	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	public Map<String, FKFieldDefinition> getFkFieldsMap() {
		return fkFieldsMap;
	}

	public void setFkFieldsMap(Map<String, FKFieldDefinition> fkFieldsMap) {
		this.fkFieldsMap = fkFieldsMap;
	}
}
