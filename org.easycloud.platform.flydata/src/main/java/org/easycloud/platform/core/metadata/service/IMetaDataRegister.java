package org.easycloud.platform.core.metadata.service;

import java.net.URL;

import org.easycloud.platform.core.metadata.internal.EntityMetaData;

/**
 * 
 * @author xiangwanhong
 *
 */
public interface IMetaDataRegister {
	/**
	 * 是否提供表的元模型
	 * @param tableName
	 * @return
	 */
	public boolean contains(final String entityName);
	/**
	 * 获取表的元模型
	 * @param entityName
	 * @return
	 */
	public EntityMetaData getEntityMetaData(final String entityName);
	/**
	 * 注册元模型
	 * @param entityName
	 * @param url
	 */
	public void registerMeta(String entityName, URL url);
	/**
	 * 
	 * @param entityName
	 * @param jsonFile
	 */
	public void registerMeta(String entityName, String jsonFile);
}
