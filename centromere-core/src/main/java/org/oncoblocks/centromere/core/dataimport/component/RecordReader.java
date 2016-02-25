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

import org.oncoblocks.centromere.core.model.Model;

/**
 * Data impoer component class.  Reads from a data source and returns {@link Model} class instances.
 * 
 * @author woemler
 */
public interface RecordReader<T extends Model<?>> {

	/**
	 * Generates and returns a single {@link Model} entity from the input data source.
	 * 
	 * @return
	 * @throws DataImportException
	 */
	T readRecord() throws DataImportException;

	/**
	 * To be executed before the {@code readRecord} method is called.  Can be configured to handle 
	 *   tasks like opening a file.
	 * 
	 * @param input
	 * @throws DataImportException
	 */
	void doBefore(String input) throws DataImportException;

	/**
	 * To be executed after the last {@code readRecord} call.  Can be configured to handle tasks such
	 *   as closing an input file object.
	 * 
	 * @throws DataImportException
	 */
	void doAfter() throws DataImportException;
}
