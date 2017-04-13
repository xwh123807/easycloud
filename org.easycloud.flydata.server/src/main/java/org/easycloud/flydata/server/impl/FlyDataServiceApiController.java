package org.easycloud.flydata.server.impl;

import org.easycloud.flydata.service.api.FlyDataServiceApi;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlyDataServiceApiController implements FlyDataServiceApi {

	@Override
	public void findAll(@PathVariable("dataSource") String dataSource, @PathVariable("category") String category,
			@PathVariable("entityName") String entityName) {
		// TODO Auto-generated method stub

	}

}
