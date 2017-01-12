package org.easycloud.platform.core.metadata.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.easycloud.platform.core.metadata.define.SectionType;

/**
 * Section对应UI上一块行区域
 * 
 * @author xiangwanhong
 *
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RUNTIME)
public @interface SectionView {
	/**
	 * Section唯一标示名称
	 * @return
	 */
	String name() default "";
	/**
	 * 部件类型
	 * @return
	 */
	SectionType type() default SectionType.CUSTOM;
	/**
	 * 标题
	 * @return
	 */
	String title() default "";
	/**
	 * 字段集
	 * @return
	 */
	FieldSetView[] fieldSets() default {};
	/**
	 * 子表
	 * @return
	 */
	SubTableView[] subTables() default{};
}
