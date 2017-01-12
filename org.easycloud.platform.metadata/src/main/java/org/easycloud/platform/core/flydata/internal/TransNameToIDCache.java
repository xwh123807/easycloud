package org.easycloud.platform.core.flydata.internal;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.core.flydata.service.IJpaAccessService;
import org.easycloud.platform.core.utils.AssertUtil;

/**
 * 名称转换为ID缓存
 * @author xiangwanhong
 *
 */
public class TransNameToIDCache extends HashMap<String, HashMap<String, String>>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3319220894219349995L;
	private IJpaAccessService dataAccessService;

	public TransNameToIDCache(IJpaAccessService dataAccessService) {
		AssertUtil.parameterEmpty(dataAccessService, "dataAccessService");
		this.dataAccessService = dataAccessService;
	}
	
	public String transNameToUID(String tableName, String fieldName, String name){
		AssertUtil.parameterEmpty(tableName, "tableName");
		AssertUtil.parameterEmpty(fieldName, "fieldName");
		AssertUtil.parameterEmpty(name, "name");
		String fieldKey = tableName + "-" + fieldName;
		HashMap<String, String> item = get(fieldKey);
		if (item == null){
			item = new HashMap<>();
			put(fieldKey, item);
		}
		String uid = item.get(name);
		if (StringUtils.isBlank(uid)){
			uid = dataAccessService.transNameToUID(tableName, name);
			item.put(name, uid);
		}
		return uid;
	}
}
