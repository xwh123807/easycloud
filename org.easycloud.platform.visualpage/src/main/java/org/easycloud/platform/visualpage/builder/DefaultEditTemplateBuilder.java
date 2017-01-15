package org.easycloud.platform.visualpage.builder;

import org.easycloud.platform.visualpage.domain.VisualPageType;
import org.easycloud.platform.visualpage.service.VisualPageConstants;
import org.easycloud.platform.visualpage.ui.ViewMode;
import org.easycloud.platform.visualpage.ui.ViewType;
import org.easycloud.platform.visualpage.ui.view.FormViewRender;
import org.springframework.stereotype.Component;

@Component
public class DefaultEditTemplateBuilder extends BaseTemplateBuilder{

	@Override
	public VisualPageType getType() {
		return VisualPageType.EDIT;
	}

	@Override
	public String getDescription() {
		return "默认编辑表单模板构建器";
	}

	@Override
	public String getLayoutName() {
		return VisualPageConstants.LAYOUT_DEFAULT_NAME;
	}

	@Override
	public String buildTemplateContent(String entityName, String viewName, final ViewMode viewMode) {
		FormViewRender render = new FormViewRender(entityName, ViewType.EDIT, viewMode, viewName);
		return render.html();
	}
}
