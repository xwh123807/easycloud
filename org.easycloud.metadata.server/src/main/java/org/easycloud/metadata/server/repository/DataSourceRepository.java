package org.easycloud.metadata.server.repository;

import org.easycloud.metadata.service.model.DataSource;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataSourceRepository extends MongoRepository<DataSource, String>{

}
