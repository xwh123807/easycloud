package org.easycloud.platform.flydata.search.internal;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easycloud.platform.flydata.search.service.IFullTextSearchService;
import org.easycloud.platform.metadata.service.IEntityMetaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class FullTextSearchService implements IFullTextSearchService {
	private Log log = LogFactory.getLog(getClass());
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private IEntityMetaDataService entityMetaDataService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.myfly.platform.metadata.service.internal.IFullTextSearchService#
	 * rebuildAllIndex()
	 */
	@Override
	public void rebuildAllIndex() {
//		if (log.isInfoEnabled()) {
//			log.info("启动重建索引");
//		}
//		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
//		try {
//			fullTextEntityManager.createIndexer().startAndWait();
//			log.info("索引重建完成.");
//		} catch (InterruptedException e) {
//			log.error("重建索引失败，", e);
//			e.printStackTrace();
//		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.myfly.platform.metadata.service.internal.IFullTextSearchService#
	 * rebuildIndexByEntityName(java.lang.String)
	 */
	@Override
	public void rebuildIndexByEntityName(String entityName) {
//		AssertUtil.parameterEmpty(entityName, "entityName");
//		EntityMetaData metaData = entityMetaDataService.getEntityMetaData(entityName);
//		AssertUtil.parameterEmpty(metaData, "metaData");
//		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
//		if (log.isInfoEnabled()) {
//			log.info("重建实体[" + entityName + "]索引...");
//		}
//		try {
//			fullTextEntityManager.createIndexer(metaData.getEntityClass()).startAndWait();
//			log.info("实体[" + entityName + "]索引重建完成.");
//		} catch (Exception e) {
//			throw new IllegalArgumentException("重建实体[" + entityName + "]索引失败，错误信息：" + e.getMessage());
//		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> searchName(String searchText) {
		return null;
//		AssertUtil.parameterEmpty(searchText, "searchText");
//		List<T> result = null;
//		QueryBuilder qb = Search.getFullTextEntityManager(entityManager).getSearchFactory().buildQueryBuilder()
//				.forEntity(SGlobalName.class).get();
//		Query lucenequery = qb.keyword().onField("name").matching(searchText).createQuery();
//		javax.persistence.Query jpaQuery = Search.getFullTextEntityManager(entityManager)
//				.createFullTextQuery(lucenequery, SGlobalName.class);
//		result = jpaQuery.getResultList();
//		return result;
	}

	/**
	 * 搜索表中的关键字，返回满足条件的记录
	 * 
	 * @param entityName
	 * @param searchText
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional
	public <T> Page<T> search(String entityName, String searchText, int page, int size) {
		return null;
//		AssertUtil.parameterEmpty(entityName, "entityName");
//		if (StringUtils.isBlank(searchText)) {
//			searchText = "*:*";
//		}
//		EntityMetaData metaData = entityMetaDataService.getEntityMetaData(entityName);
//		AssertUtil.parameterEmpty(metaData, "metaData");
//		QueryParser parser = new QueryParser("fullText", new StandardAnalyzer());
//		Query luceneqQuery = null;
//		try {
//			luceneqQuery = parser.parse(searchText);
//		} catch (Exception e) {
//			throw new IllegalArgumentException(
//					"搜索实体[" + entityName + "],关键字[" + searchText + "]失败，错误信息：" + e.getMessage());
//		}
//
//		//Search.getFullTextEntityManager(entityManager).getSearchFactory().buildQueryBuilder()
//		//		.forEntity(metaData.getEntityClass()).get().keyword().onField(field)
//
//		FullTextQuery fullTextQuery = Search.getFullTextEntityManager(entityManager).createFullTextQuery(luceneqQuery,
//				metaData.getEntityClass());
//		fullTextQuery.setFirstResult(page * size);
//		fullTextQuery.setMaxResults(size);
//		PageRequest pageable = new PageRequest(page, size);
//		PageImpl<T> pageImpl = new PageImpl<>(fullTextQuery.getResultList(), pageable, fullTextQuery.getResultSize());
//		return pageImpl;
	}
}
