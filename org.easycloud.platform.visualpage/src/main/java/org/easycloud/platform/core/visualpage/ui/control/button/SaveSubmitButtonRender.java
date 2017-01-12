package org.myfly.platform.core.visualpage.ui.control.button;

import org.myfly.platform.core.utils.HttpUtil;
import org.myfly.platform.core.visualpage.ui.BaseButtonRender;
import org.springframework.web.bind.annotation.RequestMethod;

public class SaveSubmitButtonRender extends BaseButtonRender {
	@Override
	public String html() {
		return "<button class=\"btn btn-primary\" type=\"button\" name=\"" + HttpUtil.MYMETHOD_NAME
				+ "\"  value=\"" + RequestMethod.POST + "\" onclick=\"javascript:formSubmit(event)\">保存</button>";
	}
}
