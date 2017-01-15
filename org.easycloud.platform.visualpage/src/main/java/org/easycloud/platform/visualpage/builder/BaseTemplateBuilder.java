package org.easycloud.platform.visualpage.builder;

import java.io.File;
import java.lang.annotation.Annotation;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easycloud.platform.metadata.utils.AssertUtil;
import org.easycloud.platform.visualpage.domain.VisualPageType;
import org.easycloud.platform.visualpage.internal.TemplateBuilderFactory;
import org.easycloud.platform.visualpage.ui.BaseRender;
import org.easycloud.platform.visualpage.ui.ViewMode;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

/**
 * 模板基类
 * 
 * @author xiangwanhong
 *
 */
@CacheConfig(cacheNames = "visualpage-templatecontent")
public abstract class BaseTemplateBuilder {
	private Log log = LogFactory.getLog(getClass());

	/**
	 * 模板类型
	 * 
	 * @return
	 */
	public abstract VisualPageType getType();

	/**
	 * 模板描述
	 * 
	 * @return
	 */
	public abstract String getDescription();

	/**
	 * 获取布局名称
	 * 
	 * @return
	 */
	public abstract String getLayoutName();

	public BaseTemplateBuilder() {
		TemplateBuilderFactory.registerTemplate(this);
	}

	/**
	 * 获取模板注册名称，名称必须唯一
	 * 
	 * @return
	 */
	public String getTemplateName() {
		return TemplateBuilderFactory.getBuilderKey(getLayoutName(), getType());
	}

	/**
	 * 构建实体模板
	 * 
	 * @param mtable
	 * @return
	 */
	@Cacheable(key = "#entityName + '-' + #viewName + '-' + #viewMode")
	public String buildTemplateContent(String entityName, String viewName, ViewMode viewMode) {
		throw new RuntimeException("必须由子类实现");
	}

	/**
	 * 构建自定义对象模板
	 * 
	 * @param objectBean
	 * @return
	 */
	public <T extends Annotation> String buildTemplateContent(T objectBean) {
		throw new RuntimeException("必须由子类实现");
	}

	public String buildTemplateContent(BaseRender render) {
		throw new RuntimeException("必须由子类实现");
	}

	/**
	 * 构建实体的显示模板，保存到templateFileName文件中
	 * 
	 * @param entityName
	 *            实体名称
	 * @param templateFileName
	 */
	public void buildTemplateFile(String templateFileName, String entityName, final String viewName, final ViewMode viewMode) {
		AssertUtil.parameterEmpty(templateFileName, "templateFileName");
		AssertUtil.parameterEmpty(entityName, "entityName");
		try {
			String content = buildTemplateContent(entityName, viewName, viewMode);
			if (log.isDebugEnabled()) {
				log.debug("模板内容：" + content);
			}
			FileUtils.writeStringToFile(new File(templateFileName), content);
		} catch (Exception e) {
			log.error("Velocity模板文件创建失败，文件：" + templateFileName + "，错误信息：" + e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 构建自定义对象的显示模板，保存到templateFileName文件中
	 * 
	 * @param templateFileName
	 * @param objectBean
	 */
	public <T extends Annotation> void buildTemplateFile(String templateFileName, T objectBean) {
		AssertUtil.parameterEmpty(templateFileName, "templateFileName");
		AssertUtil.parameterEmpty(objectBean, "objectBean");
		try {
			String content = buildTemplateContent(objectBean);
			if (log.isDebugEnabled()) {
				log.debug("模板内容：" + content);
			}
			FileUtils.writeStringToFile(new File(templateFileName), content);
		} catch (Exception e) {
			log.error("Velocity模板文件创建失败，文件：" + templateFileName + "，错误信息：" + e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 构建Render的模板
	 * @param templateFileName
	 * @param render
	 */
	public void buildTemplateFile(String templateFileName, BaseRender render) {
		AssertUtil.parameterEmpty(templateFileName, "templateFileName");
		AssertUtil.parameterEmpty(render, "render");
		try {
			String content = buildTemplateContent(render);
			if (log.isDebugEnabled()) {
				log.debug("模板内容：" + content);
			}
			FileUtils.writeStringToFile(new File(templateFileName), content);
		} catch (Exception e) {
			log.error("Velocity模板文件创建失败，文件：" + templateFileName + "，错误信息：" + e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
}
