package org.easycloud.platform.core.metadata.annotation;

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
