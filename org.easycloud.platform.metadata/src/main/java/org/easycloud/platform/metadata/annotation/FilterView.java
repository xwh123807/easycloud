package org.easycloud.platform.metadata.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.easycloud.platform.metadata.define.SQLOperator;

/**
 * 过滤器注解，用于定义实体查询条件。<br>
 * 1、可用于在ListView注解中定义列表视图的过滤条件；<br>
 * 2、可用于在查询方法上定义过滤条件<br>
 * 
 * @author xiangwanhong
 *
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RUNTIME)
public @interface FilterView {
	/**
	 * 字段名，对应实体属性名称
	 * 
	 * @return
	 */
	String field();

	/**
	 * 默认值 TODO 还要支持函数，如本月
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * 条件操作符
	 * 
	 * @return
	 */
	SQLOperator operator() default SQLOperator.EQUAL;

	/**
	 * 在查询条件区域是否显示条件。如果不显示，则在界面上没有改条件，但是可以通过在url中传入条件和值
	 * 
	 * @return
	 */
	boolean show() default true;
}
