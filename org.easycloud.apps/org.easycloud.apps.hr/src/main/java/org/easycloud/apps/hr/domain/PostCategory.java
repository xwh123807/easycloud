package org.easycloud.apps.hr.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * 职位序列
 * 
 * @author xiangwanhong
 *
 */
@Entity
@Table(schema = SchemaConstants.HR)
@MetaDataView(tableView = @TableView(title = "职位序列") ,
//
listViews = {
		//
		@ListView(name = "default", fields = { "name", "description" }, listStyle = ListStyle.TABLE) },
		//
		formViews = {
				//
				@FormView(name = "default", sections = {
						@SectionView(fieldSets = { @FieldSetView(fields = { "name", "description" }) }),
						@SectionView(subTables = { @SubTableView(tableAttr = "posts"),
								@SubTableView(tableAttr = "postCategorySkills"),
								@SubTableView(tableAttr = "skillScoreDefines")}) }) })
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PostCategory extends SBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7056569946010581362L;

	@Column
	@FieldView(title = "描述")
	private String description;

	@FieldView(title = "职位")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "postCategory")
	private Set<Post> posts;

	@FieldView(title = "岗位技能")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "postCategory")
	private Set<PostCategorySkill> postCategorySkills;
	
	@FieldView(title="技能得分标准")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "postCategory")
	private Set<SkillScoreDefine> skillScoreDefines;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Post> getPosts() {
		return posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}

	public Set<PostCategorySkill> getPostCategorySkills() {
		return postCategorySkills;
	}

	public void setPostCategorySkills(Set<PostCategorySkill> postCategorySkills) {
		this.postCategorySkills = postCategorySkills;
	}

	public Set<SkillScoreDefine> getSkillScoreDefines() {
		return skillScoreDefines;
	}

	public void setSkillScoreDefines(Set<SkillScoreDefine> skillScoreDefines) {
		this.skillScoreDefines = skillScoreDefines;
	}
}
