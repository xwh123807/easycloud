package org.myfly.platform.core.visualpage.ui.control;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.myfly.platform.core.user.domain.Menu;
import org.myfly.platform.core.utils.HtmlUtils;
import org.myfly.platform.core.visualpage.ui.BaseRender;

public class SideBarRender implements BaseRender {
	private Menu[] menus;

	public SideBarRender(Menu[] menus) {
		this.menus = menus;
	}

	public String html() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<ul" + HtmlUtils.addPropertys(new String[] { "class", "render" },
				new String[] { "nav nav-list", getClass().getName() }) + ">").append("\n");
		for (Menu menu : menus) {
			buffer.append(htmlForMenu(menu));
		}
		buffer.append("</ul>");
		return buffer.toString();
	}

	private String htmlForMenu(Menu menu) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getMenuItemHtml(menu)).append("\n");
		if (CollectionUtils.isNotEmpty(menu.getSubMenus())) {
			buffer.append("<ul class='submenu'>").append("\n");
			for (Menu item : menu.getSubMenus()) {
				buffer.append(getMenuItemHtml(item)).append("\n");
			}
			buffer.append("</ul>").append("\n");
		}
		return buffer.toString();
	}

	private String getMenuItemHtml(Menu menu) {
		String html = "";
		if (CollectionUtils.isNotEmpty(menu.getSubMenus())) {
			// 有子菜单
			html = "<li class=''><a href='#' class='dropdown-toggle'> <i class='menu-icon " + menu.getIcon() + "'></i>"
					+ "<span class='menu-text'>" + menu.getName()
					+ "</span> <b class='arrow fa fa-angle-down'></b></a> <b class='arrow'></b>";
		} else {
			// 无下级菜单
			html = "<li class=''><a ";
			if (StringUtils.isNotBlank(menu.getUrl()) && menu.getUrl().startsWith("javascript:")) {
				//脚本模式
				html += "href='#' onclick=\"" + menu.getUrl() + ";return false\"";
			} else {
				html += "href='" + menu.getUrl() + "'";
			}
			html += "> <i class='menu-icon " + menu.getIcon() + "'></i>" + "<span class='menu-text'>" + menu.getName()
					+ "</span></a> <b class='arrow'></b></li>";
		}
		return html;
	}
}
