package org.easycloud.platform.visualpage.ui.control.button;

import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.visualpage.ui.BaseButtonRender;

/**
 * 实体新增按钮
 * @author xiangwanhong
 *
 */
public class NewButtonRender extends BaseButtonRender {
	@Override
	public String html() {
		String script = "";
		if (StringUtils.isNotBlank(getUrl())) {
			script = "openModal('" + getUrl() + "')";
		} else {
			script = "window.location=location.pathname.substring(0,location.pathname.lastIndexOf('/'))";
		}
		return "<button class=\"btn btn-default\" type=\"button\" onclick=\"javascript:" + script + "\"'>新增</button>";
	}
}
