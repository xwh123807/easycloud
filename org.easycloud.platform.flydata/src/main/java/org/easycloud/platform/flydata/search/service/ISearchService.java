package org.easycloud.platform.flydata.search.service;

import java.util.List;
import java.util.Map;

import org.easycloud.platform.metadata.annotation.entity.SKeyEntity;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;

/**
 * 搜索服务
 * 
 * @author xiangwanhong
 *
 */
public interface ISearchService {
	public final static String ARTICLE_ID = "uid";
	
	public final static String ARTICLE_CONTENT = "fileContent";

	public final static String ARTICLE_NAME = "fileName";

	public final static String ARTICLE_PATH = "filePath";

	public final static String ARTICLE_DATE = "fileDate";
	
	public final static String ARTICLE_FRAGMENT = "fragment";

	/**
	 * 对路径下的文件进行索引。如果path是目录，则索引该目录下所有文件，包含子目录
	 * 
	 * @param path
	 */
	void indexAttachments(String path);

	/**
	 * 搜索附件
	 * 
	 * @param queryString
	 * @param page
	 * @param size
	 * @return
	 */
	Page<Map<String, String>> searchAttachments(String queryString, int page, int size);

	/**
	 * 清空附件索引
	 */
	void cleanAttachmentsIndex();

	/**
	 * 重建附件索引
	 */
	void rebuildAttachmentsIndex();

	/**
	 * 为Fly实体类创建索引
	 * @param domainClass
	 * @return
	 */
	<T extends SKeyEntity> boolean createIndex(Class<T> domainClass);
	
	/**
	 * 删除Fly实体类索引
	 * @param domainClass
	 * @return
	 */
	<T extends SKeyEntity> boolean deleteIndex(Class<T> domainClass);
	
	/**
	 * 为实体对象创建索引
	 * @param document
	 */
	<T extends SKeyEntity> void index(T entity);
	
	/**
	 * 更新实体对象索引
	 * @param entity
	 */
	<T extends SKeyEntity> void update(T entity);
	
	/**
	 * 删除索引
	 * @param entity
	 */
	<T extends SKeyEntity> void delete(T entity);
	
	/**
	 * 为对象创建索引
	 * @param entity
	 * @param uid
	 */
	<T> void index(T entity, String uid);
	
	/**
	 * 
	 * @param entityName
	 * @param uid
	 */
	void deleteEntity(String entityName, String uid);
	/**
	 * 
	 * @param entityName
	 * @param uid
	 */
	void indexEntity(String entityName, String uid);
	
	/**
	 * 修改对象索引
	 * @param entity
	 * @param uid
	 */
	<T> void update(T entity, String uid);
	
	/**
	 * 删除实体索引
	 * @param uid
	 */
	void delete(String indexName, String uid);
	
	/**
	 * 建立索引
	 * @param indexQuery
	 */
	void index(IndexQuery indexQuery);
	
	/**
	 * 批量建立索引
	 * @param indexQueries
	 */
	void index(List<IndexQuery> indexQueries);
	
	/**
	 * 修改索引
	 * @param updateQuery
	 */
	void update(UpdateQuery updateQuery);
	/**
	 * 批量修改索引
	 * @param updateQueries
	 */
	void update(List<UpdateQuery> updateQueries);
	/**
	 * 删除索引
	 * @param deleteQuery
	 */
	void delete(DeleteQuery deleteQuery);
	
	<T> T search(String uid, Class<T> domainClass);
	
	void deleteEntityIndex(String entityName);
	
	SearchResponse search(SearchQuery query);
	/**
	 * 重建实体索引。首先删除数据，再分页取出数据，并构建索引
	 * @param entityName
	 */
	void rebuildEntityIndex(String entityName);
	/**
	 * 重新构建全部实体索引
	 */
	void rebuildAllEntityIndex();
}
