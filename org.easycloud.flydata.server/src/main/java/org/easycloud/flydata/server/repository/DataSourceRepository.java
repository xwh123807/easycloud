package org.easycloud.flydata.server.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DataSourceRepository {
	/**
	 * 缓存DataSource
	 */
	private static Map<String, DataSource> dataSources = new HashMap<>();
	
	@Autowired
	private MetaDataRepository metaDataRepository;
	
	/**
	 * 获取数据源
	 * @param dataSource
	 * @return
	 */
	public DataSource getDataSource(String dataSourceName) throws Exception{
		if (!dataSources.containsKey(dataSourceName)){
			org.easycloud.metadata.service.model.DataSource ds = metaDataRepository.getDataSource(dataSourceName); 
			Properties p = new Properties();
			p.setProperty("driverClassName", ds.getDriverClass());// 连接mysql，加载mysql驱动。
			p.setProperty("url", ds.getUrl());// 连接mysql的url，test为测试数据库。
			p.setProperty("password", ds.getPassword());// 连接数据库的密码
			p.setProperty("username", ds.getUser());// 连接数据库的用户名
			p.setProperty("maxActive", "30");// 最大连接数
			p.setProperty("maxIdle", "10"); // 最大空闲连接
			p.setProperty("maxWait", "1000");// 超时等待时间以毫秒为单位
			p.setProperty("removeAbandoned", "false");
			p.setProperty("removeAbandonedTimeout", "120");// 超时时间(以秒数为单位)
			p.setProperty("testOnBorrow", "true");
			p.setProperty("logAbandoned", "true");
			DataSource dataSource = BasicDataSourceFactory.createDataSource(p);
			dataSources.put(dataSourceName, dataSource);
		}
		return dataSources.get(dataSourceName);
	}
	
}
