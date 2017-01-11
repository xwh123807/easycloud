package org.myfly.platform.core.flydata.service;

import org.myfly.platform.core.domain.DBType;

/**
 * 数据源配置信息
 * @author xiangwanhong
 *
 */
public class DataSourceInfo {
	private DBType dbType;
	private String url;
	private String username;
	private String password;
	private String driverClassName;
	private String dialect;
	public DBType getDbType() {
		return dbType;
	}
	public void setDbType(DBType dbType) {
		this.dbType = dbType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public String getDialect() {
		return dialect;
	}
	public void setDialect(String dialect) {
		this.dialect = dialect;
	}
}
