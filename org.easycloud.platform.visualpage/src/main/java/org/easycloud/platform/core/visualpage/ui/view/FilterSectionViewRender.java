package org.myfly.platform.core.visualpage.ui.view;

import org.apache.commons.lang3.StringUtils;
import org.myfly.platform.core.metadata.define.FilterDefinition;
import org.myfly.platform.core.utils.HtmlUtils;
import org.myfly.platform.core.visualpage.ui.BaseFieldRender;
import org.myfly.platform.core.visualpage.ui.FieldRenderFactory;
import org.myfly.platform.core.visualpage.ui.ViewType;
import org.myfly.platform.core.visualpage.ui.WidgetBoxRender;

/**
 * 条件区域
 * 
 * @author xiangwanhong
 *
 */
public class FilterSectionViewRender extends BaseRender {
	private FilterDefinition[] filters;
	//查询请求处理url
	private String queryUrl;

	public FilterSectionViewRender(final FilterDefinition[] filters, final ViewType viewType, final String queryUrl) {
		super(viewType);
		this.filters = filters;
		this.queryUrl = queryUrl;
	}

	@Override
	public String html() {
		if (filters == null || filters.length == 0) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("<form" + HtmlUtils.addPropertys(new String[] {"class", "method", "action", "render" },
				new String[] { "form-edit", "post", queryUrl, getClass().getName() }) + ">");
		for (FilterDefinition filter : filters) {
			// 只显示允许显示的条件
			if (filter.isShow()) {
				buffer.append("<div class=\"col-md-4\">");
				BaseFieldRender fieldRender = FieldRenderFactory.getRender(filter.getField());
				// 过滤器控件设置为编辑模式，允许输入
				fieldRender.setViewType(ViewType.EDIT);
				buffer.append(fieldRender.html());
				buffer.append("</div>");
			}
		}
		buffer.append("<button class='btn btn-primary btn-sm' type=\"button\" onclick='javascript:formSubmit(event)'>查询</button>");
		buffer.append("<input class='btn btn-default btn-sm' type=\"reset\" value=\"清除\">");
		buffer.append("</form>");
		WidgetBoxRender box = new WidgetBoxRender("查询内容", getViewType());
		box.setContent(buffer.toString());
		return box.html();
	}

	@Override
	public String htmlForPrint() {
		if (filters == null || filters.length == 0) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for (FilterDefinition filter : filters) {
			if (StringUtils.isBlank((String) filter.getValue())) {
				continue;
			}
			buffer.append("<div class=\"col-md-4\">");
			BaseFieldRender fieldRender = FieldRenderFactory.getRender(filter.getField());
			fieldRender.setViewType(getViewType());
			buffer.append(fieldRender.html());
			buffer.append("</div>");
		}
		if (buffer.length() > 0) {
			WidgetBoxRender box = new WidgetBoxRender("查询内容", getViewType());
			box.setContent(buffer.toString());
			return box.html();
		} else {
			return "";
		}
	}
}
