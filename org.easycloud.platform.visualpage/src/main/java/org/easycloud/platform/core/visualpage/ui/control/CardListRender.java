package org.myfly.platform.core.visualpage.ui.control;

import org.myfly.platform.core.metadata.define.ListDefinition;
import org.myfly.platform.core.utils.HtmlUtils;
import org.myfly.platform.core.visualpage.ui.BaseRender;

/**
 * 卡片式列表
 * 
 * @author xiangwanhong
 *
 */
public class CardListRender implements BaseRender {
	private ListDefinition listDefinition;

	public CardListRender(ListDefinition listDefinition) {
		this.listDefinition = listDefinition;
	}

	@Override
	public String html() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<ul" + HtmlUtils.addPropertys(new String[] { "class", "render" },
				new String[] { "list-unstyled", getClass().getName() }) + ">");
		if (listDefinition.isSubTableScene()) {
			//子表场景下，在脚本中取数
			buffer.append("#set ($tmp = $utils.getDataUtil().findAllForSubEntity(\"" + listDefinition.getEntityName()
					+ "\", \"$uid\", \"" + listDefinition.getSubTableAttr() + "\", \""
					+ listDefinition.getName() + "\", null, false))");
			buffer.append("#foreach($obj in $tmp)");
		} else {
			//数据由调用环境传入
			buffer.append("#foreach($obj in $objlist)");
		}
		buffer.append("<li><div class='col-lg-3 col-md-4 col-sm-6 col-xs-12'><div class='thumbnail'>");
		CardRender cardRender = new CardRender(listDefinition);
		buffer.append(cardRender.html());
		buffer.append("</div></div></li>");
		buffer.append("#end");
		buffer.append("</ul>");
		buffer.append("<div class='col-xs-12'>");
		buffer.append(new PaginationRender(listDefinition.getEntityName()).html());
		buffer.append("</div>");
		return buffer.toString();
	}
}
