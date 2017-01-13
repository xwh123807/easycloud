package org.easycloud.platform.core.flydata.service;

import org.springframework.data.domain.Page;

public class Pagination {
	private long recordsTotal;
	private long totalPages;
	private long page;
	private long size;
	
	public Pagination(Page<?> page) {
		setRecordsTotal(page.getTotalElements());
		setTotalPages(page.getTotalPages());
		setPage(page.getNumber());
		setSize(page.getSize());
	}
	
	public long getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public long getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}
	public long getPage() {
		return page;
	}
	public void setPage(long page) {
		this.page = page;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
}
