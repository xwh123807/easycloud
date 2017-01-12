package org.easycloud.platform.core.flydata.service;

public class Search {
	private boolean regex;
	private String value;
	
	public Search(String value) {
		this.value = value;
	}
	
	public Search(String value, boolean regex) {
		this.value = value;
		this.regex = regex;
	}
	
	public boolean isRegex() {
		return regex;
	}
	public void setRegex(boolean regex) {
		this.regex = regex;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
