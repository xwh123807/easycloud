package org.myfly.platform.core.visualpage.ui.control;

public class HtmlCellRender {
	private String text;
	private boolean isHeader;
	
	public HtmlCellRender(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String html(){
		if (isHeader){
			return "<th>" + this.text + "</th>";
		}else{
			return "<td>" + this.text + "</td>";
		}
	}

	public boolean isHeader() {
		return isHeader;
	}

	public void setHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}
}
