package org.easycloud.metadata.server.impl;

import java.util.List;

import org.easycloud.common.entity.EntityExistsException;
import org.easycloud.common.entity.EntityNotExistsException;
import org.easycloud.metadata.server.repository.SystemRepository;
import org.easycloud.metadata.service.api.SystemApi;
import org.easycloud.metadata.service.model.System;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemApiController implements SystemApi {
	@Autowired
	private SystemRepository systemRepository;

	@Override
	public List<System> findAll() {
		return systemRepository.findAll();
	}

	@Override
	public void createSystem(@RequestBody System system) {
		if (systemRepository.exists(system.getName())){
			throw new EntityExistsException(system);
		}
		systemRepository.save(system);
	}

	@Override
	public System findByName(@PathVariable("name") String name) {
		return systemRepository.findOne(name);
	}

	@Override
	public void deleteSystem(@PathVariable("name") String name) {
		systemRepository.delete(name);
	}

	@Override
	public void updateSystem(@PathVariable("name") String name, @RequestBody System system) {
		if (systemRepository.exists(name)) {
			throw new EntityNotExistsException(system);
		} else {
			systemRepository.save(system);
		}
	}

}
