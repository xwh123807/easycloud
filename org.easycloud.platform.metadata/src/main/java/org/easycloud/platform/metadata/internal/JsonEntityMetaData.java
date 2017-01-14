package org.easycloud.platform.metadata.internal;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.metadata.annotation.entity.FieldDataType;
import org.easycloud.platform.metadata.annotation.view.EntityAction;
import org.easycloud.platform.metadata.define.entity.DefaultGetFieldValueHandler;
import org.easycloud.platform.metadata.define.entity.FKFieldDefinition;
import org.easycloud.platform.metadata.define.entity.FieldDefinition;
import org.easycloud.platform.metadata.define.entity.PKFieldDefinition;
import org.easycloud.platform.metadata.define.entity.PKFieldDefinition.KeyType;
import org.easycloud.platform.metadata.define.entity.SearchRelationGetFieldValueHandler;
import org.easycloud.platform.metadata.define.entity.TableDefinition;
import org.easycloud.platform.metadata.define.view.DivDefinition;
import org.easycloud.platform.metadata.define.view.FieldSetDefinition;
import org.easycloud.platform.metadata.define.view.FilterDefinition;
import org.easycloud.platform.metadata.define.view.FormDefinition;
import org.easycloud.platform.metadata.define.view.ListDefinition;
import org.easycloud.platform.metadata.define.view.OrderDefinition;
import org.easycloud.platform.metadata.define.view.SQLOperator;
import org.easycloud.platform.metadata.define.view.SectionDefinition;
import org.easycloud.platform.metadata.define.view.SectionType;
import org.easycloud.platform.metadata.define.view.SubTableDefinition;
import org.easycloud.platform.metadata.service.EntityMetaData;
import org.easycloud.platform.metadata.utils.JSONUtil;

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
	 * 列表视图定义
	 */
	private Map<String, JsonListDefinition> listDefinitions;
	/**
	 * 表单实体定义
	 */
	private Map<String, JsonFormDefinition> formDefinitions;
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
		listDefinitions = new LinkedHashMap<>();
		for (ListDefinition listDefinition : metaData.getListDefinitions().values()) {
			JsonListDefinition jsonList = new JsonListDefinition(listDefinition);
			listDefinitions.put(jsonList.getName(), jsonList);
		}
		formDefinitions = new LinkedHashMap<>();
		for (FormDefinition formDefinition : metaData.getFormDefinitions().values()) {
			JsonFormDefinition jsonForm = new JsonFormDefinition(formDefinition);
			formDefinitions.put(jsonForm.getName(), jsonForm);
		}
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
		// listDefinitions
		Map<String, ListDefinition> listDefinitions = new LinkedHashMap<>();
		if (MapUtils.isNotEmpty(getListDefinitions())) {
			for (JsonListDefinition jsonListDefinition : getListDefinitions().values()) {
				ListDefinition listDefinition = new ListDefinition(metaData);
				jsonListDefinition.convert(metaData, listDefinition);
				listDefinitions.put(listDefinition.getName(), listDefinition);
			}
		}
		metaData.setListDefinitions(listDefinitions);
		metaData.buildListDefinition();
		// formDefinitions
		Map<String, FormDefinition> formDefinitions = new LinkedHashMap<>();
		if (MapUtils.isNotEmpty(getFormDefinitions())) {
			for (JsonFormDefinition jsonFormDefinition : getFormDefinitions().values()) {
				FormDefinition formDefinition = jsonFormDefinition.convert(metaData);
				formDefinitions.put(formDefinition.getName(), formDefinition);
			}
		}
		metaData.setFormDefinitions(formDefinitions);
		metaData.buildFormDefinitions();
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

	public Map<String, JsonListDefinition> getListDefinitions() {
		return listDefinitions;
	}

	public void setListDefinitions(Map<String, JsonListDefinition> listDefinitions) {
		this.listDefinitions = listDefinitions;
	}

	public Map<String, JsonFormDefinition> getFormDefinitions() {
		return formDefinitions;
	}

	public void setFormDefinitions(Map<String, JsonFormDefinition> formDefinitions) {
		this.formDefinitions = formDefinitions;
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

	public static class JsonListDefinition {
		private String name;
		/**
		 * 标题，显示在Box header上
		 */
		private String title;
		/**
		 * 表格表头，一般用于复杂表头
		 */
		private String header;
		/**
		 * 数据源主实体名称
		 */
		private String entityName;
		/**
		 * 子表属性，子表场景才需要赋值
		 */
		private String subTableAttr;
		/**
		 * 表格取数是否异步Ajax处理，如果是，则由客户端再次发送请求获取数据，用于大数据量的分页；如果为否，页面直接输出全部数据
		 */
		private boolean serverSideMode;
		/**
		 * 
		 */
		private boolean enableActions;
		/**
		 * 实体列表操作集
		 */
		private EntityAction[] listActions;
		/**
		 * 实体项操作集
		 */
		private EntityAction[] itemActions;
		/**
		 * @return
		 */
		private String labelField;
		/**
		 * 排序定义
		 */
		private OrderDefinition[] orders;
		/**
		 * 字段列表定义
		 */
		private String[] fields;
		/**
		 * 过滤条件定义
		 */
		private JsonFilterDefinition[] filters;

		public JsonListDefinition() {
		}

		public void convert(EntityMetaData metaData, ListDefinition listDefinition) {
			listDefinition.setName(getName());
			listDefinition.setTitle(getTitle());
			listDefinition.setEnableActions(isEnableActions());
			listDefinition.setHeader(getHeader());
			listDefinition.setLabelField(getLabelField());
			listDefinition.setItemActions(getItemActions());
			listDefinition.setListActions(getListActions());

			listDefinition.setOrders(getOrders());
			listDefinition.setServerSideMode(isServerSideMode());
			listDefinition.setSubTableAttr(getSubTableAttr());

			// fields
			listDefinition.setFields(buildFieldDefinitionsFromNames(metaData, getFields()));

			List<FilterDefinition> filterDefinitions = new ArrayList<>();
			if (ArrayUtils.isNotEmpty(getFilters())) {
				for (JsonFilterDefinition jsonFilter : getFilters()) {
					FilterDefinition filter = jsonFilter.convert(listDefinition);
					filterDefinitions.add(filter);
				}
			}
			listDefinition.setFilters(filterDefinitions.toArray(new FilterDefinition[] {}));
		}

		public JsonListDefinition(ListDefinition listDefinition) {
			setEnableActions(listDefinition.isEnableActions());
			setHeader(listDefinition.getHeader());
			setItemActions(listDefinition.getItemActions());
			setLabelField(listDefinition.getLabelField());
			setListActions(listDefinition.getListActions());
			setName(listDefinition.getName());
			setOrders(listDefinition.getOrders());
			setServerSideMode(listDefinition.isServerSideMode());
			setSubTableAttr(listDefinition.getSubTableAttr());
			setEntityName(listDefinition.getEntityName());
			setTitle(listDefinition.getTitle());
			setFields(getNamesFromFeildDefinitions(listDefinition.getFields()));
			if (ArrayUtils.isNotEmpty(listDefinition.getFilters())) {
				List<JsonFilterDefinition> jsonFilters = new ArrayList<>();
				for (FilterDefinition filter : listDefinition.getFilters()) {
					JsonFilterDefinition jsonFilter = new JsonFilterDefinition(filter);
					jsonFilters.add(jsonFilter);
				}
				setFilters(jsonFilters.toArray(new JsonFilterDefinition[] {}));
			}
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getHeader() {
			return header;
		}

		public void setHeader(String header) {
			this.header = header;
		}

		public String getSubTableAttr() {
			return subTableAttr;
		}

		public void setSubTableAttr(String subTableAttr) {
			this.subTableAttr = subTableAttr;
		}

		public boolean isServerSideMode() {
			return serverSideMode;
		}

		public void setServerSideMode(boolean serverSideMode) {
			this.serverSideMode = serverSideMode;
		}

		public boolean isEnableActions() {
			return enableActions;
		}

		public void setEnableActions(boolean enableActions) {
			this.enableActions = enableActions;
		}

		public String getLabelField() {
			return labelField;
		}

		public void setLabelField(String labelField) {
			this.labelField = labelField;
		}

		public OrderDefinition[] getOrders() {
			return orders;
		}

		public void setOrders(OrderDefinition[] orders) {
			this.orders = orders;
		}

		public String[] getFields() {
			return fields;
		}

		public void setFields(String[] fields) {
			this.fields = fields;
		}

		public JsonFilterDefinition[] getFilters() {
			return filters;
		}

		public void setFilters(JsonFilterDefinition[] filters) {
			this.filters = filters;
		}

		public EntityAction[] getListActions() {
			return listActions;
		}

		public void setListActions(EntityAction[] listActions) {
			this.listActions = listActions;
		}

		public EntityAction[] getItemActions() {
			return itemActions;
		}

		public void setItemActions(EntityAction[] itemActions) {
			this.itemActions = itemActions;
		}

		public String getEntityName() {
			return entityName;
		}

		public void setEntityName(String entityName) {
			this.entityName = entityName;
		}

	}

	public static class JsonFilterDefinition {
		private String name;
		private String field;
		private Object value;
		private SQLOperator operator;
		private boolean show = true;

		private String fieldName;
		private Object[] multiValues;

		public JsonFilterDefinition() {
		}

		public FilterDefinition convert(ListDefinition listDefinition) {
			FilterDefinition filter = new FilterDefinition(getFieldName(), getOperator());
			filter.setField(((EntityMetaData) listDefinition.getParent()).getField(getName()));
			filter.setMultiValues(getMultiValues());
			filter.setName(getName());
			filter.setShow(isShow());
			filter.setValue(getValue());
			return filter;
		}

		public JsonFilterDefinition(FilterDefinition filter) {
			if (filter.getField() != null) {
				setField(filter.getField().getName());
				setFieldName(filter.getFieldName());
				setMultiValues(filter.getMultiValues());
				setName(filter.getName());
				setOperator(filter.getOperator());
				setShow(filter.isShow());
				setValue(filter.getValue());
			}
		}

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public SQLOperator getOperator() {
			return operator;
		}

		public void setOperator(SQLOperator operator) {
			this.operator = operator;
		}

		public boolean isShow() {
			return show;
		}

		public void setShow(boolean show) {
			this.show = show;
		}

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		public Object[] getMultiValues() {
			return multiValues;
		}

		public void setMultiValues(Object[] multiValues) {
			this.multiValues = multiValues;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public static class JsonFormDefinition {
		private String name;
		private DivDefinition[] divs;
		private JsonSectionDefinition[] sections;

		public JsonFormDefinition() {
		}

		public FormDefinition convert(EntityMetaData metaData) {
			FormDefinition formDefinition = new FormDefinition(metaData, getName());
			List<SectionDefinition> sectionDefinitions = new ArrayList<>();
			for (JsonSectionDefinition jsonSectionDefinition : getSections()) {
				sectionDefinitions.add(jsonSectionDefinition.convert(formDefinition));
			}
			formDefinition.setSections(sectionDefinitions.toArray(new SectionDefinition[] {}));
			return formDefinition;
		}

		public JsonFormDefinition(FormDefinition formDefinition) {
			setName(formDefinition.getName());
			setDivs(formDefinition.getDivs());
			List<JsonSectionDefinition> jsonSections = new ArrayList<>();
			for (SectionDefinition section : formDefinition.getSections()) {
				JsonSectionDefinition jsonSection = new JsonSectionDefinition(section);
				jsonSections.add(jsonSection);
			}
			setSections(jsonSections.toArray(new JsonSectionDefinition[] {}));
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public JsonSectionDefinition[] getSections() {
			return sections;
		}

		public void setSections(JsonSectionDefinition[] sections) {
			this.sections = sections;
		}

		public DivDefinition[] getDivs() {
			return divs;
		}

		public void setDivs(DivDefinition[] divs) {
			this.divs = divs;
		}

	}

	public static class JsonSectionDefinition {
		private String name;
		private String title;
		private SectionType type;
		private JsonFieldSetDefinition[] fieldSets;
		private JsonSubTableDefinition[] subTables;

		public JsonSectionDefinition() {
		}

		public SectionDefinition convert(FormDefinition formDefinition) {
			SectionDefinition sectionDefinition = new SectionDefinition(formDefinition, getTitle());
			sectionDefinition.setName(getName());
			sectionDefinition.setType(getType());
			// fieldSets
			if (ArrayUtils.isNotEmpty(getFieldSets())) {
				List<FieldSetDefinition> fieldSetDefinitions = new ArrayList<>();
				for (JsonFieldSetDefinition jsonFieldDefinition : getFieldSets()) {
					fieldSetDefinitions.add(jsonFieldDefinition.convert((EntityMetaData) formDefinition.getParent()));
				}
				sectionDefinition.setFieldSets(fieldSetDefinitions.toArray(new FieldSetDefinition[] {}));
			}
			// subTables
			if (ArrayUtils.isNotEmpty(getSubTables())) {
				List<SubTableDefinition> subTableDefinitions = new ArrayList<>();
				for (JsonSubTableDefinition jsonSubTableDefinition : getSubTables()) {
					SubTableDefinition subTableDefinition = new SubTableDefinition(
							(EntityMetaData) formDefinition.getParent(), null);
					jsonSubTableDefinition.convert((EntityMetaData) formDefinition.getParent(), subTableDefinition);
					subTableDefinitions.add(subTableDefinition);
				}
				sectionDefinition.setSubTables(subTableDefinitions.toArray(new SubTableDefinition[] {}));
			}
			return sectionDefinition;
		}

		public JsonSectionDefinition(SectionDefinition section) {
			setName(section.getName());
			setTitle(section.getTitle());
			setType(section.getType());
			if (ArrayUtils.isNotEmpty(section.getFieldSets())) {
				List<JsonFieldSetDefinition> jsonFieldSetList = new ArrayList<>();
				for (FieldSetDefinition fieldSetDefinition : section.getFieldSets()) {
					JsonFieldSetDefinition jsonFieldSetDefinition = new JsonFieldSetDefinition(fieldSetDefinition);
					jsonFieldSetList.add(jsonFieldSetDefinition);
				}
				setFieldSets(jsonFieldSetList.toArray(new JsonFieldSetDefinition[] {}));
			}
			if (ArrayUtils.isNotEmpty(section.getSubTables())) {
				List<JsonSubTableDefinition> jsonSubTables = new ArrayList<>();
				for (SubTableDefinition subTableDefinition : section.getSubTables()) {
					JsonSubTableDefinition jsonSubTable = new JsonSubTableDefinition(subTableDefinition);
					jsonSubTables.add(jsonSubTable);
				}
				setSubTables(jsonSubTables.toArray(new JsonSubTableDefinition[] {}));
			}
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public JsonFieldSetDefinition[] getFieldSets() {
			return fieldSets;
		}

		public void setFieldSets(JsonFieldSetDefinition[] fieldSets) {
			this.fieldSets = fieldSets;
		}

		public JsonSubTableDefinition[] getSubTables() {
			return subTables;
		}

		public void setSubTables(JsonSubTableDefinition[] subTables) {
			this.subTables = subTables;
		}

		public SectionType getType() {
			return type;
		}

		public void setType(SectionType type) {
			this.type = type;
		}

	}

	public static class JsonFieldSetDefinition {
		private String title;
		private String[] fields;

		public JsonFieldSetDefinition() {
		}

		public FieldSetDefinition convert(EntityMetaData metaData) {
			FieldSetDefinition fieldSetDefinition = new FieldSetDefinition(getTitle());
			fieldSetDefinition.setFields(buildFieldDefinitionsFromNames(metaData, getFields()));
			return fieldSetDefinition;
		}

		public JsonFieldSetDefinition(FieldSetDefinition fieldSetDefinition) {
			setTitle(fieldSetDefinition.getTitle());
			setFields(getNamesFromFeildDefinitions(fieldSetDefinition.getFields()));
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String[] getFields() {
			return fields;
		}

		public void setFields(String[] fields) {
			this.fields = fields;
		}

	}

	public static class JsonSubTableDefinition extends JsonListDefinition {
		private String refName;

		public JsonSubTableDefinition() {
		}

		public JsonSubTableDefinition(SubTableDefinition subTableDefinition) {
			super(subTableDefinition);
			setRefName(subTableDefinition.getRefName());
		}

		public String getRefName() {
			return refName;
		}

		public void setRefName(String refName) {
			this.refName = refName;
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
