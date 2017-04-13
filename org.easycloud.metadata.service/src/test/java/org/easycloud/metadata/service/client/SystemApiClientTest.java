package org.easycloud.metadata.service.client;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.easycloud.metadata.service.model.System;

public class SystemApiClientTest {
	private String baseUrl = "http://localhost:9080/system";

	private SystemApiClient client = new SystemApiClient(baseUrl);

	@Test
	public void curd() {
		String key = "sys1";
		System entity = new System();
		entity.setName(key);
		client.createSystem(entity);

		System res = client.findByName(entity.getName());
		Assert.assertNotNull(res);
		Assert.assertEquals(key, res.getName());

		List<System> items = client.findAll();
		Assert.assertTrue(items.size() > 0);

		entity.setTitle("title changed");
		client.updateSystem(entity.getName(), entity);

		res = client.findByName(entity.getName());
		Assert.assertEquals(entity.getTitle(), res.getTitle());

		client.deleteSystem(key);

		res = client.findByName(key);
		Assert.assertNull(res);
	}
}
