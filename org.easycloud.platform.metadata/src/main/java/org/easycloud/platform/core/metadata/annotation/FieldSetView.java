package org.easycloud.platform.core.metadata.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 字段组
 * @author xiangwanhong
 *
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RUNTIME)
public @interface FieldSetView {
	/**
	 * 标题
	 * @return
	 */
	String title() default "";
	
	/**
	 * 字段列表
	 * @return
	 */
	String[] fields() default {};
}
