package org.myfly.platform.core.flydata.queue;

import java.io.Serializable;

public class EntityMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2540088120077265326L;
	private EntityOperator operator;
	private Object entity;
	private String entityName;
	private String uid;
	
	/**
	 * 用于发送实体变更消息
	 * @param operator
	 * @param entity
	 */
	public EntityMessage(EntityOperator operator, Object entity) {
		this.operator = operator;
		this.entity = entity;
	}
	
	/**
	 * 用于为实体创建索引的消息
	 * @param operator
	 * @param entityName
	 * @param uid
	 */
	public EntityMessage(EntityOperator operator, String entityName, String uid) {
		this.operator = operator;
		this.entityName = entityName;
		this.uid = uid;
	}
	
	public EntityOperator getOperator() {
		return operator;
	}
	public Object getEntity() {
		return entity;
	}
	public String getEntityName() {
		return entityName;
	}
	public String getUid() {
		return uid;
	}
}
