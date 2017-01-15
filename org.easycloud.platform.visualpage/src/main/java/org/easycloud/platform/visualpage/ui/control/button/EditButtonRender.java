package org.easycloud.platform.visualpage.ui.control.button;

import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.visualpage.ui.BaseButtonRender;

public class EditButtonRender extends BaseButtonRender {
	@Override
	public String html() {
		String script = "";
		if (StringUtils.isNotBlank(getUrl())) {
			script = "openModal('" + getUrl() + "')";
		} else {
			script = "window.location=location.pathname + '?form')";
		}
		return "<button class=\"btn btn-default\" type=\"button\" onclick=\"javascript:" + script + "\"'>编辑</button>";
	}
}
