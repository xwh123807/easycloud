package org.easycloud.platform.flydata.message.service;

public class MessageQueueConstants {
	/**
	 * http访问日志队列名称
	 */
	public static final String QUEUE_ACCESS_LOG = "accessLog.queue";
	/**
	 * 系统监控数据队列名称
	 */
	public static final String QUEUE_SYSTEM_METRICS = "systemMetrics.queue";
	/**
	 * GlobalName实体数据同步队列
	 */
	public static final String QUEUE_GLOBAL_NAME = "globalName.queue";
	
	/**
	 * 导出日志队列名称
	 */
	public static final String QUEUE_EXPORT_LOG = "exportLog.queue";
	
	/**
	 * 附件存储队列名称
	 */
	public static final String QUEUE_ATTACHMENT_LOG = "attachment.queue";
	
	/**
	 * 索引创建队列
	 */
	public static final String QUEUE_INDEX = "index.queue";
	/**
	 * 导入日志队列
	 */
	public static final String QUEUE_DATAIMPORT_LOG = "dataimportlog.queue";
}
