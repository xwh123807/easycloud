package org.easycloud.platform.metadata.utils;

import java.util.UUID;

public class UUIDUtil {
	//private static UUIDHexGenerator uuidGenerator = new UUIDHexGenerator();
	
	public static String newUUID(){
		return UUID.randomUUID().toString();
		//return (String) uuidGenerator.generate(null, null);
	}
	
	public static String newHtmlID(){
		return "H" + newUUID();
	}
}
