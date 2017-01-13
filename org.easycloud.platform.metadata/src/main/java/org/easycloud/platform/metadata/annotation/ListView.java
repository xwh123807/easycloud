package org.easycloud.platform.metadata.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.easycloud.platform.core.domain.EntityAction;
import org.easycloud.platform.metadata.define.ListStyle;

/**
 * 注解，实体列表视图
 * 
 * @author xiangwanhong
 *
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RUNTIME)
public @interface ListView {
	/**
	 * 名称，唯一标示，用于区分不同的列表视图定义
	 * 
	 * @return
	 */
	String name() default "default";

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
	 * 结果字段列表
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
	 * 过滤条件，设置界面显示过滤条件。
	 * 
	 * @return
	 */
	FilterView[] filters() default {};

	/**
	 * 服务端取数模式,表示在服务端分页；为false时表示一次取出所有数据，在客户端分页
	 * 
	 * @return
	 */
	boolean serverSideMode() default true;

	/**
	 * 是否允许操作集，当属性为true，且没有配置actionsets将配置所有操作；如有配置actionsets将按配置；如果属性为false，
	 * 将不显示任何操作
	 * 
	 * @return
	 */
	boolean enableActions() default true;

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
	EntityAction[] itemActions() default {EntityAction.VIEW, EntityAction.EDIT, EntityAction.DEL};

	/**
	 * 为空时，默认取表上的labelField
	 * 
	 * @return
	 */
	String labelField() default "";

	/**
	 * 排序
	 * 
	 * @return
	 */
	OrderView[] orders() default {};

	/**
	 * 列表显示样式
	 * 
	 * @return
	 */
	ListStyle listStyle() default ListStyle.CARDLIST;
}
