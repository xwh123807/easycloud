package org.easycloud.platform.common.utils;

import java.net.URL;

import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSONUtil {
	/**
	 * 对象转换为JSON字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String toJSON(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("对象序列化失败, " + e.getMessage());
		}
	}

	/**
	 * 从Json字符串还原对象
	 * 
	 * @param json
	 * @param targetClass
	 * @return
	 */
	public static <T> T fromJSON(String json, Class<T> targetClass) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, targetClass);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"[" + targetClass.getName() + "]反序列化失败, " + e.getMessage() + ".\n" + json);
		}
	}

	/**
	 * 将URL指向的JSON资源转换为指定类
	 * 
	 * @param resourceAsStream
	 * @param class1
	 * @return
	 */
	public static <T> T fromJSON(URL url, Class<T> targetType) {
		AssertUtil.parameterNotEmpty(null, url, targetType);
		String buffer;
		try {
			buffer = new String(StreamUtils.copyToByteArray(url.openStream()));
			T result = JSONUtil.fromJSON(buffer, targetType);
			return result;
		} catch (Exception e) {
			throw new IllegalArgumentException("JSON转换为对象失败，错误信息：" + e.getMessage());
		}
	}
}
