package org.myfly.platform.core.metadata.define;

import org.myfly.platform.core.domain.BaseEnum;

public enum OrderType implements BaseEnum{
	//
	ASC("升序"), 
	//
	DESC("降序");
	
	private String title;

	private OrderType(String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}

}
