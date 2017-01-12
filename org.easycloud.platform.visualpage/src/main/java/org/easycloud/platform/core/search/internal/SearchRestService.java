package org.myfly.platform.core.search.internal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.myfly.platform.core.search.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 搜索Rest服务
 * 
 * @author xiangwanhong
 *
 */
@RestController
@RequestMapping("search2")
public class SearchRestService {
	@Autowired
	private ISearchService searchService;

	@RequestMapping
	public Map<String, String> help() {
		Map<String, String> map = new HashMap<>();
		map.put("/search2/attachments?q={queryString}&page={page}&size={size}", "搜索附件");
		map.put("/search2/attachments/indexdocs?d={docDir}", "对指定目录下的文件创建索引");
		map.put("/search2/attachments/clean", "清空附件索引");
		map.put("/search2/rebuild", "重建全部实体索引");
		map.put("/search2/rebuild/{entityName}", "重建实体索引");
		return map;
	}

	@RequestMapping("attachments")
	public Page<Map<String, String>> searchAttachments(@RequestParam(name = "q", defaultValue = "") String queryString,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		return searchService.searchAttachments(queryString, page, size);
	}

	@RequestMapping("attachments/indexdocs")
	public String indexAttachments(@RequestParam(name = "docDir", required = true) String docDir) {
		searchService.indexAttachments(docDir);
		return "以为目录[" + docDir + "]创建完索引.";
	}

	@RequestMapping("attachments/clean")
	public String cleanAttachmentsIndex() {
		searchService.cleanAttachmentsIndex();
		return "清除完附件索引.";
	}

	@RequestMapping("rebuild")
	public String rebuildAllEntityIndex() {
		searchService.rebuildAllEntityIndex();
		return "全部索引重建完成.";
	}

	@RequestMapping("rebuild/{entityName}")
	public String rebuildEntityIndex(@PathVariable("entityName") String entityName) {
		searchService.rebuildEntityIndex(entityName);
		return "重建索引完成";
	}
}
