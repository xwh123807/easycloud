package org.easycloud.metadata.server.repository;

import org.easycloud.metadata.domain.model.Table;
import org.easycloud.metadata.domain.model.TableKey;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TableRepository extends MongoRepository<Table, TableKey>{

}
