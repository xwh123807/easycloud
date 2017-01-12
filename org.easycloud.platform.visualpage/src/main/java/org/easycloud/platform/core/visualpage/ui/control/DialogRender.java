package org.myfly.platform.core.visualpage.ui.control;

import org.myfly.platform.core.visualpage.ui.BaseRender;

/**
 * 弹出窗口
 * 
 * @author xiangwanhong
 *
 */
public class DialogRender implements BaseRender {
	private String contentBody;

	@Override
	public String html() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<div class='modal fade' role='dialog'>");
		buffer.append("<div class='modal-dialog' style='width:80%' role='document'>");
		buffer.append("	<div class='modal-content'>");
		buffer.append("		<div class='modal-header'>");
		buffer.append("			<button type='button' class='close' data-dismiss='modal'");
		buffer.append("				aria-label='Close'>");
		buffer.append("				<span aria-hidden='true'>&times;</span>");
		buffer.append("			</button>");
		buffer.append("			<h4 class='modal-title'>${pageInfo.title}</h4>");
		buffer.append("		</div>");
		buffer.append("		<div class='modal-body'>");
		buffer.append(getContentBody());
		buffer.append("     </div>");
		buffer.append("		<div class='modal-footer'>");
		buffer.append("			<button type='button' class='btn btn-default' data-dismiss='modal'>Close</button>");
		buffer.append("			<button type='button' class='btn btn-primary'>Send message</button>");
		buffer.append("		</div>");
		buffer.append("	</div>");
		buffer.append("</div>");
		buffer.append("</div>");
		return buffer.toString();
	}

	public String getContentBody() {
		return contentBody;
	}

	public void setContentBody(String contentBody) {
		this.contentBody = contentBody;
	}

}
