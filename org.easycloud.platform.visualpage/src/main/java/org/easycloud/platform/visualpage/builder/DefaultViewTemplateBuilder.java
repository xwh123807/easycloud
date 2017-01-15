package org.easycloud.platform.visualpage.builder;

import org.easycloud.platform.visualpage.domain.VisualPageType;
import org.easycloud.platform.visualpage.service.VisualPageConstants;
import org.easycloud.platform.visualpage.ui.ViewMode;
import org.easycloud.platform.visualpage.ui.ViewType;
import org.easycloud.platform.visualpage.ui.view.FormViewRender;
import org.springframework.stereotype.Component;

@Component
public class DefaultViewTemplateBuilder extends BaseTemplateBuilder{

	@Override
	public VisualPageType getType() {
		return VisualPageType.VIEW;
	}

	@Override
	public String getDescription() {
		return "默认只读表单模板构建器";
	}

	@Override
	public String getLayoutName() {
		return VisualPageConstants.LAYOUT_DEFAULT_NAME;
	}

	@Override
	public String buildTemplateContent(String entityName, String formViewName, ViewMode viewMode) {
		FormViewRender render = new FormViewRender(entityName, ViewType.VIEW, viewMode, formViewName);
		return render.html();
	}
}
