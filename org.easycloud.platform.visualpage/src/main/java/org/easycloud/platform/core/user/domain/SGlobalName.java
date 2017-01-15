package org.easycloud.platform.core.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.easycloud.platform.core.domain.SKeyEntity;
import org.easycloud.platform.core.domain.SchemaConstants;
import org.easycloud.platform.metadata.annotation.entity.FieldView;
import org.easycloud.platform.metadata.annotation.entity.TableView;
import org.easycloud.platform.metadata.annotation.view.ListView;
import org.easycloud.platform.metadata.annotation.view.MetaDataView;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author xiangwanhong ID名称表，需要进行id/名称查找的都继承本表。<br>
 *         由于无法解决自动赋值，子类必须在构造函数中为internalTable赋值<br>
 */
@Entity
@Table(schema = SchemaConstants.PB)
//@Indexed
@MetaDataView(tableView =
//
@TableView(title = "全局名称", description = "全局ID名称表，由平台自动维护数据", commonSubTables = {}) , listViews = {
		@ListView(name = "default", fields = { "name", "internalTable" }, itemActions = {}, listActions = {}) })
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SGlobalName extends SKeyEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3811776971190355189L;

//	@Field(index = Index.YES, store = Store.YES)
	@Column(length = 50, nullable = true)
	@FieldView(title = "表名")
	private String internalTable;

//	@Field(index = Index.YES, store = Store.YES)
	@FieldView(title = "名称")
	@Column(length = 255, nullable = false)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInternalTable() {
		return internalTable;
	}

	public void setInternalTable(String internalTable) {
		this.internalTable = internalTable;
	}
	
	@Override
	public String toString() {
		return super.toString() + ", name:" + getName() + ", internalTable:" + getInternalTable();
	}
}
