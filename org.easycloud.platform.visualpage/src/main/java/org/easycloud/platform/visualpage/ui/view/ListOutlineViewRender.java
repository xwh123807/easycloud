package org.easycloud.platform.visualpage.ui.view;

import org.easycloud.platform.metadata.define.view.ListDefinition;
import org.easycloud.platform.metadata.service.EntityMetaData;
import org.easycloud.platform.visualpage.ui.ViewMode;
import org.easycloud.platform.visualpage.ui.ViewType;
import org.easycloud.platform.visualpage.ui.control.EntityTableWidgetBoxRender;

public class ListOutlineViewRender extends BaseRender{

	private String entityName;
	private String formViewName;


	public ListOutlineViewRender(String entityName, String formViewName, ViewMode viewMode) {
		super(ViewType.LISTOUTLINE);
		this.entityName = entityName;
		this.formViewName = formViewName;
	}

	
	@Override
	public String htmlForView() {
		EntityMetaData entityMetaData = getEntityMataData(entityName);
		ListDefinition listDefinition = entityMetaData.getListDefinition(formViewName);
		EntityTableWidgetBoxRender box = new EntityTableWidgetBoxRender(listDefinition, getViewType());
		return box.html();
	}
}
