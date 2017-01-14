package org.easycloud.platform.metadata.define.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.metadata.service.EntityMetaData;
import org.easycloud.platform.metadata.utils.ClassUtil;

/**
 * 关联字段取值，包括SearchRelation和AutoRelation
 * 
 * @author xiangwanhong
 *
 */
public class SearchRelationGetFieldValueHandler implements GetFieldValueHandler {

	private FieldDefinition fieldDefinition;

	public SearchRelationGetFieldValueHandler(FieldDefinition field) {
		this.fieldDefinition = field;
	}

	/**
	 * 返回结果为数组，元素1为key，元素2为显示值
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getFieldValue(Object obj) {
		if (obj != null) {
			FieldDefinition relField = fieldDefinition.getLabelFieldDefinition();
			String value = "";
			Object relEntity = null;
			if (obj instanceof Map) {
				Map<String, Object> entity = (Map<String, Object>) obj;
				if (fieldDefinition.getFieldName().contains("=")) {
					// 复合外键关联
					Map<String, Object> keyParams = new LinkedHashMap<>();
					String[] items = fieldDefinition.getFieldName().split(";");
					for (String item : items) {
						String name = StringUtils.substringBefore(item, "=");
						String relName = StringUtils.substringAfter(item, "=");
						if (entity.get(name) == null) {
							// 键值中有空
							return null;
						}
						keyParams.put(relName, entity.get(name));
					}
//					relEntity = AppUtil.getJdbcFlyDataAccessService().findOne(fieldDefinition.getRelationTable(),
//							keyParams);
					EntityMetaData relMetaData = relField.getParent();
					value = relMetaData.getPKFieldDefinition().getPKValue(relEntity);
				} else {
					// 单主键关联
					Object tmp1 = entity.get(fieldDefinition.getFieldName());
					if (tmp1 != null && !(tmp1 instanceof String)) {
						value = ClassUtil.convertValueToString(tmp1);
					} else {
						value = (String) tmp1;
					}
					if (value != null) {
//						relEntity = AppUtil.getJdbcFlyDataAccessService().findOne(fieldDefinition.getRelationTable(),
//								value);
					}
				}
				if (relEntity != null) {
					Object tmp2 = relField.getGetValueHandler().getFieldValue(relEntity);
					String relUid = value;
					if (tmp2 != null && !(tmp2 instanceof String)) {
						value = ClassUtil.convertValueToString(tmp2);
					} else {
						value = (String) tmp2;
					}
					return new String[] { relUid, value };
				} else {
					return null;
				}
			} else {
				try {
					relEntity = fieldDefinition.getGetter().invoke(obj);
				} catch (Exception e) {
				}
				if (relEntity != null) {
					EntityMetaData relMetaData = relField.getParent();
					String relUid = relMetaData.getPKFieldDefinition().getPKValue(relEntity);
					Object tmp2 = relField.getGetValueHandler().getFieldValue(relEntity);
					if (tmp2 != null && !(tmp2 instanceof String)) {
						value = ClassUtil.convertValueToString(tmp2);
					} else {
						value = (String) tmp2;
					}
					return new String[] { relUid, value };
				} else {
					return null;
				}
			}
		} else {
			return null;
		}
	}
}
