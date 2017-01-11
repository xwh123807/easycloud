package org.myfly.platform.core.visualpage.builder;

import org.myfly.platform.core.visualpage.domain.VisualPageType;
import org.myfly.platform.core.visualpage.service.VisualPageConstants;
import org.myfly.platform.core.visualpage.ui.BaseRender;
import org.springframework.stereotype.Component;

@Component
public class DefaultSideBarTemplateBuilder extends BaseTemplateBuilder {

	@Override
	public VisualPageType getType() {
		return VisualPageType.RENDER;
	}

	@Override
	public String getDescription() {
		return "自定义子页面";
	}

	@Override
	public String getLayoutName() {
		return VisualPageConstants.LAYOUT_DEFAULT_NAME;
	}

	@Override
	public String buildTemplateContent(BaseRender render) {
		if (render != null) {
			return render.html();
		} else {
			return "";
		}
	}
}
