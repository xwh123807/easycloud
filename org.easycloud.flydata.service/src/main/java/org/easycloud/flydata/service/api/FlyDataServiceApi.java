package org.easycloud.flydata.service.api;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path="/")
public interface FlyDataServiceApi {
	@RequestMapping(path="/{dataSource}/{category}/{entityName}")
	public void findAll(String dataSource, String category, String entityName);
}
