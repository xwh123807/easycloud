package org.myfly.platform.core.visualpage.ui.control;

import org.myfly.platform.core.visualpage.ui.InputType;

public class CheckInputFieldRender extends InputFieldRender {

	@Override
	public InputType getInputType() {
		return InputType.CHECKBOX;
	}

	@Override
	public String controlForEdit() {
		return "<input type=\"" + getInputType().name().toLowerCase()
				+ "\" class=\"form-control\" placeholder=\"\" name=\"" + getField().getName() + "\" #if ($obj."
				+ getField().getName() + "==\"true\") checked #end value=\"$!{obj." + getField().getName() + "}\">";
	}
}
