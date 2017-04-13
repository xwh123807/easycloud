package org.easycloud.metadata.service.client;

import java.util.Arrays;
import java.util.List;

import org.easycloud.common.rest.BaseApiClient;
import org.easycloud.metadata.service.api.DataSourceApi;
import org.easycloud.metadata.service.model.DataSource;

public class DataSourceApiClient extends BaseApiClient implements DataSourceApi{

	public DataSourceApiClient(String baseUrl) {
		super(baseUrl + "/datasource/");
	}

	@Override
	public List<DataSource> findAll() {
		DataSource[] items = getForObject("", DataSource[].class);
		return Arrays.asList(items);
	}

	@Override
	public void createDataSource(DataSource dataSource) {
		postForEntity("", dataSource, Void.class);
	}

	@Override
	public DataSource findByName(String name) {
		return getForObject("{name}", DataSource.class, name);
	}

	@Override
	public void deleteDataSource(String name) {
		delete("{name}", name);
	}

	@Override
	public void updateDataSource(String name, DataSource dataSource) {
		put("{name}", dataSource, name);
	}

}
