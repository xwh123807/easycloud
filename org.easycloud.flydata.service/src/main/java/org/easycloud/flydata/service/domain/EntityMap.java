package org.easycloud.flydata.service.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * 用于实体新增，修改时传递属性值
 * 
 * @author xiangwanhong
 *
 * @param <T>
 */
public class EntityMap extends LinkedHashMap<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8541813318000368514L;

	public EntityMap() {
	}

	/**
	 * 批量增加值对
	 * 
	 * @param keys
	 * @param values
	 */
	public EntityMap(String[] keys, String values[]) {
		Assert.notEmpty(keys);
		Assert.notEmpty(values);
		Assert.isTrue(keys.length == values.length);
		for (int index = 0; index < keys.length; index++) {
			put(keys[index], values[index]);
		}
	}

	public EntityMap(Map<String, String> values) {
		super(values);
	}
}
