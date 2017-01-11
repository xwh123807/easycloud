package org.myfly.platform.core.domain;

import java.util.Date;

/**
 * 数据导入信息
 * @author xiangwanhong
 *
 */
public class DataImportInfo {
	private String title;
	
	private String groupUid;
	
	private String actionUrl;
	
	private Integer totalCount;
	
	private Integer successCount;
	
	private Integer faultCount;
	
	private Integer usedtime;
	
	private Date created;
	
	private String importFile;
	
	private String message;
	
	private DataImportState state;

	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	public Integer getFaultCount() {
		return faultCount;
	}

	public void setFaultCount(Integer faultCount) {
		this.faultCount = faultCount;
	}

	public Integer getUsedtime() {
		return usedtime;
	}

	public void setUsedtime(Integer usedtime) {
		this.usedtime = usedtime;
	}

	public String getGroupUid() {
		return groupUid;
	}

	public void setGroupUid(String groupUid) {
		this.groupUid = groupUid;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getImportFile() {
		return importFile;
	}

	public void setImportFile(String importFile) {
		this.importFile = importFile;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public DataImportState getState() {
		return state;
	}

	public void setState(DataImportState state) {
		this.state = state;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
