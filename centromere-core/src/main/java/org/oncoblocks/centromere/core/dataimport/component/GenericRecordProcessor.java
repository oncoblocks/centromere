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

package org.oncoblocks.centromere.core.dataimport.component;

import org.oncoblocks.centromere.core.dataimport.pipeline.BasicImportOptions;
import org.oncoblocks.centromere.core.dataimport.pipeline.DataSetAware;
import org.oncoblocks.centromere.core.dataimport.pipeline.ImportOptions;
import org.oncoblocks.centromere.core.dataimport.pipeline.ImportOptionsAware;
import org.oncoblocks.centromere.core.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;

import java.io.File;

/**
 * Basic {@link RecordProcessor} implementation, which can be used to handle most file import jobs.
 *   The {@code doBefore} and {@code doAfter} methods can be overridden to handle data set or data
 *   file metadata persistence, pre/post-processing, or other maintenance tasks.  Uses a basic
 *   {@link BasicImportOptions} instance to set import parameters, and identify the directory to store
 *   all temporary files.
 * 
 * @author woemler
 */
public class GenericRecordProcessor<T extends Model<?>> 
		implements RecordProcessor<T>, ImportOptionsAware, DataSetAware {

	private Class<T> model;
	private RecordReader<T> reader;
	private Validator validator;
	private RecordWriter<T> writer;
	private RecordImporter importer;
	private BasicImportOptions options;
	private Object dataSetId;
	private static final Logger logger = LoggerFactory.getLogger(GenericRecordProcessor.class);

	public GenericRecordProcessor() { }

	public GenericRecordProcessor(
			Class<T> model,
			RecordReader<T> reader, 
			Validator validator,
			RecordWriter<T> writer,
			RecordImporter importer,
			BasicImportOptions options) {
		this.model = model;
		this.reader = reader;
		this.validator = validator;
		this.writer = writer;
		this.importer = importer;
		this.options = options;
	}

	/**
	 * {@link RecordProcessor#doBefore()}
	 */
	public void doBefore() {
		if (writer != null && writer instanceof ImportOptionsAware) {
			((ImportOptionsAware) writer).setImportOptions(options);
		}
		if (reader != null && reader instanceof ImportOptionsAware) {
			((ImportOptionsAware) reader).setImportOptions(options);
		}
		if (importer != null && importer instanceof ImportOptionsAware) {
			((ImportOptionsAware) importer).setImportOptions(options);
		}
		if (writer != null && writer instanceof DataSetAware) {
			((DataSetAware) writer).setDataSetId(dataSetId);
		}
		if (reader != null && reader instanceof DataSetAware) {
			((DataSetAware) reader).setDataSetId(dataSetId);
		}
		if (importer != null && importer instanceof DataSetAware) {
			((DataSetAware) importer).setDataSetId(dataSetId);
		}
	}

	/**
	 * {@link RecordProcessor#doAfter()}
	 */
	public void doAfter() {
		
	}

	/**
	 * {@link RecordProcessor#run(String)}
	 * @param inputFilePath
	 * @throws DataImportException
	 */
	public void run(String inputFilePath) throws DataImportException {
		reader.doBefore(inputFilePath);
		writer.doBefore(this.getTempFilePath(inputFilePath));
		T record = reader.readRecord();
		while (record != null) {
			if (record instanceof DataSetAware) ((DataSetAware) record).setDataSetId(dataSetId);
			if (validator != null) {
				BeanPropertyBindingResult bindingResult
						= new BeanPropertyBindingResult(record, record.getClass().getName());
				validator.validate(record, bindingResult);
				if (bindingResult.hasErrors()){
					logger.warn(String.format("Record failed validation: %s", record.toString()));
					if (!options.isSkipInvalidRecords()){
						throw new DataImportException(bindingResult.toString());
					}
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


	/**
	 * Returns the path of the temporary file to be written, if necessary.  Uses the input file's name
	 *   and the pre-determined temp file directory to generate the name, so as to overwrite previous
	 *   jobs' temp file.
	 * @param inputFilePath
	 * @return
	 */
	private String getTempFilePath(String inputFilePath){
		File tempDir = new File(options.getTempDirectoryPath());
		String fileName = new File(inputFilePath).getName() + ".tmp";
		File tempFile = new File(tempDir, fileName);
		return tempFile.getPath();
	}

	public Class<T> getModel() {
		return model;
	}

	public void setModel(Class<T> model) {
		this.model = model;
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
		return options;
	}

	public void setImportOptions(ImportOptions options) {
		this.options = new BasicImportOptions(options);
	}
	
	public void setImportOptions(BasicImportOptions options){
		this.options = options;
	}

	public Object getDataSetId() {
		return dataSetId;
	}

	public void setDataSetId(Object dataSetId) {
		this.dataSetId = dataSetId;
	}
}
