package org.easycloud.platform.visualpage.ui.control.button;

import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.visualpage.ui.BaseButtonRender;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 页面提交按钮
 * 
 * @author xiangwanhong
 *
 */
public class SubmitButtonRender extends BaseButtonRender {
	private String title;
	private RequestMethod method;
	private String url;

	public SubmitButtonRender(final String title, final RequestMethod method, final String url) {
		this.title = title;
		this.method = method;
		this.url = url;
	}

	@Override
	public String html() {
		String buffer = "<button class='btn btn-primary' name='_method' value='" + method.name() + "'";
		if (StringUtils.isNotBlank(url)){
			buffer += " action='" + url + "'";
		}
		buffer += " onclick='javascript:formSubmit(event)'>" + title + "</button>";
		return buffer;
	}
}
