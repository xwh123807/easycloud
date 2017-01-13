package org.easycloud.platform.metadata.internal;

import org.easycloud.platform.metadata.annotation.ObjectListView;
import org.easycloud.platform.metadata.annotation.PropertyView;

@ObjectListView(title = "实体视图", fields = { @PropertyView(name = "tableName", title = "表名"),
		@PropertyView(name = "entityClass", title = "实体类"),
		@PropertyView(name = "metaModel", title = "元模型"),
		@PropertyView(name = "listView", title = "列表视图"),
		@PropertyView(name = "formView", title = "表单查看视图"),
		@PropertyView(name = "formEditView", title = "表单编辑视图"),
		@PropertyView(name = "formNewView", title = "表单新增视图"),
		@PropertyView(name = "outlineView", title = "大纲视图"),
		@PropertyView(name = "searchView", title = "搜索视图"),
		@PropertyView(name = "printView", title = "打印视图")})
public class EntityViewInfo {
	/**
	 * 表名
	 */
	private String tableName;
	/**
	 * 实体类名
	 */
	private String entityClass;
	/**
	 * 元模型
	 */
	private String metaModel;
	/**
	 * 列表视图
	 */
	private String listView;
	/**
	 * 表单视图-只读
	 */
	private String formView;
	/**
	 * 表单编辑视图
	 */
	private String formEditView;
	/**
	 * 表单新增视图
	 */
	private String formNewView;
	/**
	 * 大纲视图
	 */
	private String outlineView;
	/**
	 * 搜索视图
	 */
	private String searchView;
	/**
	 * 打印视图
	 */
	private String printView;

	public String getListView() {
		return listView;
	}

	public void setListView(String listView) {
		this.listView = listView;
	}

	public String getOutlineView() {
		return outlineView;
	}

	public void setOutlineView(String outlineView) {
		this.outlineView = outlineView;
	}

	public String getSearchView() {
		return searchView;
	}

	public void setSearchView(String searchView) {
		this.searchView = searchView;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(String entityClass) {
		this.entityClass = entityClass;
	}

	public String getFormView() {
		return formView;
	}

	public void setFormView(String formView) {
		this.formView = formView;
	}

	public String getFormEditView() {
		return formEditView;
	}

	public void setFormEditView(String formEditView) {
		this.formEditView = formEditView;
	}

	public String getFormNewView() {
		return formNewView;
	}

	public void setFormNewView(String formNewView) {
		this.formNewView = formNewView;
	}

	public String getMetaModel() {
		return metaModel;
	}

	public void setMetaModel(String metaModel) {
		this.metaModel = metaModel;
	}

	public String getPrintView() {
		return printView;
	}

	public void setPrintView(String printView) {
		this.printView = printView;
	}
}
