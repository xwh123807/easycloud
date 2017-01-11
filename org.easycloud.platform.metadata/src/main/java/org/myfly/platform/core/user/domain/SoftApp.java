package org.myfly.platform.core.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.myfly.platform.core.domain.LanguageLocaleType;
import org.myfly.platform.core.domain.SBaseEntity;
import org.myfly.platform.core.domain.SchemaConstants;
import org.myfly.platform.core.metadata.annotation.FieldSetView;
import org.myfly.platform.core.metadata.annotation.FieldView;
import org.myfly.platform.core.metadata.annotation.FilterView;
import org.myfly.platform.core.metadata.annotation.FormView;
import org.myfly.platform.core.metadata.annotation.ListView;
import org.myfly.platform.core.metadata.annotation.MetaDataView;
import org.myfly.platform.core.metadata.annotation.SectionView;
import org.myfly.platform.core.metadata.annotation.TableView;
import org.myfly.platform.core.metadata.define.SectionType;

/**
 * 软件包<br>
 * 
 * @author xiangwanhong<br>
 * 
 *         软件包名称 bill.ON 版本号 2.31.1 <br>
 *         语言 英语 首个安装版本编号 2.31.1<br>
 *         版本名称 Release 2.31.1 软件包类型 受管<br>
 *         命名空间前缀 ONB2 API 访问 不受限制 [启用限制]<br>
 *         发布者 OnCommerce GmbH 修改人 wanhong xiang, 2015-10-26 上午10:12<br>
 *         描述 Recurring billing based on professional contract and subscription
 *         management.<br>
 *         发布备注 https://oncommerce.atlassian.net/wiki/x/NQAwAg <br>
 *         安装者 wanhong xiang, 2015-10-26 上午10:12 <br>
 * 
 *         计入限制数 未选取的 选项卡 12<br>
 *         应用程序 1 对象 50<br>
 */
@Entity
@Table(schema = SchemaConstants.PB, indexes = {
		@Index(name = "idx_ssoftapp_name", unique = true, columnList = "name") })
@MetaDataView(
//
tableView = @TableView(title = "软件包", description = "用于管理发布到平台上的应用软件包") ,
//
listViews = @ListView(fields = { "name", "description", "namespace", "publisher", "version",
		"publishContent" }, filters = { @FilterView(field = "name"), @FilterView(field = "createdBy") }) ,
		//
		formViews = @FormView(sections = {
				@SectionView(title = "基本信息", fieldSets = {
						@FieldSetView(title = "基本信息", fields = { "name", "description", "languageLocaleKey",
								"namespace", "publisher", "version", "publishContent" }),
						@FieldSetView(title = "审计", fields = { "active", "createdBy", "created", "updatedBy",
								"updated" }), }),
				@SectionView(type = SectionType.NOTE), @SectionView(type = SectionType.ATTACHMENT) }) )
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SoftApp extends SBaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3338143040803570766L;
	/**
	 * 语言
	 */
	@FieldView(title = "语言")
	@Column
	@Enumerated(EnumType.STRING)
	private LanguageLocaleType languageLocaleKey;

	/**
	 * 命名空间前缀
	 */
	@FieldView(title = "命名空间")
	@Column(length = 40)
	private String namespace;
	/**
	 * 描述
	 */
	@FieldView(title = "描述")
	@Column
	private String description;

	/**
	 * 发布备注
	 */
	@FieldView(title = "发布备注")
	@Column(length = 1000)
	private String publishContent;

	/**
	 * 发布者
	 */
	@FieldView(title = "发布者")
	@Column(length = 50)
	private String publisher;

	/**
	 * 当前版本
	 */
	@FieldView(title = "当前版本")
	@Column(length = 50)
	private String version;

	public SoftApp() {
		setInternalTable("SSoftApp");
	}

	public LanguageLocaleType getLanguageLocaleKey() {
		return languageLocaleKey;
	}

	public void setLanguageLocaleKey(LanguageLocaleType languageLocaleKey) {
		this.languageLocaleKey = languageLocaleKey;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPublishContent() {
		return publishContent;
	}

	public void setPublishContent(String publishContent) {
		this.publishContent = publishContent;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
