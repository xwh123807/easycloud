package org.easycloud.platform.visualpage.ui;

import java.util.HashMap;
import java.util.Map;

import org.easycloud.platform.metadata.annotation.view.EntityAction;

public class StyleConstants {
	/**
	 * Bootstrap 表格基本样式
	 */
	public static final String TABLE_BASE_CLASS = "table responsive table-bordered table-striped table-hover";
	/**
	 * DataTables控件服务端处理表格样式
	 */
	public static final String TABLE_SERVER_SIDE_CLASS = "dataTable-ServerSide";
	/**
	 * DataTables控件客户端处理表格样式
	 */
	public static final String TABLE_CLIENT_SIDE_CLASS = "dataTable";

	/**
	 * 实体查看图标
	 */
	public static final String ENTITY_VIEW_ICON_CLASS = "glyphicon glyphicon-eye-open";
	/**
	 * 实体编辑图标
	 */
	public static final String ENTITY_EDIT_ICON_CLASS = "glyphicon glyphicon-edit";
	/**
	 * 实体新增图标
	 */
	public static final String ENTITY_NEW_ICON_CLASS = "glyphicon glyphicon-plus";
	/**
	 * 实体删除图标
	 */
	public static final String ENTITY_DEL_ICON_CLASS = "glyphicon glyphicon-remove";
	/**
	 * 从Excel导入实体图标
	 */
	public static final String ENTITY_EXCEL_IMPORT_ICON_CLASS = "glyphicon glyphicon-import";
	/**
	 * 导出到Excel图标
	 */
	public static final String ENTITY_EXCEL_EXPORT_ICON_CLASS = "glyphicon glyphicon-export";
	/**
	 * 打印格式实体图标
	 */
	public static final String ENTITY_PRINT_ICON_CLASS = "glyphicon glyphicon-print";
	/**
	 * 导出到pdf图标
	 */
	public static final String ENTITY_PDF_EXPORT_ICON_CLASS = "glyphicon glyphicon-envelope";
	/**
	 * 实体操作图标
	 */
	public static final Map<EntityAction, String> ENTITY_ACTION_ICON_CLASS_MAP = new HashMap<>();

	static {
		ENTITY_ACTION_ICON_CLASS_MAP.put(EntityAction.VIEW, ENTITY_VIEW_ICON_CLASS);
		ENTITY_ACTION_ICON_CLASS_MAP.put(EntityAction.EDIT, ENTITY_EDIT_ICON_CLASS);
		ENTITY_ACTION_ICON_CLASS_MAP.put(EntityAction.DEL, ENTITY_DEL_ICON_CLASS);
		ENTITY_ACTION_ICON_CLASS_MAP.put(EntityAction.NEW, ENTITY_NEW_ICON_CLASS);
		ENTITY_ACTION_ICON_CLASS_MAP.put(EntityAction.SHOWEXCELIMPORT, ENTITY_EXCEL_IMPORT_ICON_CLASS);
		ENTITY_ACTION_ICON_CLASS_MAP.put(EntityAction.EXCEL, ENTITY_EXCEL_EXPORT_ICON_CLASS);
		ENTITY_ACTION_ICON_CLASS_MAP.put(EntityAction.PDF, ENTITY_PDF_EXPORT_ICON_CLASS);
		ENTITY_ACTION_ICON_CLASS_MAP.put(EntityAction.PRINT, ENTITY_PRINT_ICON_CLASS);
	}
}
