package org.easycloud.platform.core.domain;

import org.easycloud.platform.metadata.annotation.BaseEnum;

/**
 * 邮件字符集编码
 * 
 * @author xiangwanhong
 *
 */
public enum EmailEncodingType implements BaseEnum {
	UTF8("UTF-8");

	private String title;

	private EmailEncodingType(final String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

}
