package org.easycloud.platform.visualpage.ui.control;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.metadata.define.view.DivDefinition;
import org.easycloud.platform.metadata.utils.HtmlUtils;
import org.easycloud.platform.visualpage.ui.BaseRender;
import org.easycloud.platform.visualpage.ui.view.SectionViewRender;

/**
 * 布局显示
 * 
 * <div class='col-md-{width} {extClass}' divType='{divType}' name='{name}'>
 * </div>
 * 
 * @author xiangwanhong
 *
 */
public class DivsRender implements BaseRender {

	private DivDefinition[] divs;

	private Map<String, SectionViewRender> secitonRenderMaps = new HashMap<>();

	public DivsRender(DivDefinition[] divs, SectionViewRender[] secitonRenders) {
		this.divs = divs;
		if (ArrayUtils.isNotEmpty(secitonRenders)) {
			for (SectionViewRender render : secitonRenders) {
				secitonRenderMaps.put(render.getSectionDefinition().getName(), render);
			}
		}
	}

	@Override
	public String html() {
		StringBuffer buffer = new StringBuffer();
		// TODO 标记为row时，子节点折行了
		buffer.append("<div" + HtmlUtils.addProperty("render", getClass().getName()) + ">");
		buffer.append(divRenderHtml(divs));
		buffer.append("</div>");
		return buffer.toString();
	}

	/**
	 * 递归处理布局
	 * 
	 * @param divs
	 * @return
	 */
	private String divRenderHtml(DivDefinition[] divs) {
		StringBuffer buffer = new StringBuffer();
		for (DivDefinition divDefinition : divs) {
			String extClass = StringUtils.isEmpty(divDefinition.getExtClass()) ? "" : divDefinition.getExtClass();
			buffer.append("<div class='");
			if (divDefinition.getWidth() != 12) {
				buffer.append("col-md-" + divDefinition.getWidth() + " ");
			}
			buffer.append(extClass + "'" + HtmlUtils.addProperty("divType", divDefinition.getDivType().name())
					+ HtmlUtils.addProperty("name", divDefinition.getName()) + ">");
			// 如果有名称属性，则不能下级div
			if (StringUtils.isNotBlank(divDefinition.getName())) {
				SectionViewRender sectionRender = secitonRenderMaps.get(divDefinition.getName());
				if (sectionRender != null) {
					// 增加显示内容
					buffer.append(sectionRender.html());
				}
			} else {
				// 获取子节点的布局定义
				if (ArrayUtils.isNotEmpty(divDefinition.getSubs())) {
					buffer.append(divRenderHtml(divDefinition.getSubs()));
				}
			}
			buffer.append("</div>");
		}
		return buffer.toString();
	}
}
