package org.easycloud.platform.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.core.domain.EntityAction;

/**
 * 实体常用操作url
 * 
 * @author xiangwanhong
 *
 */
public class EntityUrlUtil {
	/**
	 * 根据ViewType获取实体操作URL
	 * 
	 * @param action
	 * @param tableName
	 * @param uid
	 * @return
	 */
	public static String getEntityActionUrl(final EntityAction action, final String tableName, final String uid,
			String view) {
		AssertUtil.parameterEmpty(action, "viewType");
		AssertUtil.parameterEmpty(tableName, "tableName");
		String entityName = tableName.toLowerCase();
		String url = "/vp/";
		switch (action) {
		// get
		case NEW:
			// post
		case SAVE:
			// patch
		case SAVEANDNEW:
			// delete
		case DELALL:
			url += entityName;
			break;
		// get
		case VIEW:
			// get ? form
		case EDIT:
			// delete
		case DEL:
			// put
		case SAVEUPDATE:
			AssertUtil.parameterEmpty(uid, "uid");
			url += entityName + "/" + uid;
			if (EntityAction.EDIT.equals(action)) {
				url += "?form";
			}
			break;
		// get
		case OUTLINE:
			AssertUtil.parameterEmpty(uid, "uid");
			url += action.name().toLowerCase() + "/" + entityName + "/" + uid;
			break;
		// get
		case SHOWEXCELIMPORT:
			url += "excelimport/" + entityName;
			break;
		// get
		case LIST:
			// get
		case PRINT:
		case LISTPRINT:
			// get
		case PDF:
			// get
		case EXCEL:
			url += action.name().toLowerCase() + "/" + entityName;
			if (StringUtils.isNotBlank(uid)) {
				url += "/" + uid;
			}
			break;
		case UPLOAD:
			url = "/file/showUpload/" + uid;
			break;
		case BACK:
			break;
		default:
			AssertUtil.notSupport("操作[" + action.getTitle() + "]还未实现.");
			break;
		}
		if (StringUtils.isNotBlank(view)) {
			return url + "?view=" + view;
		} else {
			return url;
		}
	}

	/**
	 * 根据ViewType获取实体子表操作URL
	 * 
	 * @param action
	 * @param tableName
	 * @param uid
	 * @param subTableAttr
	 * @param subUid
	 * @return
	 */
	public static String getSubEntityActionUrl(final EntityAction action, final String tableName, final String uid,
			final String subTableAttr, final String subUid, String view) {
		AssertUtil.parameterEmpty(action, "viewType");
		AssertUtil.parameterEmpty(tableName, "tableName");
		AssertUtil.parameterEmpty(uid, "uid");
		AssertUtil.parameterEmpty(subTableAttr, "subTableAttr");
		String entityName = tableName.toLowerCase();

		String url = "/vp/";
		switch (action) {
		// get
		case NEW:
			// post
		case SAVE:
			// patch
		case SAVEANDNEW:
			// delete
		case DELALL:
			url += entityName + "/" + uid + "/" + subTableAttr;
			break;
		// get
		case VIEW:
			// get ? form
		case EDIT:
			// delete
		case DEL:
			// put
		case SAVEUPDATE:
			AssertUtil.parameterEmpty(uid, "subUid");
			url += entityName + "/" + uid + "/" + subTableAttr + "/" + subUid;
			if (EntityAction.EDIT.equals(action)) {
				url += "?form";
			}
			break;
		// get
		case OUTLINE:
			AssertUtil.parameterEmpty(uid, "subUid");
			url += action.name().toLowerCase() + "/" + entityName + "/" + uid + "/" + subTableAttr + "/" + subUid;
			break;
		case SHOWEXCELIMPORT:
			url += "excelimport/" + entityName + "/" + uid + "/" + subTableAttr;
			break;
		// get
		case LIST:
			// get
		case PRINT:
		case LISTPRINT:
			// get
		case PDF:
			// get
		case EXCEL:
			url += action.name().toLowerCase() + "/" + entityName + "/" + uid + "/" + subTableAttr;
			break;
		case BACK:
			break;
		case UPLOAD:
			url = "/file/showUpload/" + uid;
			break;
		default:
			AssertUtil.notSupport("操作[" + action.getTitle() + "]还未实现.");
			break;
		}
		if (StringUtils.isNotBlank(view)) {
			return url + "?view=" + view;
		} else {
			return url;
		}
	}

	/**
	 * 获取实体查找关系URL
	 * 
	 * @param tableName
	 * @param uid
	 * @return
	 */
	public static String getEntitySearchRelationViewUrl(String tableName, String uid) {
		return getEntityActionUrl(EntityAction.VIEW, tableName, uid, null);
	}

	/**
	 * 获取文件下载URL
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileDownloadUrl(String fileName) {
		AssertUtil.parameterEmpty(fileName, "fileName");
		return "/file/download?fileName=" + fileName;
	}
}
