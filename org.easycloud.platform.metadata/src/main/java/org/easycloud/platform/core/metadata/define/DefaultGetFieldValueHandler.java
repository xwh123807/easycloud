package org.easycloud.platform.core.metadata.define;

import java.util.Map;

import org.easycloud.platform.core.utils.AssertUtil;

public class DefaultGetFieldValueHandler implements GetFieldValueHandler {
	private FieldDefinition field;

	public DefaultGetFieldValueHandler(FieldDefinition field) {
		this.field = field;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getFieldValue(Object entity) {
		if (entity == null) {
			return null;
		} else {
			if (entity instanceof Map) {
				return ((Map)entity).get(field.getName());
			} else {
				try {
					return field.getGetter().invoke(entity);
				} catch (Exception e) {
					AssertUtil.parameterEmpty(field.getGetter(), "field.getGetter()",
							"属性[" + field.getName() + "]没有定义Get方法");
					e.printStackTrace();
					throw new IllegalArgumentException("实体属性[" + field.getName() + "]值获取失败，错误信息：" + e.getMessage());
				}
			}
		}
	}

}
