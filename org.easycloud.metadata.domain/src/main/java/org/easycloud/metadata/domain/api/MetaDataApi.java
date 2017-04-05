package org.easycloud.metadata.domain.api;

import org.easycloud.metadata.domain.model.Table;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "metadata", description = "MetaData Api")
public interface MetaDataApi {
	@ApiOperation(value = "获取元模型", response = Table.class)
	@RequestMapping(path = "/metadata/{schema}/{table}", method = RequestMethod.GET)
	ResponseEntity<Table> getMetaData(@ApiParam(required = true) @PathVariable("schema") String schema,
			@ApiParam(required = true) @PathVariable("table") String table);
}
