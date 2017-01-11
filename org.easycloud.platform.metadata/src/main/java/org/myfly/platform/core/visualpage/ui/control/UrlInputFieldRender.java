package org.myfly.platform.core.visualpage.ui.control;

import org.myfly.platform.core.visualpage.ui.InputType;

public class UrlInputFieldRender extends InputFieldRender {

	@Override
	public InputType getInputType() {
		return InputType.URL;
	}

	@Override
	public String controlForView() {
		return "<span class=\"bg-info\">" + "<a href='$!{obj." + getField().getName() + "}'>$!{obj."
				+ getField().getName() + "}</a>" + "</span>";
	}
}
