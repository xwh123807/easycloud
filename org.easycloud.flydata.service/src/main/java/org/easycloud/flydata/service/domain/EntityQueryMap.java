package org.easycloud.flydata.service.domain;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询参数Map，用于实体查询；当value值为多个时，表示in查询
 * @author xiangwanhong
 *
 */
public class EntityQueryMap extends LinkedHashMap<String, String[]>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 203409803281102872L;

	public EntityQueryMap(Map<String, String[]> parameterMap) {
		super(parameterMap);
	}

	public EntityQueryMap() {
	}
}
