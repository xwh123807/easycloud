package org.easycloud.metadata.server.impl;

import org.apache.commons.lang3.StringUtils;
import org.easycloud.metadata.domain.api.EntityMetaDataApi;
import org.easycloud.metadata.domain.model.Entity;
import org.easycloud.metadata.domain.model.EntityKey;
import org.easycloud.metadata.server.repository.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class EntityMetaDataApiController implements EntityMetaDataApi {
	@Autowired
	private EntityRepository entityRepository;

	private boolean checkParam(String category, String entityName) {
		return (StringUtils.isEmpty(category) || StringUtils.isEmpty(entityName));
	}

	@Override
	public ResponseEntity<Entity> getMetaData(@PathVariable("category") String category,
			@PathVariable("entityName") String entityName) {
		if (checkParam(category, entityName)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		;
		Entity entity = entityRepository.findOne(new EntityKey(category, entityName));
		if (entity != null) {
			return new ResponseEntity<Entity>(entity, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<Void> newMetaData(@RequestBody Entity entity) {
		if (entityRepository.exists(entity.getKey())) {
			return new ResponseEntity<>(HttpStatus.FOUND);
		}
		try {
			entityRepository.save(entity);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<Void> updateMetaData(@PathVariable("category") String category,
			@PathVariable("entityName") String entityName, @RequestBody Entity entity) {
		if (checkParam(category, entityName)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		;
		EntityKey key = new EntityKey(category, entityName);
		if (!entityRepository.exists(key)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		entity.setKey(key);
		try {
			entityRepository.save(entity);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<Void> deleteMetaData(@PathVariable("category") String category,
			@PathVariable("entityName") String entityName) {
		if (checkParam(category, entityName)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		EntityKey key = new EntityKey(category, entityName);
		if (!entityRepository.exists(key)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		try {
			entityRepository.delete(key);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
