package org.myfly.platform.core.flydata.queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.myfly.platform.core.flydata.service.IJpaAccessService;
import org.myfly.platform.core.message.service.IMessageQueueService;
import org.myfly.platform.core.message.service.MessageQueueConstants;
import org.myfly.platform.core.search.service.ISearchService;
import org.myfly.platform.core.user.domain.SGlobalName;
import org.myfly.platform.core.utils.JSONUtil;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

@Component
public class GlobalNameQueueProcessor {
	private static Log log = LogFactory.getLog(GlobalNameQueueProcessor.class);
	/**
	 * GlobalName数据同步队列
	 * 
	 * @return
	 */
	@Bean(name = MessageQueueConstants.QUEUE_GLOBAL_NAME)
	public Queue globalNameSyncQueue() {
		return new Queue(MessageQueueConstants.QUEUE_GLOBAL_NAME);
	}

	@Autowired
	private IJpaAccessService globalNameRepository;

	@Autowired
	private IMessageQueueService messageQueueService;

	@Autowired
	private ISearchService searchService;

	private static final String SGLOBALNAME_INDEXNAME = "sglobalname";

	@RabbitListener(queues = MessageQueueConstants.QUEUE_GLOBAL_NAME)
	@RabbitHandler
	public void receiveGlobalNameData(EntityMessage message) {
		SGlobalName entity = (SGlobalName) message.getEntity();
		try {
			switch (message.getOperator()) {
			case DELETE:
				globalNameRepository.delOne(entity);
				searchService.delete(SGLOBALNAME_INDEXNAME, entity.getUid());
				break;
			case INSERT:
				try {
					globalNameRepository.saveEntity(entity);
					IndexQuery indexQuery = new IndexQueryBuilder().withIndexName(SGLOBALNAME_INDEXNAME)
							.withType(entity.getInternalTable().toLowerCase()).withId(entity.getUid())
							.withSource(JSONUtil.toJSON(entity)).build();
					searchService.index(indexQuery);
				} catch (Exception e) {
					// e.printStackTrace();
				}
				break;
			case UPDATE:
				globalNameRepository.updateEntity(entity);
				IndexQuery indexQuery2 = new IndexQueryBuilder().withIndexName(SGLOBALNAME_INDEXNAME)
						.withType(entity.getInternalTable().toLowerCase()).withId(entity.getUid())
						.withSource(JSONUtil.toJSON(entity)).build();
				searchService.index(indexQuery2);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}

	/**
	 * 
	 * @param globalName
	 */
	public void sendGlobalNameData(EntityOperator operator, SGlobalName globalName) {
		EntityMessage message = new EntityMessage(operator, globalName);
		messageQueueService.sendMessageToQueue(MessageQueueConstants.QUEUE_GLOBAL_NAME, message);
	}
}
