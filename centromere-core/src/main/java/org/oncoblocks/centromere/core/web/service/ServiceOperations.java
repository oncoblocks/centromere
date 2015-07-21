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

package org.oncoblocks.centromere.core.web.service;

import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * Service-level interface for resource operations.  Exposes most of the functionality of the 
 *   repository layer to the web layer.
 * 
 * @author woemler
 */
public interface ServiceOperations<T, ID extends Serializable> {
	
	/**
	 * Retrieves a single record by it's {@code ID}.
	 *
	 * @param id {@code ID} value for the entity.
	 * @return the target entity, {@code T}, or {@code null} if not found.
	 */
	T findById(ID id);

	/**
	 * Tests whether or not a record with the given {@code ID} exists in the repository.
	 *
	 * @param id {@code ID} value for the entity.
	 * @return {@code true} if the record exists, or {@code false} if it does not.
	 */
	boolean exists(ID id);

	/**
	 * Retrieves all records from the repository.
	 *
	 * @return every instance of {@code T} in the repository.
	 */
	Iterable<T> findAll();

	/**
	 * Retrieves all records from the repository, sorted into the desired order.
	 *
	 * @param sort {@link org.springframework.data.domain.Sort}
	 * @return every instance of {@code T} in the repository.
	 */
	Iterable<T> findAllSorted(Sort sort);

	/**
	 * Retrieves a paged representation of every records in the repository.
	 *
	 * @param pageable {@link org.springframework.data.domain.Pageable}
	 * @return {@link org.springframework.data.domain.Page} containing the desired set of records. 
	 */
	Page<T> findAllPaged(Pageable pageable);

	/**
	 * Returns a count of total records in the repository.
	 *
	 * @return count of total {@code T} records.
	 */
	long count();

	/**
	 * Searches for all records that satisfy the requested criteria.
	 *
	 * @param queryCriterias {@link org.oncoblocks.centromere.core.repository.QueryCriteria}
	 * @return all matching {@code T} records.
	 */
	Iterable<T> find(Iterable<QueryCriteria> queryCriterias);

	/**
	 * Searches for all records that satisfy the requested criteria, and returns them in the 
	 *   requested order.
	 *
	 * @param queryCriterias {@link org.oncoblocks.centromere.core.repository.QueryCriteria}
	 * @param sort {@link org.springframework.data.domain.Sort}
	 * @return all matching {@code T} records.
	 */
	Iterable<T> findSorted(Iterable<QueryCriteria> queryCriterias, Sort sort);

	/**
	 * Searches for all records that satisfy the requested criteria, and returns them as a paged
	 *   collection.
	 *
	 * @param queryCriterias {@link org.oncoblocks.centromere.core.repository.QueryCriteria}
	 * @param pageable {@link org.springframework.data.domain.Pageable}
	 * @return {@link org.springframework.data.domain.Page} containing the desired set of records. 
	 */
	Page<T> findPaged(Iterable<QueryCriteria> queryCriterias, Pageable pageable);

	/**
	 * Returns a count of all records that satify the requested criteria.
	 *
	 * @param queryCriterias {@link org.oncoblocks.centromere.core.repository.QueryCriteria}
	 * @return a count of {@code T} records.
	 */
	long count(Iterable<QueryCriteria> queryCriterias);

	/**
	 * Searches for all records that match the submitted entity model object attributes.
	 *
	 * @param entityQuery partial model object
	 * @return all matching {@code T} records.
	 */
	Iterable<T> find(T entityQuery);

	/**
	 * Searches for all records that match the submitted entity model object attributes, and 
	 *   returns them in the requested order.
	 *
	 * @param entityQuery partial model object
	 * @param sort {@link org.springframework.data.domain.Sort}
	 * @return all matching {@code T} records
	 */
	Iterable<T> findSorted(T entityQuery, Sort sort);

	/**
	 * Searches for all records that match the submitted entity model object attributes, and 
	 *   returns a paged collection.
	 *
	 * @param entityQuery partial model object
	 * @param pageable {@link org.springframework.data.domain.Pageable}
	 * @return {@link org.springframework.data.domain.Page} containing the desired set of records.
	 */
	Page<T> findPaged(T entityQuery, Pageable pageable);

	/**
	 * Returns the count of records that match the submitted entity model object attributes.
	 *
	 * @param entityQuery parial model object
	 * @return count of matching records
	 */
	long count(T entityQuery);

	/**
	 * Creates a new record in the repository and returns the updated model object.
	 *
	 * @param entity instance of {@code T} to be persisted.
	 * @return updated instance of the entity.
	 */
	<S extends T> S insert(S entity);

	/**
	 * Updates an existing record in the repository and returns its instance.
	 *
	 * @param entity updated record to be persisted in the repository.
	 * @return the updated entity object.
	 */
	<S extends T> S update(S entity);

	/**
	 * Deletes a record in the repository, identified by its {@code ID}.
	 *
	 * @param id identifier for the record to be deleted.
	 */
	void delete(ID id);
	
}
