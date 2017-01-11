package org.myfly.platform.core.visualpage.ui.control;

import org.myfly.platform.core.utils.HtmlUtils;
import org.myfly.platform.core.visualpage.ui.BaseRender;

/**
 * 搜索框
 * 
 * @author xiangwanhong
 *
 */
public class SearchBoxRender implements BaseRender {

	@Override
	public String html() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<div" + HtmlUtils.addPropertys(new String[] { "class", "render" },
				new String[] { "col-xs-12 header blue", getClass().getName() }) + ">");
		buffer.append("<form method='GET'>");
		buffer.append("<div class='col-md-3'></div>");
		buffer.append("<div class='col-md-6'>");
		buffer.append("	<div class='input-group'>");
		buffer.append(
				"		<input id='entitySearchText' name='q' type='text' class='form-control' placeholder='Search for...' value='$!{obj.q}'>");
		buffer.append(" 	<span class='input-group-btn'>");
		buffer.append(
				" 		<button class='btn btn-info btn-sm' type='submit'><i class='ace-icon fa fa-search icon-on-right bigger-110'></i></button>");
		buffer.append(" 	</span></div>");
		buffer.append("	</div>");
		buffer.append("<div class='col-md-3'></div>");
		buffer.append("</form>");
		buffer.append("</div>");
		return buffer.toString();
	}

}
