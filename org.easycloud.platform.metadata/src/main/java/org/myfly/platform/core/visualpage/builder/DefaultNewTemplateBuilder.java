package org.myfly.platform.core.visualpage.builder;

import org.myfly.platform.core.visualpage.domain.VisualPageType;
import org.myfly.platform.core.visualpage.service.VisualPageConstants;
import org.myfly.platform.core.visualpage.ui.ViewMode;
import org.myfly.platform.core.visualpage.ui.ViewType;
import org.myfly.platform.core.visualpage.ui.view.FormViewRender;
import org.springframework.stereotype.Component;

@Component
public class DefaultNewTemplateBuilder extends BaseTemplateBuilder{

	@Override
	public VisualPageType getType() {
		return VisualPageType.NEW;
	}

	@Override
	public String getDescription() {
		return "默认新增表单模板构建器";
	}

	@Override
	public String getLayoutName() {
		return VisualPageConstants.LAYOUT_DEFAULT_NAME;
	}

	@Override
	public String buildTemplateContent(String entityName, String formViewName, ViewMode viewMode) {
		FormViewRender render = new FormViewRender(entityName, ViewType.NEW, viewMode, formViewName);
		return render.html();
	}
}
