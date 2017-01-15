package org.easycloud.platform.visualpage.internal;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easycloud.platform.core.admin.BaseController;
import org.easycloud.platform.metadata.service.EntityMetaData;
import org.easycloud.platform.metadata.utils.AssertUtil;
import org.easycloud.platform.visualpage.domain.PageInfo;
import org.easycloud.platform.visualpage.domain.VisualPageType;
import org.easycloud.platform.visualpage.service.VisualPageConstants;
import org.easycloud.platform.visualpage.ui.ViewMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 实体列表查询
 * 
 * @author xiangwanhong
 *
 */
@Controller
@RequestMapping("vp")
public class EntityListVisualPageController extends BaseController {
	private Log log = LogFactory.getLog(getClass());
	
	@RequestMapping()
	@ResponseBody
	public Map<String, Map<String, String>> help(){
		Map<String, String> map = new LinkedHashMap<>();
		map.put("/vp/list/{table}", "实体列表查询");
		map.put("/vp/outline/{table}", "outline视图显示实体列表");
		map.put("/vp/outline/{table}/{uid}/{subTableAttr}", "outline视图显示实体子表列表");
		map.put("/vp/search/{table}", "显示实体搜索视图");
		map.put("/vp/listprint/{table}", "显示实体列表打印视图");
		map.put("/vp/excel/{table}", "将实体列表导出到Excel");
		map.put("/vp/excelimport/{table}", "显示实体excel导入界面");
		map.put("/vp/excelimport/{table}/{uploadDir}", "将目录下的excel导入到表中");
		
		Map<String, Map<String, String>> help = new LinkedHashMap<>();
		help.put("实体列表", map);
		help.put("单个实体", EntityVisualPageController.help());
		help.put("实体子表", SubEntityVisualPageController.help());
		return help;
	}

	/**
	 * 对象列表查询。<br>
	 * 如果列表取数是异步处理，则不用取数，由客户端异步取数；否则取全部数据。<br>
	 * 
	 * 支持查询 参考资料： http://blog.csdn.net/yaerfeng/article/details/23658391 <br>
	 * 
	 * @param table
	 * @return
	 */
	@RequestMapping(value = { "list/{table}" })
	public ModelAndView listEntity(@PathVariable("table") String table,
			@RequestParam(name = "view", required = false) String view,
			@RequestParam(name = "viewmode", required = false) ViewMode viewMode, HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("viewEntityListByTable, table=" + table);
		}
		AssertUtil.parameterEmpty(table, "table");
		viewMode = updateViewMode(viewMode, request);
		return processEntityListModelView(table, view, request, VisualPageType.LIST, viewMode);
	}

	/**
	 * outline视图显示实体列表，支持分页
	 * 
	 * @param table
	 * @param view
	 * @param viewMode
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "outline/{table}" })
	public ModelAndView outlineListEntity(@PathVariable("table") String table,
			@RequestParam(name = "view", required = false) String listViewName,
			@RequestParam(name = "viewmode", required = false) ViewMode viewMode, HttpServletRequest request) {
		viewMode = updateViewMode(viewMode, request);
		ModelAndView mv = processEntityListModelView(table, listViewName, request, VisualPageType.LISTOUTLINE, viewMode);
		return mv;
	}

	/**
	 * outline视图显示实体子表列表，支持分页
	 * 
	 * @param table
	 * @param view
	 * @param viewMode
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "outline/{table}/{uid}/{subTableAttr}" })
	public ModelAndView outlineListSubEntity(@PathVariable("table") String table, @PathVariable("uid") String uid,
			@PathVariable("subTableAttr") String subTableAttr,
			@RequestParam(name = "view", required = false) String formViewName,
			@RequestParam(name = "viewmode", required = false) ViewMode viewMode, HttpServletRequest request) {
		viewMode = updateViewMode(viewMode, request);
		String subTableName = getSubTableName(table, subTableAttr);
		String subViewName = getVisualPageService().getEntityTemplateFile(subTableName, VisualPageType.LISTOUTLINE,
				formViewName, viewMode);
		PageInfo pageInfo = getBasePageInfo("查看子表数据", subTableName, subViewName);
		ModelAndView mv = getBaseModelAndView();
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("table", table);
		mv.addObject("uid", uid);
		mv.addObject("subTableName", subTableName);
		mv.addObject("subTableAttr", subTableAttr);
		mv.addObject("view", formViewName);
		PageRequest pageable = buildPageRequestFromRequest(request);
		Page<?> page = getFlyDataAccessService(table).findAllForSubEntityWithPage(table, uid, subTableAttr, formViewName, null,
				pageable, false);
		mv.addObject("pager", new Pagination(page));
		mv.addObject("objlist", page.getContent());
		return mv;
	}

	/**
	 * 实体搜索
	 * 
	 * @param table
	 * @param view
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "search/{table}" })
	public ModelAndView searchEntity(@PathVariable("table") String table,
			@RequestParam(name = "q", required = false, defaultValue = "*:*") String searchText,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "view", required = false) String listViewName,
			@RequestParam(name = "viewmode", required = false) ViewMode viewMode, HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("searchEntity, table=" + table + ", searchText=" + searchText + ", view=" + listViewName);
		}
		AssertUtil.parameterEmpty(table, "table");
		EntityMetaData entityMetaData = getEntityMetaDataService().getEntityMetaData(table);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData");

		VisualPageType pageType = VisualPageType.SEARCH;
		viewMode = updateViewMode(viewMode, request);
		String subViewName = getVisualPageService().getEntityTemplateFile(table, pageType, listViewName, viewMode);
		PageInfo pageInfo = getBasePageInfo("搜索", entityMetaData.getTableDefinition().getTitle(), subViewName);
		ModelAndView mv = getBaseModelAndView(pageType, pageInfo);
		// 获取参数，在界面上显示条件的值
		Map<String, String> queryMap = new LinkedHashMap<>();
		for (Map.Entry<String, String[]> param : request.getParameterMap().entrySet()) {
			queryMap.put(param.getKey(), param.getValue()[0]);
		}
		mv.addObject("obj", queryMap);
		Page<?> pagedata = getFlyDataAccessService(table).findAllForSearch(table, listViewName, searchText, page - 1,
				size);
		mv.addObject("pager", new Pagination(pagedata));
		mv.addObject("objlist", pagedata.getContent());
		return mv;
	}

	/**
	 * 处理列表视图数据查询
	 * 
	 * @param table
	 * @param view
	 * @param request
	 * @param pageType
	 * @return
	 */
	private ModelAndView processEntityListModelView(String table, String view, HttpServletRequest request,
			VisualPageType pageType, ViewMode viewMode) {
		EntityMetaData entityMetaData = getEntityMetaDataService().getEntityMetaData(table);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData", "[" + table + "]不是有效的表名、类名或实体名.");

		String subViewName = getVisualPageService().getEntityTemplateFile(table, pageType, view, viewMode);
		PageInfo pageInfo = getBasePageInfo("列表", entityMetaData.getTableDefinition().getTitle(), subViewName);
		ModelAndView mv = getBaseModelAndView(pageType, pageInfo, request);

		// 非异步处理，一次加载全部数据
		Map<String, String[]> params = new LinkedHashMap<>(request.getParameterMap());
		// 获取参数，在界面上显示条件的值
		Map<String, String> queryMap = new LinkedHashMap<>();
		for (Map.Entry<String, String[]> param : params.entrySet()) {
			queryMap.put(param.getKey(), param.getValue()[0]);
		}
		mv.addObject("obj", queryMap);

		boolean printMode = VisualPageType.LISTPRINT.equals(pageType);
		// 为打印视图时，一次取出全部数据
		if (printMode || !entityMetaData.getListDefinition(view).isServerSideMode()) {
			List<FlyEntityMap> list = getFlyDataAccessService(table).findAll(table, view,
					new EntityQueryMap(params), false);
			mv.addObject("objlist", list);
		} else {
			PageRequest pageable = buildPageRequestFromRequest(request);
			Page<FlyEntityMap> page = getFlyDataAccessService(table).findAll(table, view,
					new EntityQueryMap(params), pageable, printMode);
			mv.addObject("pager", new Pagination(page));
			mv.addObject("objlist", page.getContent());
		}
		return mv;
	}

	private PageRequest buildPageRequestFromRequest(HttpServletRequest request) {
		int page = 0;
		int size = 12;
		if (request != null) {
			if (StringUtils.isNotBlank(request.getParameter("page"))) {
				page = Integer.parseInt(request.getParameter("page"));
				page = page - 1;
			}
			if (StringUtils.isNotBlank(request.getParameter("size"))) {
				size = Integer.parseInt(request.getParameter("size"));
			}
		}
		PageRequest pageRequest = new PageRequest(page, size);
		return pageRequest;
	}

	/**
	 * 打印视图
	 * 
	 * @param table
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "listprint/{table}" })
	public ModelAndView printListEntity(@PathVariable("table") String table,
			@RequestParam(name = "view", required = false) String view, HttpServletRequest request,
			HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("printView, table=" + table);
		}
		AssertUtil.parameterEmpty(table, "table");

		return processEntityListModelView(table, view, request, VisualPageType.LISTPRINT, ViewMode.PAGE);
	}

	/**
	 * 导出到Excel
	 * 
	 * @param table
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "excel/{table}" })
	public ModelAndView exportToExcel(@PathVariable("table") String table,
			@RequestParam(name = "view", required = false) String view, HttpServletRequest request,
			HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("exportToExcel, table=" + table);
		}
		AssertUtil.parameterEmpty(table, "table");
		return processExportToExcel(table, view, request.getRequestURI(), new EntityQueryMap(request.getParameterMap()));
	}

	/**
	 * 导出到Pdf
	 * 
	 * @param table
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "pdf/{table}" })
	public ModelAndView exportToPdf(@PathVariable("table") String table,
			@RequestParam(name = "view", required = false) String view, HttpServletRequest request,
			HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("exportToPdf, table=" + table);
		}
		AssertUtil.parameterEmpty(table, "table");
		return processExportToPdf(table, view, request.getRequestURI());
	}

	/**
	 * 显示实体Excel导入界面，导入提交到/file/upload，之后转向到/vp/excelimport/{table}/{uploadDir}
	 * 
	 * @param table
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "excelimport/{table}" }, method = RequestMethod.GET)
	public ModelAndView showExcelImport(@PathVariable("table") String table,
			@RequestParam(name = "view", required = false) String view, HttpServletRequest request,
			HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("excelImport, table=" + table);
		}
		AssertUtil.parameterEmpty(table, "table");
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
	@RequestMapping(value = { "excelimport/{table}/{uploadDir}" }, method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView postExcelImport(@PathVariable("table") String table,
			@PathVariable("uploadDir") String uploadDir, @RequestParam(name = "view", required = false) String view,
			HttpServletRequest request, HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("postExcelImport, table=" + table + ", uploadDir=" + uploadDir);
		}
		AssertUtil.parameterEmpty(table, "table");
		AssertUtil.parameterEmpty(uploadDir, "uploadDir");
		try {
			processImportExcell(table, uploadDir, view, request.getRequestURI());
			response.getWriter().write("{}");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
