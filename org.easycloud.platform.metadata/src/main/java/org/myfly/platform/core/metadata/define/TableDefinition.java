package org.myfly.platform.core.metadata.define;

import org.springframework.util.Assert;

/**
 * 数据库表信息定义
 * @author xiangwanhong
 *
 */
public class TableDefinition {
	/**
	 * 表标题，也就是简要名称
	 */
	private String title;
	/**
	 * 表详细描述
	 */
	private String description;
	/**
	 * 数据库Schema
	 */
	private String schema;
	/**
	 * 表名称，数据库创建名称
	 */
	private String tableName;
	/**
	 * 所在数据库
	 */
	private String catalog;
	/**
	 * 显示字段，用于标记记录简要显示时显示的字段
	 */
	private String labelField;
	
	/**
	 * 通用子表
	 */
	private CommonSubTableType[]  commonSubTables;
	/**
	 * 是否创建索引
	 */
	private boolean createIndex;
	/**
	 * 索引名称
	 */
	private String indexName;
	/**
	 * 主键字段
	 */
	private String primaryKeys;

	public TableDefinition() {
	}

	public TableDefinition(String title, String description) {
		setTitle(title);
		setDescription(description);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getLabelField() {
		return labelField;
	}

	public void setLabelField(String labelField) {
		this.labelField = labelField;
	}

	@Override
	public String toString() {
		return "tableName: " + getTableName() + ", title: " + getTitle() + ", schema: " + getSchema() + ", labelField: "
				+ getLabelField() + ", createIndex: " + isCreateIndex() + ", indexName: " + getIndexName();
	}

	public CommonSubTableType[] getCommonSubTables() {
		return commonSubTables;
	}

	public void setCommonSubTables(CommonSubTableType[] commonSubTables) {
		this.commonSubTables = commonSubTables;
	}

	public boolean isCreateIndex() {
		return createIndex;
	}

	public void setCreateIndex(boolean createIndex) {
		this.createIndex = createIndex;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getPrimaryKeys() {
		return primaryKeys;
	}

	public void setPrimaryKeys(String primaryKeys) {
		this.primaryKeys = primaryKeys;
	}
	
	public void validate(){
		Assert.hasLength(getTitle());
		Assert.hasLength(getTableName());
		Assert.hasLength(getPrimaryKeys());
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
}
