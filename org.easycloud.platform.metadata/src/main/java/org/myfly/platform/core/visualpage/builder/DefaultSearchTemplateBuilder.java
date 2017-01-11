package org.myfly.platform.core.visualpage.builder;

import org.myfly.platform.core.utils.AssertUtil;
import org.myfly.platform.core.visualpage.domain.VisualPageType;
import org.myfly.platform.core.visualpage.service.VisualPageConstants;
import org.myfly.platform.core.visualpage.ui.ViewMode;
import org.myfly.platform.core.visualpage.ui.ViewType;
import org.myfly.platform.core.visualpage.ui.view.SearchListViewRender;
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
