package org.easycloud.platform.core.flydata.internal;

import java.util.Map;

/**
 * 用于DataTables Server Side数据提供
 * 
 * @author xiangwanhong
 *
 */
public class DataTablesResponse {
	/**
	 * 调用次数
	 */
	private int draw;
	/**
	 * 过滤条数
	 */
	private long recordsFiltered;
	/**
	 * 总记录数
	 */
	private long recordsTotal;
	/**
	 * 当前页
	 */
	private int page;
	/**
	 * 总页数
	 */
	private int totalPages;
	/**
	 * 每页大小
	 */
	private int size;
	/**
	 * 结果数据元模型
	 */
	private DataTableMetaData metaData;
	/**
	 * 结果
	 */
	private String[][] data;
	/**
	 * Map结构数据集
	 */
	private Map<String, String>[] mapData;
	/**
	 * 结果集是否为map数组，否则为二维字符数组
	 */
	private boolean resultIsMap;

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public long getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public long getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public String[][] getData() {
		return data;
	}

	public void setData(String[][] data) {
		this.data = data;
	}

	public DataTableMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(DataTableMetaData metaData) {
		this.metaData = metaData;
	}

	public Map<String, String>[] getMapData() {
		return mapData;
	}

	public void setMapData(Map<String, String>[] mapData) {
		this.mapData = mapData;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public boolean isResultIsMap() {
		return resultIsMap;
	}

	public void setResultIsMap(boolean resultIsMap) {
		this.resultIsMap = resultIsMap;
	}

	@Override
	public String toString() {
		return "recordsTotal: " + getRecordsTotal() + ", page: " + getPage() + ", size: " + getSize() + ", resultIsMap: " + isResultIsMap();
	}
}
