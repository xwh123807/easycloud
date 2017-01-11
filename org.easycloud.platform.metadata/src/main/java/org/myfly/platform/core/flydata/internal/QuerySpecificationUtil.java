package org.myfly.platform.core.flydata.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.myfly.platform.core.metadata.define.FieldDefinition;
import org.myfly.platform.core.metadata.define.FilterDefinition;
import org.myfly.platform.core.metadata.define.SQLOperator;
import org.myfly.platform.core.utils.AssertUtil;
import org.myfly.platform.core.utils.ClassUtil;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

/**
 * 查询过滤条件工具类
 * 
 * @author xiangwanhong
 *
 */
public class QuerySpecificationUtil {
	/**
	 * 构造查询器
	 * 
	 * @param filters
	 *            元模型查询定义
	 * @param params
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Specifications buildQuerySpecifications(final FilterDefinition[] filterDefinitions,
			final Map<String, String[]> params) {
		if (params == null || params.size() == 0) {
			return null;
		}
		FilterDefinition[] filters = buildFilterDefinition(filterDefinitions, params);
		return buildQuerySpecifications(filters);
	}

	/**
	 * 构建Where条件，所有条件均为And
	 * 
	 * @param paramNames
	 * @return
	 */
	public static String buildQueryWhereSql(final Map<String, Object> params, List<Object> paramsValues) {
		String where = "";
		if (MapUtils.isNotEmpty(params)) {
			if (paramsValues == null) {
				paramsValues = new ArrayList<>();
			}
			for (Map.Entry<String, Object> item : params.entrySet()) {
				where += " AND " + item.getKey() + "= ?";
				paramsValues.add(item.getValue());
			}
			where = " WHERE " + where.substring(5);
		}
		return where;
	}

	/**
	 * 构造查询SQL条件子句
	 * 
	 * @param filters
	 * @param reurnArgs
	 * @return
	 */
	public static String buildQueryWhereSql(final FilterDefinition[] filters, List<Object> paramValues) {
		if (ArrayUtils.isEmpty(filters)) {
			return "";
		}
		if (paramValues == null) {
			paramValues = new ArrayList<>();
		}
		String where = "";
		int index = 0;
		for (FilterDefinition filter : filters) {
			if (index > 0) {
				where += " and ";
			}
			index++;
			switch (filter.getOperator()) {
			case ISNULL:
				where += filter.getFieldName() + " is null";
				break;
			case NOTNULL:
				where += filter.getFieldName() + " is not null";
				break;
			case EQUAL:
				where += filter.getFieldName() + " = ?";
				paramValues.add(filter.getValue());
				break;
			case NOTEQUAL:
				where += filter.getFieldName() + " != ?";
				paramValues.add(filter.getValue());
				break;
			case IN:
				where += filter.getFieldName() + " in (";
				for (int i = 0; i < filter.getMultiValues().length; i++) {
					if (i > 0) {
						where += ", ";
					}
					where += "?";
					paramValues.add(filter.getMultiValues()[i]);
				}
				where += ")";
				break;
			case LIKE:
				if (filter.getValue() instanceof String) {
					where += filter.getFieldName() + " like ?";
					String value = (String) filter.getValue();
					if (value.indexOf("%") == -1) {
						value = "%" + value + "%";
					}
					paramValues.add(value);
				} else {
					AssertUtil.notSupport("LIKE只支持字符，实际类型为" + filter.getValue().getClass());
				}
				break;
			case NOTLIKE:
				if (filter.getValue() instanceof String) {
					where += filter.getFieldName() + " not like ?";
					paramValues.add(filter.getValue());
				} else {
					AssertUtil.notSupport("NOTLIKE只支持字符，实际类型为" + filter.getValue().getClass());
				}
				break;
			case GREATERTHAN:
				where += filter.getFieldName() + " > ?";
				paramValues.add(filter.getValue());
				break;
			case LESSTHAN:
				where += filter.getFieldName() + " < ?";
				paramValues.add(filter.getValue());
				break;
			case BETWEEN:
				if (filter.getMultiValues()[0] instanceof Date) {
					where += filter.getFieldName() + " between ? and ?";
					paramValues.add(filter.getMultiValues()[0]);
					paramValues.add(filter.getMultiValues()[1]);
				} else if (filter.getMultiValues()[0] instanceof Number) {
					where += filter.getFieldName() + " >= ? and " + filter.getFieldName() + " <= ?";
					paramValues.add(filter.getMultiValues()[0]);
					paramValues.add(filter.getMultiValues()[1]);
				} else {
					AssertUtil.notSupport("BETWEEN只支持日期和数值，实际类型为" + filter.getValue().getClass());
				}
				break;
			case GREATERTHANOREQUALTO:
				where += filter.getFieldName() + " >= ?";
				paramValues.add(filter.getValue());
				break;
			case LESSTHANOREQUALTO:
				where += filter.getFieldName() + " <= ?";
				paramValues.add(filter.getValue());
				break;
			}
		}
		if (StringUtils.isNotBlank(where)) {
			where = " where " + where;
		}
		return where;
	}

	/**
	 * 根据过滤器构建实体查询
	 * 
	 * @param filters
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Specifications buildQuerySpecifications(final FilterDefinition[] filters) {
		if (ArrayUtils.isEmpty(filters)) {
			return null;
		}
		return Specifications.where(new Specification() {

			@Override
			public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				for (FilterDefinition filter : filters) {
					switch (filter.getOperator()) {
					case ISNULL:
						predicates.add(root.get(filter.getName()).isNull());
						break;
					case NOTNULL:
						predicates.add(root.get(filter.getName()).isNotNull());
						break;
					case EQUAL:
						predicates.add(cb.equal(root.get(filter.getName()), filter.getValue()));
						break;
					case NOTEQUAL:
						predicates.add(cb.notEqual(root.get(filter.getName()), filter.getValue()));
						break;
					case IN:
						predicates.add(root.get(filter.getName()).in(filter.getMultiValues()));
						break;
					case LIKE:
						if (filter.getValue() instanceof String) {
							String value = (String) filter.getValue();
							if (value.indexOf("%") == -1) {
								value = "%" + value + "%";
							}
							predicates.add(cb.like(root.get(filter.getName()), value));
						} else {
							AssertUtil.notSupport("LIKE只支持字符，实际类型为" + filter.getValue().getClass());
						}
						break;
					case NOTLIKE:
						if (filter.getValue() instanceof String) {
							predicates.add(cb.notLike(root.get(filter.getName()), (String) filter.getValue()));
						} else {
							AssertUtil.notSupport("NOTLIKE只支持字符，实际类型为" + filter.getValue().getClass());
						}
						break;
					case GREATERTHAN:
						if (filter.getValue() instanceof String) {
							predicates.add(cb.greaterThan(root.get(filter.getName()), (String) filter.getValue()));
						} else if (filter.getValue() instanceof Number) {
							predicates.add(cb.gt(root.get(filter.getName()), (Number) filter.getValue()));
						} else if (filter.getValue() instanceof Date) {
							predicates.add(cb.greaterThan(root.get(filter.getName()), (Date) filter.getValue()));
						}
						break;
					case LESSTHAN:
						if (filter.getValue() instanceof String) {
							predicates.add(cb.lessThan(root.get(filter.getName()), (String) filter.getValue()));
						} else if (filter.getValue() instanceof Number) {
							predicates.add(cb.lt(root.get(filter.getName()), (Number) filter.getValue()));
						} else if (filter.getValue() instanceof Date) {
							predicates.add(cb.lessThan(root.get(filter.getName()), (Date) filter.getValue()));
						}
						break;
					case BETWEEN:
						if (filter.getMultiValues()[0] instanceof Date) {
							predicates.add(cb.between(root.get(filter.getName()),
									(Date) filter.getMultiValues()[0], (Date) filter.getMultiValues()[1]));
						} else if (filter.getMultiValues()[0] instanceof Number) {
							predicates.add(cb.ge(root.get(filter.getName()), (Number) filter.getMultiValues()[0]));
							predicates.add(cb.le(root.get(filter.getName()), (Number) filter.getMultiValues()[1]));
						} else {
							AssertUtil.notSupport("BETWEEN只支持日期和数值，实际类型为" + filter.getValue().getClass());
						}
						break;
					case GREATERTHANOREQUALTO:
						if (filter.getValue() instanceof Number) {
							predicates.add(cb.ge(root.get(filter.getName()), (Number) filter.getValue()));
						} else if (filter.getValue() instanceof Date) {
							predicates.add(
									cb.greaterThanOrEqualTo(root.get(filter.getName()), (Date) filter.getValue()));
						}
						break;
					case LESSTHANOREQUALTO:
						if (filter.getValue() instanceof Number) {
							predicates.add(cb.le(root.get(filter.getName()), (Number) filter.getValue()));
						} else if (filter.getValue() instanceof Date) {
							predicates.add(
									cb.lessThanOrEqualTo(root.get(filter.getName()), (Date) filter.getValue()));
						}
						break;
					}
				}
				return query.where(predicates.toArray(new Predicate[] {})).getRestriction();
			}
		});
	}

	/**
	 * 根据实体元模型查询定义构建查询过滤器<br>
	 * TODO 过滤条件为查找关系的处理、参数默认有值的处理
	 * 
	 * @param filterDefinitions
	 *            元模型中过滤器定义信息
	 * @param params
	 *            过滤参数值
	 * @return
	 */
	public static FilterDefinition[] buildFilterDefinition(final FilterDefinition[] filterDefinitions,
			final Map<String, String[]> params) {
		List<FilterDefinition> result = new ArrayList<>();
		if (ArrayUtils.isNotEmpty(filterDefinitions)) {
			// 遍历过滤器定义，处理有赋值的过滤器
			for (FilterDefinition filterDefinition : filterDefinitions) {
				String name = filterDefinition.getField().getName();
				Class<?> type = filterDefinition.getField().getType();
				// 先判断过滤器是否有赋值
				if (params != null && params.containsKey(name)) {
					String[] values = params.get(name);
					// 再判断赋值是否有效
					if (ArrayUtils.isNotEmpty(values) && StringUtils.isNotBlank(values[0])) {
						FilterDefinition filter = new FilterDefinition(name, filterDefinition.getOperator());
						filter.setName(filterDefinition.getField().getName());
						filter.setFieldName(filterDefinition.getField().getFieldName());
						switch (filterDefinition.getOperator()) {
						case BETWEEN:
							String startValue = null;
							String endValue = null;
							String[] tmp = params.get(name + "._startValue");
							if (ArrayUtils.isNotEmpty(tmp) && StringUtils.isNotBlank(tmp[0])) {
								startValue = tmp[0];
							}
							tmp = params.get(name + "._endValue");
							if (ArrayUtils.isNotEmpty(tmp) && StringUtils.isNotBlank(tmp[0])) {
								endValue = tmp[0];
							}
							filter.setMultiValues(new Object[] { ClassUtil.convert(startValue, type),
									ClassUtil.convert(endValue, type) });
							break;
						case IN:
							List<Object> tmp2 = new ArrayList<>();
							for (String item : values) {
								if (StringUtils.isNotBlank(item)) {
									tmp2.add(ClassUtil.convert(item, type));
								}
							}
							filter.setMultiValues(tmp2.toArray(new Object[] {}));
							break;
						default:
							filter.setValue(ClassUtil.convert(values[0], type));
							break;
						}
						result.add(filter);
					}
				}
			}
		}
		return result.toArray(new FilterDefinition[] {});
	}

	/**
	 * 构建过滤条件
	 * 
	 * @param fieldDefinitions
	 * @param params
	 * @return
	 */
	public static FilterDefinition[] buildFilterDefinition2(final FieldDefinition[] fieldDefinitions,
			final Map<String, Object> params) {
		List<FilterDefinition> filters = new ArrayList<>();
		if (ArrayUtils.isNotEmpty(fieldDefinitions)) {
			// 遍历过滤器定义，处理有赋值的过滤器
			for (FieldDefinition fieldDefinition : fieldDefinitions) {
				Object value = params.get(fieldDefinition.getName());
				if (value != null) {
					FilterDefinition filterDefinition = new FilterDefinition(fieldDefinition, SQLOperator.EQUAL);
					filterDefinition.setValue(value);
					filters.add(filterDefinition);
				}
			}
		}
		return filters.toArray(new FilterDefinition[] {});
	}

	/**
	 * 构建过滤条件，赋条件时会将值转换属性对应的数据类型
	 * @param fieldDefinitions
	 * @param params
	 * @return
	 */
	public static FilterDefinition[] buildFilterDefinition(FieldDefinition[] fieldDefinitions,
			Map<String, String> params) {
		List<FilterDefinition> filters = new ArrayList<>();
		if (ArrayUtils.isNotEmpty(fieldDefinitions)) {
			// 遍历过滤器定义，处理有赋值的过滤器
			for (FieldDefinition fieldDefinition : fieldDefinitions) {
				String value = params.get(fieldDefinition.getName());
				if (StringUtils.isNotBlank(value)) {
					FilterDefinition filterDefinition = new FilterDefinition(fieldDefinition, SQLOperator.EQUAL);
					filterDefinition.setValue(ClassUtil.convert(value, fieldDefinition.getType()));
					filters.add(filterDefinition);
				}
			}
		}
		return filters.toArray(new FilterDefinition[] {});
	}

	/**
	 * 根据查询条件构造查询，条件全部都是And关系
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Specifications<?> buildQuerySpecifications(final Map<String, Object> params) {
		if (MapUtils.isNotEmpty(params)) {
			return Specifications.where(new Specification() {
				@Override
				public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
					List<Predicate> predicates = new ArrayList<>();
					for (Map.Entry<String, Object> item : params.entrySet()) {
						predicates.add(cb.equal(root.get(item.getKey()), item.getValue()));
					}
					return query.where(predicates.toArray(new Predicate[] {})).getRestriction();
				}

			});
		} else {
			return null;
		}
	}

}
