package org.myfly.platform.core.flydata.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.myfly.platform.core.domain.FieldDataType;
import org.myfly.platform.core.flydata.service.EntityMap;
import org.myfly.platform.core.flydata.service.EntityQueryMap;
import org.myfly.platform.core.flydata.service.FlyEntityMap;
import org.myfly.platform.core.flydata.service.IJdbcDataAccessService;
import org.myfly.platform.core.metadata.define.FKFieldDefinition;
import org.myfly.platform.core.metadata.define.FieldDefinition;
import org.myfly.platform.core.metadata.define.FilterDefinition;
import org.myfly.platform.core.metadata.define.FormDefinition;
import org.myfly.platform.core.metadata.define.ListDefinition;
import org.myfly.platform.core.metadata.define.PKFieldDefinition;
import org.myfly.platform.core.metadata.define.SQLOperator;
import org.myfly.platform.core.metadata.define.SubTableDefinition;
import org.myfly.platform.core.metadata.internal.EntityMetaData;
import org.myfly.platform.core.metadata.internal.EntityMetaDataConstants;
import org.myfly.platform.core.utils.AssertUtil;
import org.myfly.platform.core.utils.ClassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service("jdbcFlyDataAccessService")
@Transactional
public class JdbcFlyDataAccessService extends AbstractFlyDataAccessService {
	@Autowired
	private IJdbcDataAccessService jdbcDataAccessService;

	/**
	 * 从元模型中获取数据库表名
	 * 
	 * @param entityMetaData
	 * @return
	 */
	private String convertTableName(EntityMetaData entityMetaData) {
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData");
		String schemaName = entityMetaData.getTableDefinition().getSchema();
		String tableName = entityMetaData.getTableDefinition().getTableName();
		if (StringUtils.isNotBlank(schemaName)) {
			tableName = schemaName + "." + tableName;
		}
		return tableName;
	}

	/**
	 * 获取实体数据库表名
	 * 
	 * @param entityName
	 * @return
	 */
	private String convertTableName(String entityName) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		EntityMetaData entityMetaData = getEntityMetaData(entityName);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData");
		return convertTableName(entityMetaData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IFlyDataAccessService#findOne(java
	 * .lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T findOne(String entityName, String uid) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		AssertUtil.parameterEmpty(uid, "uid");
		EntityMetaData entityMetaData = getEntityMetaData(entityName);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData");

		// 构建主键
		Map<String, Object> pkValue = entityMetaData.getPKFieldDefinition().buildPKMap(uid);
		AssertUtil.parameterEmpty(pkValue, "pkValue");

		// 查询数据，结果返回为Map
		Map<String, Object> dbEntity = jdbcDataAccessService.findOne(entityMetaData.getDataSourceId(),
				convertTableName(entityMetaData), pkValue);
		AssertUtil.parameterEmpty(dbEntity, "dbEntity");

		return (T) convertToViewMap(entityName, null, null, dbEntity, uid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IFlyDataAccessService#findOne(java
	 * .lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T findOne(String entityName, Map<String, Object> keyParams) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		AssertUtil.parameterEmpty(keyParams, "keyParams");
		EntityMetaData entityMetaData = getEntityMetaData(entityName);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData");

		Map<String, Object> dbEntity = jdbcDataAccessService.findOne(entityMetaData.getDataSourceId(),
				convertTableName(entityName), keyParams);
		AssertUtil.parameterEmpty(dbEntity, "dbEntity");

		String uid = entityMetaData.getPKFieldDefinition().getPKValue(dbEntity);
		AssertUtil.parameterEmpty(uid, "uid");

		return (T) convertToViewMap(entityName, null, null, dbEntity, uid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IFlyDataAccessService#findOne(java
	 * .lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public FlyEntityMap findOne(String entityName, String uid, String formViewName, boolean printMode) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		AssertUtil.parameterEmpty(uid, "uid");
		EntityMetaData entityMetaData = getEntityMetaData(entityName);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData");

		// 构建主键
		Map<String, Object> pkValue = entityMetaData.getPKFieldDefinition().buildPKMap(uid);
		AssertUtil.parameterEmpty(pkValue, "pkValue");

		// 查询数据，结果返回为Map
		Map<String, Object> dbEntity = jdbcDataAccessService.findOne(entityMetaData.getDataSourceId(),
				convertTableName(entityName), pkValue);
		AssertUtil.parameterEmpty(dbEntity, "dbEntity");

		FormDefinition formDefinition = entityMetaData.getFormDefinition(formViewName);
		AssertUtil.parameterEmpty(formDefinition, "formDefinition");

		String labelFieldName = entityMetaData.getLabelFieldName();
		return convertToViewMap(entityName, null, null, dbEntity, formDefinition.getFields(), labelFieldName, uid,
				formViewName, printMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IFlyDataAccessService#findAll(java
	 * .lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll2(String entityName, Map<String, Object> params) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		EntityMetaData entityMetaData = getEntityMetaData(entityName);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData");

		PKFieldDefinition pkFieldDefinition = entityMetaData.getPKFieldDefinition();

		List<T> list = (List<T>) jdbcDataAccessService.findAll(entityMetaData.getDataSourceId(),
				convertTableName(entityName), params);
		List<T> result = new ArrayList<>();
		for (T dbEntity : list) {
			String uid = pkFieldDefinition.getPKValue(dbEntity);
			result.add((T) convertToViewMap(entityName, null, null, dbEntity, uid));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IFlyDataAccessService#findAll(java
	 * .lang.String, java.lang.String, java.util.Map, boolean)
	 */
	@Override
	public List<FlyEntityMap> findAll(String entityName, String listViewName, EntityQueryMap params,
			boolean printMode) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		return findAll(entityName, listViewName, params, 0, 0, printMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IFlyDataAccessService#findAll(java
	 * .lang.String, java.lang.String, java.util.Map, int, int, boolean)
	 */
	@Override
	public List<FlyEntityMap> findAll(String entityName, String listViewName, EntityQueryMap params, int page, int size,
			boolean printMode) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		EntityMetaData entityMetaData = getEntityMetaData(entityName);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData");

		ListDefinition listDefinition = entityMetaData.getListDefinition(listViewName);
		AssertUtil.parameterEmpty(listDefinition, "listDefinition");
		String labelFieldName = listDefinition.getLabelField();
		PKFieldDefinition pkField = entityMetaData.getPKFieldDefinition();

		FieldDefinition[] fields = listDefinition.getFields(printMode);
		FilterDefinition[] filters = QuerySpecificationUtil.buildFilterDefinition(listDefinition.getFilters(), params);
		List<Map<String, Object>> dbEntities = jdbcDataAccessService.findAll(entityMetaData.getDataSourceId(),
				convertTableName(entityMetaData), filters, page, size, listDefinition.getSort());

		// 结果转换
		List<FlyEntityMap> resultList = new ArrayList<>();
		for (Map<String, Object> entity : dbEntities) {
			String pkValue = pkField.getPKValueByFieldName(entity);
			resultList.add(convertToViewMap(entityName, null, null, entity, fields, labelFieldName, pkValue,
					listViewName, printMode));
		}
		return resultList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IFlyDataAccessService#count(java.
	 * lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public long count(String entityName, String uid, String subTableAttr, String formViewName, EntityQueryMap params) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		AssertUtil.parameterEmpty(uid, "uid");
		AssertUtil.parameterEmpty(subTableAttr, "subTableAttr");

		// 构造子表过滤条件
		if (params == null) {
			params = new EntityQueryMap();
		}

		EntityMetaData entityMetaData = getEntityMetaData(entityName);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData");
		EntityMetaData subEntityMetaData = getEntityMetaData(entityMetaData.getField(subTableAttr).getRelationTable());
		FKFieldDefinition fkFieldDefinition = subEntityMetaData.getFkFieldDefinitions()
				.get(entityMetaData.getField(subTableAttr).getRelationField().getName());
		AssertUtil.parameterEmpty(fkFieldDefinition, "fkFieldDefinition");

		Map<String, Object> params2 = new HashMap<>();
		Map<String, Object> pkMap = entityMetaData.getPKFieldDefinition().buildPKMap(uid);
		for (String fkField : fkFieldDefinition.getFkFields()) {
			Object value = pkMap.get(fkFieldDefinition.getRelationFieldByFKField(fkField));
			params2.put(subEntityMetaData.getField(fkField).getFieldName(), value);
		}
		return jdbcDataAccessService.count(subEntityMetaData.getDataSourceId(),
				subEntityMetaData.getTableDefinition().getTableName(), params2);
	}

	@Override
	public List<FlyEntityMap> findAllForSubEntity(String entityName, String uid, String subTableAttr,
			String formViewName, EntityQueryMap params, int page, int size, boolean printMode) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		AssertUtil.parameterEmpty(uid, "uid");
		AssertUtil.parameterEmpty(subTableAttr, "subTableAttr");

		EntityMetaData entityMetaData = getEntityMetaData(entityName);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData");
		SubTableDefinition subTableDefinition = entityMetaData.getFormDefinition(formViewName)
				.getSubTableDefinition(subTableAttr);
		AssertUtil.parameterEmpty(subTableDefinition, "subTableDefinition");
		FieldDefinition subField = entityMetaData.getField(subTableAttr);
		AssertUtil.parameterEmpty(subField, "subField");
		FieldDefinition relationField = subField.getRelationField();
		AssertUtil.parameterEmpty(relationField, "relationField");
		EntityMetaData subEntityMetaData = getEntityMetaData(subField.getRelationTable());
		String subTableName = subEntityMetaData.getTableDefinition().getTableName();
		// 外键
		FKFieldDefinition fkFieldDefinition = subEntityMetaData.getFkFieldDefinitions().get(relationField.getName());
		AssertUtil.parameterEmpty(fkFieldDefinition, "fkFieldDefinition");

		FilterDefinition[] filters = QuerySpecificationUtil.buildFilterDefinition(subTableDefinition.getFilters(),
				params);
		Map<String, Object> pkMap = entityMetaData.getPKFieldDefinition().buildPKMap(uid);
		for (String fkField : fkFieldDefinition.getFkFields()) {
			String fkFieldName = subEntityMetaData.getField(fkField).getFieldName();
			FilterDefinition filter = new FilterDefinition(fkFieldName, SQLOperator.EQUAL);
			filter.setValue(pkMap.get(fkFieldDefinition.getRelationFieldByFKField(fkField)));
			filters = ArrayUtils.add(filters, filter);
		}

		// 查询数据
		List<Map<String, Object>> dbEntities = jdbcDataAccessService.findAll(subEntityMetaData.getDataSourceId(),
				subTableName, filters, page, size, subTableDefinition.getSort());

		// 结果转换
		String labelFieldName = subTableDefinition.getLabelField();
		FieldDefinition[] fields = subTableDefinition.getFields(printMode);
		PKFieldDefinition pkFieldDefinition = subEntityMetaData.getPKFieldDefinition();
		List<FlyEntityMap> resultList = new ArrayList<>();
		for (Map<String, Object> entity : dbEntities) {
			String pkValue = pkFieldDefinition.getPKValueByFieldName(entity);
			resultList.add(convertToViewMap(entityName, uid, subTableAttr, entity, fields, labelFieldName, pkValue,
					formViewName, printMode));
		}
		return resultList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IFlyDataAccessService#count2(java.
	 * lang.String, java.util.Map)
	 */
	@Override
	public long count2(String entityName, Map<String, Object> params) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		EntityMetaData entityMetaData = getEntityMetaData(entityName);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData");

		FilterDefinition[] filters = QuerySpecificationUtil.buildFilterDefinition2(entityMetaData.getAllFields(),
				params);
		long count = jdbcDataAccessService.count(entityMetaData.getDataSourceId(), convertTableName(entityMetaData),
				filters);
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IFlyDataAccessService#count(java.
	 * lang.String, java.util.Map)
	 */
	@Override
	public long count(String entityName, Map<String, String> params) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		EntityMetaData entityMetaData = getEntityMetaData(entityName);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData");

		FilterDefinition[] filters = QuerySpecificationUtil.buildFilterDefinition(entityMetaData.getAllFields(),
				params);
		long count = jdbcDataAccessService.count(entityMetaData.getDataSourceId(), convertTableName(entityMetaData),
				filters);
		return count;
	}

	/**
	 * 返回记录数
	 * 
	 * @param entityName
	 * @param listViewName
	 * @param params
	 * @return
	 */
	@Override
	public long count(String entityName, String listViewName, EntityQueryMap params) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		EntityMetaData entityMetaData = getEntityMetaData(entityName);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData");
		ListDefinition listDefinition = entityMetaData.getListDefinition(listViewName);
		AssertUtil.parameterEmpty(listDefinition, "listDefinition");

		FilterDefinition[] filters = QuerySpecificationUtil.buildFilterDefinition(listDefinition.getFilters(), params);
		long count = jdbcDataAccessService.count(entityMetaData.getDataSourceId(), convertTableName(entityMetaData),
				filters);
		return count;
	}

	@Override
	public Page<FlyEntityMap> findAll(String entityName, String listViewName, EntityQueryMap params, Pageable pageable,
			boolean printMode) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		AssertUtil.parameterEmpty(pageable, "pageable");

		long total = count(entityName, listViewName, params);
		List<FlyEntityMap> content = findAll(entityName, listViewName, params, pageable.getPageNumber(),
				pageable.getPageSize(), printMode);
		Page<FlyEntityMap> pageData = new PageImpl<FlyEntityMap>(content, pageable, total);
		return pageData;
	}

	@Override
	public int delOne(String entityName, String uid) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		AssertUtil.parameterEmpty(uid, "uid");
		EntityMetaData entityMetaData = getEntityMetaData(entityName);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData");

		Map<String, Object> pkParams = entityMetaData.getPKFieldDefinition().buildPKMap(uid);
		return jdbcDataAccessService.delOne(entityMetaData.getDataSourceId(),
				entityMetaData.getTableDefinition().getTableName(), pkParams);
	}

	@Override
	public <T> String saveEntity(T entity) {
		AssertUtil.parameterEmpty(entity, "entity");
		return internalSaveEntity(ClassUtils.getShortClassName(entity.getClass()),
				EntityUtil.entityValueToDBMap(entity), false);
	}

	@Override
	public String saveEntity(String table, Map<String, Object> values) {
		return internalSaveEntity(table, values, true);
	}

	@Override
	public String transUIDToName(String entityName, String uid) {
		Map<String, Object> keyParams = new HashMap<>();
		keyParams.put("UID", uid);
		Map<String, Object> entity = jdbcDataAccessService.findOne(null, EntityMetaDataConstants.TABLENAME_SGLOBAL_NAME,
				keyParams);
		return (String) entity.get("NAME");
	}

	@Override
	public String transNameToUID(String entityName, String name) {
		Map<String, Object> keyParams = new HashMap<>();
		keyParams.put("INTERNAL_TABLE", entityName);
		keyParams.put("NAME", name);
		Map<String, Object> entity = jdbcDataAccessService.findOne(null, EntityMetaDataConstants.TABLENAME_SGLOBAL_NAME,
				keyParams);
		return (String) entity.get("UID");
	}

	/**
	 * 保存ID名称表
	 * 
	 * @param values
	 */
	private void saveGlobalName(final String table, final Object uid, final Object name) {

		Map<String, Object> values = new HashMap<>();
		values.put("INTERNAL_TABLE", table);
		values.put("UID", uid);
		values.put("NAME", name);
		jdbcDataAccessService.saveEntity(null, EntityMetaDataConstants.TABLENAME_SGLOBAL_NAME, values);
		// SGlobalName entity = new SGlobalName();
		// entity.setUid(String.valueOf(uid));
		// entity.setInternalTable(table);
		// entity.setName(String.valueOf(name));
		// globalNameRepo.save(entity);
	}

	/**
	 * 修改名称
	 * 
	 * @param table
	 * @param uid
	 * @param name
	 */
	private void updateGlobalName(final String table, final Object uid, final Object name) {
		Map<String, Object> values = new HashMap<>();
		values.put("NAME", name);
		Map<String, Object> keyParams = new HashMap<>();
		keyParams.put("UID", uid);
		jdbcDataAccessService.updateEntity(null, EntityMetaDataConstants.TABLENAME_SGLOBAL_NAME, keyParams, values);
	}

	private String internalSaveEntity(String table, Map<String, Object> values, boolean processDefault) {
		EntityMetaData metaData = getEntityMetaData(table);
		if (processDefault) {
			updateEntityForDefaultValue(metaData, values);
		}
		int count = jdbcDataAccessService.saveEntity(metaData.getDataSourceId(),
				metaData.getTableDefinition().getTableName(), values);
		if (count != 1) {
			throw new IllegalArgumentException("插入记录数必须是1，实际是[" + count + "]");
		}
		if (metaData.isFlyEntity()) {
			saveGlobalName(table, values.get("UID"), values.get("NAME"));
		}
		return metaData.getPKFieldDefinition().getPKValue(values);
	}

	@Override
	public void updateEntity(String table, String uid, Map<String, Object> values) {
		EntityMetaData metaData = getEntityMetaData(table);
		Map<String, Object> keyParams = metaData.getPKFieldDefinition().buildPKMap(uid);
		if (MapUtils.isNotEmpty(values)) {
			int count = jdbcDataAccessService.updateEntity(metaData.getDataSourceId(),
					metaData.getTableDefinition().getTableName(), keyParams, values);
			if (count != 1) {
				throw new IllegalArgumentException("插入记录数必须是1，实际是[" + count + "]");
			}
			if (metaData.isFlyEntity() && values.containsKey("NAME")) {
				updateGlobalName(table, uid, values.get("NAME"));
			}
		}
	}

	@Override
	public <T> void saveEntity(List<T> entities) {

	}

	@Override
	public String saveEntity(final String table, final String formViewName, EntityMap values) {
		Map<String, Object> entity = values.newEntity(table, null);
		return internalSaveEntity(table, entity, false);
	}

	/**
	 * 从请求参数中构建实体
	 * 
	 * @param metaData
	 * @param formViewName
	 * @param params
	 * @return
	 */
	private Map<String, Object> buildEntityFromRequest(final EntityMetaData metaData, final String formViewName,
			final EntityQueryMap params, final boolean isNew) {
		Map<String, Object> entity = new LinkedHashMap<>();
		for (FieldDefinition field : metaData.getFormDefinition(formViewName).getFields()) {
			if (!FieldDataType.AUTORELATION.equals(field.getDataType())) {
				String[] values = params.get(field.getName());
				if (ArrayUtils.isNotEmpty(values)) {
					if (field.getType() == String.class || FieldDataType.SYSENUM.equals(field.getDataType())) {
						// 属性是字符类型
						entity.put(field.getFieldName(), values[0]);
					} else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
						// 属性是Boolean类型，只要params中存在属性，表示true
						entity.put(field.getFieldName(), true);
					} else if (FieldDataType.SEARCHRELATION.equals(field.getDataType())) {
						// 关联实体
						if (field.relationClassIsFlyEntity()) {
							entity.put(field.getFieldName(), values[0]);
						} else {
							throw new IllegalArgumentException("关联实体只能是Fly Entity");
						}
					} else if (StringUtils.isNotBlank(values[0])) {
						entity.put(field.getFieldName(), ClassUtil.convert(values[0], field.getType()));
					}
				} else if ((field.getType() == Boolean.class || field.getType() == boolean.class)) {
					// 如果为Boolean类型，且params参数中没有则标示为false
					entity.put(field.getFieldName(), false);
				}
			}
		}
		if (isNew) {
			updateEntityForDefaultValue(metaData, entity);
		}
		return entity;
	}

	/**
	 * 更新实体默认值
	 * 
	 * @param metaData
	 * @param entity
	 */
	private void updateEntityForDefaultValue(final EntityMetaData metaData, Map<String, Object> entity) {
		// 新增模式时，为自动赋值字段填值
		for (FieldDefinition field : metaData.getFieldMap().values()) {
			if (field.getGenerator() != null && ObjectUtils.isEmpty(entity.get(field.getFieldName()))) {
				field.generatedValue(entity);
			} else if (!entity.containsKey(field.getFieldName())) {
				if (field.getType() == Boolean.class || field.getType() == boolean.class) {
					if (field.getName().equals("active")) {
						entity.put(field.getFieldName(), true);
					} else {
						entity.put(field.getFieldName(), false);
					}
				}
				// else if (field.getName().equals("created") ||
				// field.getName().equals("updated")) {
				// entity.put(field.getFieldName(), DateUtil.nowSqlTimestamp());
				// } else if (field.getName().equals("createdBy") ||
				// field.getName().equals("updatedBy")) {
				// entity.put(field.getFieldName(),
				// UserContext.getUserSession().getUser().getUid());
				// } else if (field.getName().equals("tenant")) {
				// entity.put(field.getFieldName(),
				// UserContext.getUserSession().getTenant().getUid());
				// }
			}
		}
	}

	@Override
	public String saveEntity(String table, String uid, String subTableAttr, String formViewName, EntityMap values) {
		EntityMetaData metaData = getEntityMetaData(table);
		FieldDefinition subField = metaData.getField(subTableAttr);
		Map<String, Object> entity = values.newSubEntity(metaData, uid, subTableAttr, null);
		return internalSaveEntity(subField.getRelationTable(), entity, false);
	}

	@Override
	public <T> void updateEntity(T entity) {

	}

	@Override
	public void updateEntity(String table, String uid, String formViewName, EntityMap values) {
		EntityMetaData metaData = getEntityMetaData(table);
		Map<String, Object> entity = values.newEntity(metaData, uid);
		updateEntity(table, uid, entity);
	}

	@Override
	public void updateEntity(String table, String uid, String subTableAttr, String subUid, String formViewName,
			EntityMap values) {
		EntityMetaData metaData = getEntityMetaData(table);
		FieldDefinition subField = metaData.getField(subTableAttr);
		Map<String, Object> entity = values.newSubEntity(metaData, uid, subTableAttr, subUid);
		updateEntity(subField.getRelationTable(), subUid, entity);
	}
}
