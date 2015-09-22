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

package org.oncoblocks.centromere.core.dataimport.processor;

import org.oncoblocks.centromere.core.dataimport.importer.EntityRecordImporter;
import org.oncoblocks.centromere.core.dataimport.reader.EntityRecordReader;
import org.oncoblocks.centromere.core.dataimport.validator.EntityValidator;
import org.oncoblocks.centromere.core.dataimport.writer.EntityRecordWriter;
import org.oncoblocks.centromere.core.model.Model;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * @author woemler
 */
public class GeneralFileProcessor<T extends Model<ID>, ID extends Serializable> 
		implements EntityRecordProcessor<T, ID> {

	private EntityRecordReader<T, ID> reader;
	private EntityValidator<T> validator;
	private EntityRecordWriter<T, ID> writer;
	private EntityRecordImporter importer;
	
	public GeneralFileProcessor(){ }

	public GeneralFileProcessor(
			EntityRecordReader<T, ID> reader,
			EntityRecordWriter<T, ID> writer,
			EntityValidator<T> validator,
			EntityRecordImporter importer) {
		Assert.notNull(reader);
		Assert.notNull(writer);
		this.reader = reader;
		this.validator = validator;
		this.writer = writer;
		this.importer = importer;
	}

	@Override
	public long run(String inputFilePath, String tempFilePath, ID dataSetId, ID dataFileId){
		reader.setDataFileId(dataFileId);
		reader.setDataSetId(dataSetId);
		long counter = 0;
		try {
			reader.open(inputFilePath);
			writer.open(tempFilePath);
			T record = reader.readRecord();
			while (record != null) {
				if (validator != null) {
					if (validator.validate(record)) {
						writer.writeRecord(record);
						
					}
				} else {
					writer.writeRecord(record);
				}
				record = reader.readRecord();
				counter++;
			}
		} catch (Exception e){
			e.printStackTrace();
			throw new DataFileProcessingException(e.getMessage());
		} finally {
			reader.close();
			writer.close();
		}
		if (importer != null) {
			importer.importFile(tempFilePath);
		}
		return counter;
	}

	public EntityRecordReader<T, ID> getReader() {
		return reader;
	}

	public void setReader(
			EntityRecordReader<T, ID> reader) {
		this.reader = reader;
	}

	public EntityValidator<T> getValidator() {
		return validator;
	}

	public void setValidator(
			EntityValidator<T> validator) {
		this.validator = validator;
	}

	public EntityRecordWriter<T, ID> getWriter() {
		return writer;
	}

	public void setWriter(
			EntityRecordWriter<T, ID> writer) {
		this.writer = writer;
	}

	public EntityRecordImporter getImporter() {
		return importer;
	}

	public void setImporter(
			EntityRecordImporter importer) {
		this.importer = importer;
	}
}
