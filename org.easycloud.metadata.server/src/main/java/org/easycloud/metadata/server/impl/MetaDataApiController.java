package org.easycloud.metadata.server.impl;

import org.easycloud.metadata.domain.api.MetaDataApi;
import org.easycloud.metadata.domain.model.Table;
import org.easycloud.metadata.domain.model.TableKey;
import org.easycloud.metadata.server.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MetaDataApiController implements MetaDataApi {
	@Autowired
	private TableRepository tableRepository;

	@Override
	public ResponseEntity<Table> getMetaData(@PathVariable("schema") String schema,
			@PathVariable("table") String table) {
		Table entity = tableRepository.findOne(new TableKey(schema, table));
		if (entity != null) {
			return new ResponseEntity<Table>(entity, HttpStatus.OK);
		}else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
