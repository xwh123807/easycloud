package org.easycloud.flydata.server.impl;

import java.util.Map;

import org.easycloud.flydata.service.domain.EntityMap;
import org.easycloud.flydata.service.domain.EntityMetaData;
import org.easycloud.flydata.service.domain.FieldDefinition;
import org.springframework.util.Assert;

public class EntityUtil {
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
