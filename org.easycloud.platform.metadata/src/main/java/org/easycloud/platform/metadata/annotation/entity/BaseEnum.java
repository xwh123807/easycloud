package org.easycloud.platform.metadata.annotation.entity;

import java.util.List;

import org.apache.commons.lang3.EnumUtils;

/**
 * 枚举基类
 * 
 * @author xiangwanhong
 *
 */
public interface BaseEnum {
	/**
	 * 枚举显示名称
	 * 
	 * @return
	 */
	public String getTitle();

	/**
	 * 根据枚举值获取枚举类
	 * 
	 * @param title
	 * @return
	 */
	public static BaseEnum getEnumByTitle(Class enumClass, String title) {
		String key = enumClass.getName() + "-" + title;
		List<BaseEnum> list = EnumUtils.getEnumList(enumClass);
		for (BaseEnum item : list) {
			if (item.getTitle().equals(title)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * 根据枚举名称转换为枚举值
	 * @param enumClassName
	 * @param name
	 * @return
	 */
	public static String getEnumTitleByName(final String enumClassName, final String name) {
		try {
			Class enumClass = Class.forName(enumClassName);
			BaseEnum baseEnum = (BaseEnum) Enum.valueOf(enumClass, name);
			return baseEnum.getTitle();
		} catch (Exception e) {
			return name;
		}
	}
}
