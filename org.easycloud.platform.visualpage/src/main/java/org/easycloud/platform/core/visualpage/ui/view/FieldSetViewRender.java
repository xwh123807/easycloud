package org.myfly.platform.core.visualpage.ui.view;

import org.myfly.platform.core.metadata.define.FieldDefinition;
import org.myfly.platform.core.metadata.define.FieldSetDefinition;
import org.myfly.platform.core.utils.AssertUtil;
import org.myfly.platform.core.utils.HtmlUtils;
import org.myfly.platform.core.visualpage.ui.BaseFieldRender;
import org.myfly.platform.core.visualpage.ui.FieldRenderFactory;
import org.myfly.platform.core.visualpage.ui.ViewType;

/**
 * 字段集
 * 
 * @author xiangwanhong
 *
 */
public class FieldSetViewRender extends BaseRender {
	private FieldSetDefinition fieldSetDefinition;

	private boolean fullSize = false;

	public FieldSetViewRender(final FieldSetDefinition fieldSetDefinition, final ViewType viewType) {
		super(viewType);
		AssertUtil.parameterEmpty(fieldSetDefinition, "FieldSetViewRender.fieldSetDefinition");
		this.fieldSetDefinition = fieldSetDefinition;
	}

	public String html() {
		StringBuffer buffer = new StringBuffer();
		String sizeClass = isFullSize() ? "" : "col-sm-6";
		buffer.append("<fieldset class=\"form-horizontal " + sizeClass + "\""
				+ HtmlUtils.addProperty("render", getClass().getName()) + ">");
		for (FieldDefinition field : fieldSetDefinition.getFields()) {
			BaseFieldRender render = FieldRenderFactory.getRender(field);
			render.setViewType(getViewType());
			buffer.append(render.html());
		}
		buffer.append("</fieldset>");
		return buffer.toString();
	}

	public boolean isFullSize() {
		return fullSize;
	}

	public void setFullSize(boolean fullSize) {
		this.fullSize = fullSize;
	}
}
