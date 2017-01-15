package org.easycloud.platform.visualpage.builder;

import org.easycloud.platform.visualpage.domain.VisualPageType;
import org.easycloud.platform.visualpage.service.VisualPageConstants;
import org.easycloud.platform.visualpage.ui.ViewMode;
import org.easycloud.platform.visualpage.ui.ViewType;
import org.easycloud.platform.visualpage.ui.view.OutlineViewRender;
import org.springframework.stereotype.Component;

@Component
public class DefaultOutlineTemplateBuilder extends BaseTemplateBuilder{

	@Override
	public VisualPageType getType() {
		return VisualPageType.OUTLINE;
	}

	@Override
	public String getDescription() {
		return "默认大纲表单模板构建器";
	}

	@Override
	public String getLayoutName() {
		return VisualPageConstants.LAYOUT_DEFAULT_NAME;
	}

	@Override
	public String buildTemplateContent(String entityName, String formViewName, ViewMode viewMode) {
		OutlineViewRender render = new OutlineViewRender(entityName, ViewType.VIEW, viewMode, formViewName);
		return render.html();
	}
}
