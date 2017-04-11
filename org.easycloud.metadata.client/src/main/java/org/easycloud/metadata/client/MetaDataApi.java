package org.easycloud.metadata.client;

import org.easycloud.metadata.domain.model.Entity;
import org.springframework.http.ResponseEntity;

public class MetaDataApi extends BaseApiClient implements org.easycloud.metadata.domain.api.MetaDataApi{
	
	public MetaDataApi(String baseUrl) {
		super(baseUrl);
	}

}
