package org.myfly.platform.core.visualpage.ui.control;

/**
 * 选择类控件基类
 * 
 * @author xiangwanhong
 *
 */
public abstract class SelectFieldRender extends InputFieldRender {

	@Override
	public String controlForEdit() {
		return "<select class=\"select2\" style=\"width:100%\" name=\"" + getField().getName() + "\" value=\"$!{obj."
				+ getField().getName() + "}\">" + getOptions() + "</select>";
	}

	/**
	 * 获取选择项内容 ${tool.buildEnumSelectOptions(\""+ getField().getRelationClass()+"\")}
	 * 
	 * @return
	 */
	public abstract String getOptions();
}
