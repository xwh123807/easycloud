package org.easycloud.platform.visualpage.ui.control;

import java.util.HashMap;
import java.util.Map;

import org.easycloud.platform.metadata.annotation.view.EntityAction;
import org.easycloud.platform.visualpage.ui.ViewType;

public class SelectListViewRender extends SelectViewRender {

	public SelectListViewRender(String entityName, ViewType viewType, String viewName) {
		super(entityName, viewType, viewName);
	}

	@Override
	public Map<String, String> getOptions() {
		Map<String, String> map = new HashMap<>();
		for (String name : getEntityMetaData().getListDefinitions().keySet()) {
			map.put(name, name);
		}
		return map;
	}

	@Override
	public String getUrl() {
		return EntityUrlUtil.getEntityActionUrl(EntityAction.LIST, getEntityMetaData().getEntityName(), null, null);
	}
}
