package org.easycloud.platform.visualpage.ui;

import org.easycloud.platform.metadata.annotation.entity.BaseEnum;

/**
 * HTML5 Input控件类型
 * 
 * @author xiangwanhong
 *
 */
public enum InputType implements BaseEnum {
	TEXT("文本框"), PASSWORD("密码框"), CHECKBOX("复选框"), EMAIL("邮件"), URL("超链接"), DATE("日期"), DATETIME("日期/时间"), FILE("文件");

	private String title;

	private InputType(final String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}
}
