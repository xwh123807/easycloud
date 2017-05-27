package org.easycloud.metadata.generator.define.entity;

import java.util.Map;

import org.easycloud.platform.metadata.service.EntityMetaData;
import org.easycloud.platform.metadata.service.ServiceUtil;
import org.easycloud.platform.metadata.utils.AssertUtil;

public class AssociationSetFieldValueHandler implements SetFieldValueHandler {
	private FieldDefinition field;

	public AssociationSetFieldValueHandler(FieldDefinition field) {
		this.field = field;
	}

	@Override
	public void setFieldValue(Object obj, Object value) {
		if (obj instanceof Map) {
			Map<String, Object> entity = ((Map<String, Object>) obj);
			if (field.getFieldName().contains("=")) {
				
			}else{
				entity.put(field.getFieldName(), value);
			}
		} else {
			try {
				if (value != null && value.getClass() == field.getType()) {
					// 类型相同，不需要转换
					field.getSetter().invoke(obj, value);
				} else {
					// 类型不同，需要进行转换
					EntityMetaData metaData = ServiceUtil.getEntityMetaDataService()
							.getEntityMetaData(field.getRelationClass());
					Object newEntity = metaData.getPKFieldDefinition().newEntity((String) value);
					field.getSetter().invoke(obj, newEntity);
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
