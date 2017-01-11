package org.myfly.platform.core.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpUtil {
	private static Log log = LogFactory.getLog(HttpUtil.class);
	
	public static final String MYMETHOD_NAME = "_method";
	
	/**
	 * 获取url地址后面的部分,如http://localhost:9080/vp/?form,返回/vp/?form
	 * @param request
	 * @return
	 */
	public static String getUrl(HttpServletRequest request){
		return request.getRequestURL().substring(request.getRequestURL().indexOf(request.getRequestURI()));
	}

}
