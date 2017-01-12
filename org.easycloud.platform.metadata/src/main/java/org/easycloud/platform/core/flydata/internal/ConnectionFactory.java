package org.easycloud.platform.core.flydata.internal;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.myfly.platform.core.flydata.service.DataSourceInfo;

public class ConnectionFactory {
	/**
	 * 缓存DataSource
	 */
	private static Map<String, DataSource> dataSources = new HashMap<>();

	public static Connection getConnection(DataSourceInfo currentDataSource) throws Exception {
		DataSource dataSource = getDataSource(currentDataSource);
		return dataSource.getConnection();
	}

	/**
	 * 获取DataSource，首先从缓存中查找，找不到才创建
	 * 
	 * @param currentDataSource
	 * @return
	 * @throws Exception
	 */
	public static DataSource getDataSource(DataSourceInfo ds) throws Exception {
		String key = ds.getUrl() + "-" + ds.getUsername();
		if (!dataSources.containsKey(key)) {
			Properties p = new Properties();
			p.setProperty("driverClassName", ds.getDriverClassName());// 连接mysql，加载mysql驱动。
			p.setProperty("url", ds.getUrl());// 连接mysql的url，test为测试数据库。
			p.setProperty("password", ds.getPassword());// 连接数据库的密码
			p.setProperty("username", ds.getUsername());// 连接数据库的用户名
			p.setProperty("maxActive", "30");// 最大连接数
			p.setProperty("maxIdle", "10"); // 最大空闲连接
			p.setProperty("maxWait", "1000");// 超时等待时间以毫秒为单位
			p.setProperty("removeAbandoned", "false");
			p.setProperty("removeAbandonedTimeout", "120");// 超时时间(以秒数为单位)
			p.setProperty("testOnBorrow", "true");
			p.setProperty("logAbandoned", "true");
			DataSource dataSource = BasicDataSourceFactory.createDataSource(p);
			dataSources.put(key, dataSource);
		}
		return dataSources.get(key);
	}

}
