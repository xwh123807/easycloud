package org.easycloud.platform.visualpage.ui.control.component;

import org.easycloud.platform.metadata.utils.HtmlUtils;
import org.easycloud.platform.visualpage.ui.ViewType;
import org.easycloud.platform.visualpage.ui.view.BaseRender;

public abstract class BaseComponentRender extends BaseRender{

	public BaseComponentRender(ViewType viewType) {
		super(viewType);
	}

	public abstract String getComponentName();
	
	@Override
	public String htmlForView() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<div" + HtmlUtils.addPropertys(new String[] { "render" }, new String[] { getClass().getName() })
				+ ">");
		buffer.append("#parse(\"${pageInfo.layout}/components/" + getComponentName() + ".vm\")");
		buffer.append("</div>");
		return buffer.toString();
	}
	
	@Override
	public String htmlForPrint() {
		return "";
	}
}
