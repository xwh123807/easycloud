package org.easycloud.platform.visualpage.service;

import java.lang.annotation.Annotation;
import java.util.List;

import org.easycloud.platform.core.domain.TemplateBuilderInfo;
import org.easycloud.platform.metadata.internal.EntityViewInfo;
import org.easycloud.platform.visualpage.domain.VisualPageType;
import org.easycloud.platform.visualpage.ui.ViewMode;

public interface IVisualPageService {

	/**
	 * 获取当前布局
	 * 
	 * @return
	 */
	String getCurrentLayout();

	/**
	 * 默认模板布局名称
	 * 
	 * @return
	 */
	String getDefaultLayout();

	/**
	 * 检查模板是否已经存在
	 * 
	 * @param template
	 * @return
	 */
	String checkTemplateExistsLayout(String template);

	/**
	 * 获取模板工作目录
	 * 
	 * @return
	 */
	String getWorkTemplatePath();

	/**
	 * 获取模板文件
	 * @param subViewName
	 * @return
	 */
	String getTemplateFile(String subViewName);

	/**
	 * 获取实体页面模板
	 * 
	 * @param table
	 *            表名
	 * @param pageType
	 *            视图类型
	 * @return
	 */
	String getEntityTemplateFile(String table, VisualPageType pageType, String view, ViewMode viewMode);

	/**
	 * 根据显示注解获取对象页面模板
	 * 
	 * @param objectName
	 *            对象名，唯一标示对象
	 * @param objectViewBean
	 *            对象显示定义注解
	 * @param pageType
	 *            视图类型
	 * @return
	 */
	<T extends Annotation> String getObjectTemplateFile(String objectName, T objectViewBean, VisualPageType pageType);

	/**
	 * 获取对象页面模板，先从对象类上获取显示注解
	 * 
	 * @param objectName
	 *            对象名，唯一标示对象
	 * @param objectClass
	 *            对象
	 * @param annotationClass
	 *            对象显示定义注解
	 * @param pageType
	 *            视图类型
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	String getObjectTemplateFile(String objectName, Class<?> objectClass, Class annotationClass,
			VisualPageType pageType);

	/**
	 * 获取对象页面模板，先从方法上获取显示注解
	 * 
	 * @param objectName
	 * @param defineClass
	 * @param methodName
	 * @param annotationClass
	 * @param pageType
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	String getObjectTemplateFile(String objectName, Class<?> defineClass, String methodName, Class annotationClass,
			VisualPageType pageType);

	/**
	 * 获取所有实体视图信息
	 * 
	 * @return
	 */
	List<EntityViewInfo> getAllEntitiesViewInfo();

	/**
	 * 获取系统已注册的模板构造器
	 * 
	 * @return
	 */
	List<TemplateBuilderInfo> getAllTemplateBuilder();

	/**
	 * 获取实体模板内容
	 * 
	 * @param table
	 * @param pageType
	 * @return
	 */
	String getEntityTemplateContent(String table, VisualPageType pageType, String view);

	/**
	 * 获取用户菜单模板
	 * @param userTokenid
	 * @param pageType
	 * @return
	 */
	String getUserSideBarTemplteFile(String userTokenid, VisualPageType pageType);

}
