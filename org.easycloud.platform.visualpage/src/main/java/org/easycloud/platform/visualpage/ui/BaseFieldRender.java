package org.easycloud.platform.visualpage.ui;

import java.util.HashMap;
import java.util.Map;

import org.easycloud.platform.metadata.define.entity.FieldDefinition;

public abstract class BaseFieldRender extends BaseControlRender{
	/**
	 * 控件数据提供者
	 */
	private FieldDefinition field;
	
	/**
	 * 自定义参数
	 */
	private Map<String, String> parameters;

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	/**
	 * 设置参数
	 * @param name
	 * @param value
	 */
	public void setParameter(String name, String value){
		if (parameters == null){
			parameters = new HashMap<>();
		}
		parameters.put(name, value);
	}
	
	public FieldDefinition getField() {
		return field;
	}
	public void setField(FieldDefinition field) {
		this.field = field;
	}
	
	@Override
	public String htmlForEdit() {
		return "";
	}
	
	@Override
	public String htmlForPrint() {
		return htmlForView();
	}
	
	@Override
	public String htmlForView() {
		return htmlForEdit();
	}
	
	@Override
	public String htmlForNew() {
		return htmlForEdit();
	}
	
	@Override
	public String controlForEdit() {
		return "";
	}
	
	@Override
	public String controlForPrint() {
		return controlForView();
	}
	
	@Override
	public String controlForView() {
		return "<span class=\"bg-info\">$!{obj." + getField().getName() + "}</span>";
	}
	
	@Override
	public String controlForNew() {
		return controlForEdit();
	}
	
	@Override
	public String controlForSearch() {
		return "<span class=\"bg-info\">$!{objitem." + getField().getName() + "}</span>";
	}
	
	@Override
	public String htmlForSearch() {
		return htmlForView();
	}
}
