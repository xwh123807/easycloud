package org.easycloud.platform.visualpage.builder;

import org.easycloud.platform.metadata.utils.AssertUtil;
import org.easycloud.platform.visualpage.domain.VisualPageType;
import org.easycloud.platform.visualpage.service.VisualPageConstants;
import org.easycloud.platform.visualpage.ui.ViewMode;
import org.easycloud.platform.visualpage.ui.ViewType;
import org.easycloud.platform.visualpage.ui.view.ListViewRender;
import org.springframework.stereotype.Component;

@Component
public class DefaultListTemplateBuilder extends BaseTemplateBuilder{
	@Override
	public VisualPageType getType() {
		return VisualPageType.LIST;
	}
	
	@Override
	public String getLayoutName() {
		return VisualPageConstants.LAYOUT_DEFAULT_NAME;
	}

	@Override
	public String buildTemplateContent(String entityName, String viewName, ViewMode viewMode) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		ListViewRender listViewRender = new ListViewRender(entityName, ViewType.VIEW, viewMode, viewName);
		return listViewRender.html();
	}

	@Override
	public String getDescription() {
		return "实体对象默认列表模板";
	}
}
