package org.myfly.platform.core.flydata.internal.hibernate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.myfly.platform.core.context.UserContext;
import org.myfly.platform.core.context.UserSession;
import org.myfly.platform.core.domain.SBaseEntity;
import org.myfly.platform.core.flydata.queue.EntityOperator;
import org.myfly.platform.core.flydata.queue.GlobalNameQueueProcessor;
import org.myfly.platform.core.flydata.queue.IndexQueueProcessor;
import org.myfly.platform.core.user.domain.SGlobalName;
import org.myfly.platform.core.user.domain.SUser;
import org.myfly.platform.core.user.domain.Tenant;
import org.myfly.platform.core.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 监听FlyEntity（继承SBaseEntity）增加、删除、修改，同步维护GlobalName表。
 * 监听FlyEntity（继承SBaseEntity）增加前、修改前事件，自动更新审计字段。
 * 
 * @author xiangwanhong
 *
 */
@Component
public class FlyEntityListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener,
		PreInsertEventListener, PreUpdateEventListener {

	@Autowired
	private GlobalNameQueueProcessor globalNameQueueProcessor;
	
	@Autowired
	private IndexQueueProcessor indexQueueProcessor;
	/**
	 * 
	 */
	private static final long serialVersionUID = -5630387701139880667L;

	/**
	 * 实体删除后事件
	 */
	@Override
	public void onPostDelete(PostDeleteEvent event) {
		if (event.getEntity() instanceof SBaseEntity) {
			SBaseEntity entity = (SBaseEntity) event.getEntity();
			SGlobalName nameEntity = new SGlobalName();
			entity.setUid((String) event.getId());
			globalNameQueueProcessor.sendGlobalNameData(EntityOperator.DELETE, nameEntity);
			indexQueueProcessor.sendIndexData(EntityOperator.DELETE, entity.getClass().getName(), (String) event.getId());
		}
	}

	/**
	 * 实体更新完成后事件
	 */
	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		if (event.getEntity() instanceof SBaseEntity) {
			SBaseEntity entity = (SBaseEntity) event.getEntity();
			SGlobalName nameEntity = new SGlobalName();
			nameEntity.setUid(entity.getUid());
			nameEntity.setName(entity.getName());
			nameEntity.setInternalTable(entity.getClass().getSimpleName());
			globalNameQueueProcessor.sendGlobalNameData(EntityOperator.UPDATE, nameEntity);
			indexQueueProcessor.sendIndexData(EntityOperator.UPDATE, entity.getClass().getName(), (String) event.getId());
		}
	}

	/**
	 * 实体新增后事件
	 */
	@Override
	public void onPostInsert(PostInsertEvent event) {
		if (event.getEntity() instanceof SBaseEntity) {
			SBaseEntity entity = (SBaseEntity) event.getEntity();
			SGlobalName nameEntity = new SGlobalName();
			nameEntity.setUid(entity.getUid());
			nameEntity.setName(entity.getName());
			nameEntity.setInternalTable(entity.getClass().getSimpleName());
			globalNameQueueProcessor.sendGlobalNameData(EntityOperator.INSERT, nameEntity);
			indexQueueProcessor.sendIndexData(EntityOperator.INSERT, entity.getClass().getName(), (String) event.getId());
		}
	}

	@Override
	public boolean requiresPostCommitHanding(EntityPersister persister) {
		return true;
	}

	static class ProperyIndexCache {
		/**
		 * 存储FlyEntity实体审计字段在实体属性中的位置
		 */
		private static Map<String, Map<String, Integer>> auditablePropertyIndexMap = new ConcurrentHashMap<>();

		public static final String CREATED = "created";

		public static final String UPDATED = "updated";

		public static final String CREATEDBY = "createdBy";

		public static final String UPDATEDBY = "updatedBy";

		public static final String TENANT = "tenant";

		private static final String[] AUDIT_PROPERTIES = new String[] { CREATED, UPDATED, CREATEDBY, UPDATEDBY,
				TENANT };

		public static boolean containsEntity(String entityName, EntityPersister entityPersister) {
			if (!auditablePropertyIndexMap.containsKey(entityName)) {
				Map<String, Integer> propertyIndexMap = new HashMap<>();
				String[] propertyNames = entityPersister.getPropertyNames();
				for (int index = 0; index < propertyNames.length; index++) {
					for (int i = 0; i < AUDIT_PROPERTIES.length; i++) {
						if (AUDIT_PROPERTIES[i].equals(propertyNames[index])) {
							propertyIndexMap.put(AUDIT_PROPERTIES[i], index);
						}
					}
				}
				auditablePropertyIndexMap.put(entityName, propertyIndexMap);
			}
			return true;
		}

		public static Map<String, Integer> getPropertyIndexMap(String entityName) {
			return auditablePropertyIndexMap.get(entityName);
		}
	}

	/**
	 * 实体增加前事件
	 */
	@Override
	public boolean onPreInsert(PreInsertEvent event) {
		if (event.getEntity() instanceof SBaseEntity && !(event.getEntity() instanceof Tenant)
				&& !(event.getEntity() instanceof SUser)) {
			if (ProperyIndexCache.containsEntity(event.getEntityName(), event.getPersister())) {
				SBaseEntity entity = (SBaseEntity) event.getEntity();
				entity.setCreated(DateUtil.nowSqlTimestamp());
				UserSession userSession = UserContext.getUserSession();
				entity.setCreatedBy(userSession.getUser());
				entity.setTenant(userSession.getTenant());
				
				Map<String, Integer> propertyIndexMap = ProperyIndexCache.getPropertyIndexMap(event.getEntityName());
				event.getState()[propertyIndexMap.get(ProperyIndexCache.CREATED)] = entity.getCreated();
				event.getState()[propertyIndexMap.get(ProperyIndexCache.CREATEDBY)] = entity.getCreatedBy();
				event.getState()[propertyIndexMap.get(ProperyIndexCache.TENANT)] = entity.getTenant();
				
				event.getState()[propertyIndexMap.get(ProperyIndexCache.UPDATED)] = DateUtil.nowSqlTimestamp();
				event.getState()[propertyIndexMap.get(ProperyIndexCache.UPDATEDBY)] = userSession.getUser();
			}
		}
		return false;
	}

	/**
	 * 实体修改前事件
	 */
	@Override
	public boolean onPreUpdate(PreUpdateEvent event) {
		if (event.getEntity() instanceof SBaseEntity && !(event.getEntity() instanceof Tenant)
				&& !(event.getEntity() instanceof SUser)) {
			if (ProperyIndexCache.containsEntity(event.getEntityName(), event.getPersister())) {
				SBaseEntity entity = (SBaseEntity) event.getEntity();
				entity.setUpdated(DateUtil.nowSqlTimestamp());
				UserSession userSession = UserContext.getUserSession();
				entity.setUpdatedBy(userSession.getUser());
				
				Map<String, Integer> propertyIndexMap = ProperyIndexCache.getPropertyIndexMap(event.getEntityName());
				event.getState()[propertyIndexMap.get(ProperyIndexCache.UPDATED)] = entity.getUpdated();
				event.getState()[propertyIndexMap.get(ProperyIndexCache.UPDATEDBY)] = entity.getUpdatedBy();
			}
		}
		return false;
	}

}
