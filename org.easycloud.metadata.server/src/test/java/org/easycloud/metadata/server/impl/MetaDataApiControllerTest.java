package org.easycloud.metadata.server.impl;

import org.easycloud.metadata.domain.model.Table;
import org.easycloud.metadata.server.MetaDataServer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(MetaDataServer.class)
@WebIntegrationTest
public class MetaDataApiControllerTest {
	private RestTemplate restTemplate;
	
	@Before
	public void setUp(){
		restTemplate = new RestTemplate();
	}
	
	@Test
	public void curd(){
		ResponseEntity<Table> response = restTemplate.getForEntity("http://localhost:9080/metadata/metadata/default/user", Table.class);
		Table table = response.getBody();
		Assert.assertNull(table);
		Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
}