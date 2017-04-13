package org.easycloud.metadata.generator.annotation.view;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

/**
 * 对象列表视图
 * @author xiangwanhong
 *
 */
@Retention(RUNTIME)
public @interface ObjectListView {
	/**
	 * 标题
	 * @return
	 */
	String title() default "";
	/**
	 * 指定列头，如果没有指定，自动按fields展示
	 * @return
	 */
	String header() default "";
	/**
	 * 显示的属性列表
	 * @return
	 */
	PropertyView[] fields() default {};
	/**
	 * 需要显示链接的字段
	 * @return
	 */
	String linkField() default "";
	/**
	 * 链接URL，如/vp/$!{objitem.uid}
	 * @return
	 */
	String linkUrl() default "";
}
