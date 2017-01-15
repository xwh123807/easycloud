package org.easycloud.platform.visualpage.ui.view;

import org.easycloud.platform.metadata.define.entity.FieldDefinition;
import org.easycloud.platform.metadata.define.view.FieldSetDefinition;
import org.easycloud.platform.metadata.utils.AssertUtil;
import org.easycloud.platform.metadata.utils.HtmlUtils;
import org.easycloud.platform.visualpage.ui.BaseFieldRender;
import org.easycloud.platform.visualpage.ui.FieldRenderFactory;
import org.easycloud.platform.visualpage.ui.ViewType;

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
