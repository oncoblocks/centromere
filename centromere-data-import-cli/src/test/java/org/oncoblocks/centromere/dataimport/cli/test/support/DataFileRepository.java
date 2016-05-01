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

package org.oncoblocks.centromere.dataimport.cli.test.support;

import org.oncoblocks.centromere.core.repository.ModelRepository;
import org.oncoblocks.centromere.core.repository.support.DataFileMetadataRepository;
import org.oncoblocks.centromere.mongodb.GenericMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author woemler
 */
@ModelRepository(DataFile.class)
public class DataFileRepository extends GenericMongoRepository<DataFile, String> 
		implements DataFileMetadataRepository<DataFile, String> {
	
	@Autowired
	public DataFileRepository(MongoTemplate mongoTemplate) {
		super(mongoTemplate, DataFile.class);
	}

	public List<DataFile> getByDataType(String dataType) {
		return this.getMongoOperations().find(new Query(Criteria.where("dataType").is(dataType)), DataFile.class);
	}

	public List<DataFile> getByFilePath(String filePath) {
		return this.getMongoOperations().find(new Query(Criteria.where("filePath").is(filePath)), DataFile.class);
	}
}
