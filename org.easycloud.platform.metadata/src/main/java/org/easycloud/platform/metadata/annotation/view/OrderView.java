package org.easycloud.platform.metadata.annotation.view;

import org.easycloud.platform.metadata.define.view.OrderType;

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
