package org.easycloud.platform.visualpage.ui.control;

import java.util.HashMap;
import java.util.Map;

import org.easycloud.platform.metadata.annotation.view.EntityAction;
import org.easycloud.platform.visualpage.ui.ViewType;

public class SelectFormViewRender extends SelectViewRender{

	public SelectFormViewRender(String entityName, ViewType viewType, String viewName) {
		super(entityName, viewType, viewName);
	}

	@Override
	public Map<String, String> getOptions() {
		Map<String, String> map = new HashMap<>();
		for (String name : getEntityMetaData().getFormDefinitions().keySet()){
			map.put(name, name);
		}
		return map;
	}
	
	@Override
	public String getUrl() {
		//TODO 此处要区分实体子表场景
		return EntityUrlUtil.getEntityActionUrl(EntityAction.VIEW, getEntityMetaData().getEntityName(), "${uid}", null);
	}
}
