package org.easycloud.apps.hr.internal;

import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.myfly.apps.hr.domain.Post;
import org.myfly.apps.hr.domain.PostCategory;
import org.myfly.apps.hr.domain.Rank;
import org.myfly.apps.hr.domain.Skill;
import org.myfly.apps.hr.domain.Special;

public class SampleDataFromEcellTest {
	private static SampleDataFromExcel sampleData;

	@BeforeClass
	public static void before() throws Exception {
		sampleData = new SampleDataFromExcel();
		sampleData.loadData(SampleDataFromEcellTest.class.getResource("/data/远光员工任职资格2016（开发工程师）0108.xlsx"));
	}

	@Test
	public void test开发工程师() {
		Map<String, Rank> ranks = sampleData.getRanks();
		Assert.assertNotNull(ranks);
		Assert.assertEquals("P8", ranks.get("P8").getName());
		PostCategory postCategory = sampleData.getPostCategories().get("开发工程师");
		Assert.assertNotNull(postCategory);
		Special special = sampleData.getSpecials().get("计算机相关专业");
		Assert.assertNotNull(special);
		Post post = sampleData.getPosts().get("首席开发工程师");
		Assert.assertNotNull(post);
		Assert.assertEquals("P8", post.getRank().getName());
		Skill skill = sampleData.getSkills().get("产品与业务");
		Assert.assertNotNull(skill);
		Assert.assertTrue(sampleData.getBasicConditions().size() > 0);
		Assert.assertTrue(sampleData.getSkillScoreDefines().size() > 0);
		Assert.assertTrue(sampleData.getPostSkillDescs().size() > 0);
	}
}
