package org.easycloud.platform.visualpage.ui.view;

import org.easycloud.platform.metadata.service.EntityMetaData;
import org.easycloud.platform.metadata.service.ServiceUtil;
import org.easycloud.platform.visualpage.ui.ViewType;

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
		return ServiceUtil.getEntityMetaDataService().getEntityMetaData(entityName);
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
