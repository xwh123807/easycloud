package org.easycloud.platform.visualpage.builder;

import org.easycloud.platform.metadata.utils.AssertUtil;
import org.easycloud.platform.visualpage.domain.VisualPageType;
import org.easycloud.platform.visualpage.service.VisualPageConstants;
import org.easycloud.platform.visualpage.ui.ViewMode;
import org.easycloud.platform.visualpage.ui.ViewType;
import org.easycloud.platform.visualpage.ui.view.FormViewRender;
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
