package org.myfly.platform.core.search.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.search.SearchResponse;
import org.myfly.platform.core.domain.SKeyEntity;
import org.myfly.platform.core.flydata.service.FlyEntityMap;
import org.myfly.platform.core.flydata.service.IFlyDataAccessService;
import org.myfly.platform.core.metadata.internal.EntityMetaData;
import org.myfly.platform.core.metadata.internal.EntityMetaDataConstants;
import org.myfly.platform.core.metadata.service.IEntityMetaDataService;
import org.myfly.platform.core.search.service.ISearchService;
import org.myfly.platform.core.utils.AppUtil;
import org.myfly.platform.core.utils.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
// @CacheConfig(cacheNames = "search-service")
public class SearchService implements ISearchService {

	private static Log log = LogFactory.getLog(SearchService.class);

	@Autowired
	private ElasticsearchTemplate searchTemplate;

	@Autowired
	private IEntityMetaDataService entityMetaDataService;

	@Override
	public void indexAttachments(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public Page<Map<String, String>> searchAttachments(String queryString, int page, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cleanAttachmentsIndex() {
		// TODO Auto-generated method stub

	}

	@Override
	public void rebuildAttachmentsIndex() {
		// TODO Auto-generated method stub

	}

	private String getIndexName(Class<?> domainClass) {
		return ClassUtils.getShortClassName(domainClass).toLowerCase();
	}

	@Override
	public <T extends SKeyEntity> boolean createIndex(Class<T> domainClass) {
		Assert.notNull(domainClass);
		return searchTemplate.createIndex(domainClass);
	}

	@Override
	public <T extends SKeyEntity> boolean deleteIndex(Class<T> domainClass) {
		Assert.notNull(domainClass);
		return searchTemplate.deleteIndex(domainClass);
	}

	@Override
	public <T extends SKeyEntity> void index(T entity) {
		Assert.notNull(entity);
		Assert.hasLength(entity.getUid());
		index(entity, entity.getUid());
	}

	@Override
	public <T extends SKeyEntity> void update(T entity) {
		Assert.notNull(entity);
		Assert.hasLength(entity.getUid());
		update(entity, entity.getUid());
	}

	@Override
	public <T> void index(T entity, String uid) {
		Assert.notNull(entity);
		Assert.hasLength(uid);
		String indexName = getIndexName(entity.getClass());
		searchTemplate.getClient().prepareIndex(indexName, indexName, uid).setSource(JSONUtil.toJSON(entity)).get();
	}

	@Override
	public <T> void update(T entity, String uid) {
		Assert.notNull(entity);
		Assert.hasLength(uid);
		String indexName = getIndexName(entity.getClass());
		try {
			searchTemplate.getClient().prepareUpdate(indexName, indexName, uid)
					.setSource(JSONUtil.toJSON(entity).getBytes()).get();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public <T extends SKeyEntity> void delete(T entity) {
		Assert.notNull(entity);
		Assert.hasLength(entity.getUid());
		String indexName = getIndexName(entity.getClass());
		delete(indexName, entity.getUid());
	}

	@Override
	public void delete(String indexName, String uid) {
		Assert.hasLength(uid);
		searchTemplate.getClient().prepareDelete(indexName, indexName, uid).get();
	}

	private String getIndexName(String entityName) {
		return entityMetaDataService.getEntityMetaData(entityName).getTableDefinition().getIndexName();
	}

	@Override
	public void deleteEntity(String entityName, String uid) {
		delete(getIndexName(entityName), uid);
	}

	@Override
	public void indexEntity(String entityName, String uid) {
		Assert.hasLength(entityName);
		Assert.hasLength(uid);
		FlyEntityMap entity = AppUtil.getFlyDataAccessService(entityName).findOne(entityName, uid,
				EntityMetaDataConstants.DEFAULT_ALL_NAME, true);
		if (entity == null){
			return;
		}
		String indexName = getIndexName(entityName);
		searchTemplate.getClient().prepareIndex(indexName, indexName, uid).setSource(JSONUtil.toJSON(entity)).get();
	}

	@Override
	public void index(IndexQuery indexQuery) {
		Assert.notNull(indexQuery);
		searchTemplate.index(indexQuery);
	}

	@Override
	public void index(List<IndexQuery> indexQueries) {
		Assert.notEmpty(indexQueries);
		searchTemplate.bulkIndex(indexQueries);
	}

	@Override
	public void update(UpdateQuery updateQuery) {
		Assert.notNull(updateQuery);
		searchTemplate.update(updateQuery);
	}

	@Override
	public void update(List<UpdateQuery> updateQueries) {
		Assert.notEmpty(updateQueries);
		searchTemplate.bulkUpdate(updateQueries);
	}

	@Override
	public void delete(DeleteQuery deleteQuery) {
		Assert.notNull(deleteQuery);
		searchTemplate.delete(deleteQuery);
	}

	@Override
	public <T> T search(String uid, Class<T> domainClass) {
		GetQuery query = new GetQuery();
		query.setId(uid);
		return searchTemplate.queryForObject(query, domainClass);
	}

	@Override
	public SearchResponse search(SearchQuery query) {
		return null;
	}

	/**
	 * TODO 数据量太大时，可能会导致内存溢出，应当分页处理
	 */
	@Override
	public void rebuildEntityIndex(String entityName) {
		if (log.isInfoEnabled()) {
			log.info("重建实体[" + entityName + "]索引...");
		}
		// 先删除索引
		deleteEntityIndex(entityName);
		IFlyDataAccessService dataService = AppUtil.getFlyDataAccessService(entityName);
		EntityMetaData metaData = AppUtil.getEntityMetadata(entityName);
		List<FlyEntityMap> lists = dataService.findAll(entityName, EntityMetaDataConstants.DEFAULT_ALL_NAME, null,
				true);
		if (log.isInfoEnabled()) {
			log.info("实体[" + entityName + "]记录数[" + lists.size() + "]");
		}
		if (lists.isEmpty()) {
			return;
		}
		List<IndexQuery> queries = new ArrayList<>();
		String indexName = metaData.getTableDefinition().getIndexName();
		lists.forEach(new Consumer<FlyEntityMap>() {

			@Override
			public void accept(FlyEntityMap entity) {
				IndexQuery query = new IndexQueryBuilder().withIndexName(indexName).withType(indexName)
						.withId(entity.get("uid")).withSource(JSONUtil.toJSON(entity)).build();
				queries.add(query);
			}
		});
		index(queries);
		log.info("重建实体[" + entityName + "]索引完成.");
	}

	@Override
	public void rebuildAllEntityIndex() {
		entityMetaDataService.getAllEntityClasses().forEach(new Consumer<Class<?>>() {

			@Override
			public void accept(Class<?> t) {
				try {
					rebuildEntityIndex(t.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void deleteEntityIndex(String entityName) {
		searchTemplate.deleteIndex(getIndexName(entityName));
	}

	// private IndexWriter indexWriter;
	// private IndexReader indexReader;
	// private Analyzer analyzer = new StandardAnalyzer();
	// private Object lock = new Object();
	//
	// @Autowired
	// private SearchProperties searchProperties;
	//
	// // @Cacheable(key = "'getIndexWriter-' + #indexPath")
	// private IndexWriter getIndexWriter(String indexPath) {
	// // if (indexWriter == null) {
	// synchronized (lock) {
	// File file = new File(indexPath);
	// if (!file.exists()) {
	// file.mkdirs();
	// }
	// try {
	// FSDirectory directory = FSDirectory.open(Paths.get(indexPath));
	// IndexWriterConfig conf = new IndexWriterConfig(analyzer);
	// indexWriter = new IndexWriter(directory, conf);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// return indexWriter;
	// }
	//
	// // @Cacheable(key = "'getIndexSearcher-' + #indexPath")
	// private IndexReader getIndexReader(String indexPath) {
	// // if (indexReader == null) {
	// synchronized (lock) {
	// try {
	// indexReader =
	// DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// return indexReader;
	// }
	//
	// private IndexWriter getIndexWriterForAttachement() {
	// return getIndexWriter(getSearchProperties().getAttachmentIndexPath());
	// }
	//
	// private IndexReader getIndexReaderForAttachment() {
	// return getIndexReader(getSearchProperties().getAttachmentIndexPath());
	// }
	//
	// private void indexDocs(final IndexWriter writer, Path path) {
	// try {
	// if (Files.isDirectory(path)) {
	// Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
	// @Override
	// public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
	// throws IOException {
	// try {
	// indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
	// } catch (IOException ignore) {
	// }
	// return FileVisitResult.CONTINUE;
	// }
	// });
	// } else {
	// indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// writer.close();
	// } catch (IOException e) {
	// }
	// }
	// }
	//
	// /** Indexes a single document */
	// private void indexDoc(IndexWriter writer, Path file, long lastModified)
	// throws IOException {
	// try (InputStream stream = Files.newInputStream(file)) {
	// Document doc = new Document();
	// Field pathField = new StringField(ARTICLE_PATH, file.toString(),
	// Field.Store.YES);
	// doc.add(pathField);
	// Field nameField = new StringField(ARTICLE_NAME,
	// file.getFileName().toString(), Field.Store.YES);
	// doc.add(nameField);
	// doc.add(new LongField(ARTICLE_DATE, lastModified, Field.Store.NO));
	//
	// AutoDetectParser parser = new AutoDetectParser();
	// BodyContentHandler handler = new BodyContentHandler();
	// Metadata metadata = new Metadata();
	// try (InputStream tikaStream = Files.newInputStream(file)) {
	// try {
	// parser.parse(tikaStream, handler, metadata);
	// String fragment = handler.toString();
	// doc.add(new StringField(ARTICLE_CONTENT, fragment, Field.Store.NO));
	// } catch (Exception e) {
	// }
	// }
	//
	// if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
	// log.info("adding " + file);
	// writer.addDocument(doc);
	// } else {
	// log.info("updating " + file);
	// writer.updateDocument(new Term(ARTICLE_ID, file.toString()), doc);
	// }
	// }
	// }
	//
	// /**
	// *
	// * @param indexSercher
	// * @param queryString
	// * @param page
	// * 从1开始
	// * @param size
	// * @return
	// * @throws Exception
	// */
	// private Page<Map<String, String>> searchDocs(IndexReader indexReader,
	// String queryString, int page, int size) {
	// if (size == 0) {
	// size = 20;
	// }
	// Page<Map<String, String>> pageData = null;
	// PageRequest pageable = new PageRequest(page - 1, size);
	// try {
	// IndexSearcher indexSearcher = new IndexSearcher(indexReader);
	// String field = ARTICLE_CONTENT;
	// QueryParser parser = new QueryParser(field, analyzer);
	// Query query = parser.parse(queryString);
	// TopDocs results = indexSearcher.search(query, page * size);
	// ScoreDoc[] hits = results.scoreDocs;
	// int numTotalHits = results.totalHits;
	// int endNuM = Math.min(numTotalHits, page * size);
	// List<Map<String, String>> list = new ArrayList<>();
	// for (int i = (page - 1) * size; i < endNuM; i++) {
	// Document doc = indexSearcher.doc(hits[i].doc);
	// Map<String, String> map = new HashMap<>();
	// map.put(ARTICLE_ID, doc.get(ARTICLE_ID));
	// map.put(ARTICLE_PATH, doc.get(ARTICLE_PATH));
	// map.put(ARTICLE_NAME, doc.get(ARTICLE_NAME));
	// map.put(ARTICLE_DATE, doc.get(ARTICLE_DATE));
	// map.put(ARTICLE_FRAGMENT, doc.get(ARTICLE_FRAGMENT));
	// list.add(map);
	// }
	//
	// pageData = new PageImpl<>(list, pageable, numTotalHits);
	// log.info("搜索[" + queryString + "]，找到记录总数[" + numTotalHits + "]");
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// indexReader.close();
	// } catch (IOException e) {
	// }
	// }
	// return pageData;
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// *
	// org.myfly.platform.system.search.service.ISearchService#indexAttachments(
	// * java.lang.String)
	// */
	// @Override
	// public void indexAttachments(String path) {
	// indexDocs(getIndexWriterForAttachement(), Paths.get(path));
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// *
	// org.myfly.platform.system.search.service.ISearchService#searchAttachments
	// * (java.lang.String)
	// */
	// @Override
	// public Page<Map<String, String>> searchAttachments(String queryString,
	// int page, int size) {
	// return searchDocs(getIndexReaderForAttachment(), queryString, page,
	// size);
	// }
	//
	// public SearchProperties getSearchProperties() {
	// return searchProperties;
	// }
	//
	// public void setSearchProperties(SearchProperties searchProperties) {
	// this.searchProperties = searchProperties;
	// }
	//
	// @Override
	// public void cleanAttachmentsIndex() {
	// FileUtils.deleteRecursive(searchProperties.getAttachmentIndexPath(),
	// true);
	// }
	//
	// @Override
	// public void rebuildAttachmentsIndex() {
	// cleanAttachmentsIndex();
	// // 遍历附件记录，并对附件全部重新索引
	// }

}
