package org.easycloud.platform.core.domain;

import org.easycloud.platform.metadata.annotation.BaseEnum;

/**
 * 页面视图类型
 * 
 * @author xiangwanhong
 *
 */
public enum ViewType implements BaseEnum {
	//
	LIST("列表"),
	//
	OUTLINE("简档"),
	//
	NEW("新增"),
	//
	VIEW("查看"),
	//
	PRINT("打印"),
	//
	EDIT("编辑"),
	//
	PDF("PDF导出"),
	//
	EXCEL("EXCEL导出"),
	//
	DEL("删除"),
	//
	SEARCH("搜索"),
	//
	EXCELIMPORT("EXCEL导入"), 
	//
	LISTOUTLINE("大纲列表");

	private String title;

	private ViewType(final String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}

}
