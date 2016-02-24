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

package org.oncoblocks.centromere.core.input.pipeline;

/**
 * Defines basic parameters required for a data import pipeline.
 * 
 * @author woemler
 */
public interface ImportOptions {

	/**
	 * The temp directory is the location that all temp files will be written to.  This should be 
	 *   precondifured.
	 * @return
	 */
	String getTempDirectoryPath();

	/**
	 * When true, the pipeline will throw an exception if a record fails validation.
	 * 
	 * @return
	 */
	boolean failOnInvalidRecord();

	/**
	 * WHen true, the data import peipleine will halt on any thrown 
	 *   {@link org.oncoblocks.centromere.core.input.DataImportException}.
	 * 
	 * @return
	 */
	boolean failOnDataImportException();

	/**
	 * When true, will throw an exception if the input file cannot be found or is not readable.
	 * 
	 * @return
	 */
	boolean failOnMissingFile();
}
