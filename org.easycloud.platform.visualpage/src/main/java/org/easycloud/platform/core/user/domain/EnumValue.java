package org.easycloud.platform.core.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
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
 * 枚举值
 * 
 * @author xiangwanhong
 *
 */

@Entity
@Table(schema = SchemaConstants.PB)
@MetaDataView(tableView = @TableView(title = "枚举值") ,
//
listViews = { @ListView(fields = { "name", "isDefault", "createdBy" }, listStyle = ListStyle.TABLE) },
//
formViews = { @FormView(sections = { @SectionView(fieldSets = { @FieldSetView(fields = { "name", "isDefault" }) }),
		@SectionView(type = SectionType.NOTE), @SectionView(type = SectionType.ATTACHMENT) }) })
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EnumValue extends SBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8229221002232821733L;

	@FieldView(title = "是否首选")
	@Column
	private Boolean isDefault;

	@FieldView(title = "枚举类型")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private EnumType enumType;

	public EnumType getEnumType() {
		return enumType;
	}

	public void setEnumType(EnumType enumType) {
		this.enumType = enumType;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
}
