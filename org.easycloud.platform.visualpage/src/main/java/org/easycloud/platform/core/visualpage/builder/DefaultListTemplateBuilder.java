package org.myfly.platform.core.visualpage.builder;

import org.myfly.platform.core.utils.AssertUtil;
import org.myfly.platform.core.visualpage.domain.VisualPageType;
import org.myfly.platform.core.visualpage.service.VisualPageConstants;
import org.myfly.platform.core.visualpage.ui.ViewMode;
import org.myfly.platform.core.visualpage.ui.ViewType;
import org.myfly.platform.core.visualpage.ui.view.ListViewRender;
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
