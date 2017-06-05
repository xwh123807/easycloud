package org.easycloud.metadata.service.api;

import java.util.List;

import org.easycloud.metadata.service.model.Entity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "entity metadata", description = "实体原模型API")
public interface EntityMetaDataApi {

	@ApiOperation(value = "获取所有实体元模型", response = List.class)
	@RequestMapping(path = "/entity/", method = RequestMethod.GET)
	List<Entity> findAll();

	@ApiOperation(value = "获取指定类别下所有实体", response = Entity.class)
	@RequestMapping(path = "/entity/{category}", method = RequestMethod.GET)
	List<Entity> findByCategory(
			@ApiParam(required = true, value = "类别") @PathVariable(name = "category", required = true) String category);

	@ApiOperation(value = "获取实体元模型", response = Entity.class)
	@ApiResponses({ @ApiResponse(code = 400, message = "参数不合法，category和entityName不能为空"),
			@ApiResponse(code = 404, message = "实体不存在") })
	@RequestMapping(path = "/entity/{category}/{entityName}", method = RequestMethod.GET)
	Entity getMetaData(
			@ApiParam(required = true, value = "类别") @PathVariable(name = "category", required = true) String category,
			@ApiParam(required = true, value = "实体名称") @PathVariable(name = "entityName", required = true) String entityName);

	@ApiOperation(value = "新增实体元模型", response = Void.class)
	@ApiResponses({ @ApiResponse(code = 400, message = "参数不合法，entity校验有问题"),
			@ApiResponse(code = 500, message = "实体新增失败") })
	@RequestMapping(path = "/entity", method = RequestMethod.POST)
	void newMetaData(@RequestBody Entity entity);

	@ApiOperation(value = "修改实体元模型", response = Void.class)
	@ApiResponses({ @ApiResponse(code = 400, message = "参数不合法，category和entityName不能为空"),
			@ApiResponse(code = 404, message = "实体不存在"), @ApiResponse(code = 500, message = "实体修改失败") })
	@RequestMapping(path = "/entity/{category}/{entityName}", method = RequestMethod.PUT)
	void updateMetaData(
			@ApiParam(required = true, value = "类别") @PathVariable(name = "category", required = true) String category,
			@ApiParam(required = true, value = "实体名称") @PathVariable(name = "entityName", required = true) String entityName,
			@RequestBody Entity entity);

	@ApiOperation(value = "删除实体元模型", response = Entity.class)
	@ApiResponses({ @ApiResponse(code = 400, message = "参数不合法，category和entityName不能为空"),
			@ApiResponse(code = 404, message = "实体不存在"), @ApiResponse(code = 500, message = "实体删除失败") })
	@RequestMapping(path = "/entity/{category}/{entityName}", method = RequestMethod.DELETE)
	void deleteMetaData(@ApiParam(required = true, value = "类别") @PathVariable("category") String category,
			@ApiParam(required = true, value = "实体名称") @PathVariable("entityName") String entityName);
}
