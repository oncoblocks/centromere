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
import org.oncoblocks.centromere.core.repository.RepositoryOperations;

/**
 * Simple implementation of {@link RecordWriter}, that writes all records directly to the database
 *   using a {@link RepositoryOperations} implementation.
 * 
 * @author woemler
 */
public class RepositoryRecordWriter<T extends Model<?>> implements RecordWriter<T> {
	
	private RepositoryOperations<T, ?> repository;

	public RepositoryRecordWriter() { }

	public RepositoryRecordWriter(
			RepositoryOperations<T, ?> repository) {
		this.repository = repository;
	}

	/**
	 * Writes the input {@link Model} record to the target {@link RepositoryOperations} implementation,
	 *   using an insert operation.
	 * @param entity
	 */ 
	public void writeRecord(T entity) {
		repository.insert(entity);	
	}

	public RepositoryOperations<T, ?> getRepository() {
		return repository;
	}

	public void setRepository(
			RepositoryOperations<T, ?> repository) {
		this.repository = repository;
	}

	/**
	 * Performs no action.
	 * 
	 * @param destination
	 * @throws DataImportException
	 */
	public void doBefore(String destination) throws DataImportException {
		return;
	}

	/**
	 * Performs no action.
	 * 
	 * @throws DataImportException
	 */
	public void doAfter() throws DataImportException {
		return;
	}
}
