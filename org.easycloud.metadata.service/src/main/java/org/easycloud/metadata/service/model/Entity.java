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
}
