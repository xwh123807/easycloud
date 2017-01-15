package org.easycloud.platform.visualpage.builder;

import org.easycloud.platform.metadata.utils.AssertUtil;
import org.easycloud.platform.visualpage.domain.VisualPageType;
import org.easycloud.platform.visualpage.service.VisualPageConstants;
import org.easycloud.platform.visualpage.ui.ViewMode;
import org.easycloud.platform.visualpage.ui.ViewType;
import org.easycloud.platform.visualpage.ui.view.SearchListViewRender;
import org.springframework.stereotype.Component;

@Component
public class DefaultSearchTemplateBuilder extends BaseTemplateBuilder{
	@Override
	public VisualPageType getType() {
		return VisualPageType.SEARCH;
	}
	
	@Override
	public String getLayoutName() {
		return VisualPageConstants.LAYOUT_DEFAULT_NAME;
	}

	@Override
	public String buildTemplateContent(String entityName, String name, ViewMode viewMode) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		SearchListViewRender searchListViewRender = new SearchListViewRender(entityName, ViewType.VIEW, name);
		return searchListViewRender.html();
	}

	@Override
	public String getDescription() {
		return "实体对象搜索模板";
	}
}
