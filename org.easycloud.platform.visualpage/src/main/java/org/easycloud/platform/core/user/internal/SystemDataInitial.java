package org.easycloud.platform.core.user.internal;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easycloud.platform.core.context.SystemContext;
import org.easycloud.platform.core.domain.SBaseEntity;
import org.easycloud.platform.core.user.domain.SUser;
import org.easycloud.platform.core.user.domain.SoftApp;
import org.easycloud.platform.core.user.domain.Tenant;
import org.easycloud.platform.metadata.utils.DateUtil;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SystemDataInitial {
	private static final Log log = LogFactory.getLog(SystemDataInitial.class);
	/**
	 * 判断是否已经初始化
	 */
	private static boolean isInited;

	private static final String description = "系统数据，由系统初始化创建，不能修改和删除。";

	/**
	 * 存储系统级实体数据
	 */
	private SysEntities sysEntities = new SysEntities();

	/**
	 * 初始化系统初始数据
	 */
	public void initSystemInternalData() {
		if (isInited) {
			return;
		}
		Tenant stenant = new Tenant();
		stenant.setUid(SystemContext.SYS_STENANT_UID);
		stenant.setName("flyme.com");
		stenant.setCreated(DateUtil.nowSqlTimestamp());
		stenant.setDescription(description);
		saveEntity(stenant);
		sysEntities.put(stenant);
		if (log.isDebugEnabled()) {
			log.debug("初始化STenant，" + stenant.getUid());
		}
		SystemContext.SYS_STENANT_UID = stenant.getUid();

		SUser suser = new SUser();
		suser.setUid(SystemContext.SYS_SUSER_UID);
		suser.setName("sys");
		suser.setCommunityNickname("sys");
		suser.setAboutMe(description);
		suser.setEmail("sys@flyme.com");
		suser.setCompanyName("flyme.com");
		suser.setCreated(DateUtil.nowSqlTimestamp());
		suser.setTenant(stenant);
		saveEntity(suser);
		sysEntities.put(suser);
		if (log.isDebugEnabled()) {
			log.debug("初始化SUser，" + suser.getUid());
		}
		SystemContext.SYS_SUSER_UID = suser.getUid();

		SoftApp ssoftApp = new SoftApp();
		ssoftApp.setUid(SystemContext.SYS_SSOFTAPP_UID);
		ssoftApp.setName("flyme.com");
		ssoftApp.setVersion("v0.1");
		ssoftApp.setDescription(description);
		ssoftApp.setTenant(stenant);
		ssoftApp.setCreated(DateUtil.nowSqlTimestamp());
		ssoftApp.setCreatedBy(suser);
		saveEntity(ssoftApp);
		sysEntities.put(ssoftApp);
		if (log.isDebugEnabled()) {
			log.debug("初始化SSoftApp，" + ssoftApp.getUid());
		}
		SystemContext.SYS_SSOFTAPP_UID = ssoftApp.getUid();

		isInited = true;
	}

	private void saveEntity(SBaseEntity entity) {
		Assert.notNull(entity);
		try {
			AppUtil.getFlyDataAccessService(entity.getClass().getName()).saveEntity(entity);
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("类[" + entity.getClass().getName() + "]保存失败，错误信息：" + e.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes", "serial" })
	public class SysEntities extends ConcurrentHashMap {
		@SuppressWarnings("unchecked")
		public void put(Object entity) {
			put(entity.getClass().getName(), entity);
		}
	}

	public Tenant getSysStenant() {
		return (Tenant) sysEntities.get(Tenant.class.getName());
	}

	public SUser getSysSUser() {
		return (SUser) sysEntities.get(SUser.class.getName());
	}

	public SoftApp getSysSSoftApp() {
		return (SoftApp) sysEntities.get(SoftApp.class.getName());
	}
}
