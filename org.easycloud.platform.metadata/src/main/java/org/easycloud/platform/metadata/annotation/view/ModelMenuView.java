package org.easycloud.platform.metadata.annotation.view;

/**
 * 模块菜单定义注解
 * @author xiangwanhong
 *
 */
public @interface ModelMenuView {
	/**
	 * 名称
	 * @return
	 */
	String title() default "";
	/**
	 * 简述
	 * @return
	 */
	String description() default "";
	/**
	 * 链接
	 * @return
	 */
	String url() default "";
	/**
	 * 图标样式
	 * @return
	 */
	String iconClass() default "";
	/**
	 * 子菜单
	 * @return
	 */
	MenuItemView[] subMenus() default {};
}
