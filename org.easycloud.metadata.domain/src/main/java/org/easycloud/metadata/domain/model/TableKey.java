package org.easycloud.metadata.domain.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class TableKey implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5146336859753761523L;
	@ApiModelProperty(value = "Schema名", required = true)
	private String schema;
	@ApiModelProperty(value = "表名，API名称，内部引用时使用，保存后不能再修改", required = true)
	private String name;
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
	public TableKey(String schema, String name) {
		super();
		this.schema = schema;
		this.name = name;
	}
	@Override
	public String toString() {
		return JSONUtil.toJSON(this);
	}
}
