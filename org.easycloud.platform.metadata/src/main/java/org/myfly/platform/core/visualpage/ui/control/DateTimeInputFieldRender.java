package org.myfly.platform.core.visualpage.ui.control;

import org.myfly.platform.core.visualpage.ui.InputType;

public class DateTimeInputFieldRender extends InputFieldRender {

	@Override
	public InputType getInputType() {
		return InputType.DATETIME;
	}

	@Override
	public String controlForEdit() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<div class=\"input-group\">");
		buffer.append(
				"  <input class=\"form-control datetime-picker\" type=\"text\" data-date-format=\"\" name=\""
						+ getField().getName() + "\" value=\"$!{obj." + getField().getName() + "}\">");
		buffer.append("  <span class=\"input-group-addon\"> <i class=\"fa fa-calendar bigger-110\"></i></span>");
		buffer.append("</div>");
		return buffer.toString();
	}
}
