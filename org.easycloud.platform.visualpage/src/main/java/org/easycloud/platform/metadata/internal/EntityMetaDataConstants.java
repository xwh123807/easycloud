package org.easycloud.platform.metadata.internal;

import org.easycloud.platform.core.domain.EntityAction;
import org.easycloud.platform.visualpage.define.ListStyle;

public class EntityMetaDataConstants {
	/**
	 * 元模型默认名称，没有定义元模型或定义了元模型但没有指定名称时，缺省使用的名称
	 */
	public static String DEFAULT_NAME = "default";
	/**
	 * 有定义元模型，自动增加包含全部的配置
	 */
	public static String DEFAULT_ALL_NAME = "all";
	/**
	 * 默认是否为分页取数
	 */
	public static boolean DEFAULT_SERVER_MODE = true;
	/**
	 * 列表默认样式
	 */
	public static ListStyle DEFAULT_LIST_STYLE = ListStyle.CARDLIST;
	/**
	 * 默认显示字段名称
	 */
	public static String DEFAULT_LABEL_FIELD_NAME = "name";
	/**
	 * 实体SGlobal_Name的表名
	 */
	public static String TABLENAME_SGLOBAL_NAME = "PB.SGlobal_Name";

	/**
	 * 实体列表、实体子表列表全部操作集，包含变更数据的操作
	 */
	public static EntityAction[] DEFAULT_ENTITY_LIST_ACTIONS = { EntityAction.NEW, EntityAction.SHOWEXCELIMPORT,
			EntityAction.EXCEL, EntityAction.PDF, EntityAction.LISTPRINT };
	
	/**
	 * 实体、实体子表全部操作集，包含变更数据的操作
	 */
	public static EntityAction[] DEFAULT_ENTITY_ITEM_ACTIONS = {EntityAction.VIEW, EntityAction.EDIT, EntityAction.DEL};
}
