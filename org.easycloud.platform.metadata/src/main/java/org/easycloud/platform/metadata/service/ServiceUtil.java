package org.easycloud.platform.metadata.service;

import org.easycloud.platform.metadata.internal.EntityMetaDataService;

public class ServiceUtil {
	public static IEntityMetaDataService getEntityMetaDataService(){
		return new EntityMetaDataService();
	}
}
