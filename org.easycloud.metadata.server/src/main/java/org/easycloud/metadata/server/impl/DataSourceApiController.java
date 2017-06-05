package org.easycloud.metadata.server.impl;

import java.util.List;

import org.easycloud.common.entity.EntityExistsException;
import org.easycloud.common.entity.EntityNotExistsException;
import org.easycloud.metadata.server.repository.DataSourceRepository;
import org.easycloud.metadata.service.api.DataSourceApi;
import org.easycloud.metadata.service.model.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataSourceApiController implements DataSourceApi {
	@Autowired
	private DataSourceRepository dataSourceRepository;

	@Override
	public List<DataSource> findAll() {
		return dataSourceRepository.findAll();
	}

	@Override
	public void createDataSource(@RequestBody DataSource dataSource) {
		if (dataSourceRepository.exists(dataSource.getName())) {
			throw new EntityExistsException(dataSource);
		} else {
			dataSourceRepository.save(dataSource);
		}
	}

	@Override
	public DataSource findByName(@PathVariable("name") String name) {
		return dataSourceRepository.findOne(name);
	}

	@Override
	public void deleteDataSource(@PathVariable("name") String name) {
		dataSourceRepository.delete(name);
	}

	@Override
	public void updateDataSource(@PathVariable("name") String name, @RequestBody DataSource dataSource) {
		if (!dataSourceRepository.exists(name)) {
			throw new EntityNotExistsException(dataSource);
		}
		dataSourceRepository.save(dataSource);
	}

}
