package org.easycloud.metadata.service.model;

import org.easycloud.common.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "字段，实体属性，存储数据库字段信息")
public class Field extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7970361938689870789L;
	@ApiModelProperty(value = "字段名，内部名称", required = true)
	private String name;
	@ApiModelProperty(value = "字段名，在数据库中的名称", required = true)
	private String dbName;
	@ApiModelProperty(value = "显示名称", required = true)
	private String title;
	@ApiModelProperty(value = "描述")
	private String description;

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
}
