package org.easycloud.metadata.server.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.easycloud.common.entity.EntityExistsException;
import org.easycloud.common.entity.EntityNotExistsException;
import org.easycloud.metadata.server.repository.EntityRepository;
import org.easycloud.metadata.service.api.EntityMetaDataApi;
import org.easycloud.metadata.service.model.Entity;
import org.easycloud.metadata.service.model.EntityKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntityMetaDataApiController implements EntityMetaDataApi {
	@Autowired
	private EntityRepository entityRepository;

	private boolean checkParam(String category, String entityName) {
		return (StringUtils.isEmpty(category) || StringUtils.isEmpty(entityName));
	}

	@Override
	public List<Entity> findAll() {
		List<Entity> items = entityRepository.findAll();
		return items;
	}

	@Override
	public List<Entity> findByCategory(@PathVariable("category") String category) {
		return entityRepository.findByCategory(category);
	}

	@Override
	public Entity getMetaData(@PathVariable("category") String category,
			@PathVariable("entityName") String entityName) {
		Entity entity = entityRepository.findOne(new EntityKey(category, entityName));
		return entity;
	}

	@Override
	public void newMetaData(@RequestBody Entity entity) {
		if (entityRepository.exists(entity.getKey())) {
			throw new EntityExistsException(entity);
		}
		entityRepository.save(entity);
	}

	@Override
	public void updateMetaData(@PathVariable("category") String category, @PathVariable("entityName") String entityName,
			@RequestBody Entity entity) {
		if (checkParam(category, entityName)) {
		}
		EntityKey key = new EntityKey(category, entityName);
		entity.setKey(key);
		if (!entityRepository.exists(key)) {
			throw new EntityNotExistsException(entity);
		}
		entityRepository.save(entity);
	}

	@Override
	public void deleteMetaData(@PathVariable("category") String category,
			@PathVariable("entityName") String entityName) {
		if (checkParam(category, entityName)) {
		}
		EntityKey key = new EntityKey(category, entityName);
		if (!entityRepository.exists(key)) {
			throw new EntityNotExistsException(key);
		}
		entityRepository.delete(key);
	}

}
