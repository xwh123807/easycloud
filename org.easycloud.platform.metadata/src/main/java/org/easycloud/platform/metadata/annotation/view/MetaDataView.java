package org.easycloud.platform.metadata.annotation.view;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.easycloud.platform.metadata.annotation.entity.TableView;

/**
 * 实体元模型定义注解
 * @author xiangwanhong
 *
 */
@Target(ElementType.TYPE)
@Retention(RUNTIME)
public @interface MetaDataView {
	/**
	 * 表信息
	 * @return
	 */
	TableView tableView();
	/**
	 * 实体列表视图
	 * @return
	 */
	ListView[] listViews() default {};
	/**
	 * 实体表单视图
	 * @return
	 */
	FormView[] formViews() default {};
	/**
	 * 大纲视图
	 * @return
	 */
	OutlineView[] outlineViews() default {};
}
