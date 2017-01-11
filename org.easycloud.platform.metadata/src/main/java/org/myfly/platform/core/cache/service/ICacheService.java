package org.myfly.platform.core.cache.service;

import java.util.List;

public interface ICacheService {

	List<CacheInfo> getCachesInfo();

	List<CacheObjectInfo> getCacheObjectsInfo(String cacheName);

	void clearAll();

	void clearCache(String cacheName);

}
