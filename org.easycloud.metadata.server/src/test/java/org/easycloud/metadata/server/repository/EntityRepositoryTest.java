package org.easycloud.metadata.server.repository;

import org.easycloud.metadata.domain.model.Entity;
import org.easycloud.metadata.domain.model.EntityKey;
import org.easycloud.metadata.domain.model.Field;
import org.easycloud.metadata.server.MetaDataServer;
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
		Entity table = new Entity();
		table.setSchema("default");
		//table.setKey(key);
		Field field = new Field();
		field.setName("f1");
		table.getFields().put(field.getName(), field);
		tableRepository.save(table);
		
		table = tableRepository.findOne(key);
		Assert.assertNotNull(table);
		field = table.getFields().get("f1");
		Assert.assertNotNull(field);
	}
}
