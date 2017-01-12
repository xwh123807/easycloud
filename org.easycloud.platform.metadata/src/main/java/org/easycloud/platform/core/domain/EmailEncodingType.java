package org.easycloud.platform.core.domain;

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
