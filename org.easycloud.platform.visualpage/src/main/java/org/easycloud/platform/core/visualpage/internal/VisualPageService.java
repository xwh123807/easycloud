package org.myfly.platform.core.visualpage.internal;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.myfly.platform.core.context.UserContext;
import org.myfly.platform.core.domain.TemplateBuilderInfo;
import org.myfly.platform.core.metadata.internal.EntityMetaData;
import org.myfly.platform.core.metadata.internal.EntityViewInfo;
import org.myfly.platform.core.metadata.service.IEntityMetaDataService;
import org.myfly.platform.core.user.domain.Menu;
import org.myfly.platform.core.user.service.IMenuService;
import org.myfly.platform.core.utils.AssertUtil;
import org.myfly.platform.core.visualpage.builder.BaseTemplateBuilder;
import org.myfly.platform.core.visualpage.config.FlyVPProperties;
import org.myfly.platform.core.visualpage.domain.VisualPageType;
import org.myfly.platform.core.visualpage.service.IVisualPageService;
import org.myfly.platform.core.visualpage.service.VisualPageConstants;
import org.myfly.platform.core.visualpage.ui.ViewMode;
import org.myfly.platform.core.visualpage.ui.control.SideBarRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * 可视页面服务。页面支持存在代码中、按模板动态创建、自定义保存在数据库中的<br>
 * 功能：<br>
 * 1、动态生成实体的列表、单屏等页面Velocity模板<br>
 * 2、提供自定义格式的支持<br>
 * 3、提供系统默认的完整布局页面，布局名称为default<br>
 * 4、支持多套布局，用户可以绑定到指定布局上，当指定布局下不存在页面模板时，使用default布局下的模板<br>
 * 5、模板存储路径：{layoutName}/{name}/{type}，存储时将名称都转换为小写，避免拼写错误<br>
 * 
 * 
 * @author xiangwanhong
 *
 */
@Service
@CacheConfig(cacheNames = "visualpage-service")
public class VisualPageService implements IVisualPageService {
	private Log log = LogFactory.getLog(getClass());

	@Autowired
	private IEntityMetaDataService entityMetaDataService;

	@Autowired
	private IMenuService menuService;
	
	@Autowired
	private FlyVPProperties flyVpProperties;

	public String getWorkTemplatePath() {
		return flyVpProperties.getWorkTemplatePath();
	}

	/**
	 * 默认模板
	 * 
	 * @return
	 */
	public String getDefaultLayout() {
		return VisualPageConstants.LAYOUT_DEFAULT_NAME;
	}

	/**
	 * 获取当前用户布局名称 TODO 应从当前上下文中获取
	 * 
	 * @return
	 */
	public String getCurrentLayout() {
		return UserContext.getUserSession().getLayoutName();
	}

	/**
	 * 检查模板是否已经存在<br>
	 * 1、检查自定义模板中是否存在;<br>
	 * 2、检查当前布局中是否存在；<br>
	 * 3、检查默认布局中是否存在；<br>
	 * 
	 * @param template
	 * @return 如果存在，返回Layout的名称，不存在则返回null
	 */
	@Override
	public String checkTemplateExistsLayout(String template) {
		String layout = getCurrentLayout();
		// 检查自定义模板中是否存在
		File file = new File(getWorkTemplatePath() + getCurrentLayout() + "/" + template);
		if (!file.exists()) {
			// 判断在系统预置代码中是否存在模板
			URL url = this.getClass().getResource("/templates/" + getCurrentLayout() + "/" + template);
			if (url == null) {
				if (!getCurrentLayout().equalsIgnoreCase(getDefaultLayout())) {
					url = this.getClass().getResource("/templates/" + getDefaultLayout() + "/" + template);
					layout = (url != null) ? getDefaultLayout() : null;
				} else {
					layout = null;
				}
			}
		}
		return layout;
	}

	/**
	 * 查找模板文件所在位置
	 * 
	 * @param templateFile
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private URL getTemplateFileLocation(String templateFile) throws Exception {
		// 先在工作目录中检查
		URL url = new URL("file:" + getWorkTemplatePath() + templateFile);
		if (url == null) {
			// 在代码中查找
			url = this.getClass().getResource("/templates/" + templateFile);
		}
		return url;
	}

	/**
	 * 根据实体和页面类型，返回实体页面模板<br>
	 * 1、检查模板是否存在，如果存在返回<br>
	 * 2、没有则创建模板，并存储后返回<br>
	 * 
	 * @param table
	 * @param pageType
	 * @return
	 */
	@Override
//	@Cacheable(key = "#table + '-' + #pageType + '-' + #view + '-' + #viewMode")
	public String getEntityTemplateFile(String table, VisualPageType pageType, String view, ViewMode viewMode) {
		AssertUtil.parameterEmpty(table, "table");
		String viewName = table.toLowerCase() + "/" + view + "/" + pageType.name().toLowerCase() + "/"
				+ viewMode.name().toLowerCase() + ".vm";
		// 检查模板在代码中是否存在，不存在则创建到临时目录
		String layout = checkTemplateExistsLayout(viewName);
		if (layout == null) {
			layout = getCurrentLayout();
			String templateFileName = getWorkTemplatePath() + layout + "/" + viewName;
			TemplateBuilderFactory.getTemplate(getCurrentLayout(), pageType).buildTemplateFile(templateFileName, table,
					view, viewMode);
			if (log.isInfoEnabled()) {
				log.info("创建实体" + table + "模板文件路径：" + templateFileName);
			}
		}
		return layout + "/" + viewName;
	}

	/**
	 * 获取用户菜单模板文件
	 * 
	 * @return
	 */
//	@Cacheable(key = "'sideBar-' + #userTokenid")
	public String getUserSideBarTemplteFile(String userTokenid, VisualPageType pageType) {
		String viewName = "users/sidebar/" + userTokenid + ".vm";
		String layout = checkTemplateExistsLayout(viewName);
		if (layout == null) {
			layout = getCurrentLayout();
			String templateFileName = getWorkTemplatePath() + layout + "/" + viewName;
			SideBarRender render = new SideBarRender(menuService.getAllMenus().toArray(new Menu[] {}));
			TemplateBuilderFactory.getTemplate(getCurrentLayout(), pageType).buildTemplateFile(templateFileName,
					render);
			if (log.isInfoEnabled()) {
				log.info("用户菜单模板文件路径：" + templateFileName);
			}
		}
		return layout + "/" + viewName;
	}

	/**
	 * 根据对象的定义注解，获取对应模板<br>
	 * 1、检查模板是否存在，如果存在返回<br>
	 * 2、没有则创建模板，并存储后返回<br>
	 * 
	 * @param objectName
	 * @param objectBean
	 * @param pageType
	 * @return
	 */
//	@Cacheable(key = "#objectName + '-' + #pageType")
	public <T extends Annotation> String getObjectTemplateFile(String objectName, T objectViewBean,
			VisualPageType pageType) {
		AssertUtil.parameterEmpty(objectName, "objectName");
		AssertUtil.parameterEmpty(objectViewBean, "objectViewBean");
		AssertUtil.parameterEmpty(pageType, "pageType");
		String view = VisualPageConstants.CUSTOM_OBJECT_PATH + objectName.toLowerCase() + "/"
				+ pageType.name().toLowerCase() + ".vm";
		// 检查模板在代码中是否存在，不存在则创建到临时目录
		String layout = checkTemplateExistsLayout(view);
		if (layout == null) {
			layout = getCurrentLayout();
			String templateFileName = getWorkTemplatePath() + layout + "/" + view;
			TemplateBuilderFactory.getTemplate(getCurrentLayout(), pageType).buildTemplateFile(templateFileName,
					objectViewBean);
			if (log.isInfoEnabled()) {
				log.info("创建对象" + objectName + "模板文件路径：" + templateFileName);
			}
		}
		return layout + "/" + view;
	}

	/**
	 * 获取对象显示模板
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getObjectTemplateFile(String objectName, Class<?> defineClass, String methodName,
			Class annotationClass, VisualPageType pageType) {
		Annotation objectBean = null;
		try {
			// 方法可能由多个参数，找到有注解的方法
			for (Method tmethod : defineClass.getMethods()) {
				if (tmethod.getName().equals(methodName)) {
					objectBean = tmethod.getAnnotation(annotationClass);
					if (objectBean != null) {
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getObjectTemplateFile(objectName, objectBean, pageType);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getObjectTemplateFile(String objectName, Class<?> objectClass, Class annotationClass,
			VisualPageType pageType) {
		AssertUtil.parameterEmpty(objectName, "objectName");
		AssertUtil.parameterEmpty(objectClass, "objectClass");
		AssertUtil.parameterEmpty(annotationClass, "annotationClass");
		AssertUtil.parameterEmpty(pageType, "pageType");
		Annotation objectViewBean = objectClass.getAnnotation(annotationClass);
		AssertUtil.parameterEmpty(objectViewBean, "objectViewBean");
		return getObjectTemplateFile(objectName, objectViewBean, pageType);
	}

	/**
	 * 获取实体的各种视图
	 * 
	 * @return
	 */
	public List<EntityViewInfo> getAllEntitiesViewInfo() {
		List<EntityViewInfo> views = new ArrayList<>();
		List<Class<?>> entities = entityMetaDataService.getAllEntityClasses();
		for (Class<?> item : entities) {
			EntityMetaData metaData = entityMetaDataService.getEntityMetaData(item.getName());
			EntityViewInfo view = new EntityViewInfo();
			String table = metaData.getTableDefinition().getTableName();
			view.setTableName(table);
			view.setEntityClass(metaData.getEntityClass().getName());
			String content = "<a href=\"/admin/meta/{0}/\" target=\"_blank\">元模型</a>";
			view.setMetaModel(MessageFormat.format(content, table));
			content = "<a href=\"/vp/{0}/{1}/{2}\" target=\"_blank\">{3}</a>&nbsp;<a href=\"/admin/template/{1}/{3}\" target=\"_blank\">模板</a>";
			view.setListView(MessageFormat.format(content, "list", table, "", "LIST"));
			view.setSearchView(MessageFormat.format(content, "search", table, "", "SEARCH"));
			view.setPrintView(MessageFormat.format(content, "print", table, "", "PRINT"));
			content = "<a href=\"/admin/template/{1}/{3}\" target=\"_blank\">模板</a>";
			view.setFormView(MessageFormat.format(content, "view", table, "{uid}", "VIEW"));
			view.setFormEditView(MessageFormat.format(content, "edit", table, "{uid}", "EDIT"));
			view.setFormNewView(MessageFormat.format(content, "new", table, "{uid}", "NEW"));
			view.setOutlineView(MessageFormat.format(content, "outline", table, "{uid}", "OUTLINE"));
			views.add(view);
		}
		return views;
	}

	@Override
	public List<TemplateBuilderInfo> getAllTemplateBuilder() {
		List<TemplateBuilderInfo> items = new ArrayList<>();
		for (BaseTemplateBuilder tb : TemplateBuilderFactory.getAllTemplates().values()) {
			TemplateBuilderInfo item = new TemplateBuilderInfo();
			item.setClassName(tb.getClass().getName());
			item.setType(tb.getType());
			item.setLayoutName(tb.getLayoutName());
			item.setDescription(tb.getDescription());
			items.add(item);
		}
		return items;
	}

	@Override
	public String getEntityTemplateContent(String table, VisualPageType pageType, String name) {
		String templateFile = getEntityTemplateFile(table, pageType, name, ViewMode.SUBPAGE);
		URL url = null;
		try {
			url = getTemplateFileLocation(templateFile);
		} catch (Exception e) {
		}
		String content = "模板文件没有找到，" + templateFile;
		if (url != null) {
			try {
				content = FileUtils.readFileToString(FileUtils.toFile(url));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content;
	}

	/**
	 * 获取视图模板文件
	 * 
	 * @param subViewName
	 * @return
	 */
	public String getTemplateFile(String subViewName) {
		return getCurrentLayout() + "/" + subViewName;

	}
}
