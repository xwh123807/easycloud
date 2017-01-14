package org.easycloud.platform.metadata.annotation.entity;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 标示实体查找关系，可以关联多个实体类型。当entityName等于*时，表示可以关联任意实体
 * 
 * @author xiangwanhong
 *
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface FlySearchRelation {
	/**
	 * 关联实体
	 * 
	 * @return
	 */
	String[] entityNames() default {};
	/**
	 * 是否可选
	 * @return
	 */
	boolean optional() default true;
}
