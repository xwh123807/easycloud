package org.easycloud.platform.core.domain;

import org.easycloud.platform.metadata.annotation.view.ObjectListView;
import org.easycloud.platform.metadata.annotation.view.PropertyView;
import org.easycloud.platform.visualpage.domain.VisualPageType;
/**
 * 
 * 模板构造器信息
 * @author xiangwanhong
 *
 */
@ObjectListView(title = "模板构造器", fields = { @PropertyView(name = "description", title = "简述"),
		@PropertyView(name = "layoutName", title = "布局名"), @PropertyView(name = "type", title = "类型"),
		@PropertyView(name = "className", title = "构造器类") })
public class TemplateBuilderInfo {
	/**
	 * 类型
	 */
	private VisualPageType type;
	/**
	 * 简述
	 */
	private String description;
	/**
	 * 布局名
	 */
	private String layoutName;
	/**
	 * 构造器类
	 */
	private String className;

	public VisualPageType getType() {
		return type;
	}

	public void setType(VisualPageType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
