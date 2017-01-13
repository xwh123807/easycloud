package org.easycloud.platform.core.domain;

import org.easycloud.platform.metadata.annotation.BaseEnum;

/**
 * 语言
 * 
 * @author xiangwanhong
 *
 */
public enum LanguageLocaleType implements BaseEnum {
	CN("中国"), HK("香港");

	private String title;

	private LanguageLocaleType(final String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return this.title;
	}
}
