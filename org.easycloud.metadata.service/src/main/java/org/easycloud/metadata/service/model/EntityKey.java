package org.easycloud.metadata.service.model;

import org.easycloud.common.entity.BaseEntity;

import io.swagger.annotations.ApiModelProperty;

public class EntityKey extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5146336859753761523L;
	@ApiModelProperty(value = "类别", required = true)
	private String category;
	@ApiModelProperty(value = "实体名，API名称，内部引用时使用，保存后不能再修改", required = true)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EntityKey() {

	}

	public EntityKey(String category, String name) {
		super();
		this.setCategory(category);
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
