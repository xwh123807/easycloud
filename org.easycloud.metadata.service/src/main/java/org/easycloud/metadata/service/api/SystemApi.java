package org.easycloud.metadata.service.api;

import java.util.List;

import org.easycloud.metadata.service.model.System;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface SystemApi {
	@RequestMapping(path = "/system", method = RequestMethod.GET)
	public List<System> findAll();

	@RequestMapping(path = "/system", method = RequestMethod.POST)
	public void createSystem(@RequestBody System entity);

	@RequestMapping(path = "/system/{name}", method = RequestMethod.GET)
	public System findByName(@PathVariable("name") String name);

	@RequestMapping(path = "/system/{name}", method = RequestMethod.DELETE)
	public void deleteSystem(@PathVariable("name") String name);

	@RequestMapping(path = "/system/{name}", method = RequestMethod.PUT)
	public void updateSystem(@PathVariable("name") String name, @RequestBody System entity);
}
