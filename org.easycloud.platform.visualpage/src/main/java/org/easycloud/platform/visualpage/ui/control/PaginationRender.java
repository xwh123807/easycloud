package org.easycloud.platform.visualpage.ui.control;

import org.easycloud.platform.visualpage.ui.BaseRender;

/**
 * 分页控件
 * 
 * @author xiangwanhong
 *
 */
public class PaginationRender implements BaseRender {

	private String entityName;

	public PaginationRender(String entityName) {
		this.entityName = entityName;
	}

	@Override
	public String html() {
		return "<div class='pager' entityName='" + entityName
				+ "' total='$!{pager.recordsTotal}' totalPages='$!{pager.totalPages}' page='$!{pager.page}' size='$!{pager.size}' />"
				+ "<script src='${pageInfo.contextPath}assets/js/pager/jquery.twbsPagination.min.js'></script>";
	}

}
