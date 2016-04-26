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

package org.oncoblocks.centromere.core.dataimport;

import org.oncoblocks.centromere.core.model.Model;
import org.springframework.validation.Validator;

/**
 * Record processors take all of the individual data import components (reader, writer, validator, 
 *   and importer), and combines them to handle a single data type's import
 * 
 * @author woemler
 */
public interface RecordProcessor<T extends Model<?>> {

	/**
	 * Executes before the {@code run} method.  Can be configured to handle tasks, such as data set 
	 *   pre-processing.
	 */
	void doBefore();

	/**
	 * Executes after the {@code run} method.  Can be configured to handle tasks, such as record
	 *   post-processing.
	 */
	void doAfter();

	/**
	 * Executes the pipeline and processes the input through the individual components.
	 * 
	 * @param path
	 * @throws DataImportException
	 */
	void run(String path) throws DataImportException;
	
	void setReader(RecordReader<T> reader);
	
	RecordReader<T> getReader();
	
	void setValidator(Validator validator);
	
	Validator getValidator();
	
	void setWriter(RecordWriter<T> writer);
	
	RecordWriter<T> getWriter();
	
	void setImporter(RecordImporter importer);
	
	RecordImporter getImporter();
	
	void setModel(Class<T> model);
	
	Class<T> getModel();
	
//	void setImportOptions(ImportOptions importOptions);
//	
//	ImportOptions getImportOptions();
	
}
