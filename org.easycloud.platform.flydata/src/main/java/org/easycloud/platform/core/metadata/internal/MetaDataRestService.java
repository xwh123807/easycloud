package org.easycloud.platform.core.metadata.internal;

import java.util.LinkedHashMap;
import java.util.Map;

import org.easycloud.platform.core.metadata.internal.JsonEntityMetaData.JsonFieldDefinition;
import org.easycloud.platform.core.metadata.internal.JsonEntityMetaData.JsonFormDefinition;
import org.easycloud.platform.core.metadata.internal.JsonEntityMetaData.JsonListDefinition;
import org.easycloud.platform.core.metadata.service.IEntityMetaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 元模型Rest服务
 * 
 * @author xiangwanhong
 *
 */
@RestController
@RequestMapping("meta")
public class MetaDataRestService {
	@Autowired
	private IEntityMetaDataService entityMetaDataService;
	
	@RequestMapping()
	public Map<String, String> help(){
		Map<String, String> map = new LinkedHashMap<>();
		map.put("/meta/{table}", "获取实体元模型数据,{table}为实体名称");
		map.put("/meta/{table}/fields", "获取实体字段集");
		map.put("/meta/{table}/fields/{name}", "获取指定实体字段定义,{name}表示属性名称");
		map.put("/meta/{table}/listviews", "获取实体列表视图定义");
		map.put("/meta/{table}/listviews/{name}", "获取指定名称实体列表视图定义,{name}表示视图名称");
		map.put("/meta/{table}/formviews", "获取实体表单视图定义");
		map.put("/meta/{table}/formviews/{name}", "获取指定实体表单视图定义,{name}表示视图名称");
		return map;
	}

	/**
	 * 获取实体元模型数据
	 * 
	 * @param table
	 * @return
	 */
	@RequestMapping("{table}")
	@ResponseBody
	public JsonEntityMetaData getJsonEntityMetaData(@PathVariable("table") String table) {
		JsonEntityMetaData metaData = entityMetaDataService.getJsonEntityMetaData(table);
		return metaData;
	}
	
	/**
	 * 获取实体字段集
	 * @param table
	 * @return
	 */
	@RequestMapping("{table}/fields")
	@ResponseBody
	public Map<String, JsonFieldDefinition> getJsonEntityMetaDataForFields(@PathVariable("table") String table){
		return getJsonEntityMetaData(table).getFieldMap();
	}
	
	/**
	 * 获取指定实体字段定义
	 * @param table
	 * @return
	 */
	@RequestMapping("{table}/fields/{name}")
	@ResponseBody
	public JsonFieldDefinition getJsonEntityMetaDataForField(@PathVariable("table") String table, @PathVariable("name") String name){
		return getJsonEntityMetaData(table).getFieldMap().get(name);
	}
	
	/**
	 * 获取实体列表视图定义
	 * @param table
	 * @return
	 */
	@RequestMapping("{table}/listviews")
	@ResponseBody
	public Map<String, JsonListDefinition> getJsonEntityMetaDataForListViews(@PathVariable("table") String table){
		return getJsonEntityMetaData(table).getListDefinitions();
	}
	
	/**
	 * 获取指定名称实体列表视图定义
	 * @param table
	 * @return
	 */
	@RequestMapping("{table}/listviews/{name}")
	@ResponseBody
	public JsonListDefinition getJsonEntityMetaDataForListViews(@PathVariable("table") String table, @PathVariable("name") String name){
		return getJsonEntityMetaData(table).getListDefinitions().get(name);
	}
	
	/**
	 * 获取实体表单视图定义
	 * @param table
	 * @return
	 */
	@RequestMapping("{table}/formviews")
	@ResponseBody
	public Map<String, JsonFormDefinition> getJsonEntityMetaDataForFormViews(@PathVariable("table") String table){
		return getJsonEntityMetaData(table).getFormDefinitions();
	}
	
	/**
	 * 获取指定实体表单视图定义
	 * @param table
	 * @return
	 */
	@RequestMapping("{table}/formviews/{name}")
	@ResponseBody
	public JsonFormDefinition getJsonEntityMetaDataForFormView(@PathVariable("table") String table, @PathVariable("name") String name){
		return getJsonEntityMetaData(table).getFormDefinitions().get(name);
	}
}
