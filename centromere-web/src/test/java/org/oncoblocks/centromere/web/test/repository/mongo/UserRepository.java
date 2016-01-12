/*
 * Copyright 2016 William Oemler, Blueprint Medicines
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.oncoblocks.centromere.web.test.repository.mongo;

import org.oncoblocks.centromere.mongodb.GenericMongoRepository;
import org.oncoblocks.centromere.web.test.security.User;
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
