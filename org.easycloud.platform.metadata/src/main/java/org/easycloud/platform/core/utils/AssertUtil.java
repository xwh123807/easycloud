package org.easycloud.platform.core.utils;

import java.text.MessageFormat;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

public class AssertUtil {
	/**
	 * 参数不能为空
	 * 
	 * @param paramName
	 * @param value
	 */
	public static void parameterEmpty(Object value, String paramName, String... moreInfo) {
		String message = MessageFormat.format(AssertConstants.PARAMETER_EMPTY, paramName);
		for (String info : moreInfo) {
			message += info;
		}
		if (value == null) {
			Assert.notNull(value, message);
		} else if (value instanceof String) {
			Assert.hasLength((String) value, message);
		}else{
			if (ObjectUtils.isEmpty(value)){
				throw new IllegalArgumentException(message);
			}
		}
	}
	
	/**
	 * 检查多个参数不能为空
	 * @param message
	 * @param parameters
	 */
	public static void parameterNotEmpty(String message, Object ...parameters){
		boolean result = false;
		int pos = 0;
		for (Object parameter : parameters){
			boolean empty = (parameter == null) || ((parameter instanceof String) && StringUtils.isBlank((String) parameter));
			if (empty){
				result = true;
				break;
			}
			pos ++;
		}
		if (result){
			throw new IllegalArgumentException("第[" + (pos+1) + "]参数不能为空." + (StringUtils.isBlank(message) ? "" : message));
		}
	}
	
	/**
	 * 检查数组不能为空
	 * @param message
	 * @param arrays
	 */
	public static void parametersNotEmpty(String message, Object[] arrays){
		boolean result = ArrayUtils.isEmpty(arrays);
		if (result){
			throw new IllegalArgumentException("数组不能为空." + (StringUtils.isBlank(message) ? "" : message));
		}
	}

	/**
	 * 记录不能为空
	 * 
	 * @param tableName
	 * @param value
	 */
	public static void recordNotFound(Object entity, String tableName, String fieldName, String fieldValue) {
		String message = MessageFormat.format(AssertConstants.RECORD_NOT_FOUND, tableName + "." + fieldName,
				fieldValue);
		Assert.notNull(entity, message);
	}

	/**
	 * 还未实现
	 * 
	 * @param name
	 */
	public static void notSupport(String name) {
		String message = MessageFormat.format(AssertConstants.NOT_SUPPORT, name);
		throw new IllegalArgumentException(message);
	}

	/**
	 * 参数无效
	 * 
	 * @param message
	 */
	public static void parameterInvalide(String parameterName, String message) {
		String msg = MessageFormat.format(AssertConstants.PARAMETER_INVALID, parameterName, message);
		throw new IllegalArgumentException(msg);
	}
	
	/**
	 * 
	 * @param parameterName
	 * @param message
	 */
	public static void parameterInvalide(boolean rule, String message) {
		if (rule){
			throw new IllegalArgumentException(message);
		}
	}
}
