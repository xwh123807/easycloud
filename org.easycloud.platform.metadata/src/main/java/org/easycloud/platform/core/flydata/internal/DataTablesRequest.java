package org.easycloud.platform.core.flydata.internal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.myfly.platform.core.flydata.service.Search;

/**
 * 
 * draw:42 order[0][column]:1 order[0][dir]:asc start:10 length:10
 * search[value]: search[regex]:false _:1449996660831
 * 
 * @author xiangwanhong
 *
 */
public class DataTablesRequest {
	private int draw;
	private int start;
	private int length;
	private Search search;

	/**
	 * 从Request中获取参数
	 * 
	 * @param request
	 */
	public DataTablesRequest(HttpServletRequest request) {
		if (request != null) {
			if (StringUtils.isNotBlank(request.getParameter("draw"))){
				setDraw(Integer.parseInt(request.getParameter("draw")));
			}else{
				setDraw(1);
			}
			if (StringUtils.isNotBlank(request.getParameter("length"))){
				setLength(Integer.parseInt(request.getParameter("length")));
			}else{
				setLength(20);
			}
			if (StringUtils.isNoneBlank(request.getParameter("start"))){
				setStart(Integer.parseInt(request.getParameter("start")));
			}else{
				setStart(0);
			}
			if (StringUtils.isNoneBlank(request.getParameter("search[value]"))){
				setSearch(new Search(request.getParameter("search[value]")));
			}else{
				setSearch(null);
			}
		}else{
			setDraw(1);
			setLength(20);
			setStart(0);
			setSearch(null);
		}
	}

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
	}
}
