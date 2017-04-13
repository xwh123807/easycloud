package org.easycloud.metadata.service.model;

import org.easycloud.common.entity.BaseEntity;
import org.springframework.data.annotation.Id;

public class DataSource extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2544655442044500880L;
	@Id
	private String name;
	private String title;
	private DBType dbType;
	private String dbName;
	private String host;
	private String port;
	private String user;
	private String password;
	private String system;

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

	public DBType getDbType() {
		return dbType;
	}

	public void setDbType(DBType dbType) {
		this.dbType = dbType;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}
}
