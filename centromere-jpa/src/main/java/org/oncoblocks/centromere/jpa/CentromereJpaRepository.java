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

package org.oncoblocks.centromere.jpa;

import org.oncoblocks.centromere.core.model.Model;
import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.oncoblocks.centromere.core.repository.RepositoryOperations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author woemler
 */
public class CentromereJpaRepository<T extends Model<ID>, ID extends Serializable> 
		extends SimpleJpaRepository<T, ID>
		implements RepositoryOperations<T, ID> {
	
	private final JpaEntityInformation<T, ID> metadata;
	private final EntityManager entityManager;
	private final JpaQueryBuilder<T> queryBuilder;

	public CentromereJpaRepository(JpaEntityInformation<T, ID> entityInformation,
			EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.metadata = entityInformation;
		this.entityManager = entityManager;
		this.queryBuilder = new JpaQueryBuilder<>(entityManager);
	}

	/**
	 * Searches for all records that satisfy the requested criteria.
	 *
	 * @param queryCriterias {@link QueryCriteria}
	 * @return all matching {@code T} records.
	 */
	public Iterable<T> find(Iterable<QueryCriteria> queryCriterias) {
		return this.findAll(queryBuilder.createSpecification(queryCriterias));
	}

	/**
	 * Searches for all records that satisfy the requested criteria, and returns them in the
	 * requested order.
	 *
	 * @param queryCriterias {@link QueryCriteria}
	 * @param sort           {@link Sort}
	 * @return all matching {@code T} records.
	 */
	public Iterable<T> find(Iterable<QueryCriteria> queryCriterias, Sort sort) {
		return this.findAll(queryBuilder.createSpecification(queryCriterias), sort);
	}

	/**
	 * Searches for all records that satisfy the requested criteria, and returns them as a paged
	 * collection.
	 *
	 * @param queryCriterias {@link QueryCriteria}
	 * @param pageable       {@link Pageable}
	 * @return {@link Page} containing the desired set of records.
	 */
	public Page<T> find(Iterable<QueryCriteria> queryCriterias, Pageable pageable) {
		return this.findAll(queryBuilder.createSpecification(queryCriterias), pageable);
	}

	/**
	 * Returns a count of all records that satify the requested criteria.
	 *
	 * @param queryCriterias {@link QueryCriteria}
	 * @return a count of {@code T} records.
	 */
	public long count(Iterable<QueryCriteria> queryCriterias) {
		return this.count(queryBuilder.createSpecification(queryCriterias));
	}

	/**
	 * Returns a unsorted list of distinct values of the requested field.
	 *
	 * @param field Model field name.
	 * @return Sorted list of distinct values of {@code field}.
	 */
	public Iterable<Object> distinct(String field) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(metadata.getJavaType());
		Root<T> root = query.from(metadata.getJavaType());
		query.select(root.get(field)).distinct(true);
		return (List) entityManager.createQuery(query).getResultList();
	}

	/**
	 * Returns a unsorted list of distinct values of the requested field, filtered using a {@link QueryCriteria}
	 * based query.
	 *
	 * @param field          Model field name.
	 * @param queryCriterias Query criteria to filter the field values by.
	 * @return Sorted list of distinct values of {@code field}.
	 */
	public Iterable<Object> distinct(String field, Iterable<QueryCriteria> queryCriterias) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(metadata.getJavaType());
		Root<T> root = query.from(metadata.getJavaType());
		query.where(queryBuilder.createSpecification(queryCriterias).toPredicate(root, query, builder));
		query.select(root.get(field)).distinct(true);
		return (List) entityManager.createQuery(query).getResultList();
	}

	/**
	 * Creates a new record in the repository and returns the updated model object.
	 *
	 * @param entity instance of {@code T} to be persisted.
	 * @return updated instance of the entity.
	 */
	public <S extends T> S insert(S entity) {
		if (!this.exists(entity.getId())){
			this.save(entity);
		} else {
			throw new DuplicateKeyException(String.format("Record already exists: %s", entity.toString()));
		}
		return entity;
	}

	/**
	 * Creates multiple new records and returns their updated representations.
	 *
	 * @param entities collection of records to be persisted.
	 * @return updated instances of the entity objects.
	 */
	public <S extends T> Iterable<S> insert(Iterable<S> entities) {
		List<S> saved = new ArrayList<>();
		for (S s: entities){
			saved.add(this.insert(s));
		}
		return saved;
	}

	/**
	 * Updates an existing record in the repository and returns its instance.
	 *
	 * @param entity updated record to be persisted in the repository.
	 * @return the updated entity object.
	 */
	public <S extends T> S update(S entity) {
		if (this.exists(entity.getId())){
			this.save(entity);
		} else {
			throw new DataIntegrityViolationException(String.format("No record with id exists: %s", entity.getId().toString()));
		}
		return entity;
	}

	/**
	 * Updates multiple records and returns their instances.
	 *
	 * @param entities collection of records to update.
	 * @return updated instances of the entity objects.
	 */
	public <S extends T> Iterable<S> update(Iterable<S> entities) {
		List<S> updated = new ArrayList<>();
		for (S s: entities){
			updated.add(this.update(s));
		}
		return updated;
	}
	
}
