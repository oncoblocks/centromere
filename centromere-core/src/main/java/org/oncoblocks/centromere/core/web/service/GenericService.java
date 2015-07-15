package org.oncoblocks.centromere.core.web.service;

import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.oncoblocks.centromere.core.repository.RepositoryOperations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;

/**
 * Generic implementation of a repository service.  Simply passes arguments and 
 *   results between the controller and repository layers without any additional
 *   manipulation.
 * 
 * @author woemler
 */
public class GenericService<T, ID extends Serializable> 
		implements ServiceOperations<T, ID> {
	
	protected RepositoryOperations<T, ID> repository;
	
	public GenericService(RepositoryOperations<T, ID> repository){
		this.repository = repository;
	}

	/**
	 * {@link RepositoryOperations#findById}
	 */
	public T findById(ID id) {
		return repository.findById(id);
	}

	/**
	 * {@link RepositoryOperations#exists}
	 */
	public boolean exists(ID id) {
		return repository.exists(id);
	}

	/**
	 * {@link RepositoryOperations#findAll}
	 */
	public List<T> findAll() {
		return (List<T>) repository.findAll();
	}

	/**
	 * {@link RepositoryOperations#findAllSorted}
	 */
	public List<T> findAllSorted(Sort sort) {
		return (List<T>) repository.findAllSorted(sort);
	}

	/**
	 * {@link RepositoryOperations#findAllPaged}
	 */
	public Page<T> findAllPaged(Pageable pageable) {
		return repository.findAllPaged(pageable);
	}

	/**
	 * {@link RepositoryOperations#count}
	 */
	public long count() {
		return repository.count();
	}

	/**
	 * {@link RepositoryOperations#find}
	 */
	public List<T> find(Iterable<QueryCriteria> queryCriterias) {
		return (List<T>) repository.find(queryCriterias);
	}

	/**
	 * {@link RepositoryOperations#findSorted}
	 */
	public List<T> findSorted(Iterable<QueryCriteria> queryCriterias, Sort sort) {
		return (List<T>) repository.findSorted(queryCriterias, sort);
	}

	/**
	 * {@link RepositoryOperations#findPaged}
	 */
	public Page<T> findPaged(Iterable<QueryCriteria> queryCriterias, Pageable pageable) {
		return repository.findPaged(queryCriterias, pageable);
	}

	/**
	 * {@link RepositoryOperations#count}
	 */
	public long count(Iterable<QueryCriteria> queryCriterias) {
		return repository.count(queryCriterias);
	}

	/**
	 * {@link RepositoryOperations#find}
	 */
	public List<T> find(T entityQuery) {
		return (List<T>) repository.find(entityQuery);
	}

	/**
	 * {@link RepositoryOperations#findSorted}
	 */
	public List<T> findSorted(T entityQuery, Sort sort) {
		return (List<T>) repository.findSorted(entityQuery, sort);
	}

	/**
	 * {@link RepositoryOperations#findPaged}
	 */
	public Page<T> findPaged(T entityQuery, Pageable pageable) {
		return repository.findPaged(entityQuery, pageable);
	}

	/**
	 * {@link RepositoryOperations#count}
	 */
	public long count(T entityQuery) {
		return repository.count(entityQuery);
	}

	/**
	 * {@link RepositoryOperations#insert}
	 */
	public <S extends T> S insert(S entity) {
		return repository.insert(entity);
	}

	/**
	 * {@link RepositoryOperations#update}
	 */
	public <S extends T> S update(S entity) {
		return repository.update(entity);
	}

	/**
	 * {@link RepositoryOperations#delete}
	 */
	public void delete(ID id) {
		repository.delete(id);
	}
}
