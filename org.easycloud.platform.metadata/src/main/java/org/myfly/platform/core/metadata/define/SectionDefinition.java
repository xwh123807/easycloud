package org.myfly.platform.core.metadata.define;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

public class SectionDefinition extends BaseDenifition {
	private String title;

	private SectionType type;

	private FieldSetDefinition[] fieldSets;

	private SubTableDefinition[] subTables;

	public SectionDefinition(FormDefinition parent, String title) {
		super(parent);
		setTitle(title);
		setType(SectionType.CUSTOM);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public FieldSetDefinition[] getFieldSets() {
		return fieldSets;
	}

	public void setFieldSets(FieldSetDefinition[] fieldSets) {
		this.fieldSets = fieldSets;
	}

	public SubTableDefinition[] getSubTables() {
		return subTables;
	}

	public void setSubTables(SubTableDefinition[] subTables) {
		this.subTables = subTables;
	}

	/**
	 * 增加子表定义
	 * 
	 * @param subTable
	 */
	public void addSubTable(SubTableDefinition subTable) {
		if (ArrayUtils.isEmpty(subTables)) {
			subTables = new SubTableDefinition[1];
			subTables[0] = subTable;
		} else {
			ArrayUtils.add(subTables, subTable);
		}
	}

	public SectionType getType() {
		return type;
	}

	public void setType(SectionType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "name: " + getName() + ", type: " + getType();
	}

	public void validate() {
		Assert.notNull(getType());
		if (ArrayUtils.isNotEmpty(getFieldSets())) {
			for (FieldSetDefinition fieldSet : getFieldSets()){
				fieldSet.validate();
			}
		}
		if (ArrayUtils.isNotEmpty(getSubTables())) {
			for (SubTableDefinition subtable : getSubTables()){
				subtable.validate();
			}
		}
	}
}
