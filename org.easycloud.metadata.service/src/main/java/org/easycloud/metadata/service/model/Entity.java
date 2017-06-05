package org.easycloud.metadata.service.model;

import java.util.HashMap;
import java.util.Map;

import org.easycloud.common.entity.BaseEntity;
import org.springframework.data.annotation.Id;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "实体，对应数据库表")
public class Entity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5658620484676631740L;
	@Id
	@ApiModelProperty(value = "主键", required = true)
	private EntityKey key;
	@ApiModelProperty(value = "类别", required = true)
	private String category;
	@ApiModelProperty(value = "实体名，API名称，内部引用时使用，保存后不能再修改", required = true)
	private String name;
	@ApiModelProperty(value = "Schema名", required = true)
	private String schema;
	@ApiModelProperty(value = "表在数据库中的名称", required = true)
	private String dbName;
	@ApiModelProperty(value = "显示名称", required = true)
	private String title;
	@ApiModelProperty(value = "描述")
	private String description;
	@ApiModelProperty(value = "字段列表")
	private Map<String, Field> fields = new HashMap<>();
	@ApiModelProperty(value="索引")
	private Map<String, Index> indexes = new HashMap<>();
	/**
	 * 显示字段，用于标记记录简要显示时显示的字段
	 */
	private String labelField;
	private CommonSubTableType[]  commonSubTables;
	/**
	 * 是否创建索引
	 */
	private boolean createIndex;
	/**
	 * 索引名称
	 */
	private String indexName;

	public Entity(EntityKey key) {
		setKey(key);
	}
	
	public Entity() {
	}
	
	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, Field> getFields() {
		return fields;
	}

	public void setFields(Map<String, Field> fields) {
		this.fields = fields;
	}

	public EntityKey getKey() {
		return key;
	}

	public void setKey(EntityKey key) {
		this.key = key;
		setCategory(key.getCategory());
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Map<String, Index> getIndexes() {
		return indexes;
	}

	public void setIndexes(Map<String, Index> indexes) {
		this.indexes = indexes;
	}

	public String getLabelField() {
		return labelField;
	}

	public void setLabelField(String labelField) {
		this.labelField = labelField;
	}

	public CommonSubTableType[] getCommonSubTables() {
		return commonSubTables;
	}

	public void setCommonSubTables(CommonSubTableType[] commonSubTables) {
		this.commonSubTables = commonSubTables;
	}

	public boolean isCreateIndex() {
		return createIndex;
	}

	public void setCreateIndex(boolean createIndex) {
		this.createIndex = createIndex;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
