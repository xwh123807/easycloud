package org.easycloud.platform.metadata.annotation;

public @interface PropertyView {
	/**
	 * 属性名称
	 * @return
	 */
	String name() default "";
	/**
	 * 标题
	 * @return
	 */
	String title() default "";
}
