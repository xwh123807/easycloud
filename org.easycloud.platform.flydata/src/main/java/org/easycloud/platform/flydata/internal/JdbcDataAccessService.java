package org.easycloud.platform.flydata.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easycloud.platform.flydata.service.DataSourceInfo;
import org.easycloud.platform.flydata.service.IEntityCacheService;
import org.easycloud.platform.flydata.service.IJdbcDataAccessService;
import org.easycloud.platform.metadata.define.view.FilterDefinition;
import org.easycloud.platform.metadata.utils.AssertUtil;
import org.easycloud.platform.metadata.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 数据访问服务，使用Jdbc直接访问数据库，返回结果为Map
 * 
 * @author xiangwanhong
 *
 */
@Service
public class JdbcDataAccessService implements IJdbcDataAccessService {
	private static Log log = LogFactory.getLog(JdbcDataAccessService.class);
	@Autowired
	private JdbcTemplate defaultJdbcTemplate;

	@Autowired
	private IEntityCacheService entityCacheService;

	private DataSourceInfo getDataSourceInfo(String dataSourceId) {
		return entityCacheService.getEntity(DataSourceInfo.class.getName(), dataSourceId);
	}

	/**
	 * 获取数据访问模板
	 * 
	 * @param dataSourceId
	 * @return
	 */
	private JdbcTemplate getJdbcTemplate(String dataSourceId) {
		if (StringUtils.isBlank(dataSourceId)) {
			return defaultJdbcTemplate;
		} else {
			try {
				return new JdbcTemplate(ConnectionFactory.getDataSource(getDataSourceInfo(dataSourceId)));
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}

	/**
	 * 构造查询绑定变量sql
	 * 
	 * @param tableName
	 * @param whereSql
	 * @param page
	 * @param size
	 * @param sort
	 * @param paramValues
	 * @return
	 */
	private String buildSelectSql(final String tableName, String whereSql, final int page, final int size,
			final Sort sort, List<Object> paramValues) {
		String sql = "SELECT * FROM " + tableName + whereSql;
		// 构建排序子句
		if (sort != null) {
			String orderStr = "";
			Iterator<Order> orders = sort.iterator();
			while (orders.hasNext()) {
				Order order = orders.next();
				orderStr += "," + order.getProperty() + " " + order.getDirection().name();
			}
			if (StringUtils.isNotBlank(orderStr)) {
				orderStr = orderStr.substring(1);
				sql += " ORDER BY " + orderStr;
			}
		}
		// 构建分页子句
		sql += buildPageSql(page, size);
		return sql;
	}

	/**
	 * 根据查询条件定义构造查询绑定变量sql
	 * 
	 * @param tableName
	 * @param filters
	 * @param page
	 * @param size
	 * @param sort
	 * @param paramValues
	 * @return
	 */
	private String buildSelectSql(final String tableName, final FilterDefinition[] filters, final int page,
			final int size, final Sort sort, List<Object> paramValues) {
		String whereSql = QuerySpecificationUtil.buildQueryWhereSql(filters, paramValues);
		return buildSelectSql(tableName, whereSql, page, size, sort, paramValues);
	}

	/**
	 * 根据查询条件值对构造查询绑定变量sql，条件都为And关系
	 * 
	 * @param tableName
	 * @param params
	 *            查询条件值对
	 * @param page
	 * @param size
	 * @param sort
	 *            排序定义
	 * @param paramValues
	 *            条件值
	 * @return
	 */
	private String buildSelectSql(final String tableName, final Map<String, Object> params, final int page,
			final int size, final Sort sort, List<Object> paramValues) {
		String whereSql = QuerySpecificationUtil.buildQueryWhereSql(params, paramValues);
		return buildSelectSql(tableName, whereSql, page, size, sort, paramValues);
	}

	/**
	 * 构建分页子句
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	private String buildPageSql(final int page, final int size) {
		String pageSql = "";
		if (size > 0) {
			if (page == 0) {
				pageSql = " fetch first " + size + " rows only";
			} else {
				pageSql = " offset " + (page * size) + " rows fetch next " + size + " rows only";
			}
		}
		return pageSql;
	}

	/**
	 * 根据查询条件值对构造查询记录总数绑定变量sql
	 * 
	 * @param tableName
	 * @param params
	 * @param paramsValues
	 * @return
	 */
	private String buildCountSql(final String tableName, final Map<String, Object> params, List<Object> paramsValues) {
		return "SELECT COUNT(*) FROM " + tableName + QuerySpecificationUtil.buildQueryWhereSql(params, paramsValues);
	}

	/**
	 * 根据查询条件定义构造查询记录数绑定变量sql
	 * 
	 * @param tableName
	 * @param filters
	 * @param paramsValues
	 * @return
	 */
	private String buildCountSql(final String tableName, final FilterDefinition[] filters, List<Object> paramsValues) {
		return "SELECT COUNT(*) FROM " + tableName + QuerySpecificationUtil.buildQueryWhereSql(filters, paramsValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IJdbcDataAccessService#count(java.
	 * lang.String, java.util.Map)
	 */
	@Override
	public int count(String dataSourceId, final String tableName, final Map<String, Object> params) {
		AssertUtil.parameterEmpty(tableName, "tableName");

		List<Object> paramsValues = new ArrayList<>();
		String sql = buildCountSql(tableName, params, paramsValues);
		Map<String, Object> result = findOne(getJdbcTemplate(dataSourceId), sql, paramsValues);
		return (int) result.values().toArray(new Object[] {})[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IJdbcDataAccessService#count(java.
	 * lang.String,
	 * org.myfly.platform.system.metadata.define.FilterDefinition[])
	 */
	@Override
	public long count(String dataSourceId, String tableName, FilterDefinition[] filters) {
		AssertUtil.parameterEmpty(tableName, "tableName");

		List<Object> paramsValues = new ArrayList<>();
		String sql = buildCountSql(tableName, filters, paramsValues);
		Map<String, Object> result = findOne(getJdbcTemplate(dataSourceId), sql, paramsValues);
		return (int) result.values().toArray(new Object[] {})[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IJdbcDataAccessService#findOne(
	 * java.lang.String, java.util.Map)
	 */
	@Override
	public Map<String, Object> findOne(String dataSourceId, final String tableName,
			final Map<String, Object> keyParams) {
		AssertUtil.parameterEmpty(tableName, "tableName");
		AssertUtil.parameterEmpty(keyParams, "keyParams");

		List<Object> paramsValues = new ArrayList<>();
		String sql = buildSelectSql(tableName, keyParams, 0, 0, null, paramsValues);
		return findOne(getJdbcTemplate(dataSourceId), sql, paramsValues);
	}

	/**
	 * 执行sql，返回查询结果。当sql使用了绑定变量，paramsValues传入变量值
	 * 
	 * @param sql
	 * @param paramsValues
	 * @return
	 */
	private Map<String, Object> findOne(JdbcTemplate jdbcTemplate, final String sql, final List<Object> paramsValues) {
		if (log.isInfoEnabled()) {
			log.info("\nmyfly: " + sql + "\n       " + StringUtil.formatSql(sql, paramsValues.toArray()));
		}
		try {
			if (CollectionUtils.isEmpty(paramsValues)) {
				// 最终语句
				return jdbcTemplate.queryForMap(sql);
			} else {
				// 绑定了变量
				return jdbcTemplate.queryForMap(sql, paramsValues.toArray(new Object[] {}));
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IJdbcDataAccessService#findAll(
	 * java.lang.String, java.util.Collection)
	 */
	@Override
	public List<Map<String, Object>> findAll(String dataSourceId, final String sql,
			final Collection<Object> paramValues) {
		if (log.isInfoEnabled()) {
			log.info("\nmyfly: " + sql + "\n       " + StringUtil.formatSql(sql, paramValues.toArray()));
		}
		if (CollectionUtils.isEmpty(paramValues)) {
			return getJdbcTemplate(dataSourceId).queryForList(sql);
		} else {
			return getJdbcTemplate(dataSourceId).queryForList(sql, paramValues.toArray(new Object[] {}));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IJdbcDataAccessService#findAll(
	 * java.lang.String, java.util.Map, int, int,
	 * org.springframework.data.domain.Sort)
	 */
	@Override
	public List<Map<String, Object>> findAll(String dataSourceId, String tableName, Map<String, Object> params,
			int page, int size, Sort sort) {
		AssertUtil.parameterEmpty(tableName, "tableName");
		List<Object> paramsValues = new ArrayList<>();
		String sql = buildSelectSql(tableName, params, page, size, sort, paramsValues);
		return findAll(dataSourceId, sql, paramsValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IJdbcDataAccessService#findAll(
	 * java.lang.String,
	 * org.myfly.platform.system.metadata.define.FilterDefinition[], int, int,
	 * org.springframework.data.domain.Sort)
	 */
	@Override
	public List<Map<String, Object>> findAll(String dataSourceId, String tableName, FilterDefinition[] filters,
			int page, int size, Sort sort) {
		AssertUtil.parameterEmpty(tableName, "tableName");
		List<Object> paramValues = new ArrayList<>();
		String sql = buildSelectSql(tableName, filters, page, size, sort, paramValues);
		return findAll(dataSourceId, sql, paramValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IJdbcDataAccessService#findAll(
	 * java.lang.String, java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> findAll(String dataSourceId, final String tableName,
			final Map<String, Object> params) {
		return findAll(dataSourceId, tableName, params, 0, 0, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IJdbcDataAccessService#findAll(
	 * java.lang.String, java.util.Map,
	 * org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<Map<String, Object>> findAll(String dataSourceId, final String tableName,
			final Map<String, Object> params, Pageable pageable) {
		int count = count(dataSourceId, tableName, params);
		List<Map<String, Object>> result = findAll(dataSourceId, tableName, params, pageable.getPageNumber(),
				pageable.getPageSize(), pageable.getSort());
		Page<Map<String, Object>> pageData = new PageImpl<>(result, pageable, count);
		return pageData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IJdbcDataAccessService#delOne(java
	 * .lang.String, java.util.Map)
	 */
	@Override
	public int delOne(String dataSourceId, String tableName, Map<String, Object> params) {
		AssertUtil.parameterEmpty(tableName, "tableName");
		AssertUtil.parameterInvalide(MapUtils.isEmpty(params), "params不能为空.");
		List<Object> paramsValues = new ArrayList<>();
		String sql = buildDeleteSql(tableName, params, paramsValues);
		return executeSql(getJdbcTemplate(dataSourceId), sql, paramsValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IJdbcDataAccessService#del(java.
	 * lang.String, java.util.Map)
	 */
	@Override
	public int del(String dataSourceId, String tableName, Map<String, Object> params) {
		AssertUtil.parameterEmpty(tableName, "tableName");
		List<Object> paramsValues = new ArrayList<>();
		String sql = buildDeleteSql(tableName, params, paramsValues);
		return executeSql(getJdbcTemplate(dataSourceId), sql, paramsValues);
	}

	/**
	 * 执行SQL语句，返回影响记录行数
	 * 
	 * @param sql
	 * @param paramsValues
	 * @return
	 */
	private int executeSql(JdbcTemplate jdbcTemplate, String sql, List<Object> paramsValues) {
		if (log.isInfoEnabled()) {
			log.info("\nmyfly: " + sql + "\n       " + StringUtil.formatSql(sql, paramsValues.toArray()));
		}
		try {
			return jdbcTemplate.update(sql, paramsValues.toArray(new Object[] {}));
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 构造删除SQL语句
	 * 
	 * @param tableName
	 * @param params
	 * @param paramsValues
	 * @return
	 */
	private String buildDeleteSql(String tableName, Map<String, Object> params, List<Object> paramsValues) {
		return "DELETE FROM " + tableName + QuerySpecificationUtil.buildQueryWhereSql(params, paramsValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IJdbcDataAccessService#saveEntity(
	 * java.lang.String, java.util.Map)
	 */
	@Override
	public int saveEntity(String dataSourceId, String tableName, Map<String, Object> values) {
		AssertUtil.parameterEmpty(tableName, "tableName");
		List<Object> paramsValues = new ArrayList<>();
		String sql = buildInsertSql(tableName, values, paramsValues);
		return executeSql(getJdbcTemplate(dataSourceId), sql, paramsValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IJdbcDataAccessService#saveEntity(
	 * java.lang.String, java.util.List)
	 */
	@Override
	public void saveEntity(String dataSourceId, String tableName, List<Map<String, Object>> entities) {
		Assert.notEmpty(entities);
		for (Map<String, Object> entity : entities) {
			saveEntity(dataSourceId, tableName, entity);
		}
	}

	/**
	 * 构建插入数据SQL
	 * 
	 * @param tableName
	 * @param values
	 * @param paramsValues
	 * @return
	 */
	private String buildInsertSql(String tableName, Map<String, Object> values, List<Object> paramsValues) {
		String fields = "";
		String params = "";
		for (String name : values.keySet()) {
			fields += "," + name;
			params += ",?";
			paramsValues.add(values.get(name));
		}
		if (StringUtils.isNotBlank(fields)) {
			fields = fields.substring(1);
			params = params.substring(1);
		}
		return "INSERT INTO " + tableName + "(" + fields + ") VALUES(" + params + ")";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.myfly.platform.system.data.service.IJdbcDataAccessService#
	 * updateEntity(java.lang.String, java.util.Map, java.util.Map)
	 */
	@Override
	public int updateEntity(String dataSourceId, String tableName, Map<String, Object> keyParams,
			Map<String, Object> values) {
		AssertUtil.parameterEmpty(tableName, "tableName");
		if (MapUtils.isEmpty(values)) {
			return 0;
		}
		List<Object> paramsValues = new ArrayList<>();
		String sql = buildUpdateSql(tableName, keyParams, values, paramsValues);
		return executeSql(getJdbcTemplate(dataSourceId), sql, paramsValues);
	}

	/**
	 * 构建修改SQL
	 * 
	 * @param tableName
	 * @param keyParams
	 * @param values
	 * @param paramsValues
	 * @return
	 */
	private String buildUpdateSql(String tableName, Map<String, Object> keyParams, Map<String, Object> values,
			List<Object> paramsValues) {
		String sql = "";
		for (String name : values.keySet()) {
			sql += ", " + name + " = ? ";
			paramsValues.add(values.get(name));
		}
		if (StringUtils.isNotBlank(sql)) {
			sql = sql.substring(1);
		}
		String whereSql = QuerySpecificationUtil.buildQueryWhereSql(keyParams, paramsValues);
		sql = "UPDATE " + tableName + " SET " + sql + whereSql;
		return sql;
	}
}
