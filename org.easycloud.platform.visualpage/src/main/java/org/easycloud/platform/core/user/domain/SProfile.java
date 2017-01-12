package org.easycloud.platform.core.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.easycloud.platform.core.domain.SBaseEntity;
import org.easycloud.platform.core.domain.SchemaConstants;
import org.easycloud.platform.core.metadata.annotation.FieldSetView;
import org.easycloud.platform.core.metadata.annotation.FieldView;
import org.easycloud.platform.core.metadata.annotation.FormView;
import org.easycloud.platform.core.metadata.annotation.ListView;
import org.easycloud.platform.core.metadata.annotation.MetaDataView;
import org.easycloud.platform.core.metadata.annotation.SectionView;
import org.easycloud.platform.core.metadata.annotation.TableView;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 简档
 * 
 * @author xiangwanhong
 *
 */
@Entity
@Table(schema = SchemaConstants.PB)
@MetaDataView(
//
tableView = @TableView(title = "简档", description = "简档信息") ,
//
listViews = @ListView(fields = { "name", "description", "active", "createdBy", "created" }) ,
//
formViews = @FormView(sections = { @SectionView(title = "简档信息", fieldSets = {
		@FieldSetView(title = "基本信息", fields = { "name", "description" }),
		@FieldSetView(title = "审计", fields = { "active", "createdBy", "created", "updatedBy", "updated" }) }) }) )
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SProfile extends SBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9095325047141587494L;

	/**
	 * 简述
	 */
	@FieldView(title = "简述")
	@Column(length = 1000)
	private String description;

	public SProfile() {
		setInternalTable("SProfile");
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
