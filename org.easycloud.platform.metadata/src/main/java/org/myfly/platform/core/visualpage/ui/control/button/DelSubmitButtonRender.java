package org.myfly.platform.core.visualpage.ui.control.button;

import org.myfly.platform.core.utils.HttpUtil;
import org.myfly.platform.core.visualpage.ui.BaseButtonRender;
import org.springframework.web.bind.annotation.RequestMethod;

public class DelSubmitButtonRender extends BaseButtonRender {
	@Override
	public String html() {
		/*
		String script = "";
		if (StringUtils.isNotBlank(getUrl())) {
			script = "openModal('" + getUrl() + "')";
			return "<button class=\"btn btn-default\" type=\"button\" onclick=\"javascript:" + script
					+ "\"'>删除</button>";
		} else {
			return "<form><button class=\"btn btn-default\" type=\"submit\" name=\"_method\" value=\""
					+ RequestMethod.DELETE + "\">删除</button></form>";
		}
		*/
		return "<button class=\"btn btn-default\" type=\"button\" name=\"" + HttpUtil.MYMETHOD_NAME
				+ "\"  value=\"" + RequestMethod.DELETE + "\" onclick=\"javascript:formSubmit(event)\">删除</button>";
	}
}
