package org.easycloud.metadata.client;

import org.easycloud.metadata.domain.model.Table;
import org.springframework.http.ResponseEntity;

public class MetaDataApi extends BaseApiClient implements org.easycloud.metadata.domain.api.MetaDataApi{
	
	public MetaDataApi(String baseUrl) {
		super(baseUrl);
	}

	@Override
	public ResponseEntity<Table> getMetaData(String schema, String table) {
		return getRestRemplate().getForEntity(getBaseUrl()+"{schema}/{table}", Table.class);
	}

}
