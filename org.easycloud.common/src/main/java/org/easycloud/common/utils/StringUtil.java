package org.easycloud.common.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
	/**
	 * 判断字符串是否为空
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNotEmpty(String value) {
		return (value != null) && (value.length() > 0);
	}

	public static boolean isEmpty(String value) {
		return (value == null) || (value.length() == 0);
	}

	/**
	 * 
	 * @param value
	 * @param regex
	 * @return
	 */
	public static boolean matches(String value, String regex) {
		return value.startsWith(regex);
	}

	/**
	 * 判断数组是否为空
	 * 
	 * @param values
	 * @return
	 */
	public static boolean isNotEmpty(String[] values) {
		return (values != null) && (values.length > 0) && (isNotEmpty(values[0]));
	}

	/**
	 * 获取Request中的post参数，返回易读的格式
	 * @param request
	 * @return
	 */
//	public static String getRequestDebugInfo(HttpServletRequest request){
//		StringBuffer buffer = new StringBuffer();
//		buffer.append("url:\t" + request.getRequestURL().toString()).append("\n");
//		buffer.append("query:\t" + request.getQueryString()).append("\n");
//		Map<String, String[]> params = request.getParameterMap();
//		for (Map.Entry<String, String[]> param : params.entrySet()){
//			buffer.append(param.getKey()).append(":\t");
//			for (String value : param.getValue()){
//				buffer.append(value).append(" , ");
//			}
//			buffer.append("\n");
//		}
//		return buffer.toString();
//	}

	/**
	 * 将名称转换为Hibernate DB的名称 stdTestTable转换为std-test-table
	 * @param name
	 * @return
	 */
	public static String getHibernateName(String name) {
		String result = String.valueOf(name.charAt(0));
		for (int i = 1; i < name.length()-1; i ++){
			result += String.valueOf(name.charAt(i));
			if (Character.isLowerCase(name.charAt(i)) && Character.isUpperCase(name.charAt(i + 1))){
				result += "_";
			}
		}
		result += name.substring(name.length()-1);
		return result.toUpperCase();
	}
	
	/**
	 * 将sql中问号替换成实际值
	 * @param sql	select * from a where name=?
	 * @param paramValues
	 * @return
	 */
	public static String formatSql(final String sql, final Object[] paramValues){
		String fsql = sql;
		for (Object value : paramValues){
			String tmp = fsql;
			fsql = StringUtils.substringBefore(tmp, "?");
			if (value instanceof String){
				fsql += "'" + value + "'";
			}else{
				fsql += ClassUtil.convertValueToString(value);
			}
			fsql += StringUtils.substringAfter(tmp, "?");
		}
		return fsql;
	}
}
