package org.easycloud.platform.visualpage.define;

/**
 * 通用子表类型定义
 * 
 * @author xiangwanhong
 *
 */
public enum CommonSubTableType {
	// 备注
	NOTE("org.myfly.platform.system.domain.Note", "备注", "notes", "parentId"),
	// 附件
	ATTACHMENT("org.myfly.platform.system.domain.Attachment", "附件", "attachments", "parentId"),
	// 社交化
	SOCIAL("org.myfly.platform.system.domain.Task", "社交化", "tasks", "whoId");

	/**
	 * 通用子表类名
	 */
	private String tableClass;
	/**
	 * 子表属性名
	 */
	private String attrName;
	/**
	 * 主对象属性名
	 */
	private String parentAttrName;
	/**
	 * 子表名称
	 */
	private String title;

	/**
	 * 
	 * @param tableClass
	 * @param attrName
	 * @param parentAttrName
	 */
	private CommonSubTableType(String tableClass, String title, String attrName,
			String parentAttrName) {
		this.tableClass = tableClass;
		this.title = title;
		this.attrName = attrName;
		this.parentAttrName = parentAttrName;
	}

	public String getTableClass() {
		return tableClass;
	}

	public String getAttrName() {
		return attrName;
	}

	public String getParentAttrName() {
		return parentAttrName;
	}

	public String getTitle() {
		return title;
	}

	/**
	 * 判断类是否为通用子表类
	 * 
	 * @param clsName
	 * @return
	 */
	public static boolean isCommonSubTable(String clsName) {
		for (CommonSubTableType subTableType : CommonSubTableType.values()) {
			if (subTableType.getTableClass().equals(clsName)) {
				return true;
			}
		}
		return false;
	}
}
