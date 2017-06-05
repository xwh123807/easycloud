package org.easycloud.metadata.service.client;

import java.util.Arrays;
import java.util.List;

import org.easycloud.common.rest.BaseApiClient;
import org.easycloud.metadata.service.api.SystemApi;
import org.easycloud.metadata.service.model.System;

public class SystemApiClient extends BaseApiClient implements SystemApi {

	public SystemApiClient(String baseUrl) {
		super(baseUrl + "/system/");
	}

	@Override
	public List<System> findAll() {
		System[] items = getForObject("", System[].class);
		return Arrays.asList(items);
	}

	@Override
	public void createSystem(System system) {
		postForEntity("", system, Void.class);
	}

	@Override
	public System findByName(String name) {
		return getForObject("{name}", System.class, name);
	}

	@Override
	public void deleteSystem(String name) {
		delete("{name}", name);
	}

	@Override
	public void updateSystem(String name, System system) {
		put("{name}", system, name);
	}

}
