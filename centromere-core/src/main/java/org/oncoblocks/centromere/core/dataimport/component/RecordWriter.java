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
 * Data import component class for writing imported {@link Model} records to a temporary file or
 *   {@link org.oncoblocks.centromere.core.repository.RepositoryOperations} implementation.
 * 
 * @author woemler
 */
public interface RecordWriter<T extends Model<?>> {

	/**
	 * Writes a single {@link Model} entity to the target destination.
	 * 
	 * @param record
	 * @throws DataImportException
	 */
	void writeRecord(T record) throws DataImportException;

	/**
	 * Executes before the first {@code writeRecord} method call.  Can be configured to perform tasks,
	 *   such as opening an output file object.
	 * 
	 * @param destination
	 * @throws DataImportException
	 */
	void doBefore(String destination) throws DataImportException;

	/**
	 * Executes after the last {@code writeRecord} call.  Can be configured to perform tasks, such as 
	 *   closing output file objects.
	 * 
	 * @throws DataImportException
	 */
	void doAfter() throws DataImportException;
}
