package org.easycloud.apps.hr.internal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.lang3.RandomUtils;
import org.myfly.apps.hr.domain.BasicCondition;
import org.myfly.apps.hr.domain.PerformanceEvaluate;
import org.myfly.apps.hr.domain.PerformanceEvaluateDetail;
import org.myfly.apps.hr.domain.Post;
import org.myfly.apps.hr.domain.PostCategory;
import org.myfly.apps.hr.domain.PostCategorySkill;
import org.myfly.apps.hr.domain.PostScore;
import org.myfly.apps.hr.domain.Rank;
import org.myfly.apps.hr.domain.Skill;
import org.myfly.apps.hr.domain.Special;
import org.myfly.platform.core.context.UserContext;
import org.myfly.platform.core.domain.SBaseEntity;
import org.myfly.platform.core.flydata.service.IFlyDataAccessService;
import org.myfly.platform.core.user.domain.SUser;
import org.myfly.platform.core.utils.AppUtil;
import org.myfly.platform.core.utils.DateUtil;
import org.myfly.platform.core.utils.UUIDUtil;
import org.springframework.stereotype.Component;

@Component
public class SampleDataBuilder {
	private interface IEntityBuilder<T extends SBaseEntity> {
		T buildEntity(Object[] data);
	}

	private <T> Map<String, T> buildEntityMap(Object[][] datas, IEntityBuilder<?> builder) {
		Map<String, T> result = new LinkedHashMap<>();
		for (Object[] data : datas) {
			SBaseEntity entity = builder.buildEntity(data);
			result.put(entity.getName(), (T) entity);
		}
		return result;
	}

	private IFlyDataAccessService getFlyDataAccessService() {
		return AppUtil.getJpaFlyDataAccessService();
	}

	public <T> void saveEntities(Map<String, T> entities) {
		List<T> list = new ArrayList<>();
		list.addAll(entities.values());
		getFlyDataAccessService().saveEntity(list);
	}

	/**
	 * 构建专业数据
	 * 
	 * @return
	 */
	public Map<String, Special> buildSpecials() {

		Object[][] datas = new Object[][] { { "计算机专业" }, { "数学专业" } };
		return buildEntityMap(datas, new IEntityBuilder<Special>() {

			@Override
			public Special buildEntity(Object[] data) {
				Special entity = new Special();
				entity.setName((String) data[0]);
				return entity;
			}
		});
	}

	/**
	 * 构建职级数据
	 * 
	 * @return
	 */
	public Map<String, Rank> buildRanks() {
		Object[][] datas = new Object[][] { { "P1", "助理" }, { "P2", "初级" }, { "P3", "中级" }, { "P4", "高级" },
				{ "P5", "资深" }, { "P6", "专家" }, { "P7", "高级专家" }, { "P8", "首席" } };
		return buildEntityMap(datas, new IEntityBuilder<Rank>() {

			@Override
			public Rank buildEntity(Object[] data) {
				Rank rank = new Rank();
				rank.setName((String) data[0]);
				rank.setTitle((String) data[1]);
				return rank;
			}

		});
	}

	/**
	 * 构建岗位序列
	 * 
	 * @return
	 */
	public Map<String, PostCategory> buildPostCategories() {
		Object[][] datas = new Object[][] { { "开发工程师" }, { "设计工程师" }, { "系统管理工程师" }, { "测试工程师" } };
		return buildEntityMap(datas, new IEntityBuilder<PostCategory>() {

			@Override
			public PostCategory buildEntity(Object[] data) {
				PostCategory entity = new PostCategory();
				entity.setName((String) data[0]);
				return entity;
			}
		});
	}

	/**
	 * 构建技能
	 * 
	 * @return
	 */
	public Map<String, Skill> buildSkill() {
		Object[][] datas = new Object[][] { { "产品与业务" }, { "专业影响力" }, { "用户思维与产品体验" }, { "设计技能" }, { "开发编码" },
				{ "技术前瞻性和多领域知识" }, { "软件架构与软件工程" }, { "公司核心价值观" }, { "客户导向" }, { "沟通协作" }, { "执行力" } };
		return buildEntityMap(datas, new IEntityBuilder<Skill>() {

			@Override
			public Skill buildEntity(Object[] data) {
				Skill entity = new Skill();
				entity.setName((String) data[0]);
				return entity;
			}
		});
	}

	/**
	 * 构建岗位序列技能
	 * 
	 * @param skills
	 * @param postCategories
	 * 
	 * @return
	 */
	public Map<String, PostCategorySkill> buildPostCategorySkills(Map<String, PostCategory> postCategories,
			Map<String, Skill> skills) {
		Map<String, PostCategorySkill> postCategorytSkills = new LinkedHashMap<>();
		postCategories.values().forEach(new Consumer<PostCategory>() {

			@Override
			public void accept(PostCategory category) {
				skills.values().forEach(new Consumer<Skill>() {

					@Override
					public void accept(Skill skill) {
						PostCategorySkill postCategorySkill = new PostCategorySkill();
						postCategorySkill.setPostCategory(category);
						postCategorySkill.setSkill(skill);
						postCategorySkill.setName(UUIDUtil.newUUID());
						postCategorytSkills.put(postCategorySkill.getName(), postCategorySkill);
					}
				});
			}
		});
		return postCategorytSkills;
	}

	/**
	 * 构建岗位
	 * 
	 * @param postCategories
	 * @param ranks
	 * @return
	 */
	public Map<String, Post> buildPosts(Map<String, Rank> ranks, Map<String, PostCategory> postCategories) {
		Map<String, Post> posts = new LinkedHashMap<>();
		postCategories.values().forEach(new Consumer<PostCategory>() {

			@Override
			public void accept(PostCategory postCategory) {
				ranks.values().forEach(new Consumer<Rank>() {

					@Override
					public void accept(Rank rank) {
						Post post = new Post();
						post.setPostCategory(postCategory);
						post.setRank(rank);
						post.setName(rank.getTitle() + postCategory.getName());
						posts.put(post.getName(), post);
					}
				});
			}
		});
		return posts;
	}
	/*
	 * "1、主持过1个及以上子系统或同等规模的平台、组件及其他定制开发工作；
	 * 2、独立承担过1个及以上子系统或同等规模的平台、组件及其他定制开发的技术解决方案的编写；
	 * 3、近2年内在外部媒体上发表过1篇以上的论文或累计成功申请过1项以上公司专利； 4、近1年来未被通报任何研发黑色事件。"
	 * 最近两次绩效评价BB及以上 "1、作为开发经理或核心骨干承担完成过累计工作量不少于60人天的核心功能开发；
	 * 2、熟练掌握1个及以上子系统或同等规模的平台、组件及其他定制开发任务的全部业务及核心代码； 3、近1年来未被通报任何研发黑色事件。"
	 * 最近两次绩效评价BB及以上 "1、主导过公司产品1个及以上子系统的开发工作或同等规模的平台、组件及其他定制开发工作；
	 * 2、作为核心骨干独立承担完成过累计工作量不少于30人天的核心功能的开发； 3、近1年来未被通报任何研发黑色事件。" 最近两次绩效评价BB及以上
	 * "1、参与过公司产品1个及以上子系统的开发工作或同等规模的平台、组件及其他定制开发工作；
	 * 2、独立承担完成过工作量相当于5人天以上的单个功能需求开发任务； 3、近1年来未被通报任何研发黑色事件。" 最近一次绩效评价B及以上
	 * "1、参与过公司产品1个及以上子系统的开发工作或同等规模的平台、组件及其他定制开发工作；
	 * 2、在指导下完成过工作量相当于3人天的单个功能需求开发任务； 3、近1年来未被通报任何研发黑色事件。" 最近一次绩效评价B及以上
	 */

	public Map<String, BasicCondition> buildBasicConditions(Map<String, Post> posts, Map<String, Special> specials) {
		Object[][] datas = new Object[][] {
				{ "首席开发工程师", "计算机专业", "1、主持过3个以上独立产品或业务模块的研发工作； 2、近1年来未被通报任何研发黑色事件。", "最近两次绩效评价BB及以上" },
				{ "高级专家开发工程师", "计算机专业", "1、主持过2个以上独立产品或业务模块的研发工作； 2、近1年来未被通报任何研发黑色事件。", "最近两次绩效评价BB及以上" },
				{ "专家开发工程师", "计算机专业", "1、主持过1个及以上独立产品或业务模块的研发工作； 2、近1年来未被通报任何研发黑色事件。", "最近两次绩效评价BB及以上" } };
		return buildEntityMap(datas, new IEntityBuilder<BasicCondition>() {

			@Override
			public BasicCondition buildEntity(Object[] data) {
				BasicCondition entity = new BasicCondition();
				entity.setPost(posts.get(data[0]));
				entity.setSpecial(specials.get(data[1]));
				entity.setWorkProduct((String) data[2]);
				entity.setPerformanceRequirement((String) data[3]);
				entity.setName(entity.getPost().getName());
				return entity;
			}
		});
	}

	public Map<String, PostScore> buildPostScores(Map<String, Post> posts,
			Map<String, PostCategorySkill> postCategorySkills) {
		Map<String, PostScore> postScores = new LinkedHashMap<>();
		postCategorySkills.values().forEach(new Consumer<PostCategorySkill>() {

			@Override
			public void accept(PostCategorySkill postCategorySkill) {
				posts.values().forEach(new Consumer<Post>() {

					@Override
					public void accept(Post post) {
						if (post.getPostCategory().getName().equals(postCategorySkill.getPostCategory().getName())) {
							PostScore postScore = new PostScore();
							postScore.setPost(post);
							postScore.setSkill(postCategorySkill.getSkill());
							postScore.setScore((long) RandomUtils.nextInt(1, 5));
							postScore.setName(UUIDUtil.newUUID());
							postScores.put(postScore.getName(), postScore);
						}
					}
				});
			}
		});
		return postScores;
	}

	public Map<String, PerformanceEvaluate> buildPerformanceEvaluates(Map<String, Post> posts,
			Map<String, Skill> skills) {
		Object[][] datas = new Object[][] { { UserContext.getUserSession().getUser(), "首席开发工程师", 30L } };
		return buildEntityMap(datas, new IEntityBuilder<PerformanceEvaluate>() {

			@Override
			public PerformanceEvaluate buildEntity(Object[] data) {
				PerformanceEvaluate entity = new PerformanceEvaluate();
				entity.setUser((SUser) data[0]);
				entity.setPost(posts.get(data[1]));
				entity.setPeriod(DateUtil.nowDate());
				entity.setTotalScore((Long) data[2]);
				entity.setName(entity.getUser().getName() + "绩效考评");
				return entity;
			}
		});
	}

	public Map<String, PerformanceEvaluateDetail> buildPerformanceEvaluateDetails(
			Map<String, PerformanceEvaluate> evaluates, Map<String, PostCategorySkill> postCategorytSkills) {
		Map<String, PerformanceEvaluateDetail> details = new LinkedHashMap<>();
		evaluates.values().forEach(new Consumer<PerformanceEvaluate>() {

			@Override
			public void accept(PerformanceEvaluate evaluate) {
				postCategorytSkills.values().forEach(new Consumer<PostCategorySkill>() {

					@Override
					public void accept(PostCategorySkill postCategorySkill) {
						if (evaluate.getPost().getPostCategory().getName()
								.equals(postCategorySkill.getPostCategory().getName())) {
							PerformanceEvaluateDetail entity = new PerformanceEvaluateDetail();
							entity.setPerformanceEvaluate(evaluate);
							entity.setSkill(postCategorySkill.getSkill());
							entity.setScore((long) RandomUtils.nextInt(1, 5));
							entity.setName(UUIDUtil.newUUID());
							details.put(entity.getName(), entity);
						}
					}

				});
			}
		});
		return details;
	}

//	public Map<String, PostSkillDesc> buildPostSkillDesc(Map<String, PostCategory> postCategories,
//			Map<String, Skill> skills) {
//		Object[][] datas = new Object[][] { { "开发工程师", "产品与业务", 5L,
//				"1、精通至少2个产品或业务领域所涉及到的业务知识；<br>" + "2、孰悉友商产品的主要功能及核心结构，能提供与远光产品主要特点和功能的分析对比；<br>"
//						+ "3、掌握产品所属行业的信息化现状及信息化发展趋势； <br>" + "4、针对该产品或业务领域的功能，能够提出业务整合、结构优化以及业务架构调整等的建议方案；<br>"
//						+ "5、具备评审业务方案的能力，并能提出改进建议；<br>" + "6、具备培训他人的能力。" } };
//		return buildEntityMap(datas, new IEntityBuilder<PostSkillDesc>() {
//
//			@Override
//			public PostSkillDesc buildEntity(Object[] data) {
//				PostSkillDesc entity = new PostSkillDesc();
//				entity.setPostCategory(postCategories.get(data[0]));
//				entity.setSkill(skills.get(data[1]));
//				entity.setScore((Long) data[2]);
//				entity.setDescription((String) data[3]);
//				entity.setName(UUIDUtil.newUUID());
//				return entity;
//			}
//		});
//	}

	public void initSampleData() {
		Map<String, Special> specials = buildSpecials();
		saveEntities(specials);
		Map<String, Rank> ranks = buildRanks();
		saveEntities(ranks);
		Map<String, PostCategory> postCategories = buildPostCategories();
		saveEntities(postCategories);
		Map<String, Skill> skills = buildSkill();
		saveEntities(skills);
		Map<String, Post> posts = buildPosts(ranks, postCategories);
		saveEntities(posts);
		Map<String, PostCategorySkill> postCategorytSkills = buildPostCategorySkills(postCategories, skills);
		saveEntities(postCategorytSkills);
		Map<String, BasicCondition> basicConditions = buildBasicConditions(posts, specials);
		saveEntities(basicConditions);
		Map<String, PostScore> postScores = buildPostScores(posts, postCategorytSkills);
		saveEntities(postScores);
		Map<String, PerformanceEvaluate> evaluates = buildPerformanceEvaluates(posts, skills);
		saveEntities(evaluates);
		Map<String, PerformanceEvaluateDetail> evaluateDetails = buildPerformanceEvaluateDetails(evaluates,
				postCategorytSkills);
		saveEntities(evaluateDetails);
//		Map<String, PostSkillDesc> postSkillDescs = buildPostSkillDesc(postCategories, skills);
//		saveEntities(postSkillDescs);
	}
}
