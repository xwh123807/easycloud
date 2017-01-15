package org.easycloud.platform.visualpage.ui.control.button;

import org.easycloud.platform.visualpage.ui.BaseButtonRender;

/**
 * 打开连接按钮
 * 
 * @author xiangwanhong
 *
 */
public class UrlButtonRender extends BaseButtonRender {
	private String title;
	private String url;

	public UrlButtonRender(String title, String url) {
		this.title = title;
		this.url = url;
	}

	@Override
	public String html() {
		String buffer = "<button class='btn btn-default' type='button' onclick='javascript:openModal(\"" + url
				+ "\")'>"+title+"</button>";
		return buffer;
	}
}
