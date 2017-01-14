package org.easycloud.platform.flydata.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "org.myfly.platform.data")
public class FlyDataProperties {
	/**
	 * 是否初始化系统级实体
	 */
	private boolean initSysData = true;
	
	/**
	 * 是否初始化样例数据
	 */
	private boolean initSampleData = false;
	
	/**
	 * 导出最大记录数
	 */
	private int maxExportSize = 10000;

	public int getMaxExportSize() {
		return maxExportSize;
	}

	public void setMaxExportSize(int maxExportSize) {
		this.maxExportSize = maxExportSize;
	}

	public boolean isInitSysData() {
		return initSysData;
	}

	public void setInitSysData(boolean initSysData) {
		this.initSysData = initSysData;
	}

	public boolean isInitSampleData() {
		return initSampleData;
	}

	public void setInitSampleData(boolean initSampleData) {
		this.initSampleData = initSampleData;
	}
}
