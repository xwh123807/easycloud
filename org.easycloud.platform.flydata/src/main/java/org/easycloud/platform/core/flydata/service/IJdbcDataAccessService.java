package org.easycloud.platform.core.flydata.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.easycloud.platform.core.metadata.define.FilterDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 数据访问服务
 * 
 * 1、构建sql时，均使用绑定变量方式，提高性能
 * 2、返回结果类型为Map，key为数据库字段名称
 * @author xiangwanhong
 *
 */
public interface IJdbcDataAccessService {

	/**
	 * 按主键查询数据，查询结果只有一条，如果没有查找到，则返回异常
	 * 
	 * @param tableName
	 * @param keyParams
	 * @return
	 */
	Map<String, Object> findOne(String dataSourceId, String tableName, Map<String, Object> keyParams);

	/**
	 * 执行指定sql查询数据，如果语句中使用了绑定变量，paramValues传入变量值
	 * 
	 * @param sql
	 * @param paramValues
	 * @return
	 */
	List<Map<String, Object>> findAll(String dataSourceId, String sql, Collection<Object> paramValues);

	/**
	 * 查询数据，所有条件为And关系
	 * 
	 * @param tableName
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> findAll(String dataSourceId, String tableName, Map<String, Object> params);

	/**
	 * 分页查询数据,返回指定页的数据，所有条件为And关系。不会执行count记录数据处理
	 * 
	 * @param tableName
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> findAll(String dataSourceId, String tableName, Map<String, Object> params, int page, int size, Sort sort);

	/**
	 * 查询记录总数
	 * 
	 * @param tableName
	 * @param params
	 * @return
	 */
	int count(String dataSourceId, String tableName, Map<String, Object> params);

	/**
	 * 分页查询数据，返回分页结果信息，包含总页数
	 * 
	 * @param tableName
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<Map<String, Object>> findAll(String dataSourceId, String tableName, Map<String, Object> params, Pageable pageable);

	/**
	 * 查询数据
	 * 
	 * @param tableName
	 * @param filters
	 * @param page
	 * @param size
	 * @param sort
	 * @return
	 */
	List<Map<String, Object>> findAll(String dataSourceId, String tableName, FilterDefinition[] filters, int page, int size, Sort sort);

	/**
	 * 查询记录总数
	 * 
	 * @param tableName
	 * @param filters
	 * @return
	 */
	long count(String dataSourceId, String tableName, FilterDefinition[] filters);

	/**
	 * 按主键删除记录，成功时删除一条数据，当记录不存时，返回异常
	 * @param tableName
	 * @param pkParams
	 * @return
	 */
	int delOne(String dataSourceId, String tableName, Map<String, Object> pkParams);
	
	/**
	 * 删除多条记录
	 * @param tableName
	 * @param params
	 * @return
	 */
	int del(String dataSourceId, String tableName, Map<String, Object> params);

	/**
	 * 新增单个实体
	 * @param tableName
	 * @param values
	 * @return
	 */
	int saveEntity(String dataSourceId, String tableName, Map<String, Object> values);
	
	/**
	 * 批量新增多个实体
	 * @param tableName
	 * @param entities
	 */
	void saveEntity(String dataSourceId, String tableName, List<Map<String, Object>> entities);

	/**
	 * 按主键过滤修改指定实体
	 * @param table
	 * @param keyParams
	 * @param values
	 * @return
	 */
	int updateEntity(String dataSourceId, String table, Map<String, Object> keyParams, Map<String, Object> values);
}
