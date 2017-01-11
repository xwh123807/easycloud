package org.myfly.platform.core.utils;

import java.util.List;

import org.myfly.platform.core.flydata.service.EntityQueryMap;
import org.myfly.platform.core.flydata.service.FlyEntityMap;
import org.myfly.platform.core.flydata.service.IFlyDataAccessService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class DataUtil {
	private static IFlyDataAccessService getDataService(final String entityName) {
		return AppUtil.getFlyDataAccessService(entityName);
	}

	/**
	 * 获取子表全部数据
	 * 
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param formViewName
	 * @param params
	 * @param printMode
	 * @return
	 */
	public static List<FlyEntityMap> findAllForSubEntity(String entityName, String uid, String subTableAttr,
			String formViewName, EntityQueryMap params, boolean printMode) {
		return getDataService(entityName).findAllForSubEntity(entityName, uid, subTableAttr, formViewName, params,
				printMode);
	}

	/**
	 * 获取子表数据，取指定分页
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param formViewName
	 * @param params
	 * @param page
	 * @param size
	 * @param printMode
	 * @return
	 */
	public static Page<FlyEntityMap> findAllForSubEntity(String entityName, String uid, String subTableAttr,
			String formViewName, EntityQueryMap params, int page, int size, boolean printMode) {
		PageRequest pageable = new PageRequest(page, size);
		return getDataService(entityName).findAllForSubEntityWithPage(entityName, uid, subTableAttr, formViewName, params, pageable, printMode);
	}
}
