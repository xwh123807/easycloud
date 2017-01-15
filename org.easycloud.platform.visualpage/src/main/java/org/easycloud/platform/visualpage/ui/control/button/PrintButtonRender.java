package org.easycloud.platform.visualpage.ui.control.button;

import org.easycloud.platform.visualpage.ui.BaseButtonRender;

public class PrintButtonRender extends BaseButtonRender {
	@Override
	public String html() {
		return "<input type='button' class='btn btn-default' value='打印'>";
	}
}
