package org.oncoblocks.centromere.core.test.repository.mongo;

import org.oncoblocks.centromere.core.repository.GenericMongoRepository;
import org.oncoblocks.centromere.core.test.models.Gene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author woemler
 */

@Repository
public class GeneRepository extends GenericMongoRepository<Gene, Long> {
	@Autowired
	public GeneRepository(MongoTemplate mongoTemplate) {
		super(mongoTemplate, Gene.class);
	}
}
