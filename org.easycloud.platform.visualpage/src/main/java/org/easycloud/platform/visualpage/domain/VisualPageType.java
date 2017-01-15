package org.easycloud.platform.visualpage.domain;

import org.easycloud.platform.metadata.annotation.entity.BaseEnum;

/**
 * 页面类型
 * 
 * @author xiangwanhong
 *
 */
public enum VisualPageType implements BaseEnum {
	//列表视图，用于实体查询
	LIST("实体查询列表视图"),
	//查看视图，用于实体查看
	VIEW("实体查看表单视图"),
	//编辑视图，用于实体编辑
	EDIT("实体编辑表单视图"),
	//新增视图，用于实体新增
	NEW("实体新增表单视图"),
	//
	SEARCH("实体搜索列表视图"),
	//列表打印视图，用于打印实体列表
	LISTPRINT("实体列表打印视图"),
	//打印视图，用于打印实体
	PRINT("实体表单打印视图"),
	//
	OUTLINE("大纲显示实体信息"),
	//
	LISTOUTLINE("列表大纲显示"),
	//
	OLIST("自定义对象列表"),
	//
	OVIEW("自定义对象表单"),
	//
	RENDER("组件");

	private String title;

	private VisualPageType(final String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}

}
