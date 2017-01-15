package org.easycloud.platform.visualpage.ui.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easycloud.platform.metadata.annotation.entity.FieldDataType;
import org.easycloud.platform.metadata.annotation.view.ObjectListView;
import org.easycloud.platform.metadata.annotation.view.PropertyView;
import org.easycloud.platform.metadata.define.entity.FieldDefinition;
import org.easycloud.platform.metadata.utils.AssertUtil;
import org.easycloud.platform.metadata.utils.HtmlUtils;
import org.easycloud.platform.visualpage.ui.control.TableRender;

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
