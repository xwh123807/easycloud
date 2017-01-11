package org.myfly.platform.core.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 工具包，用于在Velocity模板中使用
 * @author xiangwanhong
 *
 */
public class Utils {
	public DataUtil getDataUtil(){
		return new DataUtil();
	}
	
	public StringUtils getStringUtils(){
		return new StringUtils();
	}
}
