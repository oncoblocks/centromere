package org.oncoblocks.centromere.core.test.repository.mongo;

import org.oncoblocks.centromere.core.repository.GenericMongoRepository;
import org.oncoblocks.centromere.core.test.web.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author woemler
 */

@Repository
public class UserRepository extends GenericMongoRepository<User, String> {
	@Autowired
	public UserRepository(MongoTemplate mongoTemplate) {
		super(mongoTemplate, User.class);
	}
}
