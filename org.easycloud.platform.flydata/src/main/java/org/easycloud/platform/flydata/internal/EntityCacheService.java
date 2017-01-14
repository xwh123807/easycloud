package org.easycloud.platform.flydata.internal;

import org.easycloud.platform.flydata.service.DataServiceUtil;
import org.easycloud.platform.flydata.service.IEntityCacheService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class EntityCacheService implements IEntityCacheService {

	@Override
	public <T> T getEntity(String entityName, String uid) {
		Assert.hasLength(entityName);
		Assert.hasLength(uid);
		return DataServiceUtil.getFlyDataAccessService(entityName).findOne(entityName, uid);
	}

}
