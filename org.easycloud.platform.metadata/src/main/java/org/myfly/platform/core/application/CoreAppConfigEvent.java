package org.myfly.platform.core.application;

import java.net.URL;

import org.myfly.platform.core.domain.AppStartLevel;
import org.myfly.platform.core.metadata.service.IMetaDataRegister;
import org.myfly.platform.core.starter.IAppConfigEvent;
import org.myfly.platform.core.user.internal.SystemDataInitial;
import org.myfly.platform.core.user.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CoreAppConfigEvent implements IAppConfigEvent{
	@Autowired
	private SystemDataInitial systemDataInitial;

	@Override
	public AppStartLevel getAppStartLevel() {
		return AppStartLevel.LEVEL_0;
	}

	@Override
	public void registerMenus(IMenuService menuService) {
		URL url = CoreAppConfigEvent.class.getResource("/config/coreMenuConfig.json");
		menuService.registerMenus(url);
	}

	@Override
	public void initSysData() {
		systemDataInitial.initSystemInternalData();
	}

	@Override
	public void initSampleData() {
		
	}

	@Override
	public void registerExternalMetaData(IMetaDataRegister metaDataRegister) {
		
	}

}
