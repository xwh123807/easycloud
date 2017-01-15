package org.easycloud.platform.core.admin;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.easycloud.platform.core.cache.internal.RedisServerInfo;
import org.easycloud.platform.core.cache.service.CacheInfo;
import org.easycloud.platform.core.cache.service.CacheObjectInfo;
import org.easycloud.platform.core.cache.service.ICacheService;
import org.easycloud.platform.core.domain.TemplateBuilderInfo;
import org.easycloud.platform.metadata.annotation.view.ObjectListView;
import org.easycloud.platform.metadata.annotation.view.PropertyView;
import org.easycloud.platform.metadata.internal.EntityViewInfo;
import org.easycloud.platform.metadata.internal.JsonEntityMetaData;
import org.easycloud.platform.metadata.service.IEntityMetaDataService;
import org.easycloud.platform.metadata.utils.AssertUtil;
import org.easycloud.platform.metadata.utils.ClassUtil;
import org.easycloud.platform.visualpage.domain.PageInfo;
import org.easycloud.platform.visualpage.domain.VisualPageType;
import org.easycloud.platform.visualpage.service.VisualPageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 查看系统配置信息
 * 
 * @author xiangwanhong
 *
 */

@Controller
@RequestMapping("admin")
public class AdminController extends BaseController {
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private TraceRepository traceRepository;
	@Autowired
	private ConfigurationPropertiesReportEndpoint configurationPropertiesReportEndpoint;
	@Autowired
	private MetricsEndpoint metricsEndpoint;
	@Autowired
	private AutoConfigurationReportEndpoint autoConfigurationReportEndpoint;
	@Autowired
	private EnvironmentEndpoint environmentEndpoint;
	@Autowired
	private RequestMappingEndpoint requestMappingEndpoint;
	@Autowired
	private ICacheService cacheService;
	@Autowired
	private IMessageQueueService messageQueueService;

	@RequestMapping(value = { "/", "help" })
	public Map<String, String> help() {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("/admin/beans", "Spring Beans");
		map.put("/admin/mappings", "资源映射");
		map.put("/admin/autoconfig", "自动初始配置");
		map.put("/admin/trace", "跟踪");
		map.put("/admin/metrics", "监控统计");
		map.put("/admin/dump", "线程");
		map.put("/admin/env", "环境");
		map.put("/admin/configprops", "参数");
		map.put("/admin/redis", "Redis");
		return map;
	}

	private PageInfo getAdminPageInfo(String subtitle) {
		PageInfo pageInfo = getBasePageInfo("管理", subtitle, null);
		return pageInfo;
	}

	public class BeanInfo {
		private String beanName;
		private String type;
		private String scope;

		public String getBeanName() {
			return beanName;
		}

		public void setBeanName(String beanName) {
			this.beanName = beanName;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getScope() {
			return scope;
		}

		public void setScope(String scope) {
			this.scope = scope;
		}
	}

	/**
	 * 实现类：BeansEndpoint 思路：遍历spring [2015-11-24 08:55:43.831] log4j - 28766
	 * INFO [main] --- EndpointHandlerMapping: Mapped "{[/beans],methods=[GET]}"
	 * onto public java.lang.Object
	 * org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
	 * 
	 * @return
	 */
	@RequestMapping("beans")
	@ObjectListView(title = "Spring Beans", fields = { @PropertyView(name = "beanName", title = "名称"),
			@PropertyView(name = "type", title = "类型"), @PropertyView(name = "scope", title = "范围") })
	public ModelAndView beans(HttpServletRequest request) throws Exception {
		PageInfo pageInfo = getAdminPageInfo("beans");
		pageInfo.setTemplate(getVisualPageService().getObjectTemplateFile("spring.beans", getClass(), "beans",
				ObjectListView.class, VisualPageType.OLIST));
		ModelAndView mv = getBaseModelAndView(request);
		mv.addObject("pageInfo", pageInfo);
		List<BeanInfo> beans = new ArrayList<>();
		ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
		for (String beanName : context.getBeanDefinitionNames()) {
			BeanDefinition bean = context.getBeanFactory().getBeanDefinition(beanName);
			BeanInfo beanInfo = new BeanInfo();
			beanInfo.setBeanName(ClassUtil.getClassShortName(beanName));
			beanInfo.setScope(bean.getScope());
			beanInfo.setType(context.getType(beanName).getName());
			beans.add(beanInfo);
		}
		mv.addObject("objlist", beans);
		return mv;
	}

	/**
	 * [2015-11-22 19:22:53.706] log4j - 23567 INFO [main] ---
	 * EndpointHandlerMapping: Mapped "{[/mappings],methods=[GET]}" onto public
	 * java.lang.Object
	 * org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
	 * 
	 * @return
	 */
	@RequestMapping("mappings")
	@ObjectListView(title = "资源映射", fields = { @PropertyView(name = "key", title = "URL"),
			@PropertyView(name = "value", title = "配置") })
	public ModelAndView mappings(HttpServletRequest request) {
		PageInfo pageInfo = getAdminPageInfo("资源");
		pageInfo.setTemplate(getVisualPageService().getObjectTemplateFile("admin.mappings", getClass(), "mappings",
				ObjectListView.class, VisualPageType.OLIST));
		ModelAndView mv = getBaseModelAndView(request);
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("objlist", requestMappingEndpoint.invoke().entrySet());
		return mv;
	}

	/**
	 * [2015-11-22 19:22:53.706] log4j - 23567 INFO [main] ---
	 * EndpointHandlerMapping: Mapped "{[/autoconfig],methods=[GET]}" onto
	 * public java.lang.Object
	 * org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
	 * 
	 * @return
	 */
	@RequestMapping("autoconfig")
	public ModelAndView autoconfig(HttpServletRequest request) {
		PageInfo pageInfo = getAdminPageInfo("配置");
		pageInfo.setTemplate(getVisualPageService().getTemplateFile(VisualPageConstants.ADMIN_TEMPLATE_AUTOCONFIG));
		ModelAndView mv = getBaseModelAndView(request);
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("report", autoConfigurationReportEndpoint.invoke());
		return mv;
	}

	/**
	 * [2015-11-22 19:22:53.706] log4j - 23567 INFO [main] ---
	 * EndpointHandlerMapping: Mapped "{[/trace],methods=[GET]}" onto public
	 * java.lang.Object
	 * org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
	 * 
	 * @return
	 */
	@RequestMapping("trace")
	@ObjectListView(title = "跟踪", fields = { @PropertyView(name = "timestamp", title = "时间"),
			@PropertyView(name = "info.method", title = "方法"), @PropertyView(name = "info.path", title = "请求"),
			@PropertyView(name = "info.headers.response.status", title = "状态") })
	public ModelAndView trace(HttpServletRequest request) {
		PageInfo pageInfo = getAdminPageInfo("跟踪");
		pageInfo.setTemplate(getVisualPageService().getObjectTemplateFile("admin.trace", getClass(), "trace",
				ObjectListView.class, VisualPageType.OLIST));
		ModelAndView mv = getBaseModelAndView(request);
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("objlist", traceRepository.findAll());
		return mv;
	}

	/**
	 * [2015-11-22 19:22:53.707] log4j - 23567 INFO [main] ---
	 * EndpointHandlerMapping: Mapped "{[/metrics/{name:.*}],methods=[GET]}"
	 * onto public java.lang.Object
	 * org.springframework.boot.actuate.endpoint.mvc.MetricsMvcEndpoint.value(
	 * java.lang.String)
	 */

	/**
	 * [2015-11-22 19:22:53.707] log4j - 23567 INFO [main] ---
	 * EndpointHandlerMapping: Mapped "{[/metrics],methods=[GET]}" onto public
	 * java.lang.Object
	 * org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
	 * 
	 * @return
	 */
	@RequestMapping("metrics")
	@ObjectListView(title = "统计", fields = { @PropertyView(name = "key", title = "指标"),
			@PropertyView(name = "value", title = "值") })
	public ModelAndView metrics(HttpServletRequest request) {
		PageInfo pageInfo = getAdminPageInfo("统计");
		pageInfo.setTemplate(getVisualPageService().getObjectTemplateFile("admin.metrics", getClass(), "metrics",
				ObjectListView.class, VisualPageType.OLIST));
		ModelAndView mv = getBaseModelAndView(request);
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("objlist", metricsEndpoint.invoke().entrySet());
		return mv;
	}

	/**
	 * [2015-11-22 19:22:53.707] log4j - 23567 INFO [main] ---
	 * EndpointHandlerMapping: Mapped "{[/info],methods=[GET]}" onto public
	 * java.lang.Object
	 * org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
	 * 
	 * @return
	 */
	@RequestMapping("info")
	public ModelAndView info() {
		return null;
	}

	/**
	 * [2015-11-22 19:22:53.707] log4j - 23567 INFO [main] ---
	 * EndpointHandlerMapping: Mapped "{[/dump],methods=[GET]}" onto public
	 * java.lang.Object
	 * org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
	 * 
	 * @return
	 */
	@RequestMapping("dump")
	@ObjectListView(title = "线程", fields = { @PropertyView(name = "threadId", title = "ID"),
			@PropertyView(name = "threadName", title = "名称"), @PropertyView(name = "threadState", title = "状态"),
			@PropertyView(name = "lockName", title = "锁") })
	public ModelAndView dump(HttpServletRequest request) {
		PageInfo pageInfo = getAdminPageInfo("线程");
		pageInfo.setTemplate(getVisualPageService().getObjectTemplateFile("admin.dump", getClass(), "dump",
				ObjectListView.class, VisualPageType.OLIST));
		ModelAndView mv = getBaseModelAndView(request);
		mv.addObject("pageInfo", pageInfo);
		List<ThreadInfo> list = Arrays.asList(ManagementFactory.getThreadMXBean().dumpAllThreads(true, true));
		mv.addObject("objlist", list);
		return mv;
	}

	/**
	 * [2015-11-22 19:22:53.707] log4j - 23567 INFO [main] ---
	 * EndpointHandlerMapping: Mapped "{[/env/{name:.*}],methods=[GET]}" onto
	 * public java.lang.Object
	 * org.springframework.boot.actuate.endpoint.mvc.EnvironmentMvcEndpoint.
	 * value(java.lang.String)
	 * 
	 */

	/**
	 * [2015-11-22 19:22:53.707] log4j - 23567 INFO [main] ---
	 * EndpointHandlerMapping: Mapped "{[/env],methods=[GET]}" onto public
	 * java.lang.Object
	 * org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
	 * 
	 * @return
	 */
	@RequestMapping("env")
	public ModelAndView env(HttpServletRequest request) {
		PageInfo pageInfo = getAdminPageInfo("环境");
		pageInfo.setTemplate(getVisualPageService().getTemplateFile(VisualPageConstants.ADMIN_TEMPLATE_ENV));
		ModelAndView mv = getBaseModelAndView(request);
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("map", environmentEndpoint.invoke());
		return mv;
	}

	/**
	 * [2015-11-22 19:22:53.707] log4j - 23567 INFO [main] ---
	 * EndpointHandlerMapping: Mapped "{[/configprops],methods=[GET]}" onto
	 * public java.lang.Object
	 * org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()
	 * 
	 * @return
	 */
	@ObjectListView(title = "参数", fields = { @PropertyView(name = "key", title = "指标"),
			@PropertyView(name = "value", title = "值") })
	@RequestMapping("configprops")
	public ModelAndView configprops(HttpServletRequest request) {
		PageInfo pageInfo = getAdminPageInfo("参数");
		pageInfo.setTemplate(getVisualPageService().getObjectTemplateFile("admin.configprops", getClass(),
				"configprops", ObjectListView.class, VisualPageType.OLIST));
		ModelAndView mv = getBaseModelAndView(request);
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("objlist", configurationPropertiesReportEndpoint.invoke().entrySet());

		// Map<String, Object> configBeans =
		// applicationContext.getBeansWithAnnotation(ConfigurationProperties.class);
		// mv.addObject("objlist", configBeans.entrySet());
		return mv;
	}

	@RequestMapping("configprops2")
	@ResponseBody
	public Map<String, Object> getConfigProps(HttpServletRequest request) {
		return applicationContext.getBeansWithAnnotation(ConfigurationProperties.class);
	}

	/**
	 * [2015-11-22 19:22:53.708] log4j - 23567 INFO [main] ---
	 * EndpointHandlerMapping: Mapped "{[/health]}" onto public java.lang.Object
	 * org.springframework.boot.actuate.endpoint.mvc.HealthMvcEndpoint.invoke(
	 * java.security.Principal)
	 * 
	 * @return
	 */
	@RequestMapping("health")
	public ModelAndView health() {
		return null;
	}

	/**
	 * 显示系统cache情况
	 * 
	 * @return
	 */
	@RequestMapping("cache")
	public ModelAndView cacheList(HttpServletRequest request) {
		PageInfo pageInfo = getAdminPageInfo("缓存");
		pageInfo.setTemplate(getVisualPageService().getObjectTemplateFile("admin.cacheList", CacheInfo.class,
				ObjectListView.class, VisualPageType.OLIST));
		ModelAndView mv = getBaseModelAndView(request);
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("objlist", cacheService.getCachesInfo());
		return mv;
	}

	/**
	 * 清除所有缓存，清除后回到缓存查看页面
	 * 
	 * @return
	 */
	@RequestMapping("cache/clear")
	public ModelAndView cacheClear() {
		cacheService.clearAll();
		// TODO 重定向
		return new ModelAndView("redirect:/admin/cache");
	}

	/**
	 * 显示指定的缓存类型
	 * 
	 * @return
	 */
	@RequestMapping("cache/{cacheName}")
	public ModelAndView cacheItem(@PathVariable("cacheName") String cacheName, HttpServletRequest request) {
		AssertUtil.parameterEmpty(cacheName, "cacheName");
		PageInfo pageInfo = getAdminPageInfo("缓存 - " + cacheName);
		pageInfo.setTemplate(getVisualPageService().getObjectTemplateFile("admin.cacheItem", CacheObjectInfo.class,
				ObjectListView.class, VisualPageType.OLIST));
		ModelAndView mv = getBaseModelAndView(request);
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("objlist", cacheService.getCacheObjectsInfo(cacheName));
		return mv;
	}

	/**
	 * 清空指定的缓存类型
	 * 
	 * @param cacheName
	 * @return
	 */
	@RequestMapping("cache/clear/{cacheName}")
	public ModelAndView clearCacheItem(@PathVariable("cacheName") String cacheName, HttpServletRequest request) {
		AssertUtil.parameterEmpty(cacheName, "cacheName");
		cacheService.clearCache(cacheName);
		return cacheItem(cacheName, request);
	}

	/**
	 * 显示所有实体的各种视图
	 * 
	 * @return
	 */
	@RequestMapping("entityviews")
	public ModelAndView listAllEntitiesView(HttpServletRequest request) {
		PageInfo pageInfo = getAdminPageInfo("实体视图");
		pageInfo.setTemplate(getVisualPageService().getObjectTemplateFile("admin.entityview", EntityViewInfo.class,
				ObjectListView.class, VisualPageType.OLIST));
		ModelAndView mv = getBaseModelAndView(request);
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("objlist", getVisualPageService().getAllEntitiesViewInfo());
		return mv;
	}

	/**
	 * 显示所有注册的模板构造器
	 * 
	 * @return
	 */
	@RequestMapping("templatebuilders")
	public ModelAndView listAllTemplateBuilder(HttpServletRequest request) {
		PageInfo pageInfo = getAdminPageInfo("模板构造器");
		pageInfo.setTemplate(getVisualPageService().getObjectTemplateFile("admin.templatebuilder",
				TemplateBuilderInfo.class, ObjectListView.class, VisualPageType.OLIST));
		ModelAndView mv = getBaseModelAndView(request);
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("objlist", getVisualPageService().getAllTemplateBuilder());
		return mv;
	}

	/**
	 * 获取实体模板内容
	 * 
	 * @param table
	 * @return
	 */
	@RequestMapping("template/{table}/{pageType}")
	@ResponseBody
	public String getTemplateContent(@PathVariable("table") String table,
			@PathVariable("pageType") VisualPageType pageType,
			@RequestParam(name = "view", required = false) String view, HttpServletRequest request) {
		AssertUtil.parameterEmpty(table, "table");
		AssertUtil.parameterEmpty(pageType, "pageType");
		return getVisualPageService().getEntityTemplateContent(table, pageType, view);
	}

	/**
	 * 获取消息整体统计信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("jms")
	@ObjectListView(title = "消息统计", fields = { @PropertyView(name = "type", title = "类型"),
			@PropertyView(name = "name", title = "名称"), @PropertyView(name = "enqueueCount", title = "enqueueCount"),
			@PropertyView(name = "dispatchCount", title = "dispatchCount"),
			@PropertyView(name = "dequeueCount", title = "dequeueCount"),
			@PropertyView(name = "consumerCount", title = "consumerCount")})
	public ModelAndView getMessageStatics(HttpServletRequest request) throws Exception {
		PageInfo pageInfo = getAdminPageInfo("消息统计");
		pageInfo.setTemplate(getVisualPageService().getObjectTemplateFile("admin.jms", getClass(), "getMessageStatics",
				ObjectListView.class, VisualPageType.OLIST));
		ModelAndView mv = getBaseModelAndView(request);
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("objlist", messageQueueService.getStatistics());
		return mv;
	}

	@Autowired
	private IEntityMetaDataService entityMetaDataServer;

	/**
	 * 获取实体元模型数据
	 * 
	 * @param table
	 * @return
	 */
	@RequestMapping("meta/{table}")
	@ResponseBody
	public JsonEntityMetaData getJsonEntityMetaData(@PathVariable("table") String table) {
		JsonEntityMetaData metaData = entityMetaDataServer.getJsonEntityMetaData(table);
		return metaData;
	}
	
	@Autowired
	private RedisServerInfo redis;
	
	@RequestMapping("redis")
	@ObjectListView(title = "Redis信息", fields = { @PropertyView(name = "key", title = "属性"),
			@PropertyView(name = "value", title = "值") })
	public ModelAndView getRedisServerInfo(HttpServletRequest request){
		PageInfo pageInfo = getAdminPageInfo("Redis");
		pageInfo.setTemplate(getVisualPageService().getObjectTemplateFile("admin.redis", getClass(), "getRedisServerInfo",
				ObjectListView.class, VisualPageType.OLIST));
		ModelAndView mv = getBaseModelAndView(request);
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("objlist", redis.getRedisServerInfo().entrySet());
		return mv;
	}
}
