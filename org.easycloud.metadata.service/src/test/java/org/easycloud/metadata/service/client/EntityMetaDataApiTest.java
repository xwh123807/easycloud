package org.easycloud.metadata.service.client;

import java.util.List;

import org.easycloud.metadata.service.model.Entity;
import org.easycloud.metadata.service.model.EntityKey;
import org.junit.Assert;
import org.junit.Test;

public class EntityMetaDataApiTest {
	private String baseUrl = "http://localhost:9080/metadata";
	private EntityMetaDataApiClient client = new EntityMetaDataApiClient(baseUrl);

	@Test
	public void curd() {
		EntityKey key = new EntityKey("test", "user" + Math.random());
		Entity entity = new Entity(key);
		client.newMetaData(entity);

		Entity res = client.getMetaData(key.getCategory(), key.getName());
		Assert.assertEquals(key.getName(), res.getKey().getName());
		
		List<Entity> items = client.findAll();
		Assert.assertTrue(items.size() > 0);

		entity.setTitle("title changed");
		client.updateMetaData(key.getCategory(), key.getName(), entity);

		res = client.getMetaData(key.getCategory(), key.getName());
		Assert.assertEquals(entity.getTitle(), res.getTitle());
		
		client.deleteMetaData(key.getCategory(), key.getName());
		
		res = client.getMetaData(key.getCategory(), key.getName());
		Assert.assertNull(res);
	}
}
