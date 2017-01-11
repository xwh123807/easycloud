package org.myfly.platform.core.visualpage.ui.control.button;

import org.myfly.platform.core.visualpage.ui.BaseButtonRender;

public class PrintButtonRender extends BaseButtonRender {
	@Override
	public String html() {
		return "<input type='button' class='btn btn-default' value='打印'>";
	}
}
