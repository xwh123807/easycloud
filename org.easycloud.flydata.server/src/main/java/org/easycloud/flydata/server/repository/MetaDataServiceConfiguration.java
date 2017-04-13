package org.easycloud.flydata.server.repository;

import org.easycloud.metadata.service.client.DataSourceApiClient;
import org.easycloud.metadata.service.client.EntityMetaDataApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetaDataServiceConfiguration {
	@Value("${metadata.server}")
	private String metaDataServer;
	
	@Bean
	public EntityMetaDataApiClient newEntityMetaDataApiClient(){
		EntityMetaDataApiClient client = new EntityMetaDataApiClient(metaDataServer);
		return client;
	}
	
	@Bean
	public DataSourceApiClient newDataSourceApiClient(){
		DataSourceApiClient client = new DataSourceApiClient(metaDataServer);
		return client;
	}
}
