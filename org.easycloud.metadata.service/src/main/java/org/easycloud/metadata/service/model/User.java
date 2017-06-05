package org.easycloud.metadata.service.model;

import org.springframework.data.annotation.Id;

public class User {
	@Id
	private String category;
	@Id
	private String name;
	
	private String description;
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
