package org.easycloud.metadata.service.api;

import java.util.List;

import org.easycloud.metadata.service.model.DataSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface DataSourceApi {
	@RequestMapping(path = "/datasource", method = RequestMethod.GET)
	public List<DataSource> findAll();

	@RequestMapping(path = "/datasource", method = RequestMethod.POST)
	public void createDataSource(@RequestBody DataSource dataSource);

	@RequestMapping(path = "/datasource/{name}", method = RequestMethod.GET)
	public DataSource findByName(@PathVariable("name") String name);

	@RequestMapping(path = "/datasource/{name}", method = RequestMethod.DELETE)
	public void deleteDataSource(@PathVariable("name") String name);

	@RequestMapping(path = "/datasource/{name}", method = RequestMethod.PUT)
	public void updateDataSource(@PathVariable("name") String name, @RequestBody DataSource dataSource);
}
