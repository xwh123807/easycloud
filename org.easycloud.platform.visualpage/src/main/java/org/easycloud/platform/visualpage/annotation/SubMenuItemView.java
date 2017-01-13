package org.easycloud.platform.visualpage.annotation;

public @interface SubMenuItemView {
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
}
