package org.easycloud.platform.core.domain;

/**
 * 警告信息级别
 * 
 * @author xiangwanhong
 *
 */
public enum AlertLevel implements BaseEnum {
	SUCCESS("成功", "alert-success", "fa-check green"), INFO("提示", "alert-info", "fa-bullhorn"), WARNING("警告",
			"alert-warning", "fa-exclamation-triangle"), DANGER("危险", "alert-danger", "fa-ban");

	/**
	 * 样式
	 */
	private String alertClass;
	/**
	 * 图标
	 */
	private String iconClass;
	private String title;

	private AlertLevel(final String title, String alertClass, String iconClass) {
		this.title = title;
		this.alertClass = alertClass;
		this.iconClass = iconClass;
	}

	public String getAlertClass() {
		return alertClass;
	}

	public String getIconClass() {
		return iconClass;
	}

	@Override
	public String getTitle() {
		return this.title;
	}
}
