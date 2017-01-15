package org.easycloud.platform.core.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.easycloud.platform.core.domain.EmailEncodingType;
import org.easycloud.platform.core.domain.LanguageLocaleType;
import org.easycloud.platform.core.domain.SBaseEntity;
import org.easycloud.platform.core.domain.SchemaConstants;
import org.easycloud.platform.metadata.annotation.entity.FieldDataType;
import org.easycloud.platform.metadata.annotation.entity.FieldView;
import org.easycloud.platform.metadata.annotation.entity.TableView;
import org.easycloud.platform.metadata.annotation.view.Div1View;
import org.easycloud.platform.metadata.annotation.view.Div2View;
import org.easycloud.platform.metadata.annotation.view.FieldSetView;
import org.easycloud.platform.metadata.annotation.view.FilterView;
import org.easycloud.platform.metadata.annotation.view.FormView;
import org.easycloud.platform.metadata.annotation.view.ListView;
import org.easycloud.platform.metadata.annotation.view.MetaDataView;
import org.easycloud.platform.metadata.annotation.view.OutlineView;
import org.easycloud.platform.metadata.annotation.view.SectionView;
import org.easycloud.platform.metadata.define.view.SQLOperator;
import org.easycloud.platform.metadata.define.view.SectionType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 用户
 * 
 * @author xiangwanhong <br>
 *         结束时间 EndDay 选项列表 <br>
 *         经理 Manager 层次结构 <br>
 *         开始时间 StartDay 选项列表 <br>
 *         联系人 Contact 查找(联系人) 选取的<br>
 *         内部子类型 UserSubtype 选项列表 <br>
 *         区域 LocaleSidKey 选项列表 <br>
 *         时区 TimeZoneSidKey 选项列表 选取的<br>
 *         小时登录限制 LoginLimit 数字(9, 0) <br>
 *         姓名 Name 名称 选取的<br>
 *         用户名 Username 文本(80) 选取的<br>
 *         由系统控制 IsSystemControlled 复选框 <br>
 */
@Entity
@Table(schema = SchemaConstants.PB, indexes = {
		@Index(name = "idx_suser_name", unique = true, columnList = "tenant_uid, name"),
		@Index(name = "idx_suser_email", unique = true, columnList = "email") })
@MetaDataView(
//
tableView = @TableView(title = "用户", description = "用户信息") ,
//
listViews = @ListView(fields = { "name", "communityNickname", "email", "companyName", "active", "createdBy",
		"created" }, filters = { @FilterView(field = "name", operator = SQLOperator.LIKE),
				@FilterView(field = "created", operator = SQLOperator.GREATERTHANOREQUALTO), @FilterView(field = "role")
		//
}) ,
//
formViews = {
		//
		@FormView(name = "default", sections = {
				@SectionView(title = "用户信息", fieldSets = {
						@FieldSetView(title = "基本信息", fields = { "name", "communityNickname", "title", "department",
								"companyName", "aboutMe" }),
						@FieldSetView(title = "联系方式", fields = { "email", "fax", "address", "phone", "mobilePhone",
								"division", "extension" }),
				@FieldSetView(title = "邮件发送", fields = { "senderEmail", "senderName", "signature",
						"emailEncodingKey" }),
				@FieldSetView(title = "高级", fields = { "receivesAdminInfoEmails", "receivesInfoEmails",
						"forecastEnabled" }),
				@FieldSetView(title = "审计", fields = { "active", "createdBy", "created", "updatedBy", "updated" }) }),
				@SectionView(type = SectionType.NOTE), @SectionView(type = SectionType.ATTACHMENT) }),
		//
		@FormView(name = "d2", divs = {
				//
				@Div1View(width = 8, subs = { @Div2View(name = "div1"), @Div2View(name = "div2"),
						@Div2View(name = "div4"), @Div2View(name = "div5") }),
				@Div1View(width = 4, name = "div3") },
				//
				sections = {
						@SectionView(name = "div1", fieldSets = @FieldSetView(title = "基本信息", fields = { "name",
								"communityNickname", "title", "department", "companyName", "aboutMe" }) ),
						@SectionView(name = "div2", fieldSets = @FieldSetView(title = "审计", fields = { "active",
								"createdBy", "created", "updatedBy", "updated" }) ),
						@SectionView(name = "div4", type = SectionType.ATTACHMENT),
						@SectionView(name = "div5", type = SectionType.NOTE) })
		//
},
//
outlineViews = @OutlineView(title = "用户信息", sections = { @SectionView(fieldSets = {
		@FieldSetView(title = "基本信息", fields = { "name", "communityNickname", "title", "department", "companyName",
				"aboutMe" }),
		@FieldSetView(title = "联系方式", fields = { "email", "fax", "address", "phone", "mobilePhone", "division",
				"extension" }) }) }) )
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SUser extends SBaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9082451847941514640L;
	/**
	 * 昵称
	 */
	@FieldView(title = "昵称")
	@Column(length = 40)
	private String communityNickname;
	/**
	 * 职务
	 */
	@FieldView(title = "职务")
	@Column(length = 80)
	private String title;

	/**
	 * 别名
	 */
	@FieldView(title = "别名")
	@Column(length = 8)
	private String alias;
	/**
	 * 部门
	 */
	@FieldView(title = "部门")
	@Column(length = 80)
	private String department;
	/**
	 * 传真
	 */
	@FieldView(title = "传真")
	@Column(length = 20)
	private String fax;
	/**
	 * 地址
	 */
	@FieldView(title = "地址")
	@Column(length = 200)
	private String address;
	/**
	 * 电话
	 */
	@FieldView(title = "电话", dataType = FieldDataType.TELPHONE)
	@Column(length = 20)
	private String phone;
	/**
	 * 手机
	 */
	@FieldView(title = "手机", dataType = FieldDataType.TELPHONE)
	@Column(length = 20)
	private String mobilePhone;
	/**
	 * 电子邮件
	 */
	@FieldView(title = "电子邮件", dataType = FieldDataType.EMAIL)
	@Column(length = 50)
	private String email;
	/**
	 * 电子邮件编码
	 */
	@FieldView(title = "电子邮件编码")
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private EmailEncodingType emailEncodingKey;
	/**
	 * 电子邮件发件人地址
	 */
	@FieldView(title = "电子邮件发件人地址", dataType = FieldDataType.EMAIL)
	@Column(length = 50)
	private String senderEmail;
	/**
	 * 电子邮件发件人姓名
	 */
	@FieldView(title = "电子邮件发件人姓名")
	@Column(length = 80)
	private String senderName;
	/**
	 * 电子邮件签名
	 */
	@FieldView(title = "电子邮件签名")
	@Column(length = 1200)
	private String signature;
	/**
	 * 分部
	 */
	@FieldView(title = "分布")
	@Column(length = 80)
	private String division;
	/**
	 * 分机
	 */
	@FieldView(title = "分机")
	@Column(length = 20)
	private String extension;
	/**
	 * 公司名
	 */
	@FieldView(title = "公司名")
	@Column(length = 80)
	private String companyName;
	/**
	 * 关于我
	 */
	@FieldView(title = "关于我")
	@Column(length = 1000)
	private String aboutMe;
	/**
	 * 接收管理信息电子邮件
	 */
	@FieldView(title = "接收管理信息电子邮件")
	@Column
	private Boolean receivesAdminInfoEmails;
	/**
	 * 接收信息电子邮件
	 */
	@FieldView(title = "接收信息电子邮件")
	@Column
	private Boolean receivesInfoEmails;

	public Boolean getReceivesAdminInfoEmails() {
		return receivesAdminInfoEmails;
	}

	public Boolean getReceivesInfoEmails() {
		return receivesInfoEmails;
	}

	public Boolean getForecastEnabled() {
		return forecastEnabled;
	}

	/**
	 * 加入组时的默认通知频率
	 */
	@FieldView(title = "加入组时的默认通知频率")
	@Column
	private String defaultGroupNotificationFrequency;
	/**
	 * 职员数
	 */
	@FieldView(title = "职员数")
	@Column
	private Long employeeNumber;
	/**
	 * 允许预测
	 */
	@FieldView(title = "允许预测")
	@Column
	private Boolean forecastEnabled;
	/**
	 * 语言
	 */
	@FieldView(title = "语言")
	@Column
	@Enumerated(EnumType.STRING)
	private LanguageLocaleType languageLocaleKey;
	/**
	 * 简档
	 */
	@FieldView(title = "简档")
	@JoinColumn(name = "profileId")
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private SProfile profile;
	/**
	 * 角色
	 */
	@FieldView(title = "角色")
	@JoinColumn(name = "roleId")
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private SRole role;

	public SUser() {
		setInternalTable("SUser");
	}

	public String getCommunityNickname() {
		return communityNickname;
	}

	public void setCommunityNickname(String communityNickname) {
		this.communityNickname = communityNickname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public EmailEncodingType getEmailEncodingKey() {
		return emailEncodingKey;
	}

	public void setEmailEncodingKey(EmailEncodingType emailEncodingKey) {
		this.emailEncodingKey = emailEncodingKey;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public Boolean isReceivesAdminInfoEmails() {
		return receivesAdminInfoEmails;
	}

	public void setReceivesAdminInfoEmails(Boolean receivesAdminInfoEmails) {
		this.receivesAdminInfoEmails = receivesAdminInfoEmails;
	}

	public Boolean isReceivesInfoEmails() {
		return receivesInfoEmails;
	}

	public void setReceivesInfoEmails(Boolean receivesInfoEmails) {
		this.receivesInfoEmails = receivesInfoEmails;
	}

	public String getDefaultGroupNotificationFrequency() {
		return defaultGroupNotificationFrequency;
	}

	public void setDefaultGroupNotificationFrequency(String defaultGroupNotificationFrequency) {
		this.defaultGroupNotificationFrequency = defaultGroupNotificationFrequency;
	}

	public Long getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(Long employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public Boolean isForecastEnabled() {
		return forecastEnabled;
	}

	public void setForecastEnabled(Boolean forecastEnabled) {
		this.forecastEnabled = forecastEnabled;
	}

	public LanguageLocaleType getLanguageLocaleKey() {
		return languageLocaleKey;
	}

	public void setLanguageLocaleKey(LanguageLocaleType languageLocaleKey) {
		this.languageLocaleKey = languageLocaleKey;
	}

	public SProfile getProfile() {
		return profile;
	}

	public void setProfile(SProfile profile) {
		this.profile = profile;
	}

	public SRole getRole() {
		return role;
	}

	public void setRole(SRole role) {
		this.role = role;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
