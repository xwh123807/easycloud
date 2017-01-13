package org.easycloud.platform.core.utils;

import java.text.MessageFormat;

import org.easycloud.platform.core.domain.EntityAction;

public class EntityButtonUtil {
	private static final String BUTTON_OPEN_TEMPLATE = "<input type='button' value='{0}' onclick='javascript:openModal(\"{1}\", false)'>";

	public static String getEntityActionButton(EntityAction action, String tableName, String uid, String text,
			String view, boolean iconMode, boolean isNewWindow) {
		String[] params = null;
		String url = EntityUrlUtil.getEntityActionUrl(action, tableName, uid, view);
		switch (action) {
		case ALL:
			break;
		case BACK:
			break;
		case CUSTOM:
			break;
		case DEL:
			break;
		case DELALL:
			break;
		case EDIT:
			break;
		case EXCEL:
			break;
		case EXCELIMPORT:
			break;
		case LIST:
			break;
		case NEW:
			params = new String[]{action.getTitle(), url};
			break;
		case OUTLINE:
			break;
		case PDF:
			break;
		case PRINT:
			break;
		case SAVE:
			break;
		case SAVEANDNEW:
			break;
		case SAVEUPDATE:
			break;
		case SHOWEXCELIMPORT:
			break;
		case VIEW:
			break;
		default:
			break;

		}
		return MessageFormat.format(BUTTON_OPEN_TEMPLATE, params);
	}
}
