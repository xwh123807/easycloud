package org.easycloud.apps.hr.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
import org.myfly.platform.core.metadata.annotation.SubTableView;
import org.myfly.platform.core.metadata.annotation.TableView;
import org.myfly.platform.core.metadata.define.ListStyle;

/**
 * 职位
 * 
 * @author xiangwanhong
 *
 */
@Entity
@Table(schema = SchemaConstants.HR)
@MetaDataView(tableView = @TableView(title = "职位") ,
//
listViews = {
		//
		@ListView(name = "default", fields = { "name", "rank", "postCategory",
				"description" }, listStyle = ListStyle.TABLE) },
				//
				formViews = {
						//
						@FormView(name = "default", sections = {
								@SectionView(fieldSets = {
										@FieldSetView(fields = { "name", "rank", "postCategory", "description" }) }),
								@SectionView(subTables = { @SubTableView(tableAttr = "postSkillDescs"),
										@SubTableView(tableAttr = "postScores") }) }) })
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Post extends SBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3779340242083336917L;

	@Column
	@FieldView(title = "描述")
	private String description;

	@ManyToOne
	@FieldView(title = "职位序列")
	private PostCategory postCategory;

	@ManyToOne
	@FieldView(title = "职级")
	private Rank rank;

	@OneToMany(mappedBy = "post")
	@FieldView(title = "职位技能描述")
	private Set<PostSkillDesc> postSkillDescs;

	@OneToMany(mappedBy = "post")
	@FieldView(title = "职位评分")
	private Set<PostScore> postScores;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PostCategory getPostCategory() {
		return postCategory;
	}

	public void setPostCategory(PostCategory postCategory) {
		this.postCategory = postCategory;
	}

	public Rank getRank() {
		return rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}

	public Set<PostScore> getPostScores() {
		return postScores;
	}

	public void setPostScores(Set<PostScore> postScores) {
		this.postScores = postScores;
	}

	public Set<PostSkillDesc> getPostSkillDescs() {
		return postSkillDescs;
	}

	public void setPostSkillDescs(Set<PostSkillDesc> postSkillDescs) {
		this.postSkillDescs = postSkillDescs;
	}
}
