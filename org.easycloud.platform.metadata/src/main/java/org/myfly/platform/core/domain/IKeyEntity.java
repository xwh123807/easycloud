package org.myfly.platform.core.domain;

import java.io.Serializable;

public interface IKeyEntity extends Serializable{
	public String getUid();
	public void setUid(String uid);
}
