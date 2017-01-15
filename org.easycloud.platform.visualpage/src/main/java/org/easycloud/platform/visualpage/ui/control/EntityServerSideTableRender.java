package org.easycloud.platform.visualpage.ui.control;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.metadata.annotation.entity.FieldDataType;
import org.easycloud.platform.metadata.define.entity.FieldDefinition;
import org.easycloud.platform.metadata.define.view.ListDefinition;
import org.easycloud.platform.visualpage.ui.StyleConstants;
import org.easycloud.platform.visualpage.ui.ViewType;

/**
 * 服务端表格，只包含格式定义，不包含数据
 * 
 * @author xiangwanhong
 *
 */
public class EntityServerSideTableRender extends HtmlTableRender {
	private ListDefinition listDefinition;

	private ViewType viewType;

	/**
	 * 
	 * @param viewType
	 * @param fields
	 * @param tableName
	 * @param subTableAttr
	 */
	public EntityServerSideTableRender(ListDefinition listDefinition, ViewType viewType) {
		this.setListDefinition(listDefinition);
		setViewType(viewType);
		addExtAttr("entityName", listDefinition.getEntityName());
		addExtAttr("view", listDefinition.getName());
		addExtAttr("render", getClass().getName());
		if (listDefinition.isSubTableScene()) {
			// 子表应用场景时，在table上增加属性subTableAttr、uid
			addExtAttr("subTableAttr", listDefinition.getSubTableAttr());
			addExtAttr("uid", "$!{uid}");
		}
	}

	@Override
	public String getTableClass() {
		return StyleConstants.TABLE_BASE_CLASS + " " + StyleConstants.TABLE_SERVER_SIDE_CLASS;
	}

	/**
	 * 根据字段定义，获取列头定义
	 * 
	 * @return
	 */
	public String getRowHeader() {
		HtmlRowRender row = new HtmlRowRender();
		List<HtmlCellRender> cells = new ArrayList<>();
		for (FieldDefinition field : getListDefinition().getFields()) {
			if (ViewType.PRINT.equals(getViewType()) && FieldDataType.ACTIONS.equals(field.getDataType())) {
				continue;
			} else {
				HtmlCellRender cell = new HtmlCellRender(field.getLabel());
				cell.setHeader(true);
				cells.add(cell);
			}
		}
		row.setCells(cells);
		return row.html();
	}

	@Override
	public String headerHtml() {
		if (StringUtils.isNotBlank(getHeader())) {
			return getHeader();
		} else {
			return "<thead>" + getRowHeader() + "</thead>";
		}
	}

	@Override
	public String footerHtml() {
		if (StringUtils.isNotBlank(getFooter())) {
			return getFooter();
		} else {
			return "<tfoot>" + getRowHeader() + "</tfoot>";
		}
	}

	/**
	 * 根据isServerSide获取表格
	 * 
	 * @param viewType
	 * 
	 * @param isServerSide
	 *            是否为Server端表格
	 * @param fields
	 * @param tableName
	 * @param subTableAttr
	 * @return
	 */
	public static EntityServerSideTableRender getEntityTableRender(ListDefinition listDefinition, ViewType viewType) {
		EntityServerSideTableRender render;
		if (ViewType.PRINT.equals(viewType)) {
			render = new PrintTableRender(listDefinition, viewType);
		} else {
			render = !listDefinition.isServerSideMode() ? new VelocityTemplateTableRender(listDefinition, viewType)
					: new EntityServerSideTableRender(listDefinition, viewType);
		}
		return render;
	}

	public ListDefinition getListDefinition() {
		return listDefinition;
	}

	public void setListDefinition(ListDefinition listDefinition) {
		this.listDefinition = listDefinition;
	}

	public ViewType getViewType() {
		return viewType;
	}

	public void setViewType(ViewType viewType) {
		this.viewType = viewType;
	}
}
