package org.easycloud.metadata.server.repository;

import java.util.List;

import org.easycloud.metadata.service.model.Entity;
import org.easycloud.metadata.service.model.EntityKey;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EntityRepository extends MongoRepository<Entity, EntityKey>{
	List<Entity> findByCategory(String category);
	
	List<Entity> findBySchema(String schema);
}
