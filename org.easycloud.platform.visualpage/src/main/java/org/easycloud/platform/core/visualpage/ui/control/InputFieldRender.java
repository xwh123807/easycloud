package org.myfly.platform.core.visualpage.ui.control;

import org.myfly.platform.core.utils.AssertUtil;
import org.myfly.platform.core.visualpage.ui.BaseFieldRender;
import org.myfly.platform.core.visualpage.ui.InputType;
import org.myfly.platform.core.visualpage.ui.ViewType;

/**
 * Input控件基类<br>
 * text、password、datetime、datetime-local、date、month、time、week、number、email、url、
 * search、tel 和 color<br>
 * 
 * @author xiangwanhong
 *
 */
public abstract class InputFieldRender extends BaseFieldRender {
	/**
	 * input type类型
	 * 
	 * @return
	 */
	public abstract InputType getInputType();

	/**
	 * 校验格式参考：http://blog.csdn.net/hliq5399/article/details/6557789
	 */
	@Override
	public String controlForEdit() {
		AssertUtil.parameterEmpty(getField(), "getField()");
		String rule = "";
		if (getViewType().equals(ViewType.EDIT) && getViewType().equals(ViewType.NEW) && getField().isRequired()) {
			//编辑和新增模式时，校验必填
			rule += " required";
		}
		if (getField().getMaxLength() > 0) {
			rule += " maxlength=\"" + getField().getMaxLength() + "\"";
		}
		if (getField().getMinLength() > 0) {
			rule += " minlength=\"" + getField().getMinLength() + "\"";
		}
		switch (getField().getDataType()) {
		case DATE:
		case MDRELATION:
			rule += " dateISO=true";
			break;
		case NUMBER:
			rule += " number=true";
			break;
		default:
			break;
		}
		return "<input type=\"" + getInputType().name().toLowerCase()
				+ "\" class=\"form-control\" placeholder=\"\" name=\"" + getField().getName() + "\" " + rule
				+ " value=\"$!{obj." + getField().getName() + "}\">";
	}

	@Override
	public String htmlForEdit() {
		AssertUtil.parameterEmpty(getField(), "getField()");
		StringBuffer buffer = new StringBuffer();
		buffer.append("<div class=\"form-group\">");
		buffer.append("  <label class=\"col-sm-4 control-label\">" + getField().getLabel() + "</label>");
		buffer.append("  <div class=\"col-sm-8\">");
		buffer.append("    " + control());
		buffer.append("  </div>");
		buffer.append("</div>");
		return buffer.toString();
	}
}
