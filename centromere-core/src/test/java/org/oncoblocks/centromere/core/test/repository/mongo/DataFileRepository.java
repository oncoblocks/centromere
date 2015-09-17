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

package org.oncoblocks.centromere.core.test.repository.mongo;

import org.oncoblocks.centromere.core.repository.GenericMongoRepository;
import org.oncoblocks.centromere.core.repository.support.DataFileRepositoryOperations;
import org.oncoblocks.centromere.core.test.models.DataFile;
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
public class DataFileRepository extends GenericMongoRepository<DataFile, String> implements
		DataFileRepositoryOperations<DataFile, String> {

	@Autowired
	public DataFileRepository(MongoTemplate mongoTemplate) {
		super(mongoTemplate, DataFile.class);
	}

	@Override 
	public DataFile getFileByPath(String filePath) {
		return this.getMongoOperations().findOne(new Query(Criteria.where("filePath").is(filePath)), this.getModel());
	}

	@Override 
	public List<DataFile> findByDataSetId(String dataSetId) {
		return this.getMongoOperations().find(new Query(Criteria.where("dataSetId").is(dataSetId)), this.getModel());
	}

	@Override 
	public List<DataFile> findByDataType(String dataType) {
		return this.getMongoOperations().find(new Query(Criteria.where("dataType").is(dataType)), this.getModel());
	}
}
