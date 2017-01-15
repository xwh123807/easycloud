package org.easycloud.platform.visualpage.ui;

import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.metadata.utils.HtmlUtils;
import org.easycloud.platform.visualpage.ui.view.BaseRender;

/**
 * Widget控件，带工具条的面板
 * 
 * @author xiangwanhong
 *
 */
public class WidgetBoxRender extends BaseRender {
	/**
	 * widget box head
	 */
	private String title;
	/**
	 * widget body
	 */
	private String content;
	private String sizeClass;

	public WidgetBoxRender(final String title, final ViewType viewType) {
		super(viewType);
		this.title = title;
	}

	public WidgetBoxRender(final String title, final ViewType viewType, final String sizeClass) {
		super(viewType);
		this.title = title;
		this.sizeClass = sizeClass;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	/**
	 * 获取工具条UI
	 * 
	 * @return
	 */
	public String getToolbarHtml() {
		return "";
	}

	/**
	 * 特殊样式
	 * 
	 * @return
	 */
	public String getTableStyle() {
		return "";
	}

	public String getSizeClass() {
		if (StringUtils.isBlank(sizeClass)) {
			return "col-xs-12";
		} else {
			return sizeClass;
		}
	}

	@Override
	public String htmlForView() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<div" + HtmlUtils.addIdProperty()
				+ HtmlUtils.addPropertys(new String[] { "render" }, new String[] { getClass().getName() }) + ">");
		buffer.append("<div class=\"widget-box " + getSizeClass() + "\">");
		buffer.append("	<div class=\"widget-header\">");
		buffer.append("		<a href=\"#\" data-action=\"collapse\">");
		buffer.append("			<i class=\"ace-icon fa fa-chevron-up\"></i>");
		buffer.append("		</a>");
		buffer.append("		<h4 class=\"widget-title\">" + title + "</h4>");
		//工具条
		buffer.append(getToolbarHtml());
		buffer.append("	</div>");
		buffer.append("	<div class=\"widget-body\">");
		buffer.append("		<div class=\"widget-main\" style=\"" + getTableStyle() + "\">");
		buffer.append(getContent());
		buffer.append("		</div>");
		buffer.append("	</div>");
		buffer.append("</div>");
		buffer.append("</div>");
		return buffer.toString();
	}
	
	@Override
	public String htmlForPrint() {
		return getContent();
	}
}
