package org.easycloud.platform.metadata.define;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.metadata.internal.EntityMetaDataConstants;

public class FlySearchRelationGetFieldValueHandler implements GetFieldValueHandler {

	private FieldDefinition fieldDefinition;

	public FlySearchRelationGetFieldValueHandler(FieldDefinition field) {
		this.fieldDefinition = field;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getFieldValue(Object obj) {
		if (obj != null) {
			Object relEntity = null;
			String uid = null;
			String value = null;
			FieldDefinition relField = fieldDefinition.getLabelFieldDefinition();
			if (obj instanceof Map) {
				// Map
				Map<String, Object> entity = (Map<String, Object>) obj;
				uid = (String) entity.get(fieldDefinition.getFieldName());
			} else {
				// 实体类
				try {
					uid = (String) fieldDefinition.getGetter().invoke(obj);
				} catch (Exception e) {
					throw new RuntimeException("实体属性[" + fieldDefinition.getName() + "]值获取失败.");
				}
			}
			// 获取显示名称
			if (StringUtils.isNotBlank(uid)) {
				try {
					relEntity = AppUtil.getJdbcFlyDataAccessService()
							.findOne(EntityMetaDataConstants.TABLENAME_SGLOBAL_NAME, uid);
					value = (String) relField.getGetValueHandler().getFieldValue(relEntity);
				} catch (Exception e) {
					value = uid;
				}
				return new String[] { uid, value };
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
}
