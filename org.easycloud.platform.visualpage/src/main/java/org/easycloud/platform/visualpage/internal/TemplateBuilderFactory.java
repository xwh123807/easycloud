package org.easycloud.platform.visualpage.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easycloud.platform.metadata.utils.AssertUtil;
import org.easycloud.platform.visualpage.builder.BaseTemplateBuilder;
import org.easycloud.platform.visualpage.domain.VisualPageType;

public class TemplateBuilderFactory {
	private static Log log = LogFactory.getLog(TemplateBuilderFactory.class);
	/**
	 * 全局缓存，存储注册的模板
	 */
	private static Map<String, BaseTemplateBuilder> templates = new HashMap<>();
	
	/**
	 * 注册模板构造器
	 * @param template
	 */
	public static void registerTemplate(BaseTemplateBuilder template){
		templates.put(template.getTemplateName(), template);
	}
	
	/**
	 * 获取所有模板
	 * @return
	 */
	public static Map<String, BaseTemplateBuilder> getAllTemplates(){
		return templates;
	}
	
	/**
	 * 获取模板构造器Key
	 * @param layout
	 * @param pageType
	 * @return
	 */
	public static String getBuilderKey(String layout, VisualPageType pageType){
		return layout + "-" + pageType.name();
	}
	
	/**
	 * 获取指定模板
	 * @param layout
	 * @param pageType
	 * @return
	 */
	public static BaseTemplateBuilder getTemplate(String layout, VisualPageType pageType){
		String key = getBuilderKey(layout, pageType);
		BaseTemplateBuilder result = templates.get(key);
		if (result == null){
			log.error("没有注册模板构造器，名称为" + key);
		}
		AssertUtil.parameterEmpty(result, key, "通过实现BaseTemplateBuilder来增加模板构造器.");
		return result;
	}
}
