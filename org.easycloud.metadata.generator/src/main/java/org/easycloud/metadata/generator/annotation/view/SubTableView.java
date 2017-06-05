package org.easycloud.metadata.generator.annotation.view;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 实体子表视图
 * 
 * @author xiangwanhong
 *
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RUNTIME)
public @interface SubTableView {
	/**
	 * 子表属性名称
	 * 
	 * @return
	 */
	String tableAttr();
	/**
	 * 如果是引用，则为引用名称，如果没指定，表示取自己定义
	 */
	String refName() default "default";

	/**
	 * 标题
	 * 
	 * @return
	 */
	String title() default "";

	/**
	 * 指定列头，如果没有指定，自动按fields展示
	 * 
	 * @return
	 */
	String header() default "";

	/**
	 * 子表显示字段
	 * 
	 * @return
	 */
	String[] fields() default {};

	/**
	 * 需要显示链接的字段
	 * 
	 * @return
	 */
	String linkField() default "name";

	/**
	 * 链接URL，如/vp/$!{objitem.uid}
	 * 
	 * @return
	 */
	String linkUrl() default "";

	/**
	 * 允许自动增加操作
	 * 
	 * @return
	 */
	boolean enableActions() default true;

	/**
	 * 服务端取数模式,表示在服务端分页；为false时表示一次取出所有数据，在客户端分页
	 * 
	 * @return
	 */
	boolean serverSideMode() default true;

	/**
	 * 实体列表操作集，默认值为：EntityMetaDataConstants.DEFAULT_ENTITY_LIST_ACTIONS
	 * 
	 * @return
	 */
	EntityAction[] listActions() default { EntityAction.NEW, EntityAction.SHOWEXCELIMPORT, EntityAction.EXCEL,
			EntityAction.PDF, EntityAction.LISTPRINT };

	/**
	 * 实体项操作集，默认值为：EntityMetaDataConstants.DEFAULT_ENTITY_ITEM_ACTIONS
	 * 
	 * @return
	 */
	EntityAction[] itemActions() default { EntityAction.VIEW, EntityAction.EDIT, EntityAction.DEL };

	/**
	 * 为空时，默认取表上的labelField
	 * 
	 * @return
	 */
	String labelField() default "";

	/**
	 * 数据数据样式
	 * 
	 * @return
	 */
	ListStyle listStyle() default ListStyle.NONE;
}
