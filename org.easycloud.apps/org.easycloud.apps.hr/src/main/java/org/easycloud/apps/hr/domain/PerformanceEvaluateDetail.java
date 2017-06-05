package org.easycloud.apps.hr.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

@Entity
@Table(schema = SchemaConstants.HR)
@MetaDataView(tableView = @TableView(title = "绩效评价明细表") ,
//
listViews = {
		//
		@ListView(name = "default", fields = { "name", "skill", "score" }, listStyle = ListStyle.TABLE) },
		//
		formViews = {
				//
				@FormView(name = "default", sections = {
						@SectionView(fieldSets = { @FieldSetView(fields = { "name", "skill", "score" }) }) }) })
public class PerformanceEvaluateDetail extends SBaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6496744767358104239L;
	@ManyToOne
	@FieldView(title = "绩效评价主表")
	private PerformanceEvaluate performanceEvaluate;
	@ManyToOne
	@FieldView(title = "技能")
	private Skill skill;
	@Column
	@FieldView(title = "得分")
	private Long score;

	public PerformanceEvaluate getPerformanceEvaluate() {
		return performanceEvaluate;
	}

	public void setPerformanceEvaluate(PerformanceEvaluate performanceEvaluate) {
		this.performanceEvaluate = performanceEvaluate;
	}

	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}
}
