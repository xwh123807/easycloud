package org.myfly.platform.core.visualpage.ui;

import org.myfly.platform.core.domain.BaseEnum;

/**
 * UI控件类型
 * 
 * @author xiangwanhong
 *
 */
public enum ControlTpye implements BaseEnum {
	TEXT("文本输入"), CHECK("是否选择"), SELECT("单选");

	private String title;

	private ControlTpye(final String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}
}
