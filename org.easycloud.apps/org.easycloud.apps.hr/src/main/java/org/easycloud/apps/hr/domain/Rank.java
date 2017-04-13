package org.easycloud.apps.hr.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.myfly.platform.core.domain.SBaseEntity;
import org.myfly.platform.core.domain.SchemaConstants;
import org.myfly.platform.core.metadata.annotation.FieldSetView;
import org.myfly.platform.core.metadata.annotation.FieldView;
import org.myfly.platform.core.metadata.annotation.FormView;
import org.myfly.platform.core.metadata.annotation.ListView;
import org.myfly.platform.core.metadata.annotation.MetaDataView;
import org.myfly.platform.core.metadata.annotation.SectionView;
import org.myfly.platform.core.metadata.annotation.TableView;
import org.myfly.platform.core.metadata.define.ListStyle;

/**
 * 职级
 * 
 * @author xiangwanhong
 *
 */
@Entity
@Table(schema = SchemaConstants.HR)
@MetaDataView(tableView = @TableView(title = "职级") ,
//
listViews = {
		//
		@ListView(name = "default", fields = { "name", "title", "description" }, listStyle = ListStyle.TABLE) },
		//
		formViews = {
				//
				@FormView(name = "default", sections = {
						@SectionView(fieldSets = { @FieldSetView(fields = { "name", "title", "description" }) }) }) })
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Rank extends SBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3033653273244536719L;

	@Column
	@FieldView(title = "标题")
	private String title;
	/**
	 * 描述
	 */
	@Column
	@FieldView(title = "描述")
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
