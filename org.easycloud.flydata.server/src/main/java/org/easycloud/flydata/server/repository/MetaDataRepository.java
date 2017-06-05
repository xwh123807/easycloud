package org.easycloud.flydata.server.repository;

import org.easycloud.metadata.service.client.DataSourceApiClient;
import org.easycloud.metadata.service.client.EntityMetaDataApiClient;
import org.easycloud.metadata.service.model.DataSource;
import org.easycloud.metadata.service.model.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MetaDataRepository {
	@Autowired
	private DataSourceApiClient dataSourceApiClient;
	
	@Autowired
	private EntityMetaDataApiClient entityMetaDataApiClient;
	/**
	 * 获取数据源定义信息
	 * @param name
	 * @return
	 */
	public DataSource getDataSource(String name){
		return dataSourceApiClient.findByName(name);
	}
	
	/**
	 * 获取实体定义元模型
	 * @param category
	 * @param name
	 * @return
	 */
	public Entity getEntityMetaData(String category, String entityName){
		return entityMetaDataApiClient.getMetaData(category, entityName);
	}
}
