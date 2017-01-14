package org.easycloud.platform.flydata.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.metadata.define.entity.FieldDefinition;
import org.easycloud.platform.metadata.service.EntityMetaData;
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

	public static EntityMap build(Map<String, String[]> params) {
		EntityMap map = new EntityMap();
		for (Map.Entry<String, String[]> param : params.entrySet()) {
			if (ArrayUtils.isNotEmpty(param.getValue())) {
				map.put(param.getKey(), param.getValue()[0]);
			}else{
				map.put(param.getKey(), null);
			}
		}
		return map;
	}

	public <T> T newEntity(final String entityName, final String uid) {
		Assert.hasLength(entityName);
		return newEntity(getEntityMetaData(entityName), uid);
	}

	private EntityMetaData getEntityMetaData(String entityName) {
		Assert.hasLength(entityName);
		return AppUtil.getEntityMataDataService().getEntityMetaData(entityName);
	}

	/**
	 * 根据值对创建实体类
	 * 
	 * @param metaData
	 * @return
	 */
	public <T> T newEntity(EntityMetaData metaData, final String uid) {
		Assert.notNull(metaData);
		T entity = metaData.newEntityInstance();

		for (FieldDefinition field : metaData.getAllFields()) {
			if (containsKey(field.getName())) {
				field.getSetValueHandler().setFieldValue(entity, get(field.getName()));
			}
		}

		if (StringUtils.isNotBlank(uid)) {
			metaData.getPKFieldDefinition().setPKValue(entity, uid);
		}

		return entity;
	}

	public <T> T newSubEntity(final String entityName, String uid, String subTableAttr, String subUid) {
		Assert.hasLength(entityName);
		Assert.hasLength(uid);
		Assert.hasLength(subTableAttr);
		return newSubEntity(getEntityMetaData(entityName), uid, subTableAttr, subUid);
	}

	/**
	 * 根据值对创建子实体
	 * 
	 * @param metaData
	 * @param uid
	 * @param subTableAttr
	 * @param subUid
	 * @return
	 */
	public <T> T newSubEntity(EntityMetaData metaData, String uid, String subTableAttr, String subUid) {
		EntityMetaData subMetaData = metaData.getSubEntityMetaData(subTableAttr);
		FieldDefinition subField = metaData.getField(subTableAttr).getRelationField();
		T entity = newEntity(subMetaData, subUid);
		subField.getSetValueHandler().setFieldValue(entity, uid);
		return entity;
	}

	public <T> void mergeEntity(String entityName, T entity) {
		mergeEntity(getEntityMetaData(entityName), entity);
	}
	
	/**
	 * 将值对合并到entity中
	 * @param entityName
	 * @param entity
	 */
	public <T> void mergeEntity(EntityMetaData metaData, T entity) {
		for (FieldDefinition field : metaData.getAllFields()) {
			if (containsKey(field.getName())) {
				field.getSetValueHandler().setFieldValue(entity, get(field.getName()));
			}
		}
	}
}
