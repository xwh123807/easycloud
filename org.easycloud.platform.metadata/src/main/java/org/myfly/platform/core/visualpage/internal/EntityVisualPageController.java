package org.myfly.platform.core.visualpage.internal;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.myfly.platform.core.admin.BaseController;
import org.myfly.platform.core.domain.AlertInfo;
import org.myfly.platform.core.domain.AlertLevel;
import org.myfly.platform.core.flydata.internal.EntityUtil;
import org.myfly.platform.core.flydata.service.EntityMap;
import org.myfly.platform.core.metadata.internal.EntityException;
import org.myfly.platform.core.metadata.internal.EntityMetaData;
import org.myfly.platform.core.utils.AssertUtil;
import org.myfly.platform.core.utils.EntityUrlUtil;
import org.myfly.platform.core.utils.StringUtil;
import org.myfly.platform.core.visualpage.domain.VisualPageType;
import org.myfly.platform.core.visualpage.ui.EntityAction;
import org.myfly.platform.core.visualpage.ui.ViewMode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 实体增删改查UI<br>
 * 
 * @author xiangwanhong
 *
 */
@Controller
@RequestMapping("vp")
public class EntityVisualPageController extends BaseController {
	private Log log = LogFactory.getLog(getClass());

	public static Map<String, String> help() {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("/vp/{table}/{uid}", "显示实体查看视图");
		map.put("/vp/{table}/{uid}?form", "显示实体编辑视图");
		map.put("/vp/{table}", "显示新增实体视图");
		map.put("/vp/{table} [POST]", "保存实体，保存成功后显示该实体");
		map.put("/vp/{table} [PATCH]", "保存后继续新增实体，保存成功后显示新增实体视图");
		map.put("/vp/{table}/{uid} [DEL]", "实体删除");
		map.put("/vp/{table}/{uid} [PUT]", "实体修改");
		map.put("/vp/outline/{table}/{uid}", "大纲视图显示实体");
		map.put("/vp/print/{table}/{uid}", "显示实体打印视图");
		map.put("/vp/pdf/{table}/{uid}", "将实体打印视图内容导出到pdf，并下载pdf");
		return map;
	}

	/**
	 * 打开查看或编辑实体UI，如果参数中有form表示编辑打开
	 * 
	 * @param table
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = { "{table}/{uid}" }, method = RequestMethod.GET)
	public ModelAndView viewOrEditEntity(@PathVariable("table") String table, @PathVariable("uid") String uid,
			@RequestParam(name = "view", required = false) String formViewName,
			@RequestParam(name = "viewmode", required = false) ViewMode viewMode, HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("viewOrEditEntity，table=" + table + " uid=" + uid);
		}
		AssertUtil.parameterEmpty(table, "table");
		AssertUtil.parameterEmpty(uid, "uid");
		// 判断是否编辑模式，如果有form参数表示编辑模式打开
		boolean isEditOpen = request.getParameterMap().containsKey("form");

		viewMode = updateViewMode(viewMode, request);
		return getEntityViewByTableAndUidAndPageType(table, uid, isEditOpen ? VisualPageType.EDIT : VisualPageType.VIEW,
				request, viewMode, formViewName);
	}

	/**
	 * 显示实体
	 * 
	 * @param table
	 * @param uid
	 * @param formViewName
	 * @param viewMode
	 * @param request
	 * @return
	 */
	private ModelAndView viewEntity(@PathVariable("table") String table, @PathVariable("uid") String uid,
			@RequestParam(name = "view", required = false) String formViewName,
			@RequestParam(name = "viewmode", required = false) ViewMode viewMode, HttpServletRequest request) {
		viewMode = updateViewMode(viewMode, request);
		return getEntityViewByTableAndUidAndPageType(table, uid, VisualPageType.VIEW, request, viewMode, formViewName);
	}

	/**
	 * 显示新增实体UI
	 * 
	 * @param table
	 * @return
	 */
	@RequestMapping(value = "{table}", method = RequestMethod.GET)
	public ModelAndView showNewEntity(@PathVariable("table") String table,
			@RequestParam(name = "view", required = false) String formViewName,
			@RequestParam(name = "viewmode", required = false) ViewMode viewMode, HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("showNewEntity, table=" + table);
		}
		AssertUtil.parameterEmpty(table, "table");
		viewMode = updateViewMode(viewMode, request);
		return getEntityViewByTableAndUidAndPageType(table, null, VisualPageType.NEW, request, viewMode, formViewName);
	}

	/**
	 * 保存新增实体。
	 * 
	 * @param table
	 * @return
	 */
	@RequestMapping(value = "{table}", method = RequestMethod.POST)
	public ModelAndView saveEntity(@PathVariable("table") String table,
			@RequestParam(name = "view", required = false) String formViewName,
			@RequestParam(name = "viewmode", required = false) ViewMode viewMode, HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("saveEntity, table=" + table);
			log.debug(StringUtil.getRequestDebugInfo(request));
		}
		viewMode = updateViewMode(viewMode, request);
		return processSaveEntity(table, formViewName, viewMode, request, false);
	}

	/**
	 * 保存后继续新增
	 * 
	 * @param table
	 * @param view
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "{table}", method = RequestMethod.PATCH)
	public ModelAndView saveAndNewEntity(@PathVariable("table") String table,
			@RequestParam(name = "view", required = false) String formViewName,
			@RequestParam(name = "viewmode", required = false) ViewMode viewMode, HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("saveAndNewEntity, table=" + table);
			log.debug(StringUtil.getRequestDebugInfo(request));
		}
		viewMode = updateViewMode(viewMode, request);
		return processSaveEntity(table, formViewName, viewMode, request, true);
	}

	/**
	 * 实体新增保存
	 * 
	 * @param table
	 * @param view
	 * @param request
	 * @param saveAndNew
	 *            保存后是否继续新增
	 */
	private ModelAndView processSaveEntity(String table, String formViewName, ViewMode viewMode,
			HttpServletRequest request, boolean saveAndNew) {
		AssertUtil.parameterEmpty(table, "table");
		Map<String, String[]> params = new LinkedHashMap<>(request.getParameterMap());
		// 保存实体
		String pkValue = null;
		try {
			// 保存实体
			pkValue = getFlyDataAccessService(table).saveEntity(table, formViewName, EntityMap.build(params));
			AssertUtil.parameterEmpty(pkValue, "pkValue");
			setUserAlertInfo(new AlertInfo(AlertLevel.SUCCESS, "保存成功"));
		} catch (Exception e) {
			setUserAlertInfo(new AlertInfo("保存失败", e));
			// 保存失败，仍然停留在新增页面
			ModelAndView mv = showNewEntity(table, formViewName, viewMode, request);
			mv.addObject("obj", params);
			return mv;
		}
		if (saveAndNew) {
			// 保存后新增，跳转到新增页面
			return showNewEntity(table, formViewName, viewMode, request);
		} else {
			// 保存，跳转到查看页面
			return viewEntity(table, pkValue, formViewName, viewMode, request);
		}
	}

	@RequestMapping(value = { "/{table}/{uid}" }, method = RequestMethod.DELETE)
	public ModelAndView delEntity(@PathVariable("table") String table, @PathVariable("uid") String uid,
			HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("delEntity, uid=" + uid);
		}
		AssertUtil.parameterEmpty(table, "table");
		AssertUtil.parameterEmpty(uid, "uid");
		try {
			int count = getFlyDataAccessService(table).delOne(table, uid);
			if (count != 1) {
				throw new EntityException("删除错误，应当删除一条，实际删除" + count + "条.");
			}
			setUserAlertInfo(new AlertInfo(AlertLevel.SUCCESS, "删除成功"));
		} catch (Exception e) {
			setUserAlertInfo(new AlertInfo("删除失败", e));
		}
		// 删除后，跳到列表查询页面
		return redirectTo(EntityUrlUtil.getEntityActionUrl(EntityAction.LIST, table, null, null));
	}

	/**
	 * 修改后保存实体
	 * 
	 * @param table
	 * @param uid
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "{table}/{uid}" }, method = RequestMethod.PUT)
	public ModelAndView updateEntity(@PathVariable("table") String table, @PathVariable("uid") String uid,
			@RequestParam(name = "view", required = false) String formViewName, HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("saveEditedEntityByUid, uid=" + uid);
			log.debug(StringUtil.getRequestDebugInfo(request));
		}
		try {
			AssertUtil.parameterEmpty(table, "table");
			AssertUtil.parameterEmpty(uid, "uid");
			// 保存实体
			Map<String, String[]> values = request.getParameterMap();
			EntityMetaData metaData = getEntityMetaDataService().getEntityMetaData(table);
			Serializable pkValue = metaData.getPKFieldDefinition().buildPK(uid);
			Object updateEntity = EntityUtil.buildNewEntityForRequest(table, metaData, pkValue, values);
			AssertUtil.parameterEmpty(updateEntity, "updateEntity");
			getFlyDataAccessService(table).updateEntity(table, uid, formViewName, EntityMap.build(values));
			Object entity = getFlyDataAccessService(table).findOne(table, uid, formViewName, false);
			AssertUtil.parameterEmpty(entity, "entity");
			setUserAlertInfo(new AlertInfo(AlertLevel.SUCCESS, "保存成功"));
		} catch (Exception e) {
			// 保存失败，可以继续编辑再提交
			setUserAlertInfo(new AlertInfo("保存失败", e));
		}
		return viewEntity(table, uid, formViewName, ViewMode.SUBPAGE, request);
	}

	/**
	 * 大纲视图显示指定对象
	 * 
	 * @param table
	 * @param uid
	 * @return
	 */
	@RequestMapping("outline/{table}/{uid}")
	public ModelAndView outlineViewEntity(@PathVariable("table") String table, @PathVariable("uid") String uid,
			@RequestParam(name = "view", required = false) String viewName, HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("outlineViewByTableAndUid，table=" + table + " uid=" + uid);
		}
		AssertUtil.parameterEmpty(table, "table");
		AssertUtil.parameterEmpty(uid, "uid");

		return getEntityViewByTableAndUidAndPageType(table, uid, VisualPageType.OUTLINE, request, ViewMode.OUTLINEPAGE,
				viewName);
	}

	/**
	 * 打印视图
	 * 
	 * @param table
	 * @param uid
	 * @param formViewName
	 * @return
	 */
	@RequestMapping("print/{table}/{uid}")
	public ModelAndView printEntity(@PathVariable("table") String table, @PathVariable("uid") String uid,
			@RequestParam(name = "view", required = false) String formViewName, HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("printEntity，table=" + table + " uid=" + uid);
		}
		AssertUtil.parameterEmpty(table, "table");
		AssertUtil.parameterEmpty(uid, "uid");

		return getEntityViewByTableAndUidAndPageType(table, uid, VisualPageType.PRINT, request, ViewMode.SUBPAGE,
				formViewName);
	}

	/**
	 * 导出到pdf
	 * 
	 * @param table
	 * @param uid
	 * @param formViewName
	 * @return
	 */
	@RequestMapping("pdf/{table}/{uid}")
	public ModelAndView exportToPdf(@PathVariable("table") String table, @PathVariable("uid") String uid,
			@RequestParam(name = "view", required = false) String formViewName, HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("exportToPdf, table=" + table);
		}
		AssertUtil.parameterEmpty(table, "table");
		return processExportToPdf(table, uid, formViewName, request.getRequestURI());
	}
}
