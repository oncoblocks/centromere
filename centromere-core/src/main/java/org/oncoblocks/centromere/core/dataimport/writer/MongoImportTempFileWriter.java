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

package org.oncoblocks.centromere.core.dataimport.writer;

import org.oncoblocks.centromere.core.dataimport.support.MongoDbUtils;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes JSON-formatted entity records to a temp file to be imported via MongoImport.
 * 
 * @author woemler
 */
public class MongoImportTempFileWriter<T> extends TempFileWriter<T> {

	private MongoDbUtils mongoUtils;
	
	public MongoImportTempFileWriter(MongoTemplate mongoTemplate) {
		super();
		this.mongoUtils = new MongoDbUtils(mongoTemplate);
	}

	@Override 
	public void writeRecord(T record) {
		FileWriter writer = getFileWriter();
		try {
			writer.write(mongoUtils.convertEntityToJson(record));
		} catch (IOException e){
			e.printStackTrace();
			throw new TempFileWriterException(e.getMessage());
		}
	}
}
