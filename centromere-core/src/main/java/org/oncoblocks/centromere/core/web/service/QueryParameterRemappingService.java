package org.oncoblocks.centromere.core.web.service;

import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.oncoblocks.centromere.core.repository.RepositoryOperations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service extension that remaps query parameter names to database field names.
 * 
 * @author woemler
 */
public abstract class QueryParameterRemappingService<T, ID extends Serializable> extends GenericService<T, ID> {

	public QueryParameterRemappingService(RepositoryOperations<T, ID> repository) {
		super(repository);
	}

	/**
	 * Returns a map object that will be used to remap attribute names.
	 * 
	 * @return
	 */
	protected abstract Map<String,String> getRemapping();

	/**
	 * Remaps the attribute names of the query parameters, if a mapping exists.
	 * 
	 * @param queryCriterias
	 * @return
	 */
	private List<QueryCriteria> remapQueryCriteria(Iterable<QueryCriteria> queryCriterias){
		List<QueryCriteria> remapped = new ArrayList<>();
		for (QueryCriteria queryCriteria: queryCriterias){
			if (getRemapping().containsKey(queryCriteria.getKey())) {
				queryCriteria.setKey(getRemapping().get(queryCriteria.getKey()));
			}
			remapped.add(queryCriteria);
		}
		return remapped;
	}

	/**
	 * Remaps the attribute names used in the {@link org.springframework.data.domain.Sort} operations,
	 *   if the mapping exists.
	 * 
	 * @param sort
	 * @return
	 */
	private Sort remapSort(Sort sort){
		List<Sort.Order> remapped = new ArrayList<>();
		for (Sort.Order order: sort){
			String key = order.getProperty();
			if (getRemapping().containsKey(key)) key = getRemapping().get(key);
			remapped.add(new Sort.Order(order.getDirection(), key));
		}
		return new Sort(remapped);
	}

	/**
	 * Recreates the {@link org.springframework.data.domain.PageRequest} with the remapped sorts.
	 * 
	 * @param pageable
	 * @return
	 */
	private Pageable remapPageable(Pageable pageable){
		if (pageable.getSort() != null) {
			Sort sort = remapSort(pageable.getSort());
			return new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
		} else {
			return pageable;
		}
	}

	@Override 
	public List<T> find(Iterable<QueryCriteria> queryCriterias) {
		return super.find(remapQueryCriteria(queryCriterias));
	}

	@Override 
	public List<T> findSorted(Iterable<QueryCriteria> queryCriterias, Sort sort) {
		return super.findSorted(remapQueryCriteria(queryCriterias), remapSort(sort));
	}

	@Override 
	public Page<T> findPaged(Iterable<QueryCriteria> queryCriterias, Pageable pageable) {
		return super.findPaged(remapQueryCriteria(queryCriterias), remapPageable(pageable));
	}

	@Override 
	public long count(Iterable<QueryCriteria> queryCriterias) {
		return super.count(remapQueryCriteria(queryCriterias));
	}

	@Override 
	public List<T> findAllSorted(Sort sort) {
		return super.findAllSorted(remapSort(sort));
	}

	@Override 
	public List<T> findSorted(T entityQuery, Sort sort) {
		return super.findSorted(entityQuery, remapSort(sort));
	}

	@Override 
	public Page<T> findAllPaged(Pageable pageable) {
		return super.findAllPaged(remapPageable(pageable));
	}

	@Override 
	public Page<T> findPaged(T entityQuery, Pageable pageable) {
		return super.findPaged(entityQuery, remapPageable(pageable));
	}
}
