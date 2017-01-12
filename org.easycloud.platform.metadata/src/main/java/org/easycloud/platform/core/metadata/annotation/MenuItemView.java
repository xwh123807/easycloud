package org.easycloud.platform.core.metadata.annotation;

/**
 * 菜单项定义注解
 * @author xiangwanhong
 *
 */
public @interface MenuItemView {
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
	SubMenuItemView[] subMenus() default {};
}
