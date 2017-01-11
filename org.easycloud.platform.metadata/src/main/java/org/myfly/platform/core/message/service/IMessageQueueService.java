package org.myfly.platform.core.message.service;

/**
 * 消息队列服务
 * 
 * @author xiangwanhong
 *
 */
public interface IMessageQueueService {
//	/**
//	 * 获取指定消息队列统计信息，当指定队列名称时，取指定队列的，如果没有指定则取所有的
//	 * 
//	 * @param queueName
//	 * @return
//	 */
//	Map<String, Object> getMessageQueueStatistics(String queueName);
//
//	/**
//	 * 获取指定消息主题统计信息，当指定队列名称时，取指定队列的，如果没有指定则取所有的
//	 * 
//	 * @param subscription
//	 * @return
//	 */
//	Map<String, Object> getMessageSubscriptionStatistics(String subscription);

	/**
	 * 发送消息到指定队列
	 * @param queueName
	 * @param message
	 */
	<T> void  sendMessageToQueue(String queueName, T message);

	<T> T getStatistics();
}
