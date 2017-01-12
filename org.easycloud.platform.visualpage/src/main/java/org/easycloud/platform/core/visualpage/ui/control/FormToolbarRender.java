package org.myfly.platform.core.visualpage.ui.control;

import org.myfly.platform.core.metadata.define.FormDefinition;
import org.myfly.platform.core.utils.EntityUrlUtil;
import org.myfly.platform.core.utils.HtmlUtils;
import org.myfly.platform.core.visualpage.ui.BaseButtonRender;
import org.myfly.platform.core.visualpage.ui.BaseFieldRender;
import org.myfly.platform.core.visualpage.ui.ButtonRenderFactory;
import org.myfly.platform.core.visualpage.ui.EntityAction;

/**
 * 表单工具条
 * 
 * <button class="btn btn-white btn-info btn-bold"> <i class=
 * "ace-icon fa fa-floppy-o bigger-120 blue"></i> Backup </button>
 * 
 * @author xiangwanhong
 *
 */
public class FormToolbarRender extends BaseFieldRender {
	private FormDefinition formDefinition;
	private String formViewName;

	public FormToolbarRender(final FormDefinition formDefinition, final String formViewName) {
		this.formDefinition = formDefinition;
		this.formViewName = formViewName;
	}

	@Override
	public String htmlForView() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<div" + HtmlUtils.addPropertys(new String[] { "class", "role", "render" },
				new String[] { "btn-toolbar col-sm-12", "toolbar", getClass().getName() }) + ">");
		buffer.append("  <div class=\"btn-group\" role=\"group\">");
		String tableName = formDefinition.getParent().getEntityName();
		for (EntityAction action : EntityAction.getActionsByViewType(getViewType())) {
			String url = EntityUrlUtil.getEntityActionUrl(action, tableName, "$!{uid}", formViewName);
			String subUrl = EntityUrlUtil.getSubEntityActionUrl(action, tableName, "$!{uid}", "$!{subTableAttr}",
					"$!{subUid}", formViewName);
			// TODO
			// 子表的URL没有处理，解决思路是将subuid、subattr写入velocity上下文环境，在url中判断subattr是否为空
//			BaseButtonRender button = ButtonRenderFactory.getRender(action, "#if ($subTableAttr==\"\") #set ($url=\""
//					+ url + "\") else #set ($url=\"" + subUrl + "\") #end $!{url}");
			
			BaseButtonRender button = ButtonRenderFactory.getRender(action, url);
			buffer.append(button.html());
		}
		buffer.append("  </div>");
		buffer.append("</div>");
		return buffer.toString();
	}
	
	@Override
	public String htmlForEdit() {
		return htmlForView();
	}
	
	@Override
	public String htmlForPrint() {
		return "";
	}
}
