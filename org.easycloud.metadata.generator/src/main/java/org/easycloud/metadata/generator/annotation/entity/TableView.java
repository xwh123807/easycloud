package org.easycloud.metadata.generator.annotation.entity;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.easycloud.metadata.service.model.CommonSubTableType;

/**
 * 表视图注解，描述实体基本信息，用于在显示时使用
 * @author xiangwanhong
 *
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RUNTIME)
public @interface TableView {
	/**
	 * 标题
	 * @return
	 */
	String title() default "";
	
	/**
	 * 简述
	 * @return
	 */
	String description() default "";
	/**
	 * 用于在查找关系显示时，将主键显示为名称
	 * @return
	 */
	String labelField() default "name";
	
	/**
	 * 通用子表
	 * 
	 * @return
	 */
	CommonSubTableType[] commonSubTables() default { CommonSubTableType.NOTE, CommonSubTableType.ATTACHMENT };
	
	/**
	 * 是否创建索引数据
	 * @return
	 */
	boolean createIndex() default true;
	/**
	 * 索引名称，缺省和实体名称相同
	 * @return
	 */
	String indexName() default "";
}
