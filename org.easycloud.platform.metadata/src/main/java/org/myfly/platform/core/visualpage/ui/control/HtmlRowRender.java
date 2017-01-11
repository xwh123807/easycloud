package org.myfly.platform.core.visualpage.ui.control;

import java.util.ArrayList;
import java.util.List;

public class HtmlRowRender {
	private List<HtmlCellRender> cells;
	
	public HtmlRowRender() {
	}

	public List<HtmlCellRender> getCells() {
		return cells;
	}

	public void setCells(List<HtmlCellRender> cells) {
		this.cells = cells;
	}
	
	public String html(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("<tr>");
		if (cells != null){
			for (HtmlCellRender cell : cells){
				buffer.append(cell.html());
			}
		}
		buffer.append("</tr>");
		return buffer.toString();
	}
	
	public void addCell(HtmlCellRender cell){
		if (cells == null){
			cells = new ArrayList<>();
		}
		cells.add(cell);
	}
}
