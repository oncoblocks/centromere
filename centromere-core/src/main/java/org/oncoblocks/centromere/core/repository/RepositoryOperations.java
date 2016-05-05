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

package org.oncoblocks.centromere.core.repository;


import org.oncoblocks.centromere.core.model.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/**
 * Basic operations that all repositories must implement, regardless database technology used.  Based
 *   on {@link org.springframework.data.repository.PagingAndSortingRepository}, but with some additions
 *   to support dynamic queries.  
 * 
 * @author woemler
 */
@NoRepositoryBean
public interface RepositoryOperations<T extends Model<ID>, ID extends Serializable>
		extends PagingAndSortingRepository<T, ID> {

	/**
	 * Searches for all records that satisfy the requested criteria.
	 *
	 * @param queryCriterias {@link QueryCriteria}
	 * @return all matching {@code T} records.
	 */
	Iterable<T> find(Iterable<QueryCriteria> queryCriterias);

	/**
	 * Searches for all records that satisfy the requested criteria, and returns them in the 
	 *   requested order.
	 *
	 * @param queryCriterias {@link QueryCriteria}
	 * @param sort {@link Sort}
	 * @return all matching {@code T} records.
	 */
	Iterable<T> find(Iterable<QueryCriteria> queryCriterias, Sort sort);

	/**
	 * Searches for all records that satisfy the requested criteria, and returns them as a paged
	 *   collection.
	 *
	 * @param queryCriterias {@link QueryCriteria}
	 * @param pageable {@link Pageable}
	 * @return {@link Page} containing the desired set of records. 
	 */
	Page<T> find(Iterable<QueryCriteria> queryCriterias, Pageable pageable);

	/**
	 * Returns a count of all records that satify the requested criteria.
	 *
	 * @param queryCriterias {@link QueryCriteria}
	 * @return a count of {@code T} records.
	 */
	long count(Iterable<QueryCriteria> queryCriterias);

	/**
	 * Returns a unsorted list of distinct values of the requested field.
	 *
	 * @param field Model field name.
	 * @return Sorted list of distinct values of {@code field}.
	 */
	Iterable<Object> distinct(String field);

	/**
	 * Returns a unsorted list of distinct values of the requested field, filtered using a {@link QueryCriteria}
	 *   based query.
	 *
	 * @param field Model field name.
	 * @param queryCriterias Query criteria to filter the field values by.
	 * @return Sorted list of distinct values of {@code field}.
	 */
	Iterable<Object> distinct(String field, Iterable<QueryCriteria> queryCriterias);

	/* Create records */

	/**
	 * Creates a new record in the repository and returns the updated model object.
	 *
	 * @param entity instance of {@code T} to be persisted.
	 * @return updated instance of the entity.
	 */
	<S extends T> S insert(S entity);

	/**
	 * Creates multiple new records and returns their updated representations.
	 *
	 * @param entities collection of records to be persisted.
	 * @return updated instances of the entity objects.
	 */
	<S extends T> Iterable<S> insert(Iterable<S> entities);

	/* Update records */

	/**
	 * Updates an existing record in the repository and returns its instance.
	 *
	 * @param entity updated record to be persisted in the repository.
	 * @return the updated entity object.
	 */
	<S extends T> S update(S entity);

	/**
	 * Updates multiple records and returns their instances.
	 *
	 * @param entities collection of records to update.
	 * @return updated instances of the entity objects.
	 */
	<S extends T> Iterable<S> update(Iterable<S> entities);

}
