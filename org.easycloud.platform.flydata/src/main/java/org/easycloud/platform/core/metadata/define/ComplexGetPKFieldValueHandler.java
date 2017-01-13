package org.easycloud.platform.core.metadata.define;

public class ComplexGetPKFieldValueHandler implements GetFieldValueHandler {
	private PKFieldDefinition pkFieldDefinition;

	public ComplexGetPKFieldValueHandler(PKFieldDefinition pkFieldDefinition) {
		this.pkFieldDefinition = pkFieldDefinition;
	}

	@Override
	public Object getFieldValue(Object entity) {
		return pkFieldDefinition.getFieldValue(entity);
		
		/*
		if (entity == null) {
			return null;
		} else {
			try {
				if (pkFieldDefinition.getIdFields().length > 1) {
					String result = "";
					for (FieldDefinition field : pkFieldDefinition.getIdFields()) {
						Object value = field.getGetValueHandler().getFieldValue(entity);
						result += "_" + ClassUtil.convertValueToString(value);
					}
					return result.substring(1);
				} else {
					return pkFieldDefinition.getIdFields()[0].getGetValueHandler().getFieldValue(entity);
				}
			} catch (Exception e) {
				throw new IllegalArgumentException("获取主键值失败，错误信息：" + e.getMessage());
			}
		}
		*/
	}
}
