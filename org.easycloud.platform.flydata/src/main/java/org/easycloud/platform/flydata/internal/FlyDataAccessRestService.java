package org.easycloud.platform.flydata.internal;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.easycloud.platform.flydata.service.EntityMap;
import org.easycloud.platform.flydata.service.EntityQueryMap;
import org.easycloud.platform.flydata.service.FlyEntityMap;
import org.easycloud.platform.flydata.service.IFlyDataAccessService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * myFly平台扩展数据访问Rest服务
 * 
 * 
 * 1、自动进行ID名称转换<br>
 * 2、自动处理审计日志<br>
 * 3、自动处理默认值<br>
 * 4、受元模型定义控制<br>
 * 5、所有参数中entityName表示实体唯一标示<br>
 * 
 * @author xiangwanhong
 *
 */

@RestController
@RequestMapping("flydata")
public class FlyDataAccessRestService {

	@RequestMapping()
	public Map<String, String> help() {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("/flydata/{entityName}/{uid}?[view=[default]&print=[false]]", "根据实体{uid}获取实体数据");
		map.put("/flydata/find/{entityName}?[view=[default]&printt=[false]&params=[{json}]]", "获取实体全部数据");
		map.put("/flydata/findByPage/{entityName}?[view=[default]&page=[0]&size=[20]&printt=[false]&params=[{json}]]",
				"实体查询，返回指定分页的数据");
		map.put("/flydata[/findWithPage]/{entityName}?[view=[default]&page=[0]&size=[20]&printt=[false]&params=[{json}]]",
				"实体查询，返回指定分页数据和分页信息");
		map.put("/flydata/find/{entityName}/{uid}/{subTableAttr}?[view=[default]&printt=[false]&params=[{json}]]",
				"获取实体子表全部数据");
		map.put("/flydata/findByPage/{entityName}/{uid}/{subTableAttr}?[view=[default]&page=[0]&size=[20]&printt=[false]&params=[{json}]]",
				"实体子表查询，返回指定分页的数据");
		map.put("/flydata[/findWithPage]/{entityName}/{uid}/{subTableAttr}?[view=[default]&page=[0]&size=[20]&printt=[false]&params=[{json}]]",
				"实体子表查询，返回指定分页数据和分页信息");
		map.put("/flydata/{entityName}/{uid} [DELETE]", "删除实体");
		map.put("/flydata/{entityName}/{uid}/{subTableAttr}/{subUid} [DELETE]", "删除实体的子实体");
		map.put("/flydata/{entityName}?[view=[default]&values=[{json}]] [POST]", "保存实体");
		map.put("/flydata/{entityName}/{uid}/{subTableAttr}?[view=[default]&values=[{json}]] [POST]", "保存实体子表");
		map.put("/flydata/{entityName}/{uid}?[view=[default]&values=[{json}]] [PUT]", "修改实体");
		map.put("/flydata/{entityName}/{uid}/{subTableAttr}/{subUid}?[view=[default]&values=[{json}]] [PUT]", "修改实体子表");
		return map;
	}

	private IFlyDataAccessService getFlyDataAccessService(final String entityName) {
		return AppUtil.getFlyDataAccessService(entityName);
	}

	@RequestMapping(value = "findOneBase/{entityName}", method = RequestMethod.GET)
	public <T> T findOne(@PathVariable("entityName") final String entityName, final String uid) {
		return getFlyDataAccessService(entityName).findOne(entityName, uid);
	}

	@RequestMapping(value = "findAllBase/{entityName}", method = RequestMethod.GET)
	public <T> List<T> findAll(@PathVariable("entityName") final String entityName,
			@RequestParam() final EntityMap params) {
		return getFlyDataAccessService(entityName).findAll(entityName, params);
	}

	/**
	 * 获取指定实体数据
	 * 
	 * @param entityName
	 * @param uid
	 * @param formViewName
	 * @param printMode
	 * @return
	 */
	@RequestMapping(value = { "{entityName}/{uid}" }, method = RequestMethod.GET)
	public FlyEntityMap findOne(@PathVariable("entityName") final String entityName,
			@PathVariable("uid") final String uid,
			@RequestParam(name = "view", required = false, defaultValue = "default") final String formViewName,
			@RequestParam(name = "print", required = false, defaultValue = "false") final boolean printMode) {
		return getFlyDataAccessService(entityName).findOne(entityName, uid, formViewName, printMode);
	}

	private EntityQueryMap convertToRequestParametersMap(EntityMap params) {
		EntityQueryMap values = new EntityQueryMap();
		if (MapUtils.isNotEmpty(params)) {
			for (String key : params.keySet()) {
				String value = params.get(key);
				if (value != null) {
					values.put(key, new String[] { value });
				} else {
					values.put(key, new String[] {});
				}
			}
		}
		return values;
	}

	/**
	 * 获取实体全部数据
	 * 
	 * @param entityName
	 * @param listViewName
	 * @param params
	 * @param printMode
	 * @return
	 */
	@RequestMapping(value = "find/{entityName}", method = RequestMethod.GET)
	public List<FlyEntityMap> find(@PathVariable("entityName") final String entityName,
			@RequestParam(name = "view", required = false, defaultValue = "default") final String listViewName,
			@RequestParam(name = "params", required = false) final EntityMap params,
			@RequestParam(name = "print", required = false, defaultValue = "false") final boolean printMode) {
		return getFlyDataAccessService(entityName).findAll(entityName, listViewName,
				convertToRequestParametersMap(params), printMode);
	}

	/**
	 * 实体查询，返回指定分页的数据
	 * 
	 * @param entityName
	 * @param listViewName
	 * @param params
	 * @param page
	 * @param size
	 * @param printMode
	 * @return
	 */
	@RequestMapping(value = { "findByPage/{entityName}" }, method = RequestMethod.GET)
	public List<FlyEntityMap> findByPage(@PathVariable("entityName") final String entityName,
			@RequestParam(name = "view", required = false, defaultValue = "default") final String listViewName,
			@RequestParam(name = "params", required = false) final EntityMap params,
			@RequestParam(name = "page", required = false, defaultValue = "0") final int page,
			@RequestParam(name = "size", required = false, defaultValue = "20") final int size,
			@RequestParam(name = "print", required = false, defaultValue = "false") final boolean printMode) {
		return getFlyDataAccessService(entityName).findAll(entityName, listViewName,
				convertToRequestParametersMap(params), page, size, printMode);
	}

	/**
	 * 实体查询，返回指定分页数据和分页信息
	 * 
	 * @param entityName
	 * @param listViewName
	 * @param params
	 * @param page
	 * @param size
	 * @param printMode
	 * @return
	 */
	@RequestMapping(value = { "findWithPage/{entityName}", "{entityName}" }, method = RequestMethod.GET)
	public Page<FlyEntityMap> findWithPage(@PathVariable("entityName") final String entityName,
			@RequestParam(name = "view", required = false, defaultValue = "default") final String listViewName,
			@RequestParam(name = "params", required = false) final EntityMap params,
			@RequestParam(name = "page", required = false, defaultValue = "0") final int page,
			@RequestParam(name = "size", required = false, defaultValue = "20") final int size,
			@RequestParam(name = "print", required = false, defaultValue = "false") final boolean printMode) {
		return getFlyDataAccessService(entityName).findAllWithPage(entityName, listViewName,
				convertToRequestParametersMap(params), page, size, printMode);
	}

	/**
	 * 获取实体子表全部数据
	 * 
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param formViewName
	 * @param params
	 * @param printMode
	 * @return
	 */
	@RequestMapping(value = "find/{entityName}/{uid}/{subTableAttr}", method = RequestMethod.GET)
	public List<FlyEntityMap> find(@PathVariable("entityName") String entityName, @PathVariable("uid") String uid,
			@PathVariable("subTableAttr") String subTableAttr,
			@RequestParam(name = "view", required = false, defaultValue = "default") String formViewName,
			@RequestParam(name = "params", required = false) EntityMap params, boolean printMode) {
		return getFlyDataAccessService(entityName).findAllForSubEntity(entityName, uid, subTableAttr, formViewName,
				convertToRequestParametersMap(params), printMode);
	}

	/**
	 * 实体子表数据查询，返回指定分页数据
	 * 
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param formViewName
	 * @param params
	 * @param page
	 * @param size
	 * @param printMode
	 * @return
	 */
	@RequestMapping(value = { "findByPage/{entityName}/{uid}/{subTableAttr}" }, method = RequestMethod.GET)
	public List<FlyEntityMap> findByPage(@PathVariable("entityName") final String entityName,
			@PathVariable("uid") String uid, @PathVariable("subTableAttr") String subTableAttr,
			@RequestParam(name = "view", required = false, defaultValue = "default") final String formViewName,
			@RequestParam(name = "params", required = false) final EntityMap params,
			@RequestParam(name = "page", required = false, defaultValue = "0") final int page,
			@RequestParam(name = "size", required = false, defaultValue = "20") final int size,
			@RequestParam(name = "print", required = false, defaultValue = "false") final boolean printMode) {
		return getFlyDataAccessService(entityName).findAllForSubEntity(entityName, uid, subTableAttr, formViewName,
				convertToRequestParametersMap(params), page, size, printMode);
	}

	/**
	 * 实体子表数据查询，返回指定分页数据和分页信息
	 * 
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param formViewName
	 * @param params
	 * @param page
	 * @param size
	 * @param printMode
	 * @return
	 */
	@RequestMapping(value = { "findWithPage/{entityName}/{uid}/{subTableAttr}",
			"/{entityName}/{uid}/{subTableAttr}" }, method = RequestMethod.GET)
	public Page<FlyEntityMap> findWithPage(@PathVariable("entityName") final String entityName,
			@PathVariable("uid") String uid, @PathVariable("subTableAttr") String subTableAttr,
			@RequestParam(name = "view", required = false, defaultValue = "default") final String formViewName,
			@RequestParam(name = "params", required = false) final EntityMap params,
			@RequestParam(name = "page", required = false, defaultValue = "0") final int page,
			@RequestParam(name = "size", required = false, defaultValue = "20") final int size,
			@RequestParam(name = "print", required = false, defaultValue = "false") final boolean printMode) {
		return getFlyDataAccessService(entityName).findAllForSubEntityWithPage(entityName, uid, subTableAttr,
				formViewName, convertToRequestParametersMap(params), page, size, printMode);
	}

	/**
	 * 删除实体
	 * 
	 * @param entityName
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = "{entityName}/{uid}", method = RequestMethod.DELETE)
	public int delOne(@PathVariable("entityName") String entityName, @PathVariable("uid") String uid) {
		return getFlyDataAccessService(entityName).delOne(entityName, uid);
	}

	/**
	 * 删除实体的子实体
	 * 
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param subUid
	 */
	@RequestMapping(value = "{entityName}/{uid}/{subTableAttr}/{subUid}", method = RequestMethod.DELETE)
	public int delSubEntity(@PathVariable("entityName") String entityName, @PathVariable("uid") String uid,
			@PathVariable("subTableAttr") String subTableAttr, @PathVariable("subUid") String subUid) {
		return getFlyDataAccessService(entityName).delSubEntity(entityName, uid, subTableAttr, subUid);
	}

	/**
	 * 保存实体
	 * 
	 * @param entityName
	 * @param formViewName
	 * @param values
	 */
	@RequestMapping(value = "{entityName}", method = RequestMethod.POST)
	public String saveEntity(@PathVariable("entityName") String entityName,
			@RequestParam(name = "view", required = false, defaultValue = "default") String formViewName,
			@RequestParam(name = "values", required = false) EntityMap values) {
		return getFlyDataAccessService(entityName).saveEntity(entityName, formViewName, values);
	}

	/**
	 * 保存实体子表
	 * 
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param formViewName
	 * @param values
	 */
	@RequestMapping(value = "{entityName}/{uid}/{subTableAttr}", method = RequestMethod.POST)
	public String saveSubEntity(@PathVariable("entityName") String entityName, @PathVariable("uid") String uid,
			@PathVariable("subTableAttr") String subTableAttr,
			@RequestParam(name = "view", required = false, defaultValue = "default") String formViewName,
			@RequestParam(name = "values", required = false) EntityMap values) {
		return getFlyDataAccessService(entityName).saveEntity(entityName, uid, subTableAttr, formViewName, values);
	}

	/**
	 * 修改实体
	 * 
	 * @param entityName
	 * @param uid
	 * @param formViewName
	 * @param values
	 */
	@RequestMapping(value = "{entityName}/{uid}", method = RequestMethod.PUT)
	public void updateEntity(@PathVariable("entityName") String entityName, @PathVariable("uid") String uid,
			@RequestParam(name = "view", required = false, defaultValue = "default") String formViewName,
			@RequestParam(name = "values", required = false) EntityMap values) {
		getFlyDataAccessService(entityName).updateEntity(entityName, uid, formViewName, values);
	}

	/**
	 * 修改实体子表
	 * 
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param subUid
	 * @param formViewName
	 * @param values
	 */
	@RequestMapping(value = "{entityName}/{uid}/{subTableAttr}/{subUid}", method = RequestMethod.PUT)
	public void updateSubEntity(@PathVariable("entityName") String entityName, @PathVariable("uid") String uid,
			@PathVariable("subTableAttr") String subTableAttr, @PathVariable("subUid") String subUid,
			@RequestParam(name = "view", required = false, defaultValue = "default") String formViewName,
			@RequestParam(name = "values", required = false) EntityMap values) {
		getFlyDataAccessService(entityName).updateEntity(entityName, uid, subTableAttr, subUid, formViewName, values);
	}

}
