package org.easycloud.platform.core.utils;

import java.text.MessageFormat;

import javax.swing.text.StyleConstants;

import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.core.domain.EntityAction;

public class EntityLinkUtil {
	private static final String LINK_URL_TEMPLATE = "<a href=\"{0}\" target=\"{1}\" title=\"{2}\">{3}</a>";

	/**
	 * 获取链接HTML
	 * 
	 * @param url
	 * @param text
	 * @param title
	 * @param iconClass
	 * @param isNewWindow
	 * @return
	 */
	private static String getLinkHtml(final String url, final String text, final String title, String iconClass,
			boolean isNewWindow) {
		AssertUtil.parameterEmpty(url, "url");
		String target = isNewWindow ? "_blank" : "";
		String linkText = StringUtils.isNotBlank(iconClass) ? "<i class=\"" + iconClass + "\"></i>" : " " + text;
		return MessageFormat.format(LINK_URL_TEMPLATE, url, target, title, linkText);
	}

	/**
	 * 获取实体操作链接HTML
	 * 
	 * @param viewType
	 * @param tableName
	 * @param uid
	 * @param text
	 * @param iconMode
	 * @param isNewWindow
	 * @return
	 */
	public static String getEntityActionLinkHtml(EntityAction action, String tableName, String uid, String text, String view, boolean iconMode,
			boolean isNewWindow) {
		final String url = EntityUrlUtil.getEntityActionUrl(action, tableName, uid, view);
		final String iconClass = StyleConstants.ENTITY_ACTION_ICON_CLASS_MAP.get(action);
		return getLinkHtml(url, text, action.getTitle(), iconMode ? iconClass : null, isNewWindow);
	}

	/**
	 * 获取实体子表操作链接HTML
	 * 
	 * @param viewType
	 * @param tableName
	 * @param uid
	 * @param subTableAttr
	 * @param subUid
	 * @param text
	 * @param iconMode
	 * @param isNewWindow
	 * @return
	 */
	public static String getSubEntityActionLinkHtml(EntityAction action, String tableName, String uid,
			String subTableAttr, String subUid, String text, String view, boolean iconMode, boolean isNewWindow) {
		final String url = EntityUrlUtil.getSubEntityActionUrl(action, tableName, uid, subTableAttr, subUid, view);
		final String iconClass = StyleConstants.ENTITY_ACTION_ICON_CLASS_MAP.get(action);
		return getLinkHtml(url, text, action.getTitle(), iconMode ? iconClass : null, isNewWindow);
	}

	/**
	 * 实体查找关系Link Html，用于表格中显示查找关系名称
	 * 
	 * @param tableName
	 * @param attrName
	 *            查找关系属性名称
	 * @param uid
	 * @param text
	 * @return
	 */
	public static String getEntitySearchRelationLinkHtml(String tableName, String attrName, String uid, String text) {
		return "<a href=\"" + EntityUrlUtil.getEntityActionUrl(EntityAction.VIEW, tableName, uid, null) + "\" data-outline=\""
				+ EntityUrlUtil.getEntityActionUrl(EntityAction.OUTLINE, tableName, uid, null)
				+ "\" title=\"简档\" data-toggle=\"tooltip\" data-placement=\"bottom\" target=\"_blank\" >" + text
				+ "</a>";
	}

}
