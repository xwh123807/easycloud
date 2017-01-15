package org.easycloud.platform.visualpage.service;

public class VisualPageConstants {
	/**
	 * 默认布局名称
	 */
	public static final String LAYOUT_DEFAULT_NAME = "default";
	/**
	 * 自定义模板根路径
	 */
	public static final String CUSTOM_OBJECT_PATH = "customobject/";
	
	/**
	 * Portal 首页视图模板
	 */
	public static final String PORTAL_INDEX_VIEW = "portal/index";
	
	/**
	 * Portal 内容页面模板
	 */
	public static final String PORTAL_SUBPAGE_VIEW = "portal/subpage";
	
	/**
	 * 无内容模板，只有脚本
	 */
	public static final String PORTAL_EMPTY_VIEW = "portal/empty";
	
	/**
	 * 简档模板
	 */
	public static final String PORTAL_OUTLINE_VIEW = "portal/outline";
	
	/**
	 * 打印模板
	 */
	public static final String PORTAL_PRINT_VIEW = "portal/print";
	
	/**
	 * 出现错误时显示的页面
	 */
	public static final String PORTAL_ERROR = "error";
	
	//---------------------------ADMIN----------------------------<
	public static final String ADMIN_TEMPLATE_BEANS = "admin/beans.vm";
	public static final String ADMIN_TEMPLATE_MAPPINGS = "admin/mappings.vm";
	public static final String ADMIN_TEMPLATE_AUTOCONFIG = "admin/autoconfig.vm";
	public static final String ADMIN_TEMPLATE_TRACE = "admin/trace.vm";
	public static final String ADMIN_TEMPLATE_METRICS = "admin/metrics.vm";
	public static final String ADMIN_TEMPLATE_DUMP = "admin/dump.vm";
	public static final String ADMIN_TEMPLATE_ENV = "admin/env.vm";
	public static final String ADMIN_TEMPLATE_CONFIGPROPS = "admin/configprops.vm";
	//---------------------------ADMIN---------------------------->
	
	/**
	 * 导入子页面模板
	 */
	public static final String COMMON_TEMPLATE_IMPORT = "portal/import.vm";
	/**
	 * 错误子页面模板
	 */
	public static final String COMMON_TEMPLATE_ERROR = "portal/error.vm";
}
