package org.easycloud.platform.core.context;

import java.io.Serializable;
import java.util.Date;

import org.easycloud.platform.core.domain.AlertInfo;

/**
 * 用户会话信息，成功登录后登记
 * 
 * @author xiangwanhong
 *
 */
public class UserSession implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -330646850597064989L;
	/**
	 * 用户
	 */
	private SUser user;
	/**
	 * 租户
	 */
	private Tenant tenant;
	/**
	 * 登录时间
	 */
	private Date loginTime;
	/**
	 * 布局名称
	 */
	private String layoutName;
	/**
	 * 会话安全标示
	 */
	private String tokenuid;
	/**
	 * 最后一次警告信息
	 */
	private AlertInfo alertInfo;

	public SUser getUser() {
		return user;
	}

	public void setUser(SUser user) {
		this.user = user;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	public String getTokenuid() {
		return tokenuid;
	}

	public void setTokenuid(String tokenuid) {
		this.tokenuid = tokenuid;
	}

	public AlertInfo getAlertInfo() {
		return alertInfo;
	}

	public void setAlertInfo(AlertInfo alertInfo) {
		this.alertInfo = alertInfo;
	}

}
