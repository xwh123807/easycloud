package org.myfly.platform.core.domain;

import java.io.Serializable;
import java.util.Date;

import org.myfly.platform.core.user.domain.SUser;

/**
 * 审计Entity
 * @author xiangwanhong
 *
 */
public interface IAuditEntity extends Serializable{
	public Date getCreated();
	public Date getUpdated();
	public SUser getUpdatedBy();
	public SUser getCreatedBy();
}
