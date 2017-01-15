package org.easycloud.platform.visualpage.ui.control;

import org.easycloud.platform.metadata.define.view.FieldSetDefinition;
import org.easycloud.platform.metadata.define.view.ListDefinition;
import org.easycloud.platform.metadata.utils.HtmlUtils;
import org.easycloud.platform.visualpage.ui.BaseRender;
import org.easycloud.platform.visualpage.ui.ViewType;
import org.easycloud.platform.visualpage.ui.view.FieldSetViewRender;

/**
 * 卡片式
 * @author xiangwanhong
 *
 */
public class CardRender implements BaseRender{

	private ListDefinition listDefinition;

	public CardRender(ListDefinition listDefinition) {
		this.listDefinition = listDefinition;
	}

	@Override
	public String html() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<div" + HtmlUtils.addProperty("render", getClass().getName()) + ">");
		FieldSetDefinition fieldSetDefinition = new FieldSetDefinition(listDefinition.getTitle());
		fieldSetDefinition.setFields(listDefinition.getFields());
		FieldSetViewRender fieldSetViewRender = new FieldSetViewRender(fieldSetDefinition , ViewType.VIEW);
		fieldSetViewRender.setFullSize(true);
		buffer.append(fieldSetViewRender.html());
		buffer.append("</div>");
		return buffer.toString();
	}

}
