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

package org.oncoblocks.centromere.core.dataimport;

import org.oncoblocks.centromere.core.dataimport.importer.EntityRecordImporter;
import org.oncoblocks.centromere.core.dataimport.reader.EntityRecordReader;
import org.oncoblocks.centromere.core.dataimport.validator.EntityValidator;
import org.oncoblocks.centromere.core.dataimport.writer.EntityRecordWriter;
import org.springframework.util.Assert;

/**
 * @author woemler
 */
public class DataFileProcessor<T> {

	private EntityRecordReader<T> reader;
	private EntityValidator<T> validator;
	private EntityRecordWriter<T> writer;
	private EntityRecordImporter importer;

	public DataFileProcessor(
			EntityRecordReader<T> reader,
			EntityRecordWriter<T> writer,
			EntityValidator<T> validator,
			EntityRecordImporter importer) {
		Assert.notNull(reader);
		Assert.notNull(writer);
		this.reader = reader;
		this.validator = validator;
		this.writer = writer;
		this.importer = importer;
	}

	public void run(){
		try {
			reader.before();
			writer.before();
			T record = reader.readRecord();
			while (record != null) {
				if (validator != null) {
					validator.validate(record);
				}
				writer.writeRecord(record);
				record = reader.readRecord();
			}
			if (importer != null){
				importer.importFile();
			}
		} catch (Exception e) {
			throw new DataFileProcessingException(e.getMessage());
		} finally {
			reader.after();
			writer.after();
		}
	}
	
}
