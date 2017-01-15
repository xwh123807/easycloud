package org.easycloud.platform.visualpage.ui.control;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.metadata.service.EntityMetaData;
import org.easycloud.platform.metadata.utils.HtmlUtils;
import org.easycloud.platform.visualpage.ui.ViewType;
import org.easycloud.platform.visualpage.ui.view.BaseRender;

public class SelectViewRender extends BaseRender {

	private EntityMetaData entityMetaData;
	private String viewName;
	private String url;

	public SelectViewRender(String entityName, ViewType viewType, String viewName) {
		super(viewType);
		this.viewName = viewName;
		entityMetaData = getEntityMataData(entityName);
	}

	@Override
	public String htmlForView() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<div" + HtmlUtils.addPropertys(new String[] { "render" }, new String[] { getClass().getName() })
				+ ">");
		String url = getUrl() + "?view=\"+$(event.target).val()";
		buffer.append("视图：<select class='select2' style='width:150px' value='" + getValue() + "' onchange='javascript:openModal(\""
				+ url + ",false)'>");
		Map<String, String> options = getOptions();
		if (MapUtils.isNotEmpty(options)) {
			for (Map.Entry<String, String> item : options.entrySet()) {
				buffer.append("<option value='" + item.getKey() + "'>" + item.getValue() + "</option>");
			}
		}
		buffer.append("</select>");
		buffer.append("</div>");
		return buffer.toString();
	}

	public EntityMetaData getEntityMetaData() {
		return entityMetaData;
	}
	
	public Map<String, String> getOptions() {
		return null;
	}

	public String getValue() {
		return StringUtils.isBlank(viewName) ? "default" : viewName;
	}

	public String getUrl() {
		return "";
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
