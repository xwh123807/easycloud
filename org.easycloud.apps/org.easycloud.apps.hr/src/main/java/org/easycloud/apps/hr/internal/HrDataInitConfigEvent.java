package org.easycloud.apps.hr.internal;

import java.net.URL;

import org.myfly.platform.core.domain.AppStartLevel;
import org.myfly.platform.core.metadata.service.IMetaDataRegister;
import org.myfly.platform.core.starter.IAppConfigEvent;
import org.myfly.platform.core.user.service.IMenuService;
import org.springframework.stereotype.Component;

@Component
public class HrDataInitConfigEvent implements IAppConfigEvent {

	@Override
	public AppStartLevel getAppStartLevel() {
		return AppStartLevel.LEVEL_2;
	}

	@Override
	public void initSysData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initSampleData() {
		// sampleDataBuilder.initSampleData();
		try {
			SampleDataFromExcel sampleData = new SampleDataFromExcel();
			sampleData.loadData(SampleDataFromExcel.class.getResource("/data/远光员工任职资格2016（开发工程师）0108.xlsx"));
			sampleData.saveData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void registerMenus(IMenuService menuService) {
		URL url = HrDataInitConfigEvent.class.getResource("/config/hrMenuConfig.json");
		menuService.registerMenus(url);
	}

	@Override
	public void registerExternalMetaData(IMetaDataRegister metaDataRegister) {
		// TODO Auto-generated method stub
		
	}
}
