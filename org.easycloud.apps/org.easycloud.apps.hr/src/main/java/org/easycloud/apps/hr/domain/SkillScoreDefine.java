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
 * 技能分值标准
 * 
 * @author xiangwanhong
 *
 */
@Entity
@Table(schema = SchemaConstants.HR)
@MetaDataView(tableView = @TableView(title = "技能分值标准") ,
//
listViews = {
		//
		@ListView(name = "default", fields = { "postCategory", "skill", "description",
				"score" }, listStyle = ListStyle.TABLE) },
				//
				formViews = {
						//
						@FormView(name = "default", sections = { @SectionView(fieldSets = {
								@FieldSetView(fields = { "postCategory", "skill", "description", "score" }) }) }) })
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SkillScoreDefine extends SBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4773749748581922968L;

	@ManyToOne
	@FieldView(title = "职位序列")
	private PostCategory postCategory;

	@ManyToOne
	@FieldView(title = "技能")
	private Skill skill;

	@Column(length=2000)
	@FieldView(title = "描述")
	private String description;

	@Column
	@FieldView(title = "分值")
	private Long score;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public PostCategory getPostCategory() {
		return postCategory;
	}

	public void setPostCategory(PostCategory postCategory) {
		this.postCategory = postCategory;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

}
