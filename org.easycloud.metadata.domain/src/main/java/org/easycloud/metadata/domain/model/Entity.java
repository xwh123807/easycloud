package org.easycloud.metadata.domain.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "实体，对应数据库表")
public class Entity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5658620484676631740L;
	@Id
	@ApiModelProperty(value = "主键", required = true)
	private EntityKey key;
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
	}
	
	@Override
	public String toString() {
		return JSONUtil.toJSON(this);
	}
}
