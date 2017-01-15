package org.easycloud.platform.visualpage.ui.control;

import org.easycloud.platform.visualpage.ui.InputType;

public class DateInputFieldRender extends InputFieldRender {

	@Override
	public InputType getInputType() {
		return InputType.DATE;
	}

	@Override
	public String controlForEdit() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<div class=\"input-group\">");
		buffer.append(
				"  <input class=\"form-control date-picker\" type=\"text\" data-date-format=\"yyyy-mm-dd\" name=\""
						+ getField().getName() + "\" value=\"$!{obj." + getField().getName() + "}\">");
		buffer.append("  <span class=\"input-group-addon\"> <i class=\"fa fa-calendar bigger-110\"></i></span>");
		buffer.append("</div>");
		return buffer.toString();
	}
}
