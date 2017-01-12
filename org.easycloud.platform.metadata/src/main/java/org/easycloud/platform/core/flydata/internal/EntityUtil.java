package org.easycloud.platform.core.flydata.internal;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.core.domain.FieldDataType;
import org.easycloud.platform.core.domain.SKeyEntity;
import org.easycloud.platform.core.metadata.define.FieldDefinition;
import org.easycloud.platform.core.metadata.define.PKFieldDefinition;
import org.easycloud.platform.core.metadata.internal.EntityMetaData;
import org.easycloud.platform.core.utils.AppUtil;
import org.easycloud.platform.core.utils.ClassUtil;

public class EntityUtil {
	/**
	 * 修改实体属性,For Http Request
	 * 
	 * @param entity
	 * @param metaData
	 * @param values
	 * @return
	 */
	public static <T> void updateEntityForRequest(T entity, EntityMetaData metaData, Map<String, String[]> values) {
		if (metaData == null) {
			metaData = AppUtil.getEntityMataDataService().getEntityMetaData(entity.getClass().getName());
		}
		for (FieldDefinition field : metaData.getAllFields()) {
			String[] items = values.get(field.getName());
			if (ArrayUtils.isNotEmpty(items) && StringUtils.isNotEmpty(items[0])) {
				// 只处理有赋值的属性
				field.getSetValueHandler().setFieldValue(entity, items[0]);
			}
		}
	}
	
	/**
	 * 修改实体属性
	 * 
	 * @param entity
	 * @param metaData
	 * @param values
	 */
	public static <T> void updateEntity(T entity, EntityMetaData metaData, Map<String, Object> values) {
		if (metaData == null) {
			metaData = AppUtil.getEntityMataDataService().getEntityMetaData(entity.getClass().getName());
		}
		for (FieldDefinition field : metaData.getAllFields()) {
			Object value = values.get(field.getName());
			if (value != null) {
				// 只处理有赋值的属性
				field.getSetValueHandler().setFieldValue(entity, value);
			}
		}
	}

	/**
	 * 构建新实体，For Http Request
	 * 
	 * @param tableName
	 * @param values
	 * @return
	 */
	public static <T> T buildNewEntityForRequest(String tableName, EntityMetaData metaData,
			Map<String, String[]> values) {
		if (metaData == null) {
			metaData = AppUtil.getEntityMataDataService().getEntityMetaData(tableName);
		}
		T entity = metaData.newEntityInstance();
		updateEntityForRequest(entity, metaData, values);
		return entity;
	}

	/**
	 * 构建新实体
	 * 
	 * @param tableName
	 * @param metaData
	 * @param values
	 * @return
	 */
	public static <T> T buildNewEntity(String tableName, EntityMetaData metaData, Map<String, Object> values) {
		if (metaData == null) {
			metaData = AppUtil.getEntityMataDataService().getEntityMetaData(tableName);
		}
		T entity = metaData.newEntityInstance();
		updateEntity(entity, metaData, values);
		return entity;
	}

	/**
	 * 构建新实体，For Http Request
	 * 
	 * @param tableName
	 * @param values
	 * @return
	 */
	public static <T> T buildNewEntityForRequest(String tableName, Map<String, String[]> values) {
		return buildNewEntityForRequest(tableName, null, values);
	}

	/**
	 * 构建新实体
	 * 
	 * @param tableName
	 * @param values
	 * @return
	 */
	public static <T> T buildNewEntity(String tableName, Map<String, Object> values) {
		return buildNewEntity(tableName, null, values);
	}

	/**
	 * 将实体属性值取出赋值到Map上
	 * 
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, Object> entityValueToMap(T entity) {
		Map<String, Object> values = new LinkedHashMap<>();
		if (entity != null) {
			Class<T> entityClass = (Class<T>) entity.getClass();
			EntityMetaData metaData = AppUtil.getEntityMataDataService().getEntityMetaData(entityClass.getName());
			for (FieldDefinition field : metaData.getAllFields()) {
				Object value = null;
				try {
					value = field.getGetter().invoke(entity);
				} catch (Exception e) {
					throw new IllegalArgumentException("实体属性[" + field.getName() + "]值获取失败");
				}
				if (value != null) {
					values.put(field.getName(), value);
				}
			}
		}
		return values;
	}

	/**
	 * 把实体类属性值转换成可以直接插入到数据库的Map
	 * 
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, Object> entityValueToDBMap(T entity) {
		Map<String, Object> values = new LinkedHashMap<>();
		if (entity != null) {
			Class<T> entityClass = (Class<T>) entity.getClass();
			EntityMetaData metaData = AppUtil.getEntityMataDataService().getEntityMetaData(entityClass.getName());
			for (FieldDefinition field : metaData.getAllFields()) {
				Object value = null;
				try {
					value = field.getGetter().invoke(entity);
					if (value != null) {
						// 枚举的特殊处理，取出枚举名称，数据库只存枚举名称
						if (FieldDataType.SYSENUM.equals(field.getDataType())) {
							value = ((Enum<?>) value).name();
						}
						if (value instanceof SKeyEntity) {
							value = ((SKeyEntity) value).getUid();
						}
					}
				} catch (Exception e) {
					throw new IllegalArgumentException("实体属性[" + field.getName() + "]值获取失败");
				}
				if (value != null) {
					values.put(field.getFieldName(), value);
				}
			}
		}
		return values;
	}

	/**
	 * 将实体转换为Http Request URL串。
	 * 
	 * @param entity
	 * @return
	 */
	public static <T> String convertEntityToRequestParameters(T entity) {
		Map<String, Object> values = entityValueToMap(entity);
		String result = "";
		for (String name : values.keySet()) {
			Object value = values.get(name);
			if (value != null) {
				result += "&" + name + "=" + ClassUtil.convertValueToString(value);
			}
		}
		if (StringUtils.isNotBlank(result)) {
			result = result.substring(1);
		}
		return result;
	}

	/**
	 * 构建新实体，并设置主键值
	 * 
	 * @param tableName
	 * @param metaData
	 * @param pkValue
	 * @param values
	 * @return
	 */
	public static <T> T buildNewEntityForRequest(String tableName, EntityMetaData metaData, Serializable pkValue,
			Map<String, String[]> values) {
		T entity = buildNewEntityForRequest(tableName, metaData, values);
		metaData.getPKFieldDefinition().setPKValue(entity, pkValue);
		return entity;
	}

	/**
	 * 构建实体子表
	 * 
	 * @param masterMetaData
	 * @param masterUid
	 * @param subTableAttr
	 * @param subUid
	 * @param values
	 * @return
	 */
	public static <T> T buildNewSubEntityForRequest(EntityMetaData masterMetaData, String masterUid,
			String subTableAttr, String subUid, Map<String, String[]> values) {
		FieldDefinition field = masterMetaData.getField(subTableAttr).getRelationField();
		EntityMetaData metaData = field.getParent();
		T newEntity = EntityUtil.buildNewEntityForRequest(metaData.getEntityName(), metaData, values);
		if (StringUtils.isNotBlank(subUid)) {
			metaData.getPKFieldDefinition().setPKValue(newEntity, subUid);
		}
		field.getSetValueHandler().setFieldValue(newEntity, masterUid);
		return newEntity;
	}

	/**
	 * 构建实体新子表
	 * 
	 * @param masterMetaData
	 * @param masterUid
	 * @param subTableAttr
	 * @param values
	 * @return
	 */
	public static <T> T buildNewSubEntityForRequest(EntityMetaData masterMetaData, String masterUid,
			String subTableAttr, Map<String, String[]> values) {
		return buildNewSubEntityForRequest(masterMetaData, masterUid, subTableAttr, null, values);
	}

	/**
	 * 获取实体主键值，转换为字符串
	 * 
	 * @param entity
	 * @return
	 */
	public static String getEntityPKValue(Object entity) {
		if (entity == null) {
			return "";
		} else {
			EntityMetaData metaData = AppUtil.getEntityMataDataService().getEntityMetaData(entity.getClass().getName());
			PKFieldDefinition pkField = metaData.getPKFieldDefinition();
			return pkField.getPKValue(entity);
		}
	}

	/**
	 * 获取实体显示名称，转换为字符串
	 * 
	 * @param entity
	 * @return
	 */
	public static String getEntityLabelValue(Object entity) {
		if (entity == null) {
			return "";
		} else {
			EntityMetaData metaData = AppUtil.getEntityMataDataService().getEntityMetaData(entity.getClass().getName());
			FieldDefinition labelField = metaData.getLabelField();
			Object value = labelField.getGetValueHandler().getFieldValue(entity);
			return ClassUtil.convertValueToString(value);
		}
	}
}
