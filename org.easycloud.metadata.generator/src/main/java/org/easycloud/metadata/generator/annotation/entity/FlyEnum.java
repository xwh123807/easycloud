package org.easycloud.metadata.generator.annotation.entity;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 枚举类型引用注解，引用定义在另一个实体中的枚举值，如果没有指定则在当前实体中定义
 * @author xiangwanhong
 *
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface FlyEnum {
	/**
	 * 实体名
	 * @return
	 */
	String entityName() default "";
	/**
	 * 属性名
	 * @return
	 */
	String attrName() default "";
}
