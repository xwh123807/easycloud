package org.easycloud.platform.visualpage.ui.control;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.easycloud.platform.metadata.annotation.entity.FieldDataType;
import org.easycloud.platform.metadata.define.entity.FieldDefinition;
import org.easycloud.platform.metadata.utils.AssertUtil;
import org.easycloud.platform.metadata.utils.StringUtil;

/**
 * 表格控件
 * 
 * @author xiangwanhong
 *
 */
public class TableRender {
	/**
	 * 行定义
	 */
	private Tr row;
	/**
	 * 字段列表，按此生成表格列
	 */
	private FieldDefinition[] fields;
	/**
	 * 自定义列头，如果没有指定则动态生成
	 */
	private String header;
	/**
	 * 如果是子表，指定子表字段
	 */
	private String subTableField;
	/**
	 * 需要显示链接的字段
	 */
	private String linkField;
	/**
	 * 链接显示的内容
	 */
	private String linkUrl;
	/**
	 * 服务端分页取数模式
	 */
	private boolean serverSideMode;

	/**
	 * 
	 * @param fields
	 *            字段列表
	 * @param header
	 * @param subTableField
	 */
	public TableRender(FieldDefinition[] fields, String header, String subTableField) {
		AssertUtil.parameterEmpty(fields, "TableRender.fields");
		this.fields = fields;
		this.header = header;
		this.subTableField = subTableField;
		row = new Tr(this);
		for (int i = 0; i < fields.length; i++) {
			row.getTds().add(new Td(this, this.fields[i]));
		}
	}

	public String html() {
		StringBuffer buffer = new StringBuffer();
		String dataTableClass = isServerSideMode() ? "dataTable-ServerSide" : "dataTable";
		buffer.append(
				"<table class=\"table table-bordered table-striped " + dataTableClass + "\" tableName=\"${table}\">");
		if (StringUtil.isNotEmpty(header)) {
			buffer.append("<thead>").append(header).append("</thead>");
		} else {
			buffer.append("<thead>").append(row.html(false)).append("</thead>");
		}
		if (!isServerSideMode()) {
			// 为客户端取数模式时，一次性在服务端输出全部数据
			buffer.append("<tbody>").append(row.html(true)).append("</tbody>");
		}
		buffer.append("</table>");
		return buffer.toString();
	}

	public String getLinkField() {
		return linkField;
	}

	public void setLinkField(String linkField) {
		this.linkField = linkField;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public boolean isServerSideMode() {
		return serverSideMode;
	}

	public void setServerSideMode(boolean serverSideMode) {
		this.serverSideMode = serverSideMode;
	}

	public static class Tr {
		private List<Td> tds = new ArrayList<>();
		private TableRender table;

		public Tr(TableRender table) {
			this.table = table;
		}

		public String html(boolean isBody) {
			StringBuffer buffer = new StringBuffer();
			if (isBody) {
				if (table != null && StringUtil.isNotEmpty(table.subTableField)) {
					buffer.append("#foreach($objitem in $obj." + table.subTableField + ")");
				} else {
					buffer.append("#foreach($objitem in $objlist)");
				}
			}
			buffer.append("<tr>");
			for (Td td : getTds()) {
				buffer.append(isBody ? td.tbody() : td.thead());
			}
			buffer.append("</tr>");
			if (isBody) {
				buffer.append("#end");
			}
			return buffer.toString();
		}

		public List<Td> getTds() {
			return tds;
		}

		public void setTds(List<Td> tds) {
			this.tds = tds;
		}
	}

	public static class Td {
		private FieldDefinition field;
		private TableRender table;

		public Td(TableRender table, FieldDefinition field) {
			this.table = table;
			this.field = field;
		}

		public Td(FieldDefinition field) {
			this.field = field;
		}

		public String thead() {
			return "<th>" + field.getLabel() + "</th>";
		}

		public String tbody() {
			String attrName = field.getName();
			// 是否为关联字段，如果是关联字段需要显示大纲视图链接
			boolean isLink = FieldDataType.SEARCHRELATION.equals(field.getDataType());
			if (table != null && attrName.equalsIgnoreCase(table.linkField)) {
				// 需要显示链接的字段
				return "<td><a href=\"" + table.linkUrl + "\">$!{objitem." + attrName + "}</a></td>";
			} else if (isLink && (field.getRelationTable() != null)) {
				String id = field.getRelationTable() + "/$!{objitem." + attrName + ".uid}";
				String value = "$!{objitem." + attrName + ".name}";
				String tpl = "<td><a href=\"/vp/{0}\" data-outline=\"/vp/outline/{0}\" title=\"/vp/outline/{0}\" "
						+ " data-toggle=\"tooltip\" data-placement=\"bottom\" target=\"_blank\" >{1}</a></td>";
				// 关联字段
				return MessageFormat.format(tpl, id, value);
			} else if (FieldDataType.SYSENUM.equals(field.getDataType())) {
				// 枚举类型
				return "<td>$!{objitem." + attrName + ".getName()}</td>";
			} else if (field.getGetValueHandler() != null) {
				// 有指定值
				return "<td>" + field.getGetValueHandler().getFieldValue("${objitem.uid}") + "</td>";
			} else {
				// 普通字段
				return "<td>$!{objitem." + attrName + "}</td>";
			}
		}
	}
}
