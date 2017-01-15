package org.easycloud.platform.visualpage.ui.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easycloud.platform.metadata.utils.AssertUtil;
import org.easycloud.platform.visualpage.ui.ViewMode;
import org.easycloud.platform.visualpage.ui.ViewType;

/**
 * 大纲视图
 * @author xiangwanhong
 *
 */
public class OutlineViewRender extends BaseViewRender{
	private Log log = LogFactory.getLog(getClass());
	private SectionViewRender[] sections;
	private String entityName;
	private String formViewName;
	
	public OutlineViewRender(final String entityName, final ViewType viewType, ViewMode viewMode, String formViewName) {
		super(viewType, viewMode);
		this.entityName = entityName;
		this.formViewName = formViewName;
		AssertUtil.parameterEmpty(entityName, "OutlineViewRender.entityName");
	}	
	
	public String html() {
		/*
		StringBuffer buffer = new StringBuffer();
		for (SectionViewRender section : sections) {
			buffer.append(section.html());
		}
		return buffer.toString();
		*/
		FormViewRender render = new FormViewRender(entityName, getViewType(), getViewMode(), formViewName);
		return render.html();
	}
}
