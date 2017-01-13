package org.easycloud.platform.visualpage.define;

import org.apache.commons.lang3.ArrayUtils;
import org.easycloud.platform.metadata.define.BaseDenifition;
import org.easycloud.platform.metadata.define.FieldDefinition;

public class FilterDefinition extends BaseDenifition {
	private FieldDefinition field;
	private Object value;
	private SQLOperator operator;
	private boolean show = true;

	private String fieldName;
	private Object[] multiValues;

	public FilterDefinition(FieldDefinition field, SQLOperator operator) {
		super(null);
		setName(field.getName());
		setField(field);
		setOperator(operator);
	}

	public FilterDefinition(String fieldName, SQLOperator operator) {
		super(null);
		setFieldName(fieldName);
		setOperator(operator);
	}

	public FilterDefinition(String fieldName, SQLOperator operator, Object... values) {
		super(null);
		setFieldName(fieldName);
		setOperator(operator);
		setMultiValues(values);
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public SQLOperator getOperator() {
		return operator;
	}

	public void setOperator(SQLOperator operator) {
		this.operator = operator;
	}

	public FieldDefinition getField() {
		return field;
	}

	public void setField(FieldDefinition field) {
		this.field = field;
	}

	public Object[] getMultiValues() {
		return multiValues;
	}

	public void setMultiValues(Object[] multiValues) {
		this.multiValues = multiValues;
		if (ArrayUtils.isNotEmpty(multiValues)) {
			setValue(multiValues[0]);
		}
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}
	
	@Override
	public String toString() {
		return "field: " + field.getName() + ", operator: " + operator.getTitle();
	}
}
