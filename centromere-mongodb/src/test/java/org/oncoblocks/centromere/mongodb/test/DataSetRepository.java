/*
 * Copyright 2015 William Oemler, Blueprint Medicines
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

package org.oncoblocks.centromere.mongodb.test;

import org.oncoblocks.centromere.core.repository.support.DataSetRepositoryOperations;
import org.oncoblocks.centromere.mongodb.GenericMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author woemler
 */

@Repository
public class DataSetRepository extends GenericMongoRepository<DataSet, String> implements
		DataSetRepositoryOperations<DataSet, String> {

	@Autowired
	public DataSetRepository(MongoTemplate mongoTemplate) {
		super(mongoTemplate, DataSet.class);
	}

	@Override 
	public DataSet getByName(String name) {
		Query query = new Query(Criteria.where("name").is(name));
		List<DataSet> dataSets = this.getMongoOperations().find(query, getModel());
		if (!dataSets.isEmpty()){
			return dataSets.get(0);
		} else {
			return null;
		}
	}
}
