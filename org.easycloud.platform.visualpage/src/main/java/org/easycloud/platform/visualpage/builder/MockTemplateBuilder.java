package org.easycloud.platform.visualpage.builder;

import org.easycloud.platform.visualpage.domain.VisualPageType;
import org.easycloud.platform.visualpage.ui.ViewMode;
import org.springframework.stereotype.Component;

@Component
public class MockTemplateBuilder extends BaseTemplateBuilder {
	public MockTemplateBuilder() {
		super();
	}

	@Override
	public VisualPageType getType() {
		return VisualPageType.VIEW;
	}

	@Override
	public String getDescription() {
		return "模拟测试用";
	}

	@Override
	public String getLayoutName() {
		return "test";
	}

	@Override
	public String buildTemplateContent(String entityName, String name, ViewMode viewMode) {
		return "<h1>test</h1>";
	}
}