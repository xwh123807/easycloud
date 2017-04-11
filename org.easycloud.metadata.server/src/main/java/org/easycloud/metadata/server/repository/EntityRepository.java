package org.easycloud.metadata.server.repository;

import org.easycloud.metadata.domain.model.Entity;
import org.easycloud.metadata.domain.model.EntityKey;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EntityRepository extends MongoRepository<Entity, EntityKey>{

}
