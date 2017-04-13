package org.easycloud.flydata.server.repository;

import org.easycloud.flydata.server.FlyDataServerApplication;
import org.easycloud.metadata.service.model.DataSource;
import org.easycloud.metadata.service.model.Entity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(FlyDataServerApplication.class)
@WebIntegrationTest
public class MetaDataRepositoryTest {
	@Autowired
	private MetaDataRepository repo;
	
	@Test
	public void base(){
		Assert.assertNotNull(repo);
	}
	
	@Test
	public void getDataSource(){
		DataSource ds = repo.getDataSource("n");
		Assert.assertNotNull(ds);
	}
	
	@Test
	public void getEntityMetaData(){
		Entity entity = repo.getEntityMetaData("category", "entity1");
		Assert.assertNotNull(entity);
	}
}
