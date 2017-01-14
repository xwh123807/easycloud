package org.easycloud.platform.flydata.internal;

import java.util.ArrayList;
import java.util.List;

import org.easycloud.platform.metadata.annotation.entity.FieldDataType;
import org.easycloud.platform.metadata.define.entity.FieldDefinition;
import org.easycloud.platform.metadata.define.view.ListDefinition;

public class DataTableMetaData {
	private String entityName;
	private FieldMetaData[] fields;
	private boolean printMode;

	public DataTableMetaData(final String entityName, final FieldDefinition[] fields, boolean printMode) {
		this.setEntityName(entityName);
		this.printMode = printMode;
		buildFieldMetaDatas(fields);
	}
	
	public DataTableMetaData(ListDefinition listDefinition, boolean printMode){
		this.setEntityName(listDefinition.getEntityName());
		this.printMode = printMode;
		buildFieldMetaDatas(listDefinition.getFields());
	}

	private void buildFieldMetaDatas(FieldDefinition[] fields) {
		List<FieldMetaData> list = new ArrayList<>();
		for (FieldDefinition field : fields) {
			if (FieldDataType.ACTIONS.equals(field.getDataType()) && printMode) {
				//打印模式不处理操作字段
			} else {
				list.add(new FieldMetaData(field.getName(), field.getLabel()));
			}
		}
		setFields(list.toArray(new FieldMetaData[] {}));
	}

	public FieldMetaData[] getFields() {
		return fields;
	}

	public void setFields(FieldMetaData[] fields) {
		this.fields = fields;
	}

	public String[] getFieldTitles() {
		List<String> list = new ArrayList<>();
		for (FieldMetaData field : getFields()) {
			list.add(field.getTitle());
		}
		return list.toArray(new String[] {});
	}
	
	public int getFieldIndex(String name){
		int index = -1;
		for (FieldMetaData field : getFields()) {
			index ++;
			if (field.getName().equals(name)){
				break;
			}
		}
		return index;
	}
	
	@Override
	public String toString() {
		return "entityName: " + getEntityName() + ", printMode: " + printMode + ", fields: " + getFields();
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public static class FieldMetaData {
		private String name;
		private String title;

		public FieldMetaData(final String name, final String title) {
			setName(name);
			setTitle(title);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
		
		@Override
		public String toString() {
			return "name: " + getName() + ", title: " + getTitle();
		}
	}
}
