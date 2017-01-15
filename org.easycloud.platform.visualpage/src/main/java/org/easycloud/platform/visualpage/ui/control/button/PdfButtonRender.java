package org.easycloud.platform.visualpage.ui.control.button;

import org.easycloud.platform.visualpage.ui.BaseButtonRender;

public class PdfButtonRender extends BaseButtonRender {
	@Override
	public String html() {
		return "<button class=\"btn btn-default\" type=\"button\">导出PDF</button>";
	}
}
