package org.easycloud.apps.hr.internal;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.myfly.apps.hr.domain.BasicCondition;
import org.myfly.apps.hr.domain.Post;
import org.myfly.apps.hr.domain.PostCategory;
import org.myfly.apps.hr.domain.PostCategorySkill;
import org.myfly.apps.hr.domain.PostScore;
import org.myfly.apps.hr.domain.PostSkillDesc;
import org.myfly.apps.hr.domain.Rank;
import org.myfly.apps.hr.domain.Skill;
import org.myfly.apps.hr.domain.SkillScoreDefine;
import org.myfly.apps.hr.domain.Special;
import org.myfly.platform.core.flydata.service.IFlyDataAccessService;
import org.myfly.platform.core.utils.AppUtil;
import org.myfly.platform.core.utils.ExcelUtil;
import org.myfly.platform.core.utils.UUIDUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
/**
 * 
 * @author xiangwanhong
 *
 */

@Component
public class SampleDataFromExcel {
	private Map<String, Rank> ranks = new LinkedHashMap<>();
	private Map<String, Skill> skills = new LinkedHashMap<>();
	private Map<String, PostCategory> postCategories = new LinkedHashMap<>();
	private Map<String, Special> specials = new LinkedHashMap<>();
	private Map<String, Post> posts = new LinkedHashMap<>();
	private List<BasicCondition> basicConditions = new ArrayList<>();
	private List<SkillScoreDefine> skillScoreDefines = new ArrayList<>();
	private Map<String, PostCategorySkill> postCategorySkills = new LinkedHashMap<>();
	private List<PostSkillDesc> postSkillDescs = new ArrayList<>();
	private List<PostScore> postScores = new ArrayList<>();

	public SampleDataFromExcel() throws Exception {
	}

	/**
	 * 
	 * @param url
	 * @throws Exception
	 */
	public void loadData(URL url) throws Exception {
		XSSFWorkbook book = ExcelUtil.readExcel(url);
		PostCategory postCategory = getPostCategory(book.getSheet("基本条件"), 2, 1);
		loadBasicConditionSheet(book.getSheet("基本条件"));
		loadPostSkillDescSheet(book.getSheet("任职资格-描述"));
		loadSkillScoreDefineForKnowledgeSheet(book.getSheet("知识与技能"));
		loadSkillScoreDefineForCoreCompetenceSheet(book.getSheet("核心能力素质"), postCategory);
		loadPostScoreSheet(book.getSheet("任职资格-分值"));
	}

	/**
	 * 
	 * @param sheet
	 */
	private void loadPostScoreSheet(XSSFSheet sheet) {
		Assert.notNull(sheet);
		XSSFRow headRow = sheet.getRow(4);
		for (int rowNum = 5; rowNum <= sheet.getLastRowNum(); rowNum++) {
			XSSFRow row = sheet.getRow(rowNum);
			Post post = null;
			String postName = ExcelUtil.getValue(row.getCell(1));
			post = posts.get(postName);
			Assert.notNull(post);
			for (int colNum = 2; colNum <= row.getLastCellNum(); colNum++) {
				String skillName = ExcelUtil.getValue(headRow.getCell(colNum));
				if (StringUtils.isNotEmpty(skillName)) {
					// 技能
					Skill skill = skills.get(skillName);
					Assert.notNull(skill);
					long score = 0;
					try {
						score = Math.round(Float.parseFloat(ExcelUtil.getValue(row.getCell(colNum))));
					} catch (Exception e) {
					}
					// 职位技能得分标准
					PostScore postScore = new PostScore();
					postScore.setPost(post);
					postScore.setSkill(skill);
					postScore.setScore(score);
					postScore.setName(UUIDUtil.newUUID());
					postScores.add(postScore);
				}
			}
		}
	}

	/**
	 * 
	 * @param sheet
	 */
	private void loadPostSkillDescSheet(XSSFSheet sheet) {
		Assert.notNull(sheet);
		PostCategory postCategory = getPostCategory(sheet, 2, 1);
		XSSFRow headRow = sheet.getRow(4);
		for (int rowNum = 5; rowNum <= sheet.getLastRowNum(); rowNum++) {
			XSSFRow row = sheet.getRow(rowNum);
			Post post = null;
			String postName = ExcelUtil.getValue(row.getCell(1));
			post = posts.get(postName);
			Assert.notNull(post);
			for (int colNum = 3; colNum <= row.getLastCellNum(); colNum++) {
				String skillName = ExcelUtil.getValue(headRow.getCell(colNum));
				if (StringUtils.isNotEmpty(skillName)) {
					// 技能
					Skill skill = null;
					if (!skills.containsKey(skillName)) {
						skill = new Skill();
						skill.setName(skillName);
						skills.put(skill.getName(), skill);
					} else {
						skill = skills.get(skillName);
					}
					Assert.notNull(skill);
					// 岗位技能
					PostCategorySkill postCategorySkill = null;
					String pcs = postCategory.getName() + " " + skill.getName();
					if (!postCategorySkills.containsKey(pcs)) {
						postCategorySkill = new PostCategorySkill();
						postCategorySkill.setName(pcs);
						postCategorySkill.setPostCategory(postCategory);
						postCategorySkill.setSkill(skill);
						postCategorySkills.put(postCategorySkill.getName(), postCategorySkill);
					} else {
						postCategorySkill = postCategorySkills.get(pcs);
					}
					//
					PostSkillDesc postSkillDesc = new PostSkillDesc();
					postSkillDesc.setPost(post);
					postSkillDesc.setSkill(skill);
					postSkillDesc.setDescription(ExcelUtil.getValue(row.getCell(colNum), "<br>"));
					postSkillDesc.setName(UUIDUtil.newUUID());
					postSkillDescs.add(postSkillDesc);
				}
			}
		}
	}

	private IFlyDataAccessService getFlyDataAccessService() {
		return AppUtil.getJpaFlyDataAccessService();
	}

	private <T> void saveEntities(Map<String, T> entities) {
		List<T> list = new ArrayList<>();
		list.addAll(entities.values());
		getFlyDataAccessService().saveEntity(list);
	}

	public void saveData() {
		saveEntities(ranks);
		saveEntities(postCategories);
		saveEntities(specials);
		saveEntities(posts);
		getFlyDataAccessService().saveEntity(basicConditions);
		saveEntities(skills);
		getFlyDataAccessService().saveEntity(skillScoreDefines);
		saveEntities(postCategorySkills);
		getFlyDataAccessService().saveEntity(postSkillDescs);
		getFlyDataAccessService().saveEntity(postScores);
	}

	private PostCategory getPostCategory(XSSFSheet sheet, int row, int col) {
		String postCategoryName = ExcelUtil.getCellValue(sheet, row, col);
		PostCategory postCategory = null;
		if (!postCategories.containsKey(postCategoryName)) {
			postCategory = new PostCategory();
			postCategory.setName(postCategoryName);
			postCategories.put(postCategory.getName(), postCategory);
		} else {
			postCategory = postCategories.get(postCategoryName);
		}
		Assert.notNull(postCategory);
		return postCategory;
	}

	/**
	 * 
	 * @param sheet
	 */
	private void loadBasicConditionSheet(XSSFSheet sheet) {
		Assert.notNull(sheet);
		PostCategory postCategory = getPostCategory(sheet, 2, 1);

		Assert.isTrue("职级".equals(ExcelUtil.getCellValue(sheet, 3, 0)));
		for (int rowNum = 5; rowNum <= sheet.getLastRowNum(); rowNum++) {
			XSSFRow row = sheet.getRow(rowNum);
			// 职级
			String rankName = ExcelUtil.getValue(row.getCell(0));
			Rank rank = null;
			if (!ranks.containsKey(rankName)) {
				rank = new Rank();
				rank.setName(rankName);
				ranks.put(rank.getName(), rank);
			} else {
				rank = ranks.get(rankName);
			}
			Assert.notNull(rank);

			// 专业
			String specialName = ExcelUtil.getValue(row.getCell(2));
			Special special = null;
			if (!specials.containsKey(specialName)) {
				special = new Special();
				special.setName(specialName);
				specials.put(special.getName(), special);
			} else {
				special = specials.get(specialName);
			}
			Assert.notNull(special);

			// 职位
			Post post = new Post();
			post.setName(ExcelUtil.getValue(row.getCell(1)));
			post.setPostCategory(postCategory);
			post.setRank(rank);
			Assert.isTrue(!posts.containsKey(post.getName()));
			posts.put(post.getName(), post);

			// 基本条件
			BasicCondition basicCondition = new BasicCondition();
			basicCondition.setPost(post);
			basicCondition.setSpecial(special);
			basicCondition.setWorkProduct(ExcelUtil.getValue(row.getCell(3), "<br>"));
			basicCondition.setPerformanceRequirement(ExcelUtil.getValue(row.getCell(4), "<br>"));
			basicCondition.setName(post.getName() + "基本条件");
			basicConditions.add(basicCondition);
		}
	}

	private void loadSkillScoreDefineForKnowledgeSheet(XSSFSheet sheet) {
		Assert.notNull(sheet);
		PostCategory postCategory = getPostCategory(sheet, 2, 2);
		XSSFRow headRow = sheet.getRow(3);
		for (int rowNum = 5; rowNum <= sheet.getLastRowNum(); rowNum++) {
			XSSFRow row = sheet.getRow(rowNum);
			long score = Math.round(Float.valueOf(ExcelUtil.getValue(row.getCell(0))));
			for (int colNum = 2; colNum <= row.getLastCellNum(); colNum++) {
				String skillName = ExcelUtil.getValue(headRow.getCell(colNum));
				if (StringUtils.isNotEmpty(skillName)) {
					// 技能
					Skill skill = skills.get(skillName);
					Assert.notNull(skill);

					// 技能得分标准
					SkillScoreDefine skillScoreDefine = new SkillScoreDefine();
					skillScoreDefine.setPostCategory(postCategory);
					skillScoreDefine.setSkill(skill);
					skillScoreDefine.setDescription(ExcelUtil.getValue(row.getCell(colNum), "<br>"));
					skillScoreDefine.setScore(score);
					skillScoreDefine.setName(UUIDUtil.newUUID());
					skillScoreDefines.add(skillScoreDefine);
				}
			}
		}
	}

	private void loadSkillScoreDefineForCoreCompetenceSheet(XSSFSheet sheet, PostCategory postCategory) {
		Assert.notNull(sheet);
		XSSFRow headRow = sheet.getRow(2);
		for (int rowNum = 4; rowNum <= sheet.getLastRowNum(); rowNum++) {
			XSSFRow row = sheet.getRow(rowNum);
			if (StringUtils.isNotEmpty(ExcelUtil.getValue(row.getCell(0)))) {
				long score = Math.round(Float.valueOf(ExcelUtil.getValue(row.getCell(0))));
				for (int colNum = 1; colNum <= row.getLastCellNum(); colNum++) {
					String skillName = ExcelUtil.getValue(headRow.getCell(colNum));
					if (StringUtils.isNotEmpty(skillName)) {
						// 技能
						Skill skill = skills.get(skillName);
						if (skill != null) {
							// 技能得分标准
							SkillScoreDefine skillScoreDefine = new SkillScoreDefine();
							skillScoreDefine.setPostCategory(postCategory);
							skillScoreDefine.setSkill(skill);
							skillScoreDefine.setDescription(ExcelUtil.getValue(row.getCell(colNum), "<br>"));
							skillScoreDefine.setScore(score);
							skillScoreDefine.setName(UUIDUtil.newUUID());
							skillScoreDefines.add(skillScoreDefine);
						}
					}
				}
			}
		}
	}

	public Map<String, Rank> getRanks() {
		return ranks;
	}

	public Map<String, PostCategory> getPostCategories() {
		return postCategories;
	}

	public Map<String, Special> getSpecials() {
		return specials;
	}

	public Map<String, Post> getPosts() {
		return posts;
	}

	public Map<String, Skill> getSkills() {
		return skills;
	}

	public List<BasicCondition> getBasicConditions() {
		return basicConditions;
	}

	public List<SkillScoreDefine> getSkillScoreDefines() {
		return skillScoreDefines;
	}

	public List<PostSkillDesc> getPostSkillDescs() {
		return postSkillDescs;
	}
}
