package org.easycloud.metadata.generator.annotation.view;

import org.easycloud.metadata.generator.annotation.entity.BaseEnum;

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
