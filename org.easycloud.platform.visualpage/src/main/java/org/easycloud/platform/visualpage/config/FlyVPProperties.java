package org.easycloud.platform.visualpage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "org.myfly.platform.vp")
public class FlyVPProperties {

	/**
	 * 
	 */
	private boolean clearWorkTemplatePath = true;

	/**
	 * 
	 */
	private String defaultLayout = "default";

	private String workTemplatePath = "${java.io.tmpdir}velocity/";

	public boolean isClearWorkTemplatePath() {
		return clearWorkTemplatePath;
	}

	public void setClearWorkTemplatePath(boolean clearWorkTemplatePath) {
		this.clearWorkTemplatePath = clearWorkTemplatePath;
	}

	public String getDefaultLayout() {
		return defaultLayout;
	}

	public void setDefaultLayout(String defaultLayout) {
		this.defaultLayout = defaultLayout;
	}

	public String getWorkTemplatePath() {
		return workTemplatePath;
	}

	public void setWorkTemplatePath(String workTemplatePath) {
		this.workTemplatePath = workTemplatePath;
	}
}
