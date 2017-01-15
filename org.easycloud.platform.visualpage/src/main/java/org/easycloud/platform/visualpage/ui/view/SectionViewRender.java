package org.easycloud.platform.visualpage.ui.view;

import org.apache.commons.lang3.ArrayUtils;
import org.easycloud.platform.metadata.define.view.SectionDefinition;
import org.easycloud.platform.metadata.utils.AssertUtil;
import org.easycloud.platform.metadata.utils.HtmlUtils;
import org.easycloud.platform.visualpage.ui.ViewType;
import org.easycloud.platform.visualpage.ui.WidgetBoxRender;
import org.easycloud.platform.visualpage.ui.control.component.SocialRender;

/**
 * 
 * @author xiangwanhong
 *
 */
public class SectionViewRender extends BaseRender {
	private FieldSetViewRender[] fieldSets;
	private SubTableViewRender[] subTables;
	private SectionDefinition sectionDefinition;

	public SectionViewRender(final SectionDefinition sectionDefinition, final ViewType viewType) {
		super(viewType);
		this.sectionDefinition = sectionDefinition;
		AssertUtil.parameterEmpty(sectionDefinition, "SectionRender.sectionDefinition");
		if (ArrayUtils.isNotEmpty(sectionDefinition.getFieldSets())) {
			fieldSets = new FieldSetViewRender[sectionDefinition.getFieldSets().length];
			for (int i = 0; i < sectionDefinition.getFieldSets().length; i++) {
				fieldSets[i] = new FieldSetViewRender(sectionDefinition.getFieldSets()[i], getViewType());
			}
		}
		if (ViewType.EDIT.equals(viewType) || ViewType.NEW.equals(viewType)) {
			// 新增和编辑模式不显示子表
		} else if (ArrayUtils.isNotEmpty(sectionDefinition.getSubTables())) {
			subTables = new SubTableViewRender[sectionDefinition.getSubTables().length];
			for (int i = 0; i < sectionDefinition.getSubTables().length; i++) {
				subTables[i] = new SubTableViewRender(sectionDefinition.getSubTables()[i], getViewType());
			}
		}
	}

	public SectionDefinition getSectionDefinition() {
		return sectionDefinition;
	}

	public String html() {
		StringBuffer sectionHtml = new StringBuffer();
		sectionHtml
				.append("<div"
						+ HtmlUtils.addPropertys(new String[] { "name", "class", "render" }, new String[] {
								sectionDefinition.getName(), sectionDefinition.getType().name(), getClass().getName() })
				+ ">");
		switch (sectionDefinition.getType()) {
		case CUSTOM:
		case NOTE:
		case ATTACHMENT:
			// 自定义部件
			if (fieldSets != null && fieldSets.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (FieldSetViewRender fieldSet : fieldSets) {
					buffer.append(fieldSet.html());
				}
				WidgetBoxRender box = new WidgetBoxRender(sectionDefinition.getTitle(), getViewType());
				box.setContent(buffer.toString());
				sectionHtml.append(box.html());
			}
			if (ArrayUtils.isNotEmpty(subTables)) {
				StringBuffer buffer = new StringBuffer();
				// 每个子表都显示在独立的WidgetBox中
				for (SubTableViewRender subTable : subTables) {
					buffer.append(subTable.html());
				}
				sectionHtml.append(buffer.toString());
			}
			break;
		case SOCIAL:
			SocialRender render = new SocialRender(getViewType());
			sectionHtml.append(render.html());
			break;
		default:
			break;
		}
		sectionHtml.append("</div>");
		return sectionHtml.toString();
	}
}
