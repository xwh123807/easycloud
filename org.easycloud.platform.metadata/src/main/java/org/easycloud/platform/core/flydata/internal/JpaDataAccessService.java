package org.easycloud.platform.core.flydata.internal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.myfly.platform.core.domain.SKeyEntity;
import org.myfly.platform.core.flydata.service.IJpaAccessService;
import org.myfly.platform.core.metadata.internal.EntityMetaData;
import org.myfly.platform.core.metadata.service.IEntityMetaDataService;
import org.myfly.platform.core.utils.AssertUtil;
import org.myfly.platform.core.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

/**
 * 通用数据访问服务
 * 
 * @author xiangwanhong
 *
 */

@Service
public class JpaDataAccessService implements IJpaAccessService {
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private IEntityMetaDataService entityMetaDataService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.myfly.platform.system.service.IDataAccessService#
	 * getSimpleJpaRepository(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	public JpaRepository getSimpleJpaRepository(final String entityName) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		Class<?> domainClass = entityMetaDataService.getEntityMetaData(entityName).getEntityClass();
		AssertUtil.parameterEmpty(domainClass, "domainClass", "找不到名称为[" + entityName + "]的实体类.");
		return new SimpleJpaRepository<>(domainClass, entityManager);
	}

	@SuppressWarnings("rawtypes")
	public JpaSpecificationExecutor getSimpleJpaSpecificationRepository(final String entityName) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		Class<?> domainClass = entityMetaDataService.getEntityMetaData(entityName).getEntityClass();
		AssertUtil.parameterEmpty(domainClass, "domainClass", "找不到名称为[" + entityName + "]的实体类.");
		return new SimpleJpaRepository<>(domainClass, entityManager);
	}

	@SuppressWarnings("rawtypes")
	public <T> SimpleJpaRepository getSimpleJpaRepository(final Class<T> entityClass) {
		AssertUtil.parameterEmpty(entityClass, "entityClass");
		return new SimpleJpaRepository<>(entityClass, entityManager);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.service.IDataAccessService#findOne(java.lang.
	 * String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public <T> T findOne(final String tableName, final Serializable uid) {
		AssertUtil.parameterEmpty(tableName, "tableName");
		AssertUtil.parameterEmpty(uid, "uid");
		try {
			return (T) getSimpleJpaRepository(tableName).getOne(uid);
		} catch (EntityNotFoundException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.service.IDataAccessService#findOne(java.lang.
	 * Class, java.io.Serializable)
	 */
	@SuppressWarnings("unchecked")
	public <T> T findOne(final Class<T> entityClass, final Serializable uid) {
		AssertUtil.parameterEmpty(entityClass, "entityClass");
		AssertUtil.parameterEmpty(uid, "uid");
		try {
			return (T) getSimpleJpaRepository(entityClass).getOne(uid);
		} catch (EntityNotFoundException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.service.IDataAccessService#delOne(java.lang.
	 * String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public int delOne(final String tableName, final Serializable uid) {
		AssertUtil.parameterEmpty(tableName, "tableName");
		AssertUtil.parameterEmpty(uid, "uid");
		getSimpleJpaRepository(tableName).delete(uid);
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.service.IDataAccessService#delOne(java.lang.
	 * Class, java.io.Serializable)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public <T> void delOne(Class<T> entityClass, Serializable uid) {
		AssertUtil.parameterEmpty(entityClass, "entityClass");
		AssertUtil.parameterEmpty(uid, "uid");
		getSimpleJpaRepository(entityClass).delete(uid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.service.IDataAccessService#delOne(java.lang.
	 * Object)
	 */
	@Override
	@Transactional
	public <T> void delOne(T entity) {
		AssertUtil.parameterEmpty(entity, "entity");
		entityManager.remove(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.service.IDataAccessService#delEntity(java.lang.
	 * String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void delEntity(final String tableName, final Serializable uid) {
		AssertUtil.parameterEmpty(tableName, "tableName");
		AssertUtil.parameterEmpty(uid, "uid");
		getSimpleJpaRepository(tableName).delete(uid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.service.IDataAccessService#saveEntity(org.myfly
	 * .platform.system.domain.IKeyEntity)
	 */
	@Override
	@Transactional
	public <T> T saveEntity(final T entity) {
		AssertUtil.parameterEmpty(entity, "entity");
		beforeInsertFlyEntity(entity);
		entityManager.persist(entity);
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.service.IDataAccessService#saveEntity(java.lang
	 * .String, java.util.Map)
	 */
	@Override
	@Transactional
	public <T> T saveEntity(String tableName, Map<String, Object> values) {
		EntityMetaData metaData = entityMetaDataService.getEntityMetaData(tableName);
		return internalSaveEntity(tableName, values, metaData);
	}

	/**
	 * 新增实体保存
	 * 
	 * @param tableName
	 * @param values
	 * @param metaData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> T internalSaveEntity(final String tableName, final Map<String, Object> values,
			EntityMetaData metaData) {
		T entity = null;
		try {
			// 新增模式
			entity = EntityUtil.buildNewEntity(tableName, metaData, values);
			beforeInsertFlyEntity(entity);
			entity = saveEntity(entity);
		} catch (Exception e) {
			throw new RuntimeException("保存实体[" + tableName + "]失败，错误信息：" + e.getMessage());
		}
		return entity;
	}

	private <T> void internalSaveEntityOnly(final String tableName, final Map<String, Object> values,
			EntityMetaData metaData) {
		try {
			// 新增模式
			T entity = null;
			entity = EntityUtil.buildNewEntity(tableName, metaData, values);
			beforeInsertFlyEntity(entity);
			saveEntity(entity);
		} catch (Exception e) {
			throw new RuntimeException("保存实体[" + tableName + "]失败，错误信息：" + e.getMessage());
		}
	}

	/**
	 * 修改实体保存
	 * 
	 * @param tableName
	 * @param values
	 * @param metaData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> T internalUpdateEntity(final String tableName, final Serializable uid, final Map<String, Object> values,
			EntityMetaData metaData) {
		T entity = null;
		try {
			entity = findOne(tableName, uid);
			AssertUtil.parameterEmpty(entity, "entity", "找不到[" + tableName + "." + uid.toString() + "]对应实体。");
			EntityUtil.updateEntity(entity, metaData, values);
			getSimpleJpaRepository(tableName).save(entity);
		} catch (Exception e) {
			throw new RuntimeException("保存实体[" + tableName + "]失败，错误信息：" + e.getMessage());
		}
		return entity;
	}

	private <T> void internalUpdateEntityOnly(final String tableName, final Serializable uid,
			final Map<String, Object> values, EntityMetaData metaData) {
		try {
			T entity = null;
			entity = findOne(tableName, uid);
			AssertUtil.parameterEmpty(entity, "entity", "找不到[" + tableName + "." + uid.toString() + "]对应实体。");
			EntityUtil.updateEntity(entity, metaData, values);
			getSimpleJpaRepository(tableName).save(entity);
		} catch (Exception e) {
			throw new RuntimeException("保存实体[" + tableName + "]失败，错误信息：" + e.getMessage());
		}
	}

	@Override
	@Transactional
	public <T> T updateEntity(String tableName, Serializable uid, Map<String, Object> values) {
		AssertUtil.parameterEmpty(tableName, "tableName");
		AssertUtil.parameterEmpty(uid, "uid");
		EntityMetaData metaData = entityMetaDataService.getEntityMetaData(tableName);
		return internalUpdateEntity(tableName, uid, values, metaData);
	}

	@Override
	@Transactional
	public <T> T updateEntity(T entity) {
		AssertUtil.parameterEmpty(entity, "entity");
		entityManager.merge(entity);
		return entity;
	}

	@Override
	@Transactional
	public <T> T updateEntity(Serializable uid, T entity) {
		EntityMetaData metaData = entityMetaDataService.getEntityMetaData(entity.getClass().getName());
		metaData.getPKFieldDefinition().setPKValue(entity, uid);
		return updateEntity(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.service.IDataAccessService#findAll(java.lang.
	 * String)
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(final String tableName) {
		AssertUtil.parameterEmpty(tableName, "tableName");
		return getSimpleJpaRepository(tableName).findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.service.IDataAccessService#findAll(java.lang.
	 * Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findAll(Class<T> entityClass) {
		AssertUtil.parameterEmpty(entityClass, "entityClass");
		return getSimpleJpaRepository(entityClass).findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.service.IDataAccessService#findAll(java.lang.
	 * String, org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Page<T> findAll(final String tableName, final Pageable pageable) {
		return getSimpleJpaRepository(tableName).findAll(pageable);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findAll(Class<T> entityClass, Sort sort) {
		return getSimpleJpaRepository(entityClass).findAll(sort);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.service.IDataAccessService#findAll(java.lang.
	 * Class, org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Page<T> findAll(Class<T> entityClass, Pageable pageable) {
		AssertUtil.parameterEmpty(entityClass, "entityClass");
		return getSimpleJpaRepository(entityClass).findAll(pageable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.service.IDataAccessService#findAll(java.lang.
	 * String, org.springframework.data.jpa.domain.Specifications)
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(final String tableName, final Specifications<T> spec) {
		AssertUtil.parameterEmpty(tableName, "tableName");
		return getSimpleJpaSpecificationRepository(tableName).findAll(spec);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public long count(String tableName, Specifications specifications) {
		return getSimpleJpaSpecificationRepository(tableName).count(specifications);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findAll(Class<T> entityClass, Specifications<?> spec) {
		AssertUtil.parameterEmpty(entityClass, "entityClass");
		return getSimpleJpaRepository(entityClass).findAll(spec);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.service.IDataAccessService#findAll(java.lang.
	 * String, org.springframework.data.jpa.domain.Specification,
	 * org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Page<T> findAll(final String tableName, Specifications<T> spec, Pageable pageable) {
		AssertUtil.parameterEmpty(tableName, "tableName");
		return getSimpleJpaSpecificationRepository(tableName).findAll(spec, pageable);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Page<T> findAll(Class<T> entityClass, Specifications<?> spec, Pageable pageable) {
		AssertUtil.parameterEmpty(entityClass, "entityClass");
		return getSimpleJpaRepository(entityClass).findAll(spec, pageable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.service.IDataAccessService#findAll(java.lang.
	 * String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findAll(final String tableName, final Map<String, Object> params) {
		return (List<T>) findAll(tableName, QuerySpecificationUtil.buildQuerySpecifications(params));
	}

	@Override
	public <T> List<T> findAll(Class<T> entityClass, Map<String, Object> params) {
		return findAll(entityClass, QuerySpecificationUtil.buildQuerySpecifications(params));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.service.IDataAccessService#findAll(java.lang.
	 * String, java.util.Map, org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Page<T> findAll(final String tableName, final Map<String, Object> params, final Pageable pageable) {
		return (Page<T>) findAll(tableName, QuerySpecificationUtil.buildQuerySpecifications(params), pageable);
	}

	@Override
	public <T> Page<T> findAll(Class<T> entityClass, Map<String, Object> params, Pageable pageable) {
		return findAll(entityClass, QuerySpecificationUtil.buildQuerySpecifications(params), pageable);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public <T> void batchSaveEntity(final String tableName, final List<T> batchList) {
		getSimpleJpaRepository(tableName).save(batchList);
	}

	@Override
	@Transactional
	public <T> void batchSaveEntity(List<T> batchList) {
		getSimpleJpaRepository(batchList.get(0).getClass()).save(batchList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.service.IDataAccessService#transNameToUID(java.
	 * lang.String, java.lang.String)
	 */
	@Override
	public String transNameToUID(final String tableName, final String name) {
		AssertUtil.parameterEmpty(tableName, "tableName");
		AssertUtil.parameterEmpty(name, "name");
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		List<? extends SKeyEntity> items = findAll(tableName, params);
		if (items.size() != 1) {
			AssertUtil.parameterInvalide("name",
					"实体[" + tableName + "]只能有一条记录Name=[" + name + "]，实际是[" + items.size() + "]条");
		}
		return items.get(0).getUid();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.service.IDataAccessService#transUIDToName(java.
	 * lang.String, java.lang.String)
	 */
	@Override
	public String transUIDToName(final String tableName, final String uid) {
		//TODO 
		return null;
//		AssertUtil.parameterEmpty(tableName, "tableName");
//		AssertUtil.parameterEmpty(uid, "uid");
//		SBaseEntity entity = findOne(tableName, uid);
//		AssertUtil.recordNotFound(entity, tableName, "uid", uid);
//		return entity.getName();
	}

	@Override
	public void flush() {
		entityManager.flush();
	}

	/**
	 * 新增FlyEntity实体前，自动填充数据
	 * 
	 * @param entity
	 */
	private void beforeInsertFlyEntity(Object entity) {
		SKeyEntity entity2 = null;
		if (entity instanceof SKeyEntity) {
			entity2 = (SKeyEntity) entity;
			if (StringUtils.isBlank(entity2.getUid())) {
				entity2.setUid(UUIDUtil.newUUID());
			}
		}
	}
}
