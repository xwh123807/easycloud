package org.myfly.platform.core.domain;

public enum DBType implements BaseEnum {
	ORACLE("ORACLE"),
	MYSQL("MYSQL"),
	DB2("DB2"),
	H2("H2"),
	DERBY("DERBY"),
	DERBY_EMBEDDED("内嵌DERBY");
	
	private String title;

	private DBType(String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}
}
