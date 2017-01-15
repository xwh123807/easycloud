package org.easycloud.platform.visualpage.ui.control.button;

import org.easycloud.platform.visualpage.ui.BaseButtonRender;

/**
 * 返回按钮
 * @author xiangwanhong
 *
 */
public class BackButtonRender extends BaseButtonRender{
	@Override
	public String html() {
		return "<button class=\"btn btn-back\" type=\"button\" onclick=\"javascript:autoback()\">返回</button>";
	}
}
