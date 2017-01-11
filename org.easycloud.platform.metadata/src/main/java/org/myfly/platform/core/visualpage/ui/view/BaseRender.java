package org.myfly.platform.core.visualpage.ui.view;

import org.myfly.platform.core.metadata.internal.EntityMetaData;
import org.myfly.platform.core.utils.AppUtil;
import org.myfly.platform.core.visualpage.ui.ViewType;

public abstract class BaseRender {
	private ViewType viewType = ViewType.VIEW;

	public BaseRender(final ViewType viewType) {
		this.viewType = viewType;
	}

	public ViewType getViewType() {
		return viewType;
	}

	/**
	 * 获取实体元模型
	 * 
	 * @param entityName
	 * @return
	 */
	public EntityMetaData getEntityMataData(final String entityName) {
		return AppUtil.getEntityMataDataService().getEntityMetaData(entityName);
	}

	public String html() {
		switch (getViewType()) {
		case PRINT:
			return htmlForPrint();
		default:
			return htmlForView();
		}
	}

	public String htmlForView() {
		return "";
	}

	public String htmlForPrint() {
		return htmlForView();
	}
}
