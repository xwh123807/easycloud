package org.myfly.platform.core.message.internal;

import org.myfly.platform.core.message.service.IMessageQueueService;
import org.myfly.platform.core.utils.AssertUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageQueueService implements IMessageQueueService {

	@Autowired
	private RabbitTemplate messageTemplate;
	// @Autowired
	// private JmsMessagingTemplate jmsMessagingTemplate;
	//
	// @Autowired
	// private ApplicationContext applicationContext;

	// /**
	// * 根据队列名称获取队列Bean
	// *
	// * @param queueName
	// * @return
	// */
	// public Queue getQueueByName(String queueName) {
	// return (Queue) applicationContext.getBean(queueName);
	// }

	/**
	 * 发送消息到指定队列
	 * 
	 * @param queueName
	 * @param message
	 */
	public <T> void sendMessageToQueue(String queueName, T message) {
		AssertUtil.parameterEmpty(queueName, "queueName");
		AssertUtil.parameterEmpty(message, "message");
		messageTemplate.convertAndSend(queueName, message);
	}

	@Override
	public <T> T getStatistics() {
		return null;
	}
	//
	// /**
	// * 获取消息队列或主题统计信息
	// * @param isQueue
	// * @param name
	// * @return
	// */
	// @SuppressWarnings("rawtypes")
	// private Map<String, Object>
	// getMessageQueueOrSubscriptionStatistics(boolean isQueue, String name){
	// String title = isQueue ? "ActiveMQ.Statistics.Destination" :
	// "ActiveMQ.Statistics.Subscription";
	// Map<String, Object> map = new HashMap<>();
	// try {
	// Connection connection =
	// jmsMessagingTemplate.getConnectionFactory().createConnection();
	// connection.start();
	// Session session = connection.createSession(false,
	// Session.AUTO_ACKNOWLEDGE);
	//
	// Queue replyTo = session.createTemporaryQueue();
	// MessageConsumer consumer = session.createConsumer(replyTo);
	//
	// Queue query = session.createQueue(title + (StringUtil.isEmpty(name) ? ""
	// : "." + name));
	// MessageProducer producer = session.createProducer(query);
	// Message msg = session.createMessage();
	// msg.setJMSReplyTo(replyTo);
	// producer.send(msg);
	//
	// MapMessage reply = (MapMessage) consumer.receive();
	// connection.stop();
	// for (Enumeration e = reply.getMapNames(); e.hasMoreElements();) {
	// String key = e.nextElement().toString();
	// map.put(key, reply.getObject(key));
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return map;
	// }
	//
	// /**
	// * 获取指定主题的统计信息
	// * @param subscription
	// * @return
	// */
	// public Map<String, Object> getMessageSubscriptionStatistics(String
	// subscription){
	// return getMessageQueueOrSubscriptionStatistics(false, subscription);
	// }
	//
	// /**
	// * 获取指定消息队列统计信息，当指定队列名称时，取指定队列的，如果没有指定则取所有的<br>
	// * 注意：如果没有配置statisticsBrokerPlugin插件，consumer.receive会卡死等待<br>
	// * TODO activemq需要配置statisticsBrokerPlugin插件，但是全注解的方式如何配置呀？<br>
	// * http://activemq.apache.org/statisticsplugin.html<br>
	// *
	// * @param queueName
	// * @return
	// */
	// public Map<String, Object> getMessageQueueStatistics(String queueName) {
	// return getMessageQueueOrSubscriptionStatistics(true, queueName);
	// }
}
