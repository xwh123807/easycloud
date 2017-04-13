package org.easycloud.metadata.server.repository;

import org.easycloud.metadata.service.model.System;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SystemRepository extends MongoRepository<System, String> {
	
}
