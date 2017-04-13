package org.easycloud.common.entity;

public class EntityNotExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 256497079213721980L;

	public EntityNotExistsException(BaseEntity entity) {
		super("实体不存在，" + entity.toString());
	}
}
