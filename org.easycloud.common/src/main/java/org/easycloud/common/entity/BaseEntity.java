package org.easycloud.common.entity;

import java.io.Serializable;

import org.easycloud.common.util.JSONUtil;

public class BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1417376748477488211L;

	@Override
	public String toString() {
		return JSONUtil.toJSON(this);
	}
}
