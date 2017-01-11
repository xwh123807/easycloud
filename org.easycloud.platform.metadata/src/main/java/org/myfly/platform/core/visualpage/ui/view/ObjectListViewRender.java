package org.myfly.platform.core.visualpage.ui.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.myfly.platform.core.domain.FieldDataType;
import org.myfly.platform.core.metadata.annotation.ObjectListView;
import org.myfly.platform.core.metadata.annotation.PropertyView;
import org.myfly.platform.core.metadata.define.FieldDefinition;
import org.myfly.platform.core.utils.AssertUtil;
import org.myfly.platform.core.utils.HtmlUtils;
import org.myfly.platform.core.visualpage.ui.control.TableRender;

/**
 * 列表视图
 * 
 * @author xiangwanhong
 *
 */
public class ObjectListViewRender {
	private Log log = LogFactory.getLog(getClass());
	private TableRender tableRender;
	private ObjectListView objectBean;

	public ObjectListViewRender(ObjectListView objectBean) {
		this.objectBean = objectBean;
		AssertUtil.parameterEmpty(this.objectBean, "ListViewRender.objectBean");
		List<FieldDefinition> fields = new ArrayList<>();
		String header = null;
		for (PropertyView property : objectBean.fields()) {
			FieldDefinition Field = new FieldDefinition(property.title(), property.name(), FieldDataType.TEXT);
			fields.add(Field);
		}
		tableRender = new TableRender(fields.toArray(new FieldDefinition[] {}), header, null);
		tableRender.setLinkField(objectBean.linkField());
		tableRender.setLinkUrl(objectBean.linkUrl());
	}

	public String html() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<div" + HtmlUtils.addPropertys(new String[] { "class", "render" },
				new String[] { "row", getClass().getName() }) + ">");
		buffer.append("<div class='col-xs-12'>");
		buffer.append(tableRender.html());
		buffer.append("</div>");
		buffer.append("</div>");
		return buffer.toString();
	}
}
