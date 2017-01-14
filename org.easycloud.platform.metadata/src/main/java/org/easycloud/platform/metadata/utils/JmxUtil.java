package org.easycloud.platform.metadata.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.ObjectUtils;

public class JmxUtil {
	public static <T> T newProxyInstance(MBeanServerConnection connection, ObjectName objectName,
			Class<T> interfaceClass) {
		return MBeanServerInvocationHandler.newProxyInstance(connection, objectName, interfaceClass, true);
	}

	public static <T> Collection<T> getManagedObjects(MBeanServerConnection connection, ObjectName[] names,
			Class<T> type) {
		List<T> answer = new ArrayList<T>();
		for (int i = 0; i < names.length; i++) {
			ObjectName name = names[i];
			T value = (T) newProxyInstance(connection, name, type);
			if (value != null) {
				answer.add(value);
			}
		}
		return answer;
	}
	
	public static <T> T getManagedObject(MBeanServerConnection connection, ObjectName objectName) throws Exception{
		MBeanInfo info = connection.getMBeanInfo(objectName);
		String interFaceName = (String) info.getDescriptor().getFieldValue("interfaceClassName");
		Class<T> interfaceClass = (Class<T>) Class.forName(interFaceName);
		return newProxyInstance(connection, objectName, interfaceClass);
	}
	
	public static <T> T getManagedObject(MBeanServerConnection connection, ObjectName objectName, String attrName) throws Exception{
		Object value = connection.getAttribute(objectName, attrName);
		if (ObjectUtils.isArray(value)){
				if (CollectionUtils.get(value, 0) instanceof ObjectName){
					ObjectName[] names = (ObjectName[]) value;
					List<T> list = new ArrayList<>();
					for (ObjectName name : names){
						list.add((T) getManagedObject(connection, name));
					}
					return (T) list;
				}else{
					return (T) value;
				}
		}else if (value instanceof ObjectName){
			return getManagedObject(connection, (ObjectName) value);
		}else{
			return (T) value;
		}
	}

	/**
	 * 
	 * @param connection
	 * @param objectName
	 * @param attrName
	 * @param interfaceClass
	 * @return
	 * @throws Exception
	 */
	public static <T> Collection<T> getAttributes(MBeanServerConnection connection, ObjectName objectName,
			String attrName, Class<T> interfaceClass) throws Exception{
		ObjectName[] names = (ObjectName[]) connection.getAttribute(objectName, attrName);
		List<T> list = new ArrayList<>();
		for (ObjectName objName : names) {
			T obj = newProxyInstance(connection, objName, interfaceClass);
			list.add(obj);
		}
		return list;
	}
	
}
