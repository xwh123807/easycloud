package org.easycloud.platform.flydata.service;

/**
 * 实体缓存访问服务
 * @author xiangwanhong
 *
 */
public interface IEntityCacheService {

	<T> T getEntity(String entityName, String uid);

}
