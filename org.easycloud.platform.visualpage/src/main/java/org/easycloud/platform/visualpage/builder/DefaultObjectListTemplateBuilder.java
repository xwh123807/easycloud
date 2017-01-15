package org.easycloud.platform.visualpage.builder;

import java.lang.annotation.Annotation;

import org.easycloud.platform.metadata.annotation.view.ObjectListView;
import org.easycloud.platform.metadata.utils.AssertUtil;
import org.easycloud.platform.visualpage.domain.VisualPageType;
import org.easycloud.platform.visualpage.service.VisualPageConstants;
import org.easycloud.platform.visualpage.ui.view.ObjectListViewRender;
import org.springframework.stereotype.Component;

@Component
public class DefaultObjectListTemplateBuilder extends BaseTemplateBuilder {
	@Override
	public VisualPageType getType() {
		return VisualPageType.OLIST;
	}

	@Override
	public String getLayoutName() {
		return VisualPageConstants.LAYOUT_DEFAULT_NAME;
	}

	@Override
	public String getDescription() {
		return "自定义对象默认列表模板";
	}

	@Override
	public <T extends Annotation> String buildTemplateContent(T objectBean) {
		AssertUtil.parameterEmpty(objectBean, "objectBean");
		if (objectBean instanceof ObjectListView) {
			ObjectListViewRender table = new ObjectListViewRender((ObjectListView) objectBean);
			return table.html();
		}else{
			throw new IllegalArgumentException("参数objectBean只能是ObjectListView类型.");
		}
	}
}
