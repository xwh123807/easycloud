package org.easycloud.platform.core.user.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.easycloud.platform.core.domain.SBaseEntity;
import org.easycloud.platform.core.domain.SchemaConstants;
import org.easycloud.platform.metadata.annotation.entity.FieldView;
import org.easycloud.platform.metadata.annotation.entity.TableView;
import org.easycloud.platform.metadata.annotation.view.FieldSetView;
import org.easycloud.platform.metadata.annotation.view.FilterView;
import org.easycloud.platform.metadata.annotation.view.FormView;
import org.easycloud.platform.metadata.annotation.view.ListView;
import org.easycloud.platform.metadata.annotation.view.MetaDataView;
import org.easycloud.platform.metadata.annotation.view.SectionView;
import org.easycloud.platform.metadata.annotation.view.SubTableView;
import org.easycloud.platform.metadata.define.view.ListStyle;
import org.easycloud.platform.metadata.define.view.SectionType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 枚举类型
 * 
 * @author xiangwanhong
 *
 */
@Entity
@Table(schema = SchemaConstants.PB, indexes={@Index(name="idx_enumtype_entitynameattr",unique=true, columnList="entityName,attrName")})
@MetaDataView(tableView = @TableView(title = "枚举类型") , listViews = {
		//
		@ListView(name = "default", fields = { "name", "description", "entityName",
				"attrName" }, listStyle = ListStyle.TABLE, filters = { @FilterView(field = "name"),
						@FilterView(field = "description") }) }, formViews = {
								//
								@FormView(name = "default", sections = {
										@SectionView(title = "基本信息", fieldSets = { @FieldSetView(fields = { "name",
												"description", "entityName", "attrName" }) }, subTables = {
														@SubTableView(tableAttr = "enumValues") }),
										@SectionView(type = SectionType.NOTE),
										@SectionView(type = SectionType.ATTACHMENT) }) })
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EnumType extends SBaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2761528961975859308L;
	/**
	 * 描述
	 */
	@FieldView(title = "描述")
	@Column
	private String description;
	/**
	 * 实体名称，实体唯一标示
	 */
	@FieldView(title = "实体名称")
	@Column
	private String entityName;
	/**
	 * 属性名
	 */
	@FieldView(title = "属性名称")
	@Column
	private String attrName;

	@FieldView(title = "枚举值")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "enumType")
	private Set<EnumValue> enumValues;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
}
