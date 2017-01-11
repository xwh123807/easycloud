package org.myfly.platform.core.visualpage.builder;

import org.myfly.platform.core.utils.AssertUtil;
import org.myfly.platform.core.visualpage.domain.VisualPageType;
import org.myfly.platform.core.visualpage.service.VisualPageConstants;
import org.myfly.platform.core.visualpage.ui.ViewMode;
import org.myfly.platform.core.visualpage.ui.ViewType;
import org.myfly.platform.core.visualpage.ui.view.FormViewRender;
import org.springframework.stereotype.Component;

@Component
public class DefaultPrintTemplateBuilder extends BaseTemplateBuilder {

	@Override
	public VisualPageType getType() {
		return VisualPageType.PRINT;
	}

	@Override
	public String getDescription() {
		return "实体默认打印模板";
	}

	@Override
	public String getLayoutName() {
		return VisualPageConstants.LAYOUT_DEFAULT_NAME;
	}
	
	@Override
	public String buildTemplateContent(String entityName, String formViewName, ViewMode viewMode) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		FormViewRender render = new FormViewRender(entityName, ViewType.PRINT, viewMode, formViewName);
		return render.html();
	}
}
