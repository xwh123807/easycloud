package org.myfly.platform.core.visualpage.ui.control;

import org.myfly.platform.core.visualpage.ui.InputType;

public class FileInputFieldRender extends InputFieldRender {

	@Override
	public InputType getInputType() {
		return InputType.FILE;
	}

	@Override
	public String controlForView() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(
				"<input type='button' value='下载' onclick='javascript:window.open(\"/file/download/$!{obj.uid}\")' attr='"
						+ getField().getName() + "'/>");
		return buffer.toString();
	}
}
