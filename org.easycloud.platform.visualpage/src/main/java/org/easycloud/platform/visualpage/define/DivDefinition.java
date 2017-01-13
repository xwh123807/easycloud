package org.easycloud.platform.visualpage.define;

import org.easycloud.platform.metadata.define.BaseDenifition;

public class DivDefinition extends BaseDenifition{
	private DivType divType;
	
	private int width;
	
	private String extClass;
	
	private DivDefinition[] subs;
	
	public DivDefinition() {
		super(null);
	}
	
	public DivDefinition(Object owner) {
		super(owner);
		setDivType(DivType.DEFAULT);
		setWidth(12);
	}

	public DivType getDivType() {
		return divType;
	}

	public void setDivType(DivType divType) {
		this.divType = divType;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getExtClass() {
		return extClass;
	}

	public void setExtClass(String extClass) {
		this.extClass = extClass;
	}

	public DivDefinition[] getSubs() {
		return subs;
	}

	public void setSubs(DivDefinition[] subs) {
		this.subs = subs;
	}
	
	@Override
	public String toString() {
		return "name: " + getName() + ", divType: " + getDivType() + ", width: " + getWidth() + ", extClass: " + getExtClass();
	}

	public void validate() {
	}
}
