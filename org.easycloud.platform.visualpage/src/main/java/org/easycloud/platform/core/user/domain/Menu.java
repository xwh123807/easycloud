package org.myfly.platform.core.user.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.myfly.platform.core.domain.FieldDataType;
import org.myfly.platform.core.domain.SBaseEntity;
import org.myfly.platform.core.domain.SchemaConstants;
import org.myfly.platform.core.metadata.annotation.FieldView;
import org.myfly.platform.core.metadata.annotation.ListView;
import org.myfly.platform.core.metadata.annotation.MetaDataView;
import org.myfly.platform.core.metadata.annotation.TableView;
import org.myfly.platform.core.metadata.define.ListStyle;
import org.myfly.platform.core.visualpage.ui.EntityAction;

/**
 * 菜单表
 * 
 * @author xiangwanhong
 *
 */
@MetaDataView(
//
tableView = @TableView(title = "菜单", description = "菜单表") ,
//
listViews = {
		//
		@ListView(fields = { "name", "parent", "description", "orderNumber", "icon", "url", "tenant", "created",
				"createdBy" }, enableActions = false, listStyle = ListStyle.TABLE, itemActions = {
						EntityAction.VIEW }, listActions = { EntityAction.EXCEL, EntityAction.PDF,
								EntityAction.LISTPRINT }) })
@Table(schema = SchemaConstants.PB)
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Menu extends SBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 111776278576541898L;
	/**
	 * 父级菜单
	 */
	@FieldView(title = "父菜单")
	@ManyToOne
	@JoinColumn
	private Menu parent;
	/**
	 * 下级菜单
	 */
	@FieldView(title = "下级菜单")
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy("orderNumber ASC")
	private Set<Menu> subMenus;

	/**
	 * 描述
	 */
	@FieldView(title = "描述")
	@Column
	private String description;
	/**
	 * 显示图标
	 */
	@FieldView(title = "图标")
	@Column(length = 50)
	private String icon;
	/**
	 * 地址
	 */
	@FieldView(title = "地址", dataType=FieldDataType.URL)
	@Column(length = 200)
	private String url;
	/**
	 * 排序
	 */
	@FieldView(title = "排序")
	@Column
	private Long orderNumber;

	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public Set<Menu> getSubMenus() {
		return subMenus;
	}

	public void setSubMenus(Set<Menu> subMenus) {
		this.subMenus = subMenus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}
}
