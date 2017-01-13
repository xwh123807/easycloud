package org.easycloud.platform.core.utils;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class FileUtil {
	/**
	 * 创建临时目录，返回路径
	 * 
	 * @return
	 */
	public static String createTempDirectoryPath() {
		String dir = FileUtils.getTempDirectoryPath() + UUIDUtil.newUUID() + "/";
		new File(dir).mkdir();
		return dir;
	}

	/**
	 * 创建临时目录，返回目录
	 * @return
	 */
	public static String createTempDirectory() {
		String dir = FileUtils.getTempDirectoryPath() + UUIDUtil.newUUID();
		new File(dir).mkdir();
		return dir;
	}

	/**
	 * 获取文件扩展名
	 * @param name
	 * @return
	 */
	public static String getFileExtention(String name){
		if (name.lastIndexOf(".") != -1) {
			String tmp = name.substring(name.lastIndexOf(".") + 1);
			if (tmp.indexOf(File.separator) == -1){
				return tmp;
			}
		}
		return "";
	}
}
