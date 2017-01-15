package org.easycloud.platform.visualpage.ui.control.button;

import org.easycloud.platform.visualpage.ui.BaseButtonRender;
import org.springframework.web.bind.annotation.RequestMethod;

public class UpdateSaveSubmitButtonRender extends BaseButtonRender {
	@Override
	public String html() {
		return "<button class=\"btn btn-primary\" type=\"button\" name=\"" + HttpUtil.MYMETHOD_NAME
				+ "\"  value=\"" + RequestMethod.PUT + "\" onclick=\"javascript:formSubmit(event)\">保存</button>";
	}
}
