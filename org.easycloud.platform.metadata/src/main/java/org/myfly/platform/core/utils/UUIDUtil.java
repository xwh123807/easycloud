package org.myfly.platform.core.utils;

import org.hibernate.id.UUIDHexGenerator;

public class UUIDUtil {
	private static UUIDHexGenerator uuidGenerator = new UUIDHexGenerator();
	
	public static String newUUID(){
		return (String) uuidGenerator.generate(null, null);
	}
	
	public static String newHtmlID(){
		return "H" + newUUID();
	}
}
