package org.easycloud.platform.visualpage.ui.control;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easycloud.platform.metadata.annotation.entity.FieldDataType;
import org.easycloud.platform.metadata.annotation.view.EntityAction;
import org.easycloud.platform.metadata.define.entity.FieldDefinition;
import org.easycloud.platform.metadata.define.view.ListDefinition;
import org.easycloud.platform.visualpage.ui.EntityActionInfo;
import org.easycloud.platform.visualpage.ui.StyleConstants;
import org.easycloud.platform.visualpage.ui.ViewType;

/**
 * DataTable Veloctity表格模板
 * 
 * @author xiangwanhong
 *
 */
public class VelocityTemplateTableRender extends EntityServerSideTableRender {
	private static Log log = LogFactory.getLog(VelocityTemplateTableRender.class);

	public VelocityTemplateTableRender(ListDefinition listDefinition, ViewType viewType) {
		super(listDefinition, viewType);
	}

	public VelocityTemplateTableRender(FieldDefinition[] fields, String tableName, String subTableAttr) {
		super(new ListDefinition(fields, tableName, subTableAttr), ViewType.VIEW);
	}

	@Override
	public String getTableClass() {
		return StyleConstants.TABLE_BASE_CLASS + " " + StyleConstants.TABLE_CLIENT_SIDE_CLASS;
	}

	@Override
	public String tbodyHtml() {
		StringBuffer buffer = new StringBuffer();
		if (getListDefinition().isSubTableScene()) {
			buffer.append("#set ($tmp = $utils.getDataUtil().findAllForSubEntity(\""
					+ getListDefinition().getEntityName() + "\", \"$uid\", \"" + getListDefinition().getSubTableAttr()
					+ "\", \"" + getListDefinition().getName() + "\", null, false))");
			buffer.append("#foreach($objitem in $tmp)");
		} else {
			buffer.append("#foreach($objitem in $objlist)");
		}
		buffer.append(getBodyRowTemplate());
		buffer.append("#end");
		return buffer.toString();
	}

	/**
	 * 获取表体模板
	 * 
	 * @return
	 */
	private String getBodyRowTemplate() {
		HtmlRowRender row = new HtmlRowRender();
		boolean isPrint = ViewType.PRINT.equals(getViewType());
		for (FieldDefinition field : getListDefinition().getFields()) {
			if (isPrint && FieldDataType.ACTIONS.equals(field.getDataType())) {
			} else {
				HtmlCellRender cell = new HtmlCellRender(getCellText(field));
				row.addCell(cell);
			}
		}
		return row.html();
	}

	private String getCellText(FieldDefinition field) {
		return ViewType.VIEW.equals(getViewType()) ? viewForHtml(field) : printForHtml(field);
	}

	/**
	 * 获取单元格内容
	 * 
	 * @param field
	 * @return
	 */
	private String viewForHtml(FieldDefinition field) {
		EntityActionInfo actionInfo = new EntityActionInfo(getListDefinition().getEntityName(), "$!{objitem.uid}",
				getListDefinition().getSubTableAttr(), "$!{objitem.uid}", "$!{objitem." + field.getName() + "}", null,
				getListDefinition().getName());
		if (getListDefinition().isSubTableScene()) {
			actionInfo.uid = "$!{obj.uid}";
		}
		String text = actionInfo.text;
		if (field.getDataType() != null) {
			if (field.getDataType().equals(FieldDataType.ACTIONS)) {
				if (field.getGetValueHandler() != null) {
					text = (String) field.getGetValueHandler().getFieldValue(actionInfo);
				} else {
					if (log.isWarnEnabled()) {
						log.warn("ACTIONS字段没有设置GetValueHandler，取空值.");
					}
					text = "";
				}
			} else if (field.getDataType().equals(FieldDataType.SEARCHRELATION)) {
				// 查找关系数据类型自动增加实体查看的超链接
				text = EntityLinkUtil.getEntitySearchRelationLinkHtml(field.getRelationTable(), field.getName(),
						"$!{objitem." + field.getName() + ".uid}", "$!{objitem." + field.getName() + ".name}");
			} else if (FieldDataType.SYSENUM.equals(field.getDataType())) {
				text = "$!{objitem." + field.getName() + ".getTitle()}";
			}
		}
		if (StringUtils.isNotBlank(field.getLinkUrl())) {
			text = "<a href=\"" + field.getLinkUrl() + "\">" + text + "</a>";
		} else if (field.getName().equals("name")) {
			String url;
			// 字段名称为Name，自动追加链接
			if (!getListDefinition().isSubTableScene()) {
				// 主实体
				url = EntityUrlUtil.getEntityActionUrl(EntityAction.VIEW, actionInfo.tableName, actionInfo.uid,
						actionInfo.view);
			} else {
				// 子实体
				url = EntityUrlUtil.getSubEntityActionUrl(EntityAction.VIEW, actionInfo.tableName, actionInfo.uid,
						actionInfo.subTableAttr, actionInfo.subUid, actionInfo.view);
			}
			text = "<a href=\"" + url + "\">" + text + "</a>";
		}
		return text;
	}

	/**
	 * 获取单元格内容，打印模式
	 * 
	 * @param field
	 * @return
	 */
	private String printForHtml(FieldDefinition field) {
		String text = "$!{objitem." + field.getName() + "}";
		if (field.getDataType() != null && field.getDataType().equals(FieldDataType.SEARCHRELATION)) {
			text = "$!{objitem." + field.getName() + ".name}";
		}
		return text;
	}
}
