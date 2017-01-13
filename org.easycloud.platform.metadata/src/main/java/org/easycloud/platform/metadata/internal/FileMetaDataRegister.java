package org.easycloud.platform.metadata.internal;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.easycloud.platform.common.utils.AssertUtil;
import org.easycloud.platform.metadata.service.IMetaDataRegister;
import org.springframework.stereotype.Component;

/**
 * 从外部文件注册元模型
 * @author xiangwanhong
 *
 */

@Component
public class FileMetaDataRegister implements IMetaDataRegister {
	private static Map<String, URL> metaUrls = new HashMap<>();

	@Override
	public boolean contains(String entityName) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		return metaUrls.containsKey(entityName.toLowerCase());
	}

	@Override
	public EntityMetaData getEntityMetaData(String entityName) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		return JsonEntityMetaData.fromJson(metaUrls.get(entityName.toLowerCase()));
	}

	/**
	 * 注册元模型文件
	 * @param entityName
	 * @param url
	 */
	public void registerMeta(String entityName, URL url){
		AssertUtil.parameterEmpty(entityName, "entityName");
		AssertUtil.parameterEmpty(url, "url");
		metaUrls.put(entityName.toLowerCase(), url);
	}

	@Override
	public void registerMeta(String entityName, String jsonFile) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		AssertUtil.parameterEmpty(jsonFile, "jsonFile");
		try {
			metaUrls.put(entityName.toLowerCase(), new URL("file:" + jsonFile));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
