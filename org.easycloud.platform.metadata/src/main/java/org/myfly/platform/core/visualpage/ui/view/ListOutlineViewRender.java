package org.myfly.platform.core.visualpage.ui.view;

import org.myfly.platform.core.metadata.define.ListDefinition;
import org.myfly.platform.core.metadata.internal.EntityMetaData;
import org.myfly.platform.core.visualpage.ui.ViewMode;
import org.myfly.platform.core.visualpage.ui.ViewType;
import org.myfly.platform.core.visualpage.ui.control.EntityTableWidgetBoxRender;

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
