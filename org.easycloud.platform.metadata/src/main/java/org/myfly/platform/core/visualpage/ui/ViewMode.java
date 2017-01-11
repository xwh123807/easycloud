package org.myfly.platform.core.visualpage.ui;

import org.myfly.platform.core.domain.BaseEnum;

/**
 * 页面视图显示模式
 * 
 * @author xiangwanhong
 *
 */
public enum ViewMode implements BaseEnum {
	//
	PAGE("完整页面，包含页面框架"),
	//
	SUBPAGE("子页面，只显示页面内容"),
	//
	OUTLINEPAGE("简要视图子页面"),
	//
	MODALPAGE("模态窗口页面");

	private String title;
	
	public static ViewMode DEFAULT_VIEW_MODE = SUBPAGE;

	private ViewMode(final String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}

}
