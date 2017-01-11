package org.myfly.platform.core.metadata.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 实体大纲视图，用于简要显示实体信息
 * @author xiangwanhong
 *
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RUNTIME)
public @interface OutlineView {
	/**
	 * 标题
	 * @return
	 */
	String title() default "";
	/**
	 * 分组
	 * @return
	 */
	SectionView[] sections() default {};
}
