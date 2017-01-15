package org.easycloud.platform.visualpage.ui.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.easycloud.platform.metadata.utils.HtmlUtils;
import org.easycloud.platform.visualpage.ui.StyleConstants;

/**
 * HTML表格
 * 
 * @author xiangwanhong
 *
 */
public class HtmlTableRender {
	/**
	 * 表头
	 */
	private String header;
	/**
	 * 表尾
	 */
	private String footer;
	/**
	 * 标体
	 */
	private List<HtmlRowRender> tbody;
	/**
	 * 扩展属性
	 */
	private Map<String, String> extAttrs;

	/**
	 * 表格样式
	 * 
	 * @return
	 */
	public String getTableClass() {
		return StyleConstants.TABLE_BASE_CLASS;
	}

	/**
	 * 表头
	 * 
	 * @return
	 */
	public String headerHtml() {
		return header == null ? "" : header;
	}

	/**
	 * 表尾
	 * 
	 * @return
	 */
	public String footerHtml() {
		return footer == null ? "" : footer;
	}

	/**
	 * 表体
	 * 
	 * @return
	 */
	public String tbodyHtml() {
		StringBuffer buffer = new StringBuffer();
		if (getTbody() != null) {
			buffer.append("<tbody>");
			for (HtmlRowRender row : getTbody()) {
				buffer.append(row.html());
			}
			buffer.append("</tbody>");
		}
		return buffer.toString();
	}

	public String html() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<table " + getExtAttrs() + HtmlUtils.addPropertys(new String[] { "class", "style" },
				new String[] { getTableClass(), "width:100%" }) + " >");
		buffer.append(headerHtml());
		buffer.append(tbodyHtml());
		buffer.append(footerHtml());
		buffer.append("</table>");
		return buffer.toString();
	}

	private String getExtAttrs() {
		String result = "";
		if (extAttrs != null) {
			for (Entry<String, String> attr : extAttrs.entrySet()) {
				result += attr.getKey() + "=\"" + attr.getValue() + "\" ";
			}
		}
		return result;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public List<HtmlRowRender> getTbody() {
		return tbody;
	}

	public void setTbody(List<HtmlRowRender> tbody) {
		this.tbody = tbody;
	}

	/**
	 * 额外属性
	 * 
	 * @param name
	 * @param value
	 */
	public void addExtAttr(String name, String value) {
		if (extAttrs == null) {
			extAttrs = new HashMap<>();
		}
		this.extAttrs.put(name, value);
	}
}
