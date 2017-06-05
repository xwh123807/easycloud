package org.easycloud.metadata.service.client;

import java.util.Arrays;
import java.util.List;

import org.easycloud.common.rest.BaseApiClient;
import org.easycloud.metadata.service.api.EntityMetaDataApi;
import org.easycloud.metadata.service.model.Entity;

public class EntityMetaDataApiClient extends BaseApiClient implements EntityMetaDataApi{
	
	public EntityMetaDataApiClient(String baseUrl) {
		super(baseUrl + "/entity/");
	}
	
	@Override
	public List<Entity> findAll() {
		Entity[] items = getForObject("", Entity[].class);
		return Arrays.asList(items);
	}
	
	@Override
	public List<Entity> findByCategory(String category) {
		Entity[] items = getForObject("{category}", Entity[].class, category);
		return Arrays.asList(items);
	}
	
	@Override
	public Entity getMetaData(String category, String entityName) {
		return getForObject("{category}/{entityName}", Entity.class, category, entityName);
	}
	
	
	@Override
	public void newMetaData(Entity entity) {
		postForEntity("", entity, Void.class);
	}

	@Override
	public void updateMetaData(String category, String entityName, Entity entity) {
		put("{category}/{entityName}", entity, category, entityName);
	}

	@Override
	public void deleteMetaData(String category, String entityName) {
		delete("{category}/{entityName}", category, entityName);
	}

}
