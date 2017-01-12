package org.easycloud.platform.core.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.easycloud.platform.core.metadata.annotation.FieldView;

/**
 * 基础实体类，实体类都需继承本类。
 * @author xiangwanhong
 *
 */
@MappedSuperclass
@Inheritance(strategy=InheritanceType.JOINED)
public class SBaseEntity extends SKeyEntity implements IAuditEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 392066006775341218L;
	
//	@Id
//	@Column(length=32)
//	@GeneratedValue(generator="system-uuid")
//	@GenericGenerator(name="system-uuid", strategy="uuid")
//	@FieldView(title="UID", dataType=FieldDataType.UUID)
//	private String uid;
	
//	@Field(index=Index.YES, store=Store.YES)
	@FieldView(title="名称", minLength=1)
	@Column(length=255, nullable=false)
	private String name;

	/**
	 * 租户ID
	 */
	@FieldView(title="租户")
	@JoinColumn(updatable=false)
	@ManyToOne(optional = true, fetch=FetchType.LAZY)
	private Tenant tenant;
	/**
	 * 记录是否有效，默认有效
	 */
	@FieldView(title="激活")
	@Column
	private boolean active = true;
	
	/**
	 * 和超类名称重复，此处纯粹是为了让jpa创建该字段
	 */
	/*
	@JsonIgnore
	@Column(name="name", length=255)
	private String __name;
	*/
	/**
	 * 创建时间
	 */
	@FieldView(title="创建时间", dataType=FieldDataType.DATETIME)
	@Column
	private Timestamp created;
	/**
	 * 创建人
	 */
	@FieldView(title="创建者")
	@JoinColumn()
	@ManyToOne(optional = true, fetch=FetchType.LAZY)
	private SUser createdBy;
	/**
	 * 修改时间
	 */
	@FieldView(title="修改时间", dataType=FieldDataType.DATETIME)
	@Column
	private Timestamp updated;
	/**
	 * 修改人
	 */
	@FieldView(title="修改人")
	@JoinColumn()
	@ManyToOne(optional = true, fetch=FetchType.LAZY)
	private SUser updatedBy;
	
	public SBaseEntity() {
	}

	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public Timestamp getUpdated() {
		return updated;
	}
	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public SUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(SUser createdBy) {
		this.createdBy = createdBy;
	}

	public SUser getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(SUser updatedBy) {
		this.updatedBy = updatedBy;
	}
	/*
	@Override
	public void setName(String name) {
		super.setName(name);
		this.__name = name;
	}
	
	@Override
	public String getName() {
		return this.__name;
	}
	*/
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
//	public String getUid() {
//		return uid;
//	}
//	public void setUid(String uid) {
//		this.uid = uid;
//	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void setInternalTable(String tmp){
	}
	
	@Override
	public String toString() {
		return "uid: " + getUid() + ", name: " + getName();
	}
}
