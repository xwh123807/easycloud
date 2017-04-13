package org.easycloud.metadata.server.repository;

import java.util.List;

import org.easycloud.metadata.server.MetaDataServer;
import org.easycloud.metadata.service.model.Entity;
import org.easycloud.metadata.service.model.EntityKey;
import org.easycloud.metadata.service.model.Field;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(MetaDataServer.class)
@WebIntegrationTest
public class EntityRepositoryTest {
	@Autowired
	private EntityRepository tableRepository;
	
	@Before
	public void setUp(){
		tableRepository.deleteAll();
	}
	
	@Test
	public void test() throws Exception{
		EntityKey key = new EntityKey("default", "user");
		Entity table = new Entity(key);
		table.setSchema("default");
		Field field = new Field();
		field.setName("f1");
		table.getFields().put(field.getName(), field);
		tableRepository.save(table);
		
		table = tableRepository.findOne(key);
		Assert.assertNotNull(table);
		field = table.getFields().get("f1");
		Assert.assertNotNull(field);
		
		List<Entity> items = tableRepository.findBySchema("default");
		Assert.assertEquals(1, items.size());
		
		items = tableRepository.findByCategory("default");
		Assert.assertEquals(1, items.size());
	}
}
