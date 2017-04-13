package org.easycloud.flydata.server.impl;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

public class DataSourceUtil {
	/**
	 * 数据源，本地缓存
	 */
	private static Map<String, DataSource> dataSources = new HashMap<>();
	
	public static DataSource getDataSource(String name){
		return null;
	}
	
	public static Connection getConnection(String dataSourceName){
		return null;
	}
}
