package org.easycloud.platform.visualpage.builder;

import org.easycloud.platform.metadata.utils.AssertUtil;
import org.easycloud.platform.visualpage.domain.VisualPageType;
import org.easycloud.platform.visualpage.service.VisualPageConstants;
import org.easycloud.platform.visualpage.ui.ViewMode;
import org.easycloud.platform.visualpage.ui.ViewType;
import org.easycloud.platform.visualpage.ui.view.ListViewRender;
import org.springframework.stereotype.Component;

@Component
public class DefaultPrintListTemplateBuilder extends BaseTemplateBuilder {

	@Override
	public VisualPageType getType() {
		return VisualPageType.LISTPRINT;
	}

	@Override
	public String getDescription() {
		return "实体列表默认打印模板";
	}

	@Override
	public String getLayoutName() {
		return VisualPageConstants.LAYOUT_DEFAULT_NAME;
	}
	
	@Override
	public String buildTemplateContent(String entityName, String listViewName, ViewMode viewMode) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		ListViewRender listViewRender = new ListViewRender(entityName, ViewType.PRINT, viewMode, listViewName);
		return listViewRender.html();
	}
}
