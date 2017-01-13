package org.easycloud.platform.core.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="org.myfly.platform.system.admin")
public class AdminProperties {
	/**
	 * 是否启用定时产生监控数据
	 */
	private boolean enableMetrics;
	
	/**
	 * 是否启用记录访问日志
	 */
	private boolean enableAccessLog;

	public boolean isEnableAccessLog() {
		return enableAccessLog;
	}

	public void setEnableAccessLog(boolean enableAccessLog) {
		this.enableAccessLog = enableAccessLog;
	}

	public boolean isEnableMetrics() {
		return enableMetrics;
	}

	public void setEnableMetrics(boolean enableMetrics) {
		this.enableMetrics = enableMetrics;
	}
}
