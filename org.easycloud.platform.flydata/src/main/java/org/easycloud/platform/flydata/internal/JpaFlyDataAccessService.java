package org.easycloud.platform.flydata.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.easycloud.platform.flydata.service.EntityMap;
import org.easycloud.platform.flydata.service.EntityQueryMap;
import org.easycloud.platform.flydata.service.FlyEntityMap;
import org.easycloud.platform.metadata.annotation.entity.FieldDataType;
import org.easycloud.platform.metadata.annotation.entity.IKeyEntity;
import org.easycloud.platform.metadata.define.entity.FieldDefinition;
import org.easycloud.platform.metadata.define.entity.PKFieldDefinition;
import org.easycloud.platform.metadata.define.view.FilterDefinition;
import org.easycloud.platform.metadata.define.view.ListDefinition;
import org.easycloud.platform.metadata.define.view.SQLOperator;
import org.easycloud.platform.metadata.define.view.SubTableDefinition;
import org.easycloud.platform.metadata.service.EntityMetaData;
import org.easycloud.platform.metadata.utils.AssertUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service("jpaFlyDataAccessService")
@Transactional
public class JpaFlyDataAccessService extends AbstractFlyDataAccessService {
	@Override
	public <T> T findOne(String entityName, String uid) {
		EntityMetaData metaData = getEntityMetaData(entityName);
		Serializable pkValue = metaData.getPKFieldDefinition().buildPK(uid);
		return getJpaDataAccessService().findOne(entityName, pkValue);
	}

	@Override
	public <T> T findOne(String entityName, Map<String, Object> keyParams) {
		List<T> list = getJpaDataAccessService().findAll(entityName, keyParams);
		if (list == null || list.size() != 1) {
			throw new IllegalArgumentException("查询结果只能为一条.");
		}
		T result = list.get(0);
		return result;
	}

	@Override
	public <T> List<T> findAll2(String entityName, Map<String, Object> params) {
		return getJpaDataAccessService().findAll(entityName, params);
	}

	@Override
	@Transactional
	public FlyEntityMap findOne(String entityName, String uid, String formViewName, boolean printMode) {
		EntityMetaData entityMetaData = getEntityMetaData(entityName);
		Serializable pkValue = entityMetaData.getPKFieldDefinition().buildPK(uid);
		Object result = getJpaDataAccessService().findOne(entityName, pkValue);
		if (result != null) {
			if (result instanceof IKeyEntity) {
				try {
					String value = ((IKeyEntity) result).getUid();
					Assert.hasLength(value);
				} catch (Exception e) {
					return null;
				}
			}
			return convertToViewMap(entityName, null, null, result,
					entityMetaData.getFormDefinition(formViewName).getFields(), formViewName, uid, formViewName,
					printMode);
		} else {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public long count(String entityName, String listViewName, EntityQueryMap params) {
		Specifications specifications = getEntityQuerySpecifications(entityName, listViewName, params);
		return getJpaDataAccessService().count(entityName, specifications);
	}

	@Override
	public long count(String entityName, String uid, String subTableAttr, String formViewName, EntityQueryMap params) {
		// TODO Auto-generated method stub
		return 0;
	}

	private boolean hasFilter(FilterDefinition[] filterDefinitions, String paramName) {
		if (ArrayUtils.isNotEmpty(filterDefinitions)) {
			for (FilterDefinition filter : filterDefinitions) {
				if (filter.getField().getName().equals(paramName)) {

				}
			}
		}
		return false;
	}

	/**
	 * 获取实体查询定义
	 * 
	 * @param entityName
	 * @param params
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Specifications getEntityQuerySpecifications(String entityName, String view, EntityQueryMap params) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		FilterDefinition[] filterDefinitions = getEntityMetaData(entityName).getListDefinition(view).getFilters();
		// 添加没有在FilterDefinition中定义的过滤条件，缺省都为And，如果有多值缺省为In
		if (MapUtils.isNotEmpty(params)) {
			for (String paramName : params.keySet()) {
				FieldDefinition field = getEntityMetaData(entityName).getField(paramName);
				if ((field != null) && !hasFilter(filterDefinitions, paramName)) {
					SQLOperator sqlOperator = (params.get(paramName).length == 1) ? SQLOperator.EQUAL : SQLOperator.IN;
					FilterDefinition filter = new FilterDefinition(field, sqlOperator);
					filterDefinitions = ArrayUtils.add(filterDefinitions, filter);
				}
			}
		}
		Specifications specifications = QuerySpecificationUtil.buildQuerySpecifications(filterDefinitions, params);
		return specifications;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Page<FlyEntityMap> findAll(String entityName, String listViewName, EntityQueryMap params, Pageable pageable,
			boolean printMode) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		EntityMetaData metaData = getEntityMetaData(entityName);
		ListDefinition listDefinition = metaData.getListDefinition(listViewName);
		Specifications specifications = getEntityQuerySpecifications(entityName, listViewName, params);
		Page<Object> pageData1 = getJpaDataAccessService().findAll(entityName, specifications, pageable);
		List list = new ArrayList<>();
		for (Object entity : pageData1.getContent()) {
			String pkValue = metaData.getPKFieldDefinition().getPKValue(entity);
			list.add(convertToViewMap(entityName, null, null, entity, listDefinition.getFields(),
					listDefinition.getLabelField(), pkValue, listViewName, printMode));
		}
		return new PageImpl<>(list, pageable, pageData1.getTotalElements());
	}

	@Override
	public List<FlyEntityMap> findAllForSubEntity(String entityName, String uid, String subTableAttr,
			String formViewName, EntityQueryMap params, int page, int size, boolean printMode) {
		Pageable pageable = new PageRequest(page, size);
		return internalFindAllForSubEntity(entityName, uid, subTableAttr, formViewName, params, pageable, false,
				printMode);
	}

	@Override
	public List<FlyEntityMap> findAllForSubEntity(String entityName, String uid, String subTableAttr,
			String formViewName, EntityQueryMap params, boolean printMode) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		AssertUtil.parameterEmpty(uid, "uid");
		AssertUtil.parameterEmpty(subTableAttr, "subTableAttr");
		return internalFindAllForSubEntity(entityName, uid, subTableAttr, formViewName, params, null, false, printMode);
	}

	@Override
	public Page<FlyEntityMap> findAllForSubEntityWithPage(String entityName, String uid, String subTableAttr,
			String formViewName, EntityQueryMap params, Pageable pageable, boolean printMode) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		AssertUtil.parameterEmpty(uid, "uid");
		AssertUtil.parameterEmpty(subTableAttr, "subTableAttr");
		return internalFindAllForSubEntity(entityName, uid, subTableAttr, formViewName, params, pageable, true,
				printMode);
	}

	/**
	 * 子表数据查询，根据needPageable参数控制返回数据是否分页
	 * 
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param params
	 * @param pageable
	 * @param needPageable
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T> T internalFindAllForSubEntity(String entityName, String uid, String subTableAttr, String view,
			EntityQueryMap params, Pageable pageable, boolean needPageable, boolean printMode) {

		EntityMetaData entityMetaData = getEntityMetaData(entityName);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData");
		SubTableDefinition subTable = entityMetaData.getFormDefinition(view).getSubTableDefinition(subTableAttr);
		Assert.notNull(subTable, "实体[" + entityName + "]在视图[" + view + "]下没有显示定义子表[" + subTableAttr + "]");
		FieldDefinition subField = entityMetaData.getField(subTableAttr);
		String subentityName = subField.getRelationTable();
		AssertUtil.parameterEmpty(subField, "subField", "实体[" + entityName + "]不存在属性[" + subTableAttr + "]");
		final String subTableField = subField.getRelationField().getName();
		AssertUtil.parameterEmpty(subTableField, "subTableField");
		EntityMetaData subEntityMetaData = getEntityMetaData(subentityName);

		if (MapUtils.isEmpty(params)) {
			params = new EntityQueryMap();
		}
		if (FieldDataType.FLYMDRELATION.equals(subField.getDataType())) {
			params.put(subField.getRelationField().getName(), new String[] { uid });
		}
		Specifications specifications = getEntityQuerySpecifications(subentityName, view, params);
		if (FieldDataType.MDRELATION.equals(subField.getDataType())) {
			try {
				final Object entity = Class.forName(subField.getRelationField().getRelationClass()).newInstance();
				entityMetaData.getPKFieldDefinition().setPKValue(entity, uid);
				Specification entityFilter = new Specification() {
					@Override
					public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
						return cb.equal(root.get(subTableField), entity);
					}
				};
				if (specifications == null) {
					specifications = Specifications.where(entityFilter);
				} else {
					specifications.and(entityFilter);
				}
			} catch (Exception e) {
				throw new IllegalArgumentException("构造查询条件失败，错误信息：" + e.getMessage());
			}
		}
		PKFieldDefinition pkFieldDefinition = subEntityMetaData.getPKFieldDefinition();
		if (needPageable) {
			Page pageData1 = getJpaDataAccessService().findAll(subentityName, specifications, pageable);
			List<Map<String, String>> list = new ArrayList<>();
			for (Object entity : pageData1.getContent()) {
				String pkValue = pkFieldDefinition.getPKValue(entity);
				list.add(convertToViewMap(entityName, uid, subTableAttr, entity, subTable.getFields(),
						subTable.getLabelField(), pkValue, view, printMode));
			}
			return (T) new PageImpl(list, pageable, pageData1.getTotalElements());
		} else {
			List result = getJpaDataAccessService().findAll(subentityName, specifications);
			List list = new ArrayList<>();
			for (Object entity : result) {
				String pkValue = pkFieldDefinition.getPKValue(entity);
				list.add(convertToViewMap(entityName, uid, subTableAttr, entity, subTable.getFields(),
						subTable.getLabelField(), pkValue, view, printMode));
			}
			return (T) list;
		}
	}

	@Override
	@Transactional
	public int delOne(String entityName, String uid) {
		return getJpaDataAccessService().delOne(entityName, uid);
	}

	@Override
	public String saveEntity(String entityName, String formViewName, EntityMap values) {
		Assert.hasLength(entityName);
		Assert.notEmpty(values);
		Object entity = values.newEntity(entityName, null);
		return saveEntity(entity);
	}

	@Override
	@Transactional
	public <T> String saveEntity(T entity) {
		Assert.notNull(entity);
		getJpaDataAccessService().saveEntity(entity);
		return getEntityMetaData(entity.getClass().getName()).getPKFieldDefinition().getPKValue(entity);
	}

	@Override
	public String saveEntity(String entityName, Map<String, Object> values) {
		Object entity = EntityUtil.buildNewEntity(entityName, null, values);
		return saveEntity(entity);
	}

	@Override
	public String saveEntity(String entityName, String uid, String subTableAttr, String formViewName,
			EntityMap values) {
		Assert.notEmpty(values);
		Object entity = values.newSubEntity(entityName, uid, subTableAttr, null);
		return saveEntity(entity);
	}

	@Override
	@Transactional
	public <T> void saveEntity(List<T> entities) {
		for (T entity : entities) {
			saveEntity(entity);
		}
	}

	@Override
	@Transactional
	public <T> void updateEntity(T entity) {
		Assert.notNull(entity);
		getJpaDataAccessService().updateEntity(entity);
	}

	@Override
	@Transactional
	public void updateEntity(String entityName, String uid, String subTableAttr, String subUid, String formViewName,
			EntityMap values) {
		Assert.notEmpty(values);
		Object entity = values.newSubEntity(entityName, uid, subTableAttr, subUid);
		getJpaDataAccessService().updateEntity(entity);
	}

	@Override
	@Transactional
	public void updateEntity(String entityName, String uid, String formViewName, EntityMap values) {
		Object entity = values.newEntity(entityName, uid);
		getJpaDataAccessService().updateEntity(uid, entity);
	}

	@Override
	@Transactional
	public void updateEntity(String entityName, String uid, Map<String, Object> values) {
		getJpaDataAccessService().updateEntity(entityName, uid, values);
	}

	@Override
	public long count(String entityName, Map<String, String> params) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long count2(String entityName, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return 0;
	}
}
