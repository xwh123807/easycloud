package org.easycloud.apps.hr.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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
 * 基本条件
 * 
 * @author xiangwanhong
 *
 */
@Table(schema = SchemaConstants.HR)
@Entity
@MetaDataView(tableView = @TableView(title = "任职基本条件") ,
//
listViews = {
		//
		@ListView(name = "default", fields = { "post", "special", "workProduct",
				"performanceRequirement" }, listStyle = ListStyle.TABLE) },
				//
				formViews = {
						//
						@FormView(name = "default", sections = { @SectionView(fieldSets = { @FieldSetView(fields = {
								"post", "special", "workProduct", "performanceRequirement" }) }) }) })
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BasicCondition extends SBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8120245408639298174L;

	@FieldView(title = "职位")
	@ManyToOne
	private Post post;

	@FieldView(title = "专业")
	@ManyToOne
	private Special special;

	@Column
	@FieldView(title = "工作成果")
	private String workProduct;

	@Column
	@FieldView(title = "绩效要求")
	private String performanceRequirement;

	public Special getSpecial() {
		return special;
	}

	public void setSpecial(Special special) {
		this.special = special;
	}

	public String getWorkProduct() {
		return workProduct;
	}

	public void setWorkProduct(String workProduct) {
		this.workProduct = workProduct;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public String getPerformanceRequirement() {
		return performanceRequirement;
	}

	public void setPerformanceRequirement(String performanceRequirement) {
		this.performanceRequirement = performanceRequirement;
	}
}
