package org.easycloud.platform.core.domain;

import org.easycloud.platform.metadata.annotation.entity.BaseEnum;

/**
 * 数据导入状态
 * 
 * @author xiangwanhong
 *
 */
public enum DataImportState implements BaseEnum {
	SUCCESS("成功"), FAULT("失败"), PARTIAL_SUCCESS("部分成功");

	private String title;

	private DataImportState(final String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

}
