package org.easycloud.metadata.generator.define.entity;

public class ComplexSetPKFieldValueHandler implements SetFieldValueHandler {

	private PKFieldDefinition pkFieldDefinition;

	public ComplexSetPKFieldValueHandler(PKFieldDefinition pkFieldDefinition) {
		this.pkFieldDefinition = pkFieldDefinition;
	}

	@Override
	public void setFieldValue(Object entity, Object value) {
		pkFieldDefinition.setFieldValue(entity, value);
		/*
		AssertUtil.parameterNotEmpty(null, entity, value);
		if (value.getClass() != pkFieldDefinition.getIdClass()) {
			AssertUtil.parameterInvalide("value", "参数类型[" + value.getClass().getName() + "]与主键类型["
					+ pkFieldDefinition.getIdClass().getName() + "]不一致");
		}
		try {
			if (pkFieldDefinition.getIdFields().length == 0) {
				//@Embeddable模式
				pkFieldDefinition.getIdFields()[0].getSetValueHandler().setFieldValue(entity, value);
			}else{
				//实体中多个@Id模式
				
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"设置主键值失败，错误信息：" + e.getMessage());
		}
		*/
	}

}
