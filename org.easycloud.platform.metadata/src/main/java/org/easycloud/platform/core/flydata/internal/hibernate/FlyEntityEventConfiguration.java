package org.easycloud.platform.core.flydata.internal.hibernate;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlyEntityEventConfiguration {
	/**
	 * 注册Hibernate事件监听，当实体新增、修改、删除后触发
	 */
	@Autowired
	public void registerHibernateEvent(EntityManagerFactory factory, FlyEntityListener flyEntityListener) {
		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}
		SessionFactory sessionFactory = factory.unwrap(SessionFactory.class);

		EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory).getServiceRegistry()
				.getService(EventListenerRegistry.class);
		registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(flyEntityListener);
		registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(flyEntityListener);
		registry.getEventListenerGroup(EventType.POST_DELETE).appendListener(flyEntityListener);
		registry.getEventListenerGroup(EventType.PRE_INSERT).appendListener(flyEntityListener);
		registry.getEventListenerGroup(EventType.PRE_UPDATE).appendListener(flyEntityListener);
	}
}
