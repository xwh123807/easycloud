package org.myfly.platform.core.search.service;

import java.util.List;

import org.springframework.data.domain.Page;

public interface IFullTextSearchService {

	/**
	 * 重建所有索引
	 */
	void rebuildAllIndex();

	/**
	 * 重建指定实体的索引
	 * @param entityName
	 */
	void rebuildIndexByEntityName(String entityName);

	<T> Page<T> search(String entityName, String searchText, int page, int size);

	<T> List<T> searchName(String searchText);

}