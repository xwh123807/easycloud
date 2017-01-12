package org.easycloud.platform.core.flydata.internal;

import org.myfly.platform.core.flydata.service.IEntityCacheService;
import org.myfly.platform.core.utils.AppUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class EntityCacheService implements IEntityCacheService {

	@Override
	public <T> T getEntity(String entityName, String uid) {
		Assert.hasLength(entityName);
		Assert.hasLength(uid);
		return AppUtil.getFlyDataAccessService(entityName).findOne(entityName, uid);
	}

}
