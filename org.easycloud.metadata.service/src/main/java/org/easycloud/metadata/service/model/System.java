package org.easycloud.metadata.service.model;

import org.easycloud.common.entity.BaseEntity;
import org.springframework.data.annotation.Id;

public class System extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5211895285581822148L;
	@Id
	private String name;
	private String title;
	private String description;
	private String[] category;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getCategory() {
		return category;
	}

	public void setCategory(String[] category) {
		this.category = category;
	}
}
