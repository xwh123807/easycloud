package org.easycloud.metadata.domain.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "表，对应数据库实体")
public class Table implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5658620484676631740L;
	@Id
	@ApiModelProperty(value = "主键", required = true)
	private TableKey key;
	@ApiModelProperty(value = "Schema名", required = true)
	private String schema;
	@ApiModelProperty(value = "表名，API名称，内部引用时使用，保存后不能再修改", required = true)
	private String name;
	@ApiModelProperty(value = "表在数据库中的名称", required = true)
	private String dbName;
	@ApiModelProperty(value = "显示名称", required = true)
	private String title;
	@ApiModelProperty(value = "描述")
	private String description;
	@ApiModelProperty(value = "字段列表")
	private Map<String, Field> fields = new HashMap<>();

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public TableKey getKey() {
		return key;
	}

	public void setKey(TableKey key) {
		this.key = key;
	}
	
	@Override
	public String toString() {
		return JSONUtil.toJSON(this);
	}
}
