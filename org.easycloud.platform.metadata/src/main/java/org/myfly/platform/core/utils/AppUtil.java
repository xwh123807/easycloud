package org.myfly.platform.core.utils;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.myfly.platform.core.flydata.service.IFlyDataAccessService;
import org.myfly.platform.core.metadata.internal.EntityMetaData;
import org.myfly.platform.core.metadata.service.IEntityMetaDataService;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.util.Assert;

/**
 * 系统AppContext工具类
 * 
 * @author xiangwanhong
 *
 */
public class AppUtil {
	private static ApplicationContext applicationContext;

	/**
	 * 是否在Spring环境中运行
	 * 
	 * @return
	 */
	public static boolean isSpringContext() {
		return applicationContext != null;
	}

	public static ApplicationContext getApplicationConext() {
		AssertUtil.parameterEmpty(applicationContext, "applicationContext", "请确认是在Spring上下文环境中运行.");
		return applicationContext;
	}

	public static void setApplicationConext(ApplicationContext applicationContext) {
		AppUtil.applicationContext = applicationContext;
	}

	public static JpaMetamodelMappingContext getJpaMetamodelMappingContext() {
		return getApplicationConext().getBean(JpaMetamodelMappingContext.class);
	}

	/**
	 * 获取服务
	 * 
	 * @param serviceClass
	 * @return
	 */
	public static <T> T getService(Class<T> serviceClass) {
		return getApplicationConext().getBean(serviceClass);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getService(String beanName) {
		return (T) getApplicationConext().getBean(beanName);
	}

	/**
	 * 获取类型转换服务
	 * 
	 * @return
	 */
	public static ConversionService getConversionService() {
		GenericConversionService conversionService = null;
		try {
			conversionService = (GenericConversionService) getApplicationConext().getBean("mvcConversionService");
		} catch (NoSuchBeanDefinitionException e) {
		}
		Assert.notNull(conversionService);
		// if (conversionService == null) {
		// conversionService = new DefaultFormattingConversionService();
		// (new
		// ApplicationStarter()).registerConverterFactory(conversionService);
		// }
		return conversionService;
	}

	/**
	 * 获取实体元模型服务
	 * 
	 * @return
	 */
	public static IEntityMetaDataService getEntityMataDataService() {
		return getService(IEntityMetaDataService.class);
	}

	/**
	 * 获取实体元模型
	 * 
	 * @param entityName
	 * @return
	 */
	public static EntityMetaData getEntityMetadata(String entityName) {
		Assert.hasLength(entityName);
		EntityMetaData metaData = null;
		try {
			metaData = getEntityMataDataService().getEntityMetaData(entityName);
		} catch (Exception e) {
			throw new RuntimeException("查找元模型[" + entityName + "]失败，" + e.getMessage());
		}
		Assert.notNull(metaData, "找不到名称为[" + entityName + "]的元模型");
		return metaData;
	}

	/**
	 * 获取Fly数据访问服务Jdbc实现
	 * 
	 * @return
	 */
	public static IFlyDataAccessService getJdbcFlyDataAccessService() {
		return getService("jdbcFlyDataAccessService");
	}

	/**
	 * 获取Fly数据访问服务Jpa实现
	 * 
	 * @return
	 */
	public static IFlyDataAccessService getJpaFlyDataAccessService() {
		return getService("jpaFlyDataAccessService");
	}

	/**
	 * 根据实体是否有实体类获取fly扩展数据访问服务。有实体类时，调用jpa实现，反之调用jdbc实现。
	 * 
	 * @param entityName
	 * @return
	 */
	public static IFlyDataAccessService getFlyDataAccessService(String entityName) {
		if (isJpaEntity(entityName)) {
			return getJpaFlyDataAccessService();
		} else {
			return getJdbcFlyDataAccessService();
		}
	}

	/**
	 * 判断实体是否有对应实体类
	 * 
	 * @param entityName
	 * @return
	 */
	private static boolean isJpaEntity(String entityName) {
		return getEntityMetadata(entityName).isJpaEntity();
	}

	/**
	 * 获取Hibernate SessionFactory
	 * 
	 * @return
	 */
	public static SessionFactory getHibernateSessionFactory() {
		EntityManagerFactory factory = getService(EntityManagerFactory.class);
		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}
		return factory.unwrap(SessionFactory.class);
	}
}
