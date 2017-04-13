package org.easycloud.metadata.generator.annotation.view;

public @interface OrderView {
	/**
	 * 排序字段名
	 * @return
	 */
	String field();
	/**
	 * 方向
	 * @return
	 */
	OrderType orderType() default OrderType.ASC;
}
