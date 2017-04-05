package org.easycloud.metadata.server.repository;

import org.easycloud.metadata.domain.model.Field;
import org.easycloud.metadata.domain.model.Table;
import org.easycloud.metadata.domain.model.TableKey;
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
public class TableRepositoryTest {
	@Autowired
	private TableRepository tableRepository;
	
	@Before
	public void setUp(){
		tableRepository.deleteAll();
	}
	
	@Test
	public void test() throws Exception{
		TableKey key = new TableKey("default", "user");
		Table table = new Table();
		table.setSchema("default");
		table.setName("user");
		table.setKey(key);
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
