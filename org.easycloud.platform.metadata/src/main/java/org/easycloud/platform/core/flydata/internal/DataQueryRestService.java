package org.easycloud.platform.core.flydata.internal;

import javax.servlet.http.HttpServletRequest;

import org.myfly.platform.core.flydata.service.EntityQueryMap;
import org.myfly.platform.core.flydata.service.IFlyDataAccessService;
import org.myfly.platform.core.utils.AppUtil;
import org.myfly.platform.core.utils.AssertUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/query")
public class DataQueryRestService {
	private IFlyDataAccessService getFlyDataAccessService(String entityName) {
		return AppUtil.getFlyDataAccessService(entityName);
	}

	/**
	 * 查询指定实体
	 * 
	 * @param tableName
	 * @param uid
	 * @param view
	 * @param format
	 *            格式，json/html
	 * @return
	 */
	@RequestMapping("{tableName}/{uid}")
	public Object queryEntity(@PathVariable("tableName") String tableName, @PathVariable("uid") String uid,
			@RequestParam(name = "view", required = false) String formView,
			@RequestParam(name = "format", required = false, defaultValue = "json") String foramt) {
		AssertUtil.parameterEmpty(tableName, "tableName");
		AssertUtil.parameterEmpty(uid, "uid");
		if ("html".equals(foramt)) {
			return "";
		} else {
			return getFlyDataAccessService(tableName).findOne(tableName, uid, formView, false);
		}
	}

	/**
	 * 主实体分页查询，提供给DataTables表格控件
	 * 
	 * @param tableName
	 * @param request
	 * @return
	 */
	@RequestMapping("/list/{tableName}/{view}")
	public Object queryEntityList(@PathVariable("tableName") String tableName,
			@PathVariable("view") String listViewName,
			@RequestParam(name = "format", required = false, defaultValue = "json") String foramt,
			@RequestParam(name = "params", required = false) final EntityQueryMap params,
			HttpServletRequest request) {
		AssertUtil.parameterEmpty(tableName, "tableName");

		DataTablesRequest tableRequest = new DataTablesRequest(request);
		final int page = tableRequest.getStart() / tableRequest.getLength();
		final int size = tableRequest.getLength();
		if ("html".equals(foramt)) {
			return "";
		} else {
			DataTablesResponse tableResponse = getFlyDataAccessService(tableName).findAllToDataTables(tableName,
					listViewName, params, page, size, false);
			tableResponse.setDraw(tableRequest.getDraw());
			return tableResponse;
		}
	}

	/**
	 * 子表分页查询，提供给DataTables表格控件
	 * 
	 * @param tableName
	 * @param uid
	 * @param subTableAttr
	 * @param request
	 * @return
	 */
	@RequestMapping("{tableName}/{uid}/{subTableAttr}/{view}")
	public DataTablesResponse querySubEntityForDataTablesControl(@PathVariable("tableName") String tableName,
			@PathVariable("uid") String uid, @PathVariable("subTableAttr") String subTableAttr,
			@PathVariable("view") String view, @RequestParam(name = "params", required = false) final EntityQueryMap params,
			HttpServletRequest request) {

		DataTablesRequest tableRequest = new DataTablesRequest(request);
		final int page = tableRequest.getStart() / tableRequest.getLength();
		final int size = tableRequest.getLength();
		DataTablesResponse tableResponse = getFlyDataAccessService(tableName).findAllToDataTables(tableName, uid,
				subTableAttr, view, params, page, size, false);
		tableResponse.setDraw(tableRequest.getDraw());
		return tableResponse;
	}

	/**
	 * 实体数据查询，只返回ID列和显示名称列，用于Select选择控件
	 * 
	 * @param tableName
	 * @param request
	 * @return
	 */
	@RequestMapping("/select/{tableName}")
	public DataTablesResponse queryEntityForSelectControl(@PathVariable("tableName") String tableName,
			HttpServletRequest request) {
		AssertUtil.parameterEmpty(tableName, "tableName");

		DataTablesRequest tableRequest = new DataTablesRequest(request);
		PageRequest pageable = new PageRequest(tableRequest.getStart() / tableRequest.getLength(),
				tableRequest.getLength());
		DataTablesResponse tableResponse = getFlyDataAccessService(tableName).findAllForSelect(tableName,
				tableRequest.getSearch(), pageable);
		tableResponse.setDraw(tableRequest.getDraw());
		return tableResponse;
	}
}
