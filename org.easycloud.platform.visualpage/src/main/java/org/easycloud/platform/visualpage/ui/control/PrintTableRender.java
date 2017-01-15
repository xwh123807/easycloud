package org.easycloud.platform.visualpage.ui.control;

import org.easycloud.platform.metadata.define.view.ListDefinition;
import org.easycloud.platform.visualpage.ui.ViewType;

/**
 * 供打印的表格，用于输出成Pdf
 * @author xiangwanhong
 *
 */
public class PrintTableRender extends VelocityTemplateTableRender{

	public PrintTableRender(ListDefinition listDefinition, ViewType viewType) {
		super(listDefinition, viewType);
		addExtAttr("border", "1");
	}

	@Override
	public String getTableClass() {
		return "";
	}
}
