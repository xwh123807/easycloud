package org.easycloud.platform.core.cache.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easycloud.platform.core.cache.service.CacheInfo;
import org.easycloud.platform.core.cache.service.CacheObjectInfo;
import org.easycloud.platform.core.cache.service.ICacheService;
import org.easycloud.platform.core.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * 缓存管理服务
 * 
 * @author xiangwanhong
 *
 */

@Service
public class CacheService implements ICacheService {
	private Log log = LogFactory.getLog(getClass());
	@Autowired
	private CacheManager cacheManager;

	/**
	 * 获取系统当前缓存类型列表
	 */
	@Override
	public List<CacheInfo> getCachesInfo() {
		List<CacheInfo> list = new ArrayList<>();
		for (String cacheName : cacheManager.getCacheNames()) {
			CacheInfo cacheInfo = new CacheInfo();
			cacheInfo.setCacheName(cacheName);
			//updateCacheInfo(cacheInfo);
			list.add(cacheInfo);
		}
		return list;
	}

//	private void updateCacheInfo(CacheInfo cacheInfo) {
//		Cache cache = cacheManager.getCache(cacheInfo.getCacheName());
//		if (cache instanceof EhCacheCache) {
//			updateCacheInfoFormECache((EhCacheCache) cache, cacheInfo);
//		}
//	}

//	private void updateCacheInfoFormECache(EhCacheCache cache, CacheInfo cacheInfo) {
//		net.sf.ehcache.Cache ecache = (net.sf.ehcache.Cache) cache.getNativeCache();
//		cacheInfo.setSize(ecache.getSize());
//	}
//
//	private net.sf.ehcache.Cache getEcache(EhCacheCache cache) {
//		return (net.sf.ehcache.Cache) cache.getNativeCache();
//	}

	/**
	 * 获取指定缓存类型下所有的缓存对象
	 */
	@Override
	public List<CacheObjectInfo> getCacheObjectsInfo(String cacheName) {
		AssertUtil.parameterEmpty(cacheName, "cacheName");
		List<CacheObjectInfo> list = new ArrayList<>();
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			if (log.isWarnEnabled()) {
				log.warn("没有找到缓存类型，名称为" + cacheName);
			}
		}
		if (cache instanceof EhCacheCache) {
//			list.addAll(getCacheObjectsInfoFromEcache(getEcache((EhCacheCache) cache)));
		}
		return list;
	}

//	private List<CacheObjectInfo> getCacheObjectsInfoFromEcache(net.sf.ehcache.Cache cache) {
//		List<CacheObjectInfo> list = new ArrayList<>();
//		for (Object key : cache.getKeys()) {
//			Element value = cache.get(key);
//			CacheObjectInfo cacheObjectInfo = new CacheObjectInfo();
//			cacheObjectInfo.setKey((String) key.toString());
//			cacheObjectInfo.setValue(value.getObjectValue());
//			cacheObjectInfo.setHitCount(value.getHitCount());
//			cacheObjectInfo.setCreationTime(new Date(value.getCreationTime()));
//			cacheObjectInfo.setLastAccessTime(new Date(value.getLastAccessTime()));
//			list.add(cacheObjectInfo);
//		}
//		return list;
//	}

	/**
	 * 缓存全部清空
	 */
	@Override
	public void clearAll() {
		if (log.isInfoEnabled()) {
			log.info("清除全部缓存.");
		}
		for (String cacheName : cacheManager.getCacheNames()) {
			cacheManager.getCache(cacheName).clear();
		}
	}

	/**
	 * 清空指定的缓存类型
	 */
	@Override
	public void clearCache(String cacheName) {
		AssertUtil.parameterEmpty(cacheName, "cacheName");
		if (log.isInfoEnabled()) {
			log.info("清除缓存，名称为" + cacheName);
		}
		cacheManager.getCache(cacheName).clear();
		;
	}
}
