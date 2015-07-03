package org.oncoblocks.centromere.core.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * Basic operations that all repositories must implement, regardless database technology used.  Based
 *   on {@link org.springframework.data.repository.PagingAndSortingRepository}, but with some additions
 *   to support dynamic queries, and removing some JPA-specific functionality.  
 * 
 * @author woemler
 */
public interface RepositoryOperations<T, ID extends Serializable> {

	/* Find by ID */

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

	/* Find all */

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

	/* Find by QueryCriteria */

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

	/* Find by entity query */

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

	/* Delete records */

	/**
	 * Deletes a record in the repository, identified by its {@code ID}.
	 * 
	 * @param id identifier for the record to be deleted.
	 */
	void delete(ID id);

	/**
	 * Deletes every record in the repository.
	 */
	void deleteAll();
	
}
