package org.easycloud.metadata.generator.define.view;

import org.easycloud.platform.metadata.define.entity.BaseDenifition;

public class OrderDefinition extends BaseDenifition{
	private OrderType orderType;
	
	public OrderDefinition() {
		super(null);
	}

	public OrderDefinition(Object owner, String name, OrderType orderType) {
		super(owner);
		setName(name);
		setOrderType(orderType);
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	@Override
	public String toString() {
		return "name: " + getName() + ", orderType: " + orderType.getTitle();
	}
}
