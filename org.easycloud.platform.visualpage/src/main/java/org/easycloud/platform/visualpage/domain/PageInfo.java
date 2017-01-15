package org.easycloud.platform.visualpage.domain;

/**
 * 页面信息，用于在velocity模板显示
 * @author xiangwanhong
 *
 */
public class PageInfo {
	/**
	 * 页面主标题
	 */
	private String title;
	
	/**
	 * 页面副标题
	 */
	private String subTitle;
	
	/**
	 * 布局名称
	 */
	private String layout;
	
	/**
	 * 内容页面模板
	 */
	private String template;
	/**
	 * 菜单子页面模板
	 */
	private String sideBarTemplate;
	
	/**
	 * 页面脚本
	 */
	private String[] scripts;
	
	/**
	 * 页面路径，默认为根，如http://localhost:9080/
	 */
	private String contextPath = "/";
	
	public PageInfo(){
	}
	
	public PageInfo(String title, String template, String[] scripts){
		this.title = title;
		this.template = template;
		this.scripts = scripts;
	}

	public String[] getScripts() {
		return scripts;
	}

	public void setScripts(String[] scripts) {
		this.scripts = scripts;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public String getSideBarTemplate() {
		return sideBarTemplate;
	}

	public void setSideBarTemplate(String sideBarTemplate) {
		this.sideBarTemplate = sideBarTemplate;
	}
}
