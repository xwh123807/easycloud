package org.easycloud.platform.visualpage.define;

import org.easycloud.platform.metadata.define.FieldDefinition;

public class FieldSetDefinition {
	private String title;
	private FieldDefinition[] fields;
	
	public FieldSetDefinition(String title) {
		setTitle(title);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public FieldDefinition[] getFields() {
		return fields;
	}

	public void setFields(FieldDefinition[] fields) {
		this.fields = fields;
	}
	
	
	@Override
	public String toString() {
		return "title: " + getTitle() + ", fields: " + FieldDefinition.getNames(fields);
	}

	public void validate() {
	}
}
