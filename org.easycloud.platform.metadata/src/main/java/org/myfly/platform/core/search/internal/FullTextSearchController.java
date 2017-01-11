package org.myfly.platform.core.search.internal;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.myfly.platform.core.search.service.IFullTextSearchService;
import org.myfly.platform.core.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 全文搜索控制器
 * 
 * @author xiangwanhong
 *
 */
@RestController
@RequestMapping("/search")
public class FullTextSearchController {
	@Autowired
	private IFullTextSearchService fullTextSearchService;

	/**
	 * 重建所有索引
	 * 
	 * @return
	 */
	@RequestMapping("rebuildAll")
	public String rebuildAllIndex() {
		long start = System.currentTimeMillis();
		fullTextSearchService.rebuildAllIndex();
		long end = System.currentTimeMillis();
		return "索引重建完成，耗时" + (end - start) + "毫秒.";
	}

	/**
	 * 重建实体索引
	 * 
	 * @param entityName
	 * @return
	 */
	@RequestMapping("rebuild/{entityName}")
	public String rebuildIndexByEntityName(@PathVariable("entityName") String entityName) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		long start = System.currentTimeMillis();
		fullTextSearchService.rebuildIndexByEntityName(entityName);
		long end = System.currentTimeMillis();
		return "索引重建完成，耗时" + (end - start) + "毫秒.";
	}

	/**
	 * 
	 * @param entityName
	 * @param searchText
	 * @return
	 */
	@RequestMapping("{entityName}/{searchText}")
	public <T> Page<T> search(@PathVariable("entityName") String entityName,
			@PathVariable("searchText") String searchText,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size, HttpServletRequest request) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		AssertUtil.parameterEmpty(searchText, "searchText");
		return fullTextSearchService.search(entityName, searchText, page, size);
	}

	/**
	 * 
	 * @param searchText
	 * @return
	 */
	@RequestMapping("{searchText}")
	public <T> List<T> search(@PathVariable("searchText") String searchText) {
		return fullTextSearchService.searchName(searchText);
	}
}
