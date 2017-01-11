package org.myfly.platform.core.visualpage.ui.view;

import org.myfly.platform.core.utils.UUIDUtil;
import org.myfly.platform.core.visualpage.ui.ViewMode;
import org.myfly.platform.core.visualpage.ui.ViewType;

/**
 * 视图基类，可以独立返回到前端显示
 * @author xiangwanhong
 *
 */
public class BaseViewRender extends BaseRender{

	private ViewMode viewMode;

	public BaseViewRender(final ViewType viewType, final ViewMode viewMode) {
		super(viewType);
		this.viewMode = viewMode;
	}

	public ViewMode getViewMode() {
		return viewMode;
	}
	
	public String html() {
		StringBuffer buffer = new StringBuffer();
		switch (getViewMode()) {
		case MODALPAGE:
			contentForModalPage(buffer);
			break;
		default:
			contentForSubPage(buffer);
			break;
		}
		return buffer.toString();
	}
	
	/**
	 * 模态窗口模式
	 * 
	 * @param buffer
	 */
	private void contentForModalPage(StringBuffer buffer) {
		buffer.append("<div id='divModal" + UUIDUtil.newUUID() + "' class='modal fade' role='dialog'>");
		buffer.append("		<div class='modal-dialog' style='width:80%' role='document'>");
		buffer.append("			<div class='modal-content'>");
		buffer.append("				<div class='modal-header'>");
		buffer.append("					<button type='button' class='close' data-dismiss='modal'");
		buffer.append("						aria-label='Close'>");
		buffer.append("						<span aria-hidden='true'>&times;</span>");
		buffer.append("					</button>");
		buffer.append("					<h4 class='modal-title'>${pageInfo.title}</h4>");
		buffer.append("				</div>");
		buffer.append("				<div class='modal-body'>");
		buffer.append((ViewType.PRINT.equals(getViewType())) ? htmlForPrint() : htmlForView());
		buffer.append("				</div>");
		buffer.append("				<div class='modal-footer'>");
		buffer.append(
				"					<button type='button' class='btn btn-default' data-dismiss='modal'>关闭</button>");
		buffer.append(getModalAction());
		buffer.append("				</div>");
		buffer.append("			</div>");
		buffer.append("		</div>");
		buffer.append("	</div>");
	}

	/**
	 * 子页面显示模式
	 * 
	 * @param buffer
	 */
	public void contentForSubPage(StringBuffer buffer) {
		buffer.append("<div class='page-header'>");
		buffer.append("	<h1>");
		buffer.append("		$!{pageInfo.title} <small> <i");
		buffer.append("			class='ace-icon fa fa-angle-double-right'></i> $!{pageInfo.subTitle}");
		buffer.append("		</small>");
		buffer.append("	</h1>");
		buffer.append("</div>	");
		buffer.append("#parse(\"${pageInfo.layout}/portal/alert.vm\")");
		buffer.append("<div class='row'>");
		buffer.append((ViewType.PRINT.equals(getViewType())) ? htmlForPrint() : htmlForView());
		buffer.append("</div>");
	}
	
	/**
	 * 获取模态窗口操作
	 * @return
	 */
	public String getModalAction(){
		return "";
	}
}
