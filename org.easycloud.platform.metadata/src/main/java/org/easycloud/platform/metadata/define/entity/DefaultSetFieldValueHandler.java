package org.easycloud.platform.metadata.define.entity;

import java.util.Map;

import org.easycloud.platform.metadata.annotation.entity.FieldDataType;
import org.easycloud.platform.metadata.utils.AssertUtil;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.databind.util.ClassUtil;

public class DefaultSetFieldValueHandler implements SetFieldValueHandler {
	private FieldDefinition field;

	public DefaultSetFieldValueHandler(FieldDefinition field) {
		this.field = field;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setFieldValue(Object entity, Object value) {
		if (entity instanceof Map) {
			Map map = (Map) entity;
			map.put(field.getFieldName(), value);
		} else {
			try {
				if (value != null) {
					if (value.getClass() == field.getType()) {
						// 类型相同，不需要转换
						field.getSetter().invoke(entity, value);
					} else if (FieldDataType.FILE.equals(field.getDataType())) {
						// 文件类型，且传入的为字符串（base64编码后的）
						byte[] newValue = Base64Utils.decodeFromString((String) value);
						field.getSetter().invoke(entity, newValue);
					} else {
						// 类型不同，需要进行转换
//						Object newValue = (value != null) ? ClassUtil.convert(value, field.getType()) : null;
//						field.getSetter().invoke(entity, newValue);
					}
				} else {
					field.getSetter().invoke(entity, null);
				}
			} catch (Exception e) {
				AssertUtil.parameterEmpty(field.getSetter(), "field.getSetter()",
						"属性[" + field.getName() + "]没有定义Set方法");
				throw new IllegalArgumentException(
						"属性[" + field.getName() + "]值[" + value + "]设置失败，错误信息：" + e.getMessage());
			}
		}
	}

}
