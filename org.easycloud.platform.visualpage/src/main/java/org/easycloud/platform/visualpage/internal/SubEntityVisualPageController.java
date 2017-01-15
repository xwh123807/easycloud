package org.easycloud.platform.visualpage.internal;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easycloud.platform.core.admin.BaseController;
import org.easycloud.platform.core.domain.AlertInfo;
import org.easycloud.platform.core.domain.AlertLevel;
import org.easycloud.platform.metadata.annotation.view.EntityAction;
import org.easycloud.platform.metadata.utils.AssertUtil;
import org.easycloud.platform.metadata.utils.StringUtil;
import org.easycloud.platform.visualpage.domain.PageInfo;
import org.easycloud.platform.visualpage.domain.VisualPageType;
import org.easycloud.platform.visualpage.service.VisualPageConstants;
import org.easycloud.platform.visualpage.ui.ViewMode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 实体子表增删改查UI<br>
 * 
 * @author xiangwanhong
 *
 */
@Controller
@RequestMapping("vp")
public class SubEntityVisualPageController extends BaseController {
	private Log log = LogFactory.getLog(getClass());

	public static Map<String, String> help() {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("/vp/{table}/{uid}/{subTableAttr}/{subUid}", "显示实体子表查看视图");
		map.put("/vp/{table}/{uid}/{subTableAttr}/{subUid}?form", "显示实体子表编辑视图");
		map.put("/vp/{table}/{subTableAttr}", "显示新增实体子表视图");
		map.put("/vp/{table}/{subTableAttr} [POST]", "保存实体子表，保存成功后显示该实体");
		map.put("/vp/{table}/{subTableAttr} [PATCH]", "保存后继续新增实体子表，保存成功后显示新增实体子表视图");
		map.put("/vp/{table}/{uid}/{subTableAttr}/{subUid} [DEL]", "实体删除");
		map.put("/vp/{table}/{uid}/{subTableAttr}/{subUid} [PUT]", "实体修改");
		map.put("/vp/listprint/{table}/{uid}/{subTableAttr}", "显示实体子表打印视图");
		map.put("/vp/excel/{table}/{uid}/{subTableAttr}", "实体子表数据导出到Excel");
		map.put("/vp/pdf/{table}/{uid}/{subTableAttr}", "实体子表数据导出到Pdf");
		map.put("/vp/excelimport/{table}/{uid}/{subTableAttr}", "显示实体子表excel导入界面");
		map.put("/vp/excelimport/{table}/{uid}/{subTableAttr}/{uploadDir}", "将目录下的excel导入到实体子表中");
		return map;
	}

	/**
	 * 打开查看或编辑实体UI，如果参数中有form表示编辑打开
	 * 
	 * @param table
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = { "{table}/{uid}/{subTableAttr}/{subUid}" }, method = RequestMethod.GET)
	public ModelAndView viewOrEditSubEntity(@PathVariable("table") String table, @PathVariable("uid") String uid,
			@PathVariable("subTableAttr") String subTableAttr, @PathVariable("subUid") String subUid,
			@RequestParam(name = "view", required = false) String formViewName,
			@RequestParam(name = "viewmode", required = false) ViewMode viewMode, HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("viewOrEditSubEntity，table=" + table + ", uid=" + uid + ", subTableAttr=" + subTableAttr
					+ ", subUid=" + subUid);
		}
		AssertUtil.parameterEmpty(table, "table");
		AssertUtil.parameterEmpty(uid, "uid");
		AssertUtil.parameterEmpty(subTableAttr, "subTableAttr");
		AssertUtil.parameterEmpty(subUid, "subUid");

		String subTableName = super.getSubTableName(table, subTableAttr);
		AssertUtil.recordNotFound(subTableName, table, "fieldName", subTableAttr);
		// 判断是否编辑模式，如果有form参数表示编辑模式打开
		boolean isEditOpen = request.getParameterMap().containsKey("form");
		viewMode = updateViewMode(viewMode, request);
		return getEntityViewByTableAndUidAndPageType(subTableName, subUid,
				isEditOpen ? VisualPageType.EDIT : VisualPageType.VIEW, request, viewMode, formViewName);
	}

	private ModelAndView viewSubEntity(@PathVariable("table") String table, @PathVariable("uid") String uid,
			@PathVariable("subTableAttr") String subTableAttr, @PathVariable("subUid") String subUid,
			@RequestParam(name = "view", required = false) String formViewName,
			@RequestParam(name = "viewmode", required = false) ViewMode viewMode, HttpServletRequest request) {
		String subTableName = super.getSubTableName(table, subTableAttr);
		AssertUtil.recordNotFound(subTableName, table, "fieldName", subTableAttr);
		viewMode = updateViewMode(viewMode, request);
		return getEntityViewByTableAndUidAndPageType(subTableName, subUid, VisualPageType.VIEW, request, viewMode,
				formViewName);
	}

	/**
	 * 显示新增实体UI
	 * 
	 * @param table
	 * @return
	 */
	@RequestMapping(value = "{table}/{uid}/{subTableAttr}", method = RequestMethod.GET)
	public ModelAndView showNewSubEntity(@PathVariable("table") String table, @PathVariable("uid") String uid,
			@PathVariable("subTableAttr") String subTableAttr,
			@RequestParam(name = "view", required = false) String formViewName,
			@RequestParam(name = "viewmode", required = false) ViewMode viewMode, HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("viewByTableAndUid，table=" + table + ", uid=" + uid + ", subTableAttr=" + subTableAttr);
		}
		AssertUtil.parameterEmpty(table, "table");
		AssertUtil.parameterEmpty(uid, "uid");
		AssertUtil.parameterEmpty(subTableAttr, "subTableAttr");

		String subTableName = super.getSubTableName(table, subTableAttr);
		AssertUtil.recordNotFound(subTableName, table, "fieldName", subTableAttr);
		viewMode = updateViewMode(viewMode, request);
		return getEntityViewByTableAndUidAndPageType(subTableName, null, VisualPageType.NEW, request, viewMode,
				formViewName);
	}

	/**
	 * 保存新增实体。
	 *
	 * @param table
	 * @return
	 */
	@RequestMapping(value = "{table}/{uid}/{subTableAttr}", method = RequestMethod.POST)
	public ModelAndView saveSubEntity(@PathVariable("table") String table, @PathVariable("uid") String uid,
			@PathVariable("subTableAttr") String subTableAttr,
			@RequestParam(name = "view", required = false) String formViewName,
			@RequestParam(name = "viewmode", required = false) ViewMode viewMode, HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("viewByTableAndUid，table=" + table + ", uid=" + uid + ", subTableAttr=" + subTableAttr);
			log.debug(StringUtil.getRequestDebugInfo(request));
		}
		viewMode = updateViewMode(viewMode, request);
		return processSaveSubEntity(table, uid, subTableAttr, formViewName, viewMode, request, false);
	}

	/**
	 * 保存后继续新增实体
	 * 
	 * @param table
	 * @param uid
	 * @param subTableAttr
	 * @param view
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "{table}/{uid}/{subTableAttr}", method = RequestMethod.PATCH)
	public ModelAndView saveAndNewSubEntity(@PathVariable("table") String table, @PathVariable("uid") String uid,
			@PathVariable("subTableAttr") String subTableAttr,
			@RequestParam(name = "view", required = false) String formViewName,
			@RequestParam(name = "viewmode", required = false) ViewMode viewMode, HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("saveAndNewSubEntity，table=" + table + ", uid=" + uid + ", subTableAttr=" + subTableAttr);
			log.debug(StringUtil.getRequestDebugInfo(request));
		}
		viewMode = updateViewMode(viewMode, request);
		return processSaveSubEntity(table, uid, subTableAttr, formViewName, viewMode, request, true);
	}

	/**
	 * 新增实体保存
	 * 
	 * @param table
	 * @param uid
	 * @param subTableAttr
	 * @param view
	 * @param request
	 * @param newAndSave
	 * @return
	 */
	private ModelAndView processSaveSubEntity(String table, String uid, String subTableAttr, String formViewName,
			ViewMode viewMode, HttpServletRequest request, boolean newAndSave) {
		AssertUtil.parameterEmpty(table, "table");
		AssertUtil.parameterEmpty(uid, "uid");
		AssertUtil.parameterEmpty(subTableAttr, "subTableAttr");

		Map<String, String[]> params = new LinkedHashMap<>(request.getParameterMap());
		String pkValue = null;
		try {
			// 保存实体
			pkValue = getFlyDataAccessService(table).saveEntity(table, uid, subTableAttr, formViewName,
					EntityMap.build(params));
			setUserAlertInfo(new AlertInfo(AlertLevel.SUCCESS, "保存成功"));
		} catch (Exception e) {
			setUserAlertInfo(new AlertInfo("保存失败", e));
			ModelAndView mv = showNewSubEntity(table, uid, subTableAttr, formViewName, viewMode, request);
			mv.addObject("obj", params);
			return mv;
		}
		if (newAndSave) {
			// 保存后新增，跳转到新增页面
			return showNewSubEntity(table, uid, subTableAttr, formViewName, viewMode, request);
		} else {
			return viewSubEntity(table, uid, subTableAttr, pkValue, formViewName, viewMode, request);
		}

	}

	/**
	 * 修改实体
	 * 
	 * @param table
	 * @param uid
	 * @param subTableAttr
	 * @param subUid
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "{table}/{uid}/{subTableAttr}/{subUid}", method = RequestMethod.PUT)
	public ModelAndView updateSubEntity(@PathVariable("table") String table, @PathVariable("uid") String uid,
			@PathVariable("subTableAttr") String subTableAttr, @PathVariable("subUid") String subUid,
			@RequestParam(name = "view", required = false) String formViewName, HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("updateSubEntity，table=" + table + ", uid=" + uid + ", subTableAttr=" + subTableAttr + ", subUid="
					+ subUid);
		}
		AssertUtil.parameterEmpty(table, "table");
		AssertUtil.parameterEmpty(uid, "uid");
		AssertUtil.parameterEmpty(subTableAttr, "subTableAttr");
		AssertUtil.parameterEmpty(subUid, "subUid");

		try {
			// 保存实体
			Map<String, String[]> values = request.getParameterMap();
			getFlyDataAccessService(table).mergeEntity(table, uid, subTableAttr, subUid, formViewName,
					EntityMap.build(values));
			String subTableName = getEntityMetaDataService().getSubEntityMetaData(table, subTableAttr).getEntityName();
			Object entity = getFlyDataAccessService(table).findOne(subTableName, subUid, formViewName, false);
			AssertUtil.parameterEmpty(entity, "entity");
			setUserAlertInfo(new AlertInfo(AlertLevel.SUCCESS, "保存成功"));
		} catch (Exception e) {
			setUserAlertInfo(new AlertInfo("保存失败", e));
		}
		return viewSubEntity(table, uid, subTableAttr, subUid, formViewName, ViewMode.SUBPAGE, request);
	}

	/**
	 * 删除实体
	 * 
	 * @param table
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = "{table}/{uid}/{subTableAttr}/{subUid}", method = RequestMethod.DELETE)
	public ModelAndView delSubEntity(@PathVariable("table") String table, @PathVariable("uid") String uid,
			@PathVariable("subTableAttr") String subTableAttr, @PathVariable("subUid") String subUid,
			HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("delEntityByUid，table=" + table + ", uid=" + uid + ", subTableAttr=" + subTableAttr + ", subUid="
					+ subUid);
		}
		AssertUtil.parameterEmpty(table, "table");
		AssertUtil.parameterEmpty(uid, "uid");
		AssertUtil.parameterEmpty(subTableAttr, "subTableAttr");
		AssertUtil.parameterEmpty(subUid, "subUid");

		String subTableName = super.getSubTableName(table, subTableAttr);
		AssertUtil.recordNotFound(subTableName, table, "fieldName", subTableAttr);
		int count = getFlyDataAccessService(table).delOne(subTableName, subUid);
		if (count != 1) {
			setUserAlertInfo(new AlertInfo(AlertLevel.WARNING, "删除失败", "删除错误，应当删除一条，实际删除" + count + "条."));
		} else {
			setUserAlertInfo(new AlertInfo(AlertLevel.SUCCESS, "删除成功"));
		}
		// 删除后，回到实体查看页面
		return redirectTo(EntityUrlUtil.getEntityActionUrl(EntityAction.VIEW, table, uid, null));
	}

	/**
	 * 打印子表记录
	 * 
	 * @param table
	 * @param uid
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "listprint/{table}/{uid}/{subTableAttr}" })
	public ModelAndView listPrintView(@PathVariable("table") String table, @PathVariable("uid") String uid,
			@PathVariable("subTableAttr") String subTableAttr, HttpServletRequest request,
			@RequestParam(name = "view", required = false) String view, HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("printView，table=" + table + ", uid=" + uid + ", subTableAttr=" + subTableAttr);
		}
		AssertUtil.parameterEmpty(table, "table");
		AssertUtil.parameterEmpty(uid, "uid");
		AssertUtil.parameterEmpty(subTableAttr, "subTableAttr");

		PageInfo pageInfo = getBasePageInfo("列表", subTableAttr,
				getVisualPageService().getEntityTemplateFile(table, VisualPageType.PRINT, view, ViewMode.SUBPAGE));
		ModelAndView mv = getBaseModelAndView(VisualPageType.PRINT);
		mv.addObject("pageInfo", pageInfo);

		Map<String, String[]> params = new LinkedHashMap<>(request.getParameterMap());
		// 获取参数，在界面上显示条件的值
		Map<String, String> queryMap = new LinkedHashMap<>();
		for (Map.Entry<String, String[]> param : params.entrySet()) {
			queryMap.put(param.getKey(), param.getValue()[0]);
		}
		mv.addObject("obj", queryMap);
		List<FlyEntityMap> list = getFlyDataAccessService(table).findAllForSubEntity(table, uid, subTableAttr, view,
				new EntityQueryMap(params), false);
		mv.addObject("objlist", list);
		return mv;
	}

	/**
	 * 导出到Excel
	 * 
	 * @param table
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "excel/{table}/{uid}/{subTableAttr}" })
	public ModelAndView exportToExcel(@PathVariable("table") String table, @PathVariable("uid") String uid,
			@PathVariable("subTableAttr") String subTableAttr,
			@RequestParam(name = "view", required = false) String view, HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("exportToExcel，table=" + table + ", uid=" + uid + ", subTableAttr=" + subTableAttr);
		}
		AssertUtil.parameterEmpty(table, "table");
		AssertUtil.parameterEmpty(uid, "uid");
		AssertUtil.parameterEmpty(subTableAttr, "subTableAttr");

		return processExportToExcel(table, uid, subTableAttr, view, request.getRequestURI(),
				new EntityQueryMap(request.getParameterMap()));
	}

	/**
	 * 导出到Pdf
	 * 
	 * @param table
	 * @param uid
	 * @param subTableAttr
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "pdf/{table}/{uid}/{subTableAttr}" })
	public ModelAndView exportToPdf(@PathVariable("table") String table, @PathVariable("uid") String uid,
			@PathVariable("subTableAttr") String subTableAttr,
			@RequestParam(name = "view", required = false) String view, HttpServletRequest request,
			HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("exportToPdf，table=" + table + ", uid=" + uid + ", subTableAttr=" + subTableAttr);
		}
		AssertUtil.parameterEmpty(table, "table");
		AssertUtil.parameterEmpty(uid, "uid");
		AssertUtil.parameterEmpty(subTableAttr, "subTableAttr");

		return processExportToPdf(table, uid, subTableAttr, view, request.getRequestURI());
	}

	/**
	 * 显示实体Excel导入界面，导入提交到/file/upload，之后转向到/vp/excelimport/{table}/{uploadDir}
	 * 
	 * @param table
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "excelimport/{table}/{uid}/{subTableAttr}" }, method = RequestMethod.GET)
	public ModelAndView showExcelImport(@PathVariable("table") String table, @PathVariable("uid") String uid,
			@PathVariable("subTableAttr") String subTableAttr,
			@RequestParam(name = "view", required = false) String view, HttpServletRequest request,
			HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("showExcelImport，table=" + table + ", uid=" + uid + ", subTableAttr=" + subTableAttr);
		}
		AssertUtil.parameterEmpty(table, "table");
		AssertUtil.parameterEmpty(uid, "uid");
		AssertUtil.parameterEmpty(subTableAttr, "subTableAttr");

		PageInfo pageInfo = getBasePageInfo("导入数据", table, VisualPageConstants.COMMON_TEMPLATE_IMPORT);

		ModelAndView mv = getBaseModelAndView();
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("processUrl", HttpUtil.getUrl(request));
		return mv;
	}

	/**
	 * 从Excel导入数据。如果有错误返回导入失败的数据,正确的数据将写入数据库
	 * 
	 * @param table
	 * @param uploadDir
	 *            excel文件已经上传到该目录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "excelimport/{table}/{uid}/{subTableAttr}/{uploadDir}" })
	public ModelAndView postExcelImport(@PathVariable("table") String table, @PathVariable("uid") String uid,
			@PathVariable("subTableAttr") String subTableAttr, @PathVariable("uploadDir") String uploadDir,
			@RequestParam(name = "view", required = false) String view, HttpServletRequest request,
			HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("postExcelImport, table=" + table + ", uploadDir=" + uploadDir);
		}
		AssertUtil.parameterEmpty(table, "table");
		AssertUtil.parameterEmpty(uploadDir, "uploadDir");

		try {
			processImportExcel(table, uid, subTableAttr, uploadDir, view, request.getRequestURI());
			response.getWriter().write("{}");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
