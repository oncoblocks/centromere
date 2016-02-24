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

package org.oncoblocks.centromere.core.input.processor;

import org.oncoblocks.centromere.core.input.DataImportException;
import org.oncoblocks.centromere.core.input.importer.RecordImporter;
import org.oncoblocks.centromere.core.input.pipeline.ImportOptions;
import org.oncoblocks.centromere.core.input.pipeline.ProcessorConfig;
import org.oncoblocks.centromere.core.input.reader.RecordReader;
import org.oncoblocks.centromere.core.input.writer.RecordWriter;
import org.oncoblocks.centromere.core.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;

import java.io.File;

/**
 * Basic {@link RecordProcessor} implementation, which can be constructed using metadata from a 
 *   {@link ProcessorConfig} instance.
 * 
 * @author woemler
 */
public class GenericRecordProcessor<T extends Model<?>> implements RecordProcessor {

	private RecordReader<T> reader;
	private Validator validator;
	private RecordWriter<T> writer;
	private RecordImporter importer;
	private ImportOptions importOptions;
	private static final Logger logger = LoggerFactory.getLogger(GenericRecordProcessor.class);

	@Override public void doBefore() {
	}

	@Override public void doAfter() {
	}

	@Override public void run(String inputFilePath) throws DataImportException {
		File inputFile = new File(inputFilePath);
		if (!inputFile.isFile() || !inputFile.canRead()){
			if (importOptions.failOnMissingFile()){
				throw new DataImportException(String.format("[CENTROMERE] Input file cannot be found or is not readable: %s", inputFilePath));
			} else {
				return;
			}
		}
		reader.doBefore(inputFilePath);
		writer.doBefore(this.getTempFilePath(inputFilePath));
		T record = reader.readRecord();
		while (record != null) {
			if (validator != null) {
				BeanPropertyBindingResult bindingResult
						= new BeanPropertyBindingResult(record, record.getClass().getName());
				validator.validate(record, bindingResult);
				if (bindingResult.hasErrors() && importOptions.failOnInvalidRecord()){
					throw new DataImportException(bindingResult.toString());
				}
			}
			writer.writeRecord(record);
			record = reader.readRecord();
		}
		writer.doAfter();
		reader.doAfter();
		if (importer != null) {
			importer.importFile(this.getTempFilePath(inputFilePath));
		}
	}
	
	private String getTempFilePath(String inputFilePath){
		File tempDir = new File(importOptions.getTempDirectoryPath());
		String fileName = new File(inputFilePath).getName() + ".tmp";
		File tempFile = new File(tempDir, fileName);
		return tempFile.getPath();
	}

	public RecordReader<T> getReader() {
		return reader;
	}

	public void setReader(RecordReader<T> reader) {
		this.reader = reader;
	}

	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	public RecordWriter<T> getWriter() {
		return writer;
	}

	public void setWriter(RecordWriter<T> writer) {
		this.writer = writer;
	}

	public RecordImporter getImporter() {
		return importer;
	}

	public void setImporter(RecordImporter importer) {
		this.importer = importer;
	}

	public ImportOptions getImportOptions() {
		return importOptions;
	}

	public void setImportOptions(
			ImportOptions importOptions) {
		this.importOptions = importOptions;
	}
}
