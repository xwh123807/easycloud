package org.easycloud.platform.core.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.easycloud.platform.core.domain.SBaseEntity;
import org.easycloud.platform.core.domain.SchemaConstants;
import org.easycloud.platform.metadata.annotation.entity.FieldView;
import org.easycloud.platform.metadata.annotation.entity.TableView;
import org.easycloud.platform.metadata.annotation.view.FieldSetView;
import org.easycloud.platform.metadata.annotation.view.FormView;
import org.easycloud.platform.metadata.annotation.view.ListView;
import org.easycloud.platform.metadata.annotation.view.MetaDataView;
import org.easycloud.platform.metadata.annotation.view.SectionView;
import org.easycloud.platform.metadata.define.view.ListStyle;
import org.easycloud.platform.metadata.define.view.SectionType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 角色
 * 
 * @author xiangwanhong
 *
 */
@Entity
@Table(schema = SchemaConstants.PB)
@MetaDataView(
//
tableView = @TableView(title = "角色", description = "角色信息") ,
//
listViews = @ListView(fields = { "name", "description", "active", "createdBy",
		"created" }, listStyle = ListStyle.TABLE) ,
		//
		formViews = @FormView(sections = {
				@SectionView(title = "用户信息", fieldSets = {
						@FieldSetView(title = "基本信息", fields = { "name", "description" }),
						@FieldSetView(title = "审计", fields = { "active", "createdBy", "created", "updatedBy",
								"updated" }) }),
				@SectionView(type = SectionType.NOTE), @SectionView(type = SectionType.ATTACHMENT) }) )
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SRole extends SBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2217837879194985591L;

	/**
	 * 简述
	 */
	@FieldView(title = "简述")
	@Column(length = 1000)
	private String description;

	public SRole() {
		setInternalTable("SRole");
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
