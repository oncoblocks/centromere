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

package org.oncoblocks.centromere.mongodb;

import org.oncoblocks.centromere.core.dataimport.component.DataImportException;
import org.oncoblocks.centromere.core.dataimport.component.AbstractRecordFileWriter;
import org.oncoblocks.centromere.core.model.Model;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Implementation of {@link org.oncoblocks.centromere.core.dataimport.component.RecordWriter} that 
 *   creates temporary files with {@link Model} records formatted in JSON, for importing via 
 *   MongoImport.
 * 
 * @author woemler
 */
public class MongoImportTempFileWriter<T extends Model<?>> extends AbstractRecordFileWriter<T> {
	
	private final ImportUtils importUtils;

	public MongoImportTempFileWriter(ImportUtils importUtils) {
		this.importUtils = importUtils;
	}
	
	public MongoImportTempFileWriter(MongoTemplate mongoTemplate){
		this.importUtils = new ImportUtils(mongoTemplate);
	}

	/**
	 * Writes a {@link Model} record to a temp file, formatted into JSON using {@link ImportUtils#convertEntityToJson(Object)}.
	 * 
	 * @param record
	 * @throws DataImportException
	 */
	public void writeRecord(T record) throws DataImportException {
		FileWriter writer = this.getWriter();
		try {
			writer.write(importUtils.convertEntityToJson(record));
			writer.write("\n");
		} catch (IOException e){
			e.printStackTrace();
			throw new DataImportException(e.getMessage());
		}
	}
	
}
