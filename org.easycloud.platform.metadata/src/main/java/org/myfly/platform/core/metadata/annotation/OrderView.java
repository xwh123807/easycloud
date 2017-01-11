package org.myfly.platform.core.metadata.annotation;

import org.myfly.platform.core.metadata.define.OrderType;

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
