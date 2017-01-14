package org.easycloud.platform.flydata.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public interface IJpaAccessService {
	/**
	 * 获取JpaRepository
	 * 
	 * @param entityName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	JpaRepository getSimpleJpaRepository(String entityName);

	/**
	 * 获取JpaRepository
	 * 
	 * @param entityClass
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	<T> SimpleJpaRepository getSimpleJpaRepository(Class<T> entityClass);

	/**
	 * 查找指定表中主键UID对应的记录
	 * 
	 * @param tableName
	 * @param uid
	 * @return
	 */
	<T> T findOne(String tableName, Serializable uid);

	/**
	 * 查找指定表中主键UID对应的记录
	 * 
	 * @param entityClass
	 * @param uid
	 *            主键
	 * @return
	 */
	<T> T findOne(Class<T> entityClass, Serializable uid);

	/**
	 * 查找指定表中所有的数据
	 * 
	 * @param tableName
	 * @return
	 */
	<T> List<T> findAll(String tableName);
	
	/**
	 * 查找指定表中所有的数据
	 * @param entityClass
	 * @return
	 */
	<T> List<T> findAll(Class<T> entityClass);

	/**
	 * 查找指定表中所有数据，分页返回
	 * 
	 * @param tableName
	 * @param pageable
	 * @return
	 */
	<T> Page<T> findAll(String tableName, Pageable pageable);
	
	/**
	 * 查找指定表中所有数据，分页返回
	 * 
	 * @param tableName
	 * @param pageable
	 * @return
	 */
	<T> Page<T> findAll(Class<T> entityClass, Pageable pageable);

	/**
	 * 使用语句删除实体，返回变更记录数
	 * 
	 * @param tableName
	 * @param uid
	 * @return
	 */
	int delOne(String tableName, Serializable uid);
	
	/**
	 * 删除实体
	 * @param entity
	 */
	<T> void delOne(T entity);
	
	/**
	 * 
	 * @param entityClass
	 * @param uid
	 */
	<T> void delOne(Class<T> entityClass, Serializable uid);

	/**
	 * 删除记录，返回删除的记录数
	 * 
	 * @param tableName
	 * @param uid
	 * @return
	 */
	void delEntity(String tableName, Serializable uid);
	
	/**
	 * 保存实体
	 * 
	 * @param entity
	 * @return
	 */
	<T> T saveEntity(T entity);

	/**
	 * 保存实体，返回保存后的实体
	 * 
	 * @param tableName
	 * @param values
	 * @return
	 */
	<T> T saveEntity(String tableName, Map<String, Object> values);
	
	/**
	 * 修改实体
	 * @param tableName
	 * @param values
	 * @return
	 */
	<T> T updateEntity(String tableName, Serializable uid, Map<String, Object> values);
	
	/**
	 * 修改实体
	 * @param uid
	 * @param entity
	 * @return
	 */
	<T> T updateEntity(Serializable uid, T entity);
	
	/**
	 * 修改实体
	 * @param entity
	 * @return
	 */
	<T> T updateEntity(T entity);

	/**
	 * 分页查询实体数据
	 * 
	 * @param tableName
	 *            主实体名
	 * @param spec
	 *            查询定义
	 * @param pageable
	 * @return
	 */
	<T> Page<T> findAll(String tableName, Specifications<T> spec, Pageable pageable);
	
	<T> Page<T> findAll(Class<T> entityClass, Specifications<?> spec, Pageable pageable);

	/**
	 * 实体数据查询，返回满足条件的所有记录
	 * 
	 * @param tableName
	 * @param spec
	 * @return
	 */
	<T> List<T> findAll(String tableName, Specifications<T> spec);
	
	<T> List<T> findAll(Class<T> entityClass, Specifications<?> spec);
	
	<T> List<T> findAll(Class<T> entityClass, Sort sort);

	/**
	 * 实体数据查询
	 * 
	 * @param tableName
	 * @param params
	 * @return
	 */
	<T> List<T> findAll(String tableName, Map<String, Object> params);

	<T> List<T> findAll(Class<T> entityClass, Map<String, Object> params);
	/**
	 * 实体数据分页查询
	 * 
	 * @param tableName
	 * @param params
	 * @param pageable
	 * @return
	 */
	<T> Page<T> findAll(String tableName, Map<String, Object> params, Pageable pageable);
	
	<T> Page<T> findAll(Class<T> entityClass, Map<String, Object> params, Pageable pageable);

	/**
	 * 批量保存实体
	 * 
	 * @param tableName
	 * @param batchList
	 */
	<T> void batchSaveEntity(final String tableName, final List<T> batchList);
	
	/**
	 * 批量保存实体
	 * @param batchList
	 */
	<T> void batchSaveEntity(final List<T> batchList);

	/**
	 * 根据名称获取对应实体的UID，实体Name字段必须唯一
	 * 
	 * @param tableName
	 * @param name
	 * @return
	 */
	String transNameToUID(String tableName, String name);

	/**
	 * 根据UID获取对应实体的名称
	 * 
	 * @param tableName
	 * @param uid
	 * @return
	 */
	String transUIDToName(String tableName, String uid);

	@SuppressWarnings("rawtypes")
	long count(String tableName, Specifications specifications);

	/**
	 * 刷新数据，将事务提交
	 */
	void flush();

}
