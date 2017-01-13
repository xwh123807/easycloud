package org.easycloud.platform.core.flydata.service;

/**
 * 实体缓存访问服务
 * @author xiangwanhong
 *
 */
public interface IEntityCacheService {

	<T> T getEntity(String entityName, String uid);

}
