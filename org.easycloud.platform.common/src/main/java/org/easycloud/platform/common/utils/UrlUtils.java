package org.easycloud.platform.common.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;

public class UrlUtils {
	/**
	 * Decode URL
	 * 
	 * @param url
	 * @return
	 */
	public static String decodeUrl(String url) {
		if (StringUtils.isBlank(url)){
			return null;
		}
		String result = null;
		try {
			result = URLDecoder.decode(url, CharSetConstants.CHARSET_UTF8);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Encode URL
	 * @param url
	 * @return
	 */
	public static String encodeUrl(String url) {
		if (StringUtils.isBlank(url)){
			return null;
		}
		String result = null;
		try {
			result = URLEncoder.encode(url, CharSetConstants.CHARSET_UTF8);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return result;
	}
}
