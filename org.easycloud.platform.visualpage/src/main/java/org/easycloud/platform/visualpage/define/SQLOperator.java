package org.easycloud.platform.visualpage.define;

import org.easycloud.platform.core.domain.BaseEnum;

/**
 * SQL条件操作符
 * 
 * @author xiangwanhong
 *
 */
public enum SQLOperator implements BaseEnum {
	EQUAL("等于"), NOTEQUAL("不等于"), ISNULL("为空"), NOTNULL("不为空"), IN("在...范围"), GREATERTHAN("大于"), LESSTHAN(
			"小于"), GREATERTHANOREQUALTO("大于等于"), LESSTHANOREQUALTO("小于等于"), LIKE("LIKE"), NOTLIKE("NOT LIKE"), BETWEEN(
					"之间");

	private String title;

	private SQLOperator(final String title) {
		this.title = title;
	}

	/**
	 * 判断是否为隐藏类型，隐藏类型界面不显示
	 * 
	 * @return
	 */
	public boolean isHideFilter() {
		return equals(ISNULL) || equals(NOTNULL);
	}

	@Override
	public String getTitle() {
		return this.title;
	}
}
