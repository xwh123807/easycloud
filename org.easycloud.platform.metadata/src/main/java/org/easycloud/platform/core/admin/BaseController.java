package org.myfly.platform.core.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.myfly.platform.core.context.UserContext;
import org.myfly.platform.core.domain.AlertInfo;
import org.myfly.platform.core.domain.AlertLevel;
import org.myfly.platform.core.domain.FieldDataType;
import org.myfly.platform.core.flydata.service.EntityQueryMap;
import org.myfly.platform.core.flydata.service.FlyEntityMap;
import org.myfly.platform.core.flydata.service.IFlyDataAccessService;
import org.myfly.platform.core.message.service.IMessageQueueService;
import org.myfly.platform.core.message.service.MessageQueueConstants;
import org.myfly.platform.core.metadata.define.FieldDefinition;
import org.myfly.platform.core.metadata.internal.EntityMetaData;
import org.myfly.platform.core.metadata.service.IEntityMetaDataService;
import org.myfly.platform.core.utils.AppUtil;
import org.myfly.platform.core.utils.AssertUtil;
import org.myfly.platform.core.utils.DateUtil;
import org.myfly.platform.core.utils.EntityUrlUtil;
import org.myfly.platform.core.utils.FileUtil;
import org.myfly.platform.core.utils.PdfUtils;
import org.myfly.platform.core.utils.StringUtil;
import org.myfly.platform.core.utils.Utils;
import org.myfly.platform.core.visualpage.domain.PageInfo;
import org.myfly.platform.core.visualpage.domain.VisualPageType;
import org.myfly.platform.core.visualpage.service.IVisualPageService;
import org.myfly.platform.core.visualpage.service.VisualPageConstants;
import org.myfly.platform.core.visualpage.ui.EntityAction;
import org.myfly.platform.core.visualpage.ui.ViewMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;

/**
 * 实体MVC 控制器基类
 * 
 * @author xiangwanhong
 *
 */
public abstract class BaseController {
	private Log log = LogFactory.getLog(getClass());
	@Autowired
	private IVisualPageService visualPageService;
	@Autowired
	private IEntityMetaDataService entityMetaDataService;
	@Autowired
	private IMessageQueueService messageService;

	@Value("${server.port}")
	private String port;

	/**
	 * 获取服务地址
	 * 
	 * @return
	 */
	public String getBaseUrl() {
		return "http://localhost:" + port;
	}

	/**
	 * 设置用户警告信息，设置到用户会话中
	 * 
	 * @param alertInfo
	 */
	public void setUserAlertInfo(AlertInfo alertInfo) {
		UserContext.getUserSession().setAlertInfo(alertInfo);
		if (log.isDebugEnabled()) {
			log.debug(alertInfo.toString());
		}
	}

	/**
	 * 获取用户的警告信息，从用户会话中获取
	 * 
	 * @return
	 */
	public AlertInfo getUserAlertInfo() {
		return UserContext.getUserSession().getAlertInfo();
	}

	/**
	 * 根据页面类型，获取主视图名称
	 * 
	 * @param pageType
	 * @return
	 */
	public String getMasterViewName(VisualPageType pageType) {
		String name = null;
		if (pageType == null) {
			name = VisualPageConstants.PORTAL_EMPTY_VIEW;
		} else {
			switch (pageType) {
			case LISTPRINT:
			case PRINT:
				name = VisualPageConstants.PORTAL_PRINT_VIEW;
				break;
			case OUTLINE:
				name = VisualPageConstants.PORTAL_OUTLINE_VIEW;
				break;
			default:
				name = VisualPageConstants.PORTAL_SUBPAGE_VIEW;
				break;
			}
		}
		return getVisualPageService().getTemplateFile(name);
	}

	/**
	 * 获取ModelAndView，已添加常用工具类。资料获取视图时，必须从此处获取
	 * 
	 * @param pageType
	 * 
	 * @return
	 */
	public ModelAndView getBaseModelAndView(VisualPageType pageType) {
		ModelAndView mv = new ModelAndView();
		// 设置主模板视图
		mv.setViewName(getMasterViewName(pageType));
		// 增加工具集类
		mv.addObject("utils", new Utils());
		// 增加警告信息对象
		mv.addObject("alert", getUserAlertInfo());
		return mv;
	}
	
	public ModelAndView getBaseModelAndView(VisualPageType pageType, HttpServletRequest request) {
		if (request != null && StringUtils.isBlank(request.getHeader("Referer"))){
			pageType = null;
		}
		return getBaseModelAndView(pageType);
	}

	public ModelAndView getBaseModelAndView() {
		return getBaseModelAndView(VisualPageType.VIEW);
	}
	
	public ModelAndView getBaseModelAndView(HttpServletRequest request){
		VisualPageType pageType = VisualPageType.VIEW;
		if (request != null && StringUtils.isBlank(request.getHeader("Referer"))){
			pageType = null;
		}
		return getBaseModelAndView(pageType);
	}

	/**
	 * 获取MV
	 * 
	 * @param pageType
	 * @param pageInfo
	 * @param noSkin
	 * @return
	 */
	public ModelAndView getBaseModelAndView(VisualPageType pageType, PageInfo pageInfo) {
		ModelAndView mv = getBaseModelAndView(pageType);
		mv.addObject("pageInfo", pageInfo);
		return mv;
	}
	
	public ModelAndView getBaseModelAndView(VisualPageType pageType, PageInfo pageInfo, HttpServletRequest request) {
		if (request != null && StringUtils.isBlank(request.getHeader("Referer"))){
			pageType = null;
		}
		ModelAndView mv = getBaseModelAndView(pageType);
		mv.addObject("pageInfo", pageInfo);
		return mv;
	}

	/**
	 * 获取MV
	 * 
	 * @param title
	 * @param subTitle
	 * @param subViewName
	 * @param scripts
	 * @return
	 */
	public ModelAndView getBaseModelAndView(String title, String subTitle, String subViewName, String... scripts) {
		PageInfo pageInfo = getBasePageInfo(title, subTitle, subViewName);
		pageInfo.setScripts(scripts);
		ModelAndView mv = getBaseModelAndView();
		mv.addObject("pageInfo", pageInfo);
		return mv;
	}

	public ModelAndView getModelAndView(HttpServletRequest request, String title, String subTitle, String subViewName,
			String... scripts) {
		PageInfo pageInfo = getBasePageInfo(title, subTitle, subViewName);
		pageInfo.setScripts(scripts);
		ModelAndView mv = getBaseModelAndView();
		mv.addObject("pageInfo", pageInfo);
		return mv;
	}

	/**
	 * 跳转到Referer页面
	 * 
	 * @param url
	 * @return
	 */
	public ModelAndView redirectReferer(HttpServletRequest request) {
		String viewName = request.getHeader("referer");
		if (StringUtils.isBlank(viewName)) {
			viewName = getMasterViewName(VisualPageType.VIEW);
		}
		return new ModelAndView("redirect:" + viewName);
	}

	/**
	 * 重定向到页面。问题：客户端会多一次交互
	 * 
	 * @param url
	 * @return
	 */
	public ModelAndView redirectTo(String url) {
		if (log.isDebugEnabled()) {
			log.debug("redirect: " + url);
		}
		return new ModelAndView("redirect:" + url);
	}

	/**
	 * 服务端直接转向到另外一个地址。问题：客户端地址不会改变
	 * 
	 * @param url
	 * @return
	 */
	public ModelAndView forwardTo(String url) {
		if (log.isDebugEnabled()) {
			log.debug("forward: " + url);
		}
		return new ModelAndView("forward:" + url);
	}

	/**
	 * 获取PageInfo
	 * 
	 * @param title
	 * @param subTitle
	 * @param subViewName
	 * @return
	 */
	public PageInfo getBasePageInfo(final String title, final String subTitle, final String subViewName) {
		PageInfo pageInfo = new PageInfo();
		pageInfo.setTitle(title);
		pageInfo.setSubTitle(subTitle);
		pageInfo.setLayout(visualPageService.getCurrentLayout());
		if (StringUtils.isNotBlank(subViewName)) {
			if (subViewName.startsWith(pageInfo.getLayout())) {
				pageInfo.setTemplate(subViewName);
			} else {
				pageInfo.setTemplate(pageInfo.getLayout() + "/" + subViewName);
			}
		}
		pageInfo.setSideBarTemplate(
				visualPageService.getUserSideBarTemplteFile(UserContext.getUserTokenid(), VisualPageType.RENDER));
		return pageInfo;
	}

	/**
	 * 信息错误信息
	 * 
	 * @param messages
	 * @return
	 */
	public ModelAndView showError(final String title, final String... messages) {
		PageInfo pageInfo = getBasePageInfo("", "错误：" + title, VisualPageConstants.COMMON_TEMPLATE_ERROR);
		ModelAndView mv = getBaseModelAndView(VisualPageType.VIEW);
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("messages", messages);
		return mv;
	}

	/**
	 * 显示提示信息
	 * 
	 * @param title
	 * @param messages
	 * @return
	 */
	public ModelAndView showInfo(final String title, final String... messages) {
		PageInfo pageInfo = getBasePageInfo("", "提示：" + title, VisualPageConstants.COMMON_TEMPLATE_ERROR);
		ModelAndView mv = getBaseModelAndView(VisualPageType.VIEW);
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("messages", messages);
		return mv;
	}

	/**
	 * 获取实体视图
	 * 
	 * @param table
	 * @param uid
	 *            新增模式时，uid为空
	 * @param pageType
	 * @return
	 */
	public ModelAndView getEntityViewByTableAndUidAndPageType(String table, String uid, VisualPageType pageType, HttpServletRequest request,
			ViewMode viewMode, String formViewName) {
		if (log.isDebugEnabled()) {
			log.debug(
					"getViewByTableAndUidAndPageType，table=" + table + " uid=" + uid + " pageType=" + pageType.name());
		}
		ModelAndView mv = getBaseModelAndView(pageType, request);
		mv.addObject("table", table);
		try {
			AssertUtil.parameterEmpty(table, "table");
			AssertUtil.parameterEmpty(pageType, "pageType");
			if (!pageType.equals(VisualPageType.NEW)) {
				AssertUtil.parameterEmpty(uid, "uid");
			}

			EntityMetaData entityMetaData = getEntityMetaDataService().getEntityMetaData(table);
			AssertUtil.parameterEmpty(entityMetaData, "entityMetaData", "名称为[" + table + "]的元模型不存在.");

			String templateFile = getVisualPageService().getEntityTemplateFile(table.toLowerCase(), pageType,
					formViewName, viewMode);
			PageInfo pageInfo = getBasePageInfo(entityMetaData.getTableDefinition().getTitle(), null, templateFile);
			// 获取实体数据
			FlyEntityMap entity = null;
			if (StringUtil.isNotEmpty(uid)) {
				// 非新增场景
				boolean printMode = VisualPageType.PRINT.equals(pageType) || VisualPageType.LISTPRINT.equals(pageType)
						|| VisualPageType.EDIT.equals(pageType);
				entity = getFlyDataAccessService(table).findOne(table, uid, formViewName, printMode);
				AssertUtil.recordNotFound(entity, table, "uid", uid);
			}
			// 设置页面子标题
			switch (pageType) {
			case NEW:
				pageInfo.setSubTitle("新增");
				break;
			default:
				if (entity.containsKey("name")) {
					pageInfo.setSubTitle(entity.get("name"));
				}
				break;
			}
			mv.addObject("pageInfo", pageInfo);
			mv.addObject("uid", uid);
			mv.addObject("obj", entity);
		} catch (Exception e) {
			setUserAlertInfo(new AlertInfo("获取视图失败", e));
		}
		return mv;
	}

	public IVisualPageService getVisualPageService() {
		return visualPageService;
	}

	public void setVisualPageService(IVisualPageService visualPageService) {
		this.visualPageService = visualPageService;
	}

	/**
	 * 根据子表属性，获取对应子表名称
	 * 
	 * @param table
	 * @param subTableAttr
	 * @return
	 */
	public String getSubTableName(String table, String subTableAttr) {
		EntityMetaData metaData = getEntityMetaDataService().getEntityMetaData(table);
		Assert.notNull(metaData, "不存在实体名称为[" + table + "]的元模型");
		FieldDefinition field = metaData.getField(subTableAttr);
		Assert.notNull(field, "实体[" + table + "]不存在名称为[" + subTableAttr + "]的属性");
		Assert.isTrue(
				FieldDataType.MDRELATION.equals(field.getDataType())
						|| FieldDataType.FLYMDRELATION.equals(field.getDataType()),
				"属性[" + table + "." + subTableAttr + "]必须为子表属性");
		return field.getRelationTable();
	}

	/**
	 * 获取实体下载文件名
	 * 
	 * @param tableName
	 * @return
	 */
	public String getTempFileName(String tableName, String ext) {
		String tmpDir = FileUtil.createTempDirectoryPath();
		String fileName = tmpDir + tableName + "_" + DateUtil.datetimeToStr(DateUtil.now()) + "." + ext;
		return fileName;
	}

	/**
	 * 获取实体下载文件，相对路径
	 * 
	 * @param fileName
	 * @return
	 */
	public String getDownloadFileName(String fileName) {
		return fileName.substring(FileUtils.getTempDirectoryPath().length());
	}

	public IEntityMetaDataService getEntityMetaDataService() {
		return entityMetaDataService;
	}

	public void setEntityMetaDataService(IEntityMetaDataService entityMetaDataService) {
		this.entityMetaDataService = entityMetaDataService;
	}

	public IFlyDataAccessService getFlyDataAccessService(final String entityName) {
		return AppUtil.getFlyDataAccessService(entityName);
	}

	/**
	 * 更新视图显示模式
	 * 
	 * @param viewMode
	 * @param request
	 */
	public ViewMode updateViewMode(ViewMode viewMode, HttpServletRequest request) {
		if (viewMode == null) {
			viewMode = ViewMode.SUBPAGE;
		}
		return viewMode;
	}
	
	/**
	 * 发送导出附件消息
	 * @param url
	 * @param pdfFile
	 */
	public void sendExportLogDataToQueue(String url, String pdfFile){
		messageService.sendMessageToQueue(MessageQueueConstants.QUEUE_EXPORT_LOG, null);
	}

	/**
	 * 导出实体列表到PDF，导出完成后转到下载页
	 * 
	 * @param table
	 * @param listViewName
	 * @param request
	 * @return
	 */
	public ModelAndView processExportToPdf(String table, String listViewName, String url) {
		Assert.hasLength(table);
		Assert.hasLength(url);
		String pdfFile = getTempFileName(table, "pdf");
		PdfUtils.exportToPdf(getBaseUrl(),
				EntityUrlUtil.getEntityActionUrl(EntityAction.LISTPRINT, table, null, listViewName), pdfFile);
		sendExportLogDataToQueue(url, pdfFile);
		return forwardTo(EntityUrlUtil.getFileDownloadUrl(getDownloadFileName(pdfFile)));
	}

	/**
	 * 导出实体列表到Excel，导出完成后转到下载页
	 * 
	 * @param table
	 * @param listViewName
	 * @param url
	 * @param params
	 * @return
	 */
	public ModelAndView processExportToExcel(String table, String listViewName, String url, EntityQueryMap params) {
		Assert.hasLength(table);
		Assert.hasLength(url);
		String excelFile = getTempFileName(table, "csv");
		getFlyDataAccessService(table).exportToExcel(table, listViewName, params, excelFile);
		sendExportLogDataToQueue(url, excelFile);
		return forwardTo(EntityUrlUtil.getFileDownloadUrl(getDownloadFileName(excelFile)));
	}

	/**
	 * 导出指定实体到PDF，导出完成后转到下载页
	 * 
	 * @param table
	 * @param uid
	 * @param formViewName
	 * @param request
	 * @return
	 */
	public ModelAndView processExportToPdf(String table, String uid, String formViewName, String url) {
		Assert.hasLength(table);
		Assert.hasLength(uid);
		Assert.hasLength(url);
		String pdfFile = getTempFileName(table, "pdf");
		PdfUtils.exportToPdf(getBaseUrl(),
				EntityUrlUtil.getEntityActionUrl(EntityAction.PRINT, table, uid, formViewName), pdfFile);
		sendExportLogDataToQueue(url, pdfFile);
		return forwardTo(EntityUrlUtil.getFileDownloadUrl(getDownloadFileName(pdfFile)));
	}

	/**
	 * 导出指定实体的子表到PDF，导出完成后转到下载页
	 * 
	 * @param table
	 * @param uid
	 * @param formViewName
	 * @param request
	 * @return
	 */
	public ModelAndView processExportToPdf(String table, String uid, String subTableAttr, String viewName, String url) {
		Assert.hasLength(table);
		Assert.hasLength(uid);
		Assert.hasLength(url);
		String pdfFile = getTempFileName(table, "pdf");
		PdfUtils.exportToPdf(getBaseUrl(),
				EntityUrlUtil.getSubEntityActionUrl(EntityAction.PRINT, table, uid, subTableAttr, null, viewName),
				pdfFile);
		sendExportLogDataToQueue(url, pdfFile);
		return forwardTo(EntityUrlUtil.getFileDownloadUrl(getDownloadFileName(pdfFile)));
	}

	/**
	 * 导出指定实体子表到Excel，导出完成后转到下载页
	 * 
	 * @param table
	 * @param listViewName
	 * @param url
	 * @param params
	 * @return
	 */
	public ModelAndView processExportToExcel(String table, String uid, String subTableAttr, String listViewName,
			String url, EntityQueryMap params) {
		Assert.hasLength(table);
		Assert.hasLength(url);
		String excelFile = getTempFileName(table, "csv");
		getFlyDataAccessService(table).exportToExcel(table, uid, subTableAttr, listViewName, params, excelFile);
		sendExportLogDataToQueue(url, excelFile);
		return forwardTo(EntityUrlUtil.getFileDownloadUrl(getDownloadFileName(excelFile)));
	}

	/**
	 * 将目录uploadDir下的excel文件导入到实体中
	 * 
	 * @param table
	 * @param uploadDir
	 * @param view
	 * @param requestURI
	 * @return
	 */
	public ModelAndView processImportExcell(String table, String uploadDir, String view, String requestURI) {
		// 从上传文件目录导入数据到表中
		String showEntityUrl = EntityUrlUtil.getEntityActionUrl(EntityAction.LIST, table, null, view);
		String showEntityExcelImportUrl = EntityUrlUtil.getEntityActionUrl(EntityAction.SHOWEXCELIMPORT, table, null,
				view);
		try {
			String excelErrorFile = getFlyDataAccessService(table).importExcel(table, view,
					FileUtils.getTempDirectoryPath() + uploadDir, requestURI);
			return internalProcessImportExcel(uploadDir, excelErrorFile, showEntityUrl, showEntityExcelImportUrl, view);
		} catch (Exception e) {
			setUserAlertInfo(new AlertInfo("导入失败", e));
			// 导入失败，显示重新导入界面
			return redirectTo(showEntityExcelImportUrl);
		}
	}

	/**
	 * 将目录uploadDir下的excel文件导入到指定实体的子表中
	 * 
	 * @param table
	 * @param uid
	 * @param subTableAttr
	 * @param uploadDir
	 * @param view
	 * @param url
	 * @return
	 */
	public ModelAndView processImportExcel(String table, String uid, String subTableAttr, String uploadDir, String view,
			String url) {
		String showEntityUrl = EntityUrlUtil.getEntityActionUrl(EntityAction.VIEW, table, uid, view);
		String showEntityExcelImportUrl = EntityUrlUtil.getSubEntityActionUrl(EntityAction.SHOWEXCELIMPORT, table, uid,
				subTableAttr, null, view);
		try {
			String excelErrorFile = getFlyDataAccessService(table).importExcel(table, uid, subTableAttr, view,
					FileUtils.getTempDirectoryPath() + uploadDir, url);
			return internalProcessImportExcel(uploadDir, excelErrorFile, showEntityUrl, showEntityExcelImportUrl, view);
		} catch (Exception e) {
			setUserAlertInfo(new AlertInfo("导入失败", e));
			// 导入失败，显示重新导入界面
			return redirectTo(showEntityExcelImportUrl);
		}
	}

	private ModelAndView internalProcessImportExcel(String uploadDir, String excelErrorFile, String showEntityUrl,
			String showEntityExcelImportUrl, String view) {
		try {
			// 从上传文件目录导入数据到表中
			String importLogUrl = "<a href='"
					+ EntityUrlUtil.getEntityActionUrl(EntityAction.LIST, "dataimportlog", null, null) + "?groupUid="
					+ uploadDir + "' target='_blank'>点击此处查看详细日志.</a>";
			if (StringUtils.isBlank(excelErrorFile)) {
				// 导入成功
				setUserAlertInfo(new AlertInfo(AlertLevel.SUCCESS, "导入成功", importLogUrl));
			} else {
				// 导入失败或部分成功
				setUserAlertInfo(new AlertInfo(AlertLevel.WARNING, "导入失败或部分成功", importLogUrl));
			}
			// 导入成功后，跳转到实体查看页面
			return redirectTo(showEntityUrl);
		} catch (Exception e) {
			setUserAlertInfo(new AlertInfo("导入失败", e));
			// 导入失败，显示重新导入界面
			return redirectTo(showEntityExcelImportUrl);
		}
	}
}
