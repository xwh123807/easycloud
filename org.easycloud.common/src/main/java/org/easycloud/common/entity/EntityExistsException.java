package org.easycloud.common.entity;

public class EntityExistsException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 729013192359586301L;
	
	public EntityExistsException(BaseEntity entity) {
		super("实体已经存在，" + entity.toString());
	}
}
