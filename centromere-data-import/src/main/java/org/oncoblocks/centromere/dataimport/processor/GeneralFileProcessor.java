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

package org.oncoblocks.centromere.dataimport.processor;

import org.oncoblocks.centromere.core.model.Model;
import org.oncoblocks.centromere.dataimport.config.DataImportException;
import org.oncoblocks.centromere.dataimport.importer.EntityRecordImporter;
import org.oncoblocks.centromere.dataimport.reader.EntityRecordReader;
import org.oncoblocks.centromere.dataimport.validator.EntityValidator;
import org.oncoblocks.centromere.dataimport.writer.EntityRecordWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private boolean failOnInvalidRecord = true;
	
	private static final Logger logger = LoggerFactory.getLogger(GeneralFileProcessor.class);
	
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
	public void doBefore() {
		// Do nothing
	}

	@Override 
	public void doAfter() {
		// Do nothing
	}

	@Override
	public long run(String inputFilePath, String tempFilePath, ID dataSetId, ID dataFileId) 
			throws DataImportException{
		reader.setDataFileId(dataFileId);
		reader.setDataSetId(dataSetId);
		long counter = 0;
		T record = null;
		try {
			logger.debug(String.format("CENTROMERE: Preparing input file reader: %s", inputFilePath));
			reader.open(inputFilePath);
			logger.debug(String.format("CENTROMERE: Preparing temp file writer: %s", tempFilePath));
			writer.open(tempFilePath);
			if (validator == null) logger.debug(String.format("CENTROMERE: Skipping entity validation for input file: %s", inputFilePath));
			record = reader.readRecord();
			while (record != null) {
				if (validator != null) {
					try {
						if (validator.validate(record)) {
							writer.writeRecord(record);
							counter++;
						}
					} catch (Exception e){
						if (failOnInvalidRecord){
							throw e;
						} else {
							logger.warn(String.format("CENTROMERE: Skipping invalid record: %s ", record.toString()));
						}
					}
				} else {
					writer.writeRecord(record);
					counter++;
				}
				record = reader.readRecord();
			}
		} catch (Exception e){
			e.printStackTrace();
			if (record != null) logger.warn(String.format("CENTROMERE: Current record: %s", record.toString()));
			throw new DataImportException(e.getMessage());
		} finally {
			reader.close();
			logger.debug(String.format("CENTROMERE: Input file reader complete: %s", inputFilePath));
			writer.close();
			logger.debug(String.format("CENTROMERE: Temp file writer complete: %s", tempFilePath));
		}
		if (importer != null) {
			logger.debug(String.format("CENTROMERE: Preparing to import temp file: %s", tempFilePath));
			importer.importFile(tempFilePath);
			logger.debug(String.format("CENTROMERE: Temp file import complete: %s", tempFilePath));
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

	public boolean isFailOnInvalidRecord() {
		return failOnInvalidRecord;
	}

	public GeneralFileProcessor setFailOnInvalidRecord(boolean failOnInvalidRecord) {
		this.failOnInvalidRecord = failOnInvalidRecord;
		return this;
	}
}
