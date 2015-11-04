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

package org.oncoblocks.centromere.core.repository;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.oncoblocks.centromere.core.model.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Generic MongoDB implementation of {@link org.oncoblocks.centromere.core.repository.RepositoryOperations}.
 * 
 * @author woemler
 */
public class GenericMongoRepository<T extends Model<ID>, ID extends Serializable> 
		implements RepositoryOperations<T, ID>  {
	
	private final MongoOperations mongoOperations;
	private final Class<T> model;

	/**
	 * Creates a new {@link GenericMongoRepository} instance.
	 * @param mongoOperations {@link org.springframework.data.mongodb.core.MongoOperations}
	 * @param model class implementing {@link Model}
	 */
	public GenericMongoRepository(MongoOperations mongoOperations, Class<T> model) {
		Assert.notNull(mongoOperations);
		Assert.notNull(model);
		this.mongoOperations = mongoOperations;
		this.model = model;
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#findById}
	 */
	public T findById(ID id) {
		return mongoOperations.findById(id, model);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#exists}
	 */
	public boolean exists(ID id) {
		return mongoOperations.findById(id, model) != null;
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#findAll}
	 */
	public List<T> findAll() {
		return mongoOperations.findAll(model);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#find}
	 */
	public List<T> find(Sort sort) {
		return mongoOperations.find(new Query().with(sort), model);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#find}
	 */
	public Page<T> find(Pageable pageable) {
		List<T> entities = mongoOperations.find(new Query().with(pageable), model);
		long count = count();
		return new PageImpl<T>(entities, pageable, count);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#find}
	 */
	public List<T> find(Iterable<QueryCriteria> queryCriterias) {
		Criteria criteria = getQueryFromQueryCriteria(queryCriterias);
		Query query = new Query();
		if (criteria != null){
			query.addCriteria(criteria);
		}
		return mongoOperations.find(query, model);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#find}
	 */
	public List<T> find(Iterable<QueryCriteria> queryCriterias, Sort sort) {
		Criteria criteria = getQueryFromQueryCriteria(queryCriterias);
		Query query = new Query();
		if (criteria != null){
			query.addCriteria(criteria);
		}
		return mongoOperations.find(query.with(sort), model);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#find}
	 */
	public Page<T> find(Iterable<QueryCriteria> queryCriterias, Pageable pageable) {
		Criteria criteria = getQueryFromQueryCriteria(queryCriterias);
		Query query = new Query();
		if (criteria != null){
			query.addCriteria(criteria);
		}
		List<T> entities = mongoOperations.find(query.with(pageable), model);
		long count = count(queryCriterias);
		return new PageImpl<T>(entities, pageable, count);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#insert}
	 */
	public <S extends T> S insert(S s) {
		mongoOperations.insert(s);
		return s;
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#insert}
	 */
	public <S extends T> List<S> insert(Iterable<S> entities) {
//		List<S> inserted = new ArrayList<>();
//		for (S entity: entities){
//			entity = this.insert(entity);
//			if (entity != null) inserted.add(entity);
//		}
//		return inserted;
		List<S> entityList = iterableToList(entities);
		mongoOperations.insertAll(entityList);
		return entityList;
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#update}
	 */
	public <S extends T> S update(S s) {
		if (exists(s.getId())){
			mongoOperations.save(s);
			return s;
		}
		return null;
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#update}
	 */
	public <S extends T> List<S> update(Iterable<S> entities) {
		List<S> updated = new ArrayList<>();
		for (S entity: entities){
			entity = this.update(entity);
			if (entity != null) updated.add(entity);
		}
		return updated;
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#count}
	 */
	public long count() {
		return mongoOperations.count(new Query(), model);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#count}
	 */
	public long count(Iterable<QueryCriteria> queryCriterias) {
		Criteria criteria = getQueryFromQueryCriteria(queryCriterias);
		Query query = new Query();
		if (criteria != null){
			query.addCriteria(criteria);
		}
		return mongoOperations.count(query, model);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#delete}
	 */
	public void delete(ID id) {
		mongoOperations.findAndRemove(Query.query(Criteria.where("_id").is(id)), model);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#deleteAll}
	 */
	public void deleteAll() {
		mongoOperations.remove(new Query(), model);
	}

	/**
	 * Drops the entity model collection.
	 */
	public void dropCollection(){
		mongoOperations.dropCollection(model);
	}

	/**
	 * Creates a collections for the entity model.
	 */
	public void createCollection(){
		mongoOperations.createCollection(model);
	}

	/**
	 * Creates an index on the desired field in the target collection.
	 * 
	 * @param field
	 * @param direction
	 * @param isUnique
	 * @param isSparse
	 */
	public void createIndex(String field, Sort.Direction direction, boolean isUnique, boolean isSparse){
		Integer dir = direction.equals(Sort.Direction.ASC) ? 1 : -1;
		DBObject index = new BasicDBObject(field, dir);
		DBObject options = new BasicDBObject();
		if (isSparse) options.put("sparse", true);
		if (isUnique) options.put("unique", true);
		DBCollection collection = mongoOperations.getCollection(mongoOperations.getCollectionName(model));
		collection.createIndex(index, options);
	}
	
	public void createIndex(String field, Sort.Direction direction, boolean isUnique){
		this.createIndex(field, direction, isUnique, false);
	}
	
	public void createIndex(String field, Sort.Direction direction){
		this.createIndex(field, direction, false, false);
	}
	
	public void createIndex(String field){
		this.createIndex(field, Sort.Direction.ASC, false, false);
	}

	/**
	 * Converts a collection of {@link org.oncoblocks.centromere.core.repository.QueryCriteria}
	 *  objects into Spring Data MongoDB {@link org.springframework.data.mongodb.core.query.Criteria}
	 *  objects, used to build a {@link org.springframework.data.mongodb.core.query.Query}.
	 * 
	 * @param queryCriterias list of query parameters to be converted.
	 * @return {@link org.springframework.data.mongodb.core.query.Criteria} representation of the input.
	 */
	protected Criteria getQueryFromQueryCriteria(Iterable<QueryCriteria> queryCriterias){
		List<Criteria> criteriaList = new ArrayList<>();
		for (QueryCriteria queryCriteria: queryCriterias){
			Criteria criteria = null;
			if (queryCriteria != null) {
				switch (queryCriteria.getEvaluation()) {
					case EQUALS:
						criteria = new Criteria(queryCriteria.getKey()).is(queryCriteria.getValue());
						break;
					case NOT_EQUALS:
						criteria = new Criteria(queryCriteria.getKey()).not().is(queryCriteria.getValue());
						break;
					case IN:
						criteria = new Criteria(queryCriteria.getKey()).in((Collection) queryCriteria.getValue());
						break;
					case NOT_IN:
						criteria = new Criteria(queryCriteria.getKey()).nin((Collection) queryCriteria.getValue());
						break;
					case IS_NULL:
						criteria = new Criteria(queryCriteria.getKey()).is(null);
						break;
					case NOT_NULL:
						criteria = new Criteria(queryCriteria.getKey()).not().is(null);
						break;
					case GREATER_THAN:
						criteria = new Criteria(queryCriteria.getKey()).gt(queryCriteria.getValue());
						break;
					case GREATER_THAN_EQUALS:
						criteria = new Criteria(queryCriteria.getKey()).gte(queryCriteria.getValue());
						break;
					case LESS_THAN:
						criteria = new Criteria(queryCriteria.getKey()).lt(queryCriteria.getValue());
						break;
					case LESS_THAN_EQUALS:
						criteria = new Criteria(queryCriteria.getKey()).lte(queryCriteria.getValue());
						break;
					case BETWEEN:
						criteria = new Criteria().andOperator(
								Criteria.where(queryCriteria.getKey()).gt(((List) queryCriteria.getValue()).get(0)),
								Criteria.where(queryCriteria.getKey()).lt(((List) queryCriteria.getValue()).get(1)));
						break;
					case OUTSIDE:
						criteria = new Criteria().orOperator(
								Criteria.where(queryCriteria.getKey()).lt(((List) queryCriteria.getValue()).get(0)),
								Criteria.where(queryCriteria.getKey()).gt(((List) queryCriteria.getValue()).get(1)));
						break;
					case BETWEEN_INCLUSIVE:
						criteria = new Criteria().andOperator(
								Criteria.where(queryCriteria.getKey()).gte(((List) queryCriteria.getValue()).get(0)),
								Criteria.where(queryCriteria.getKey()).lte(((List) queryCriteria.getValue()).get(1)));
						break;
					case OUTSIDE_INCLUSIVE:
						criteria = new Criteria().orOperator(
								Criteria.where(queryCriteria.getKey()).lte(((List) queryCriteria.getValue()).get(0)),
								Criteria.where(queryCriteria.getKey()).gte(((List) queryCriteria.getValue()).get(1)));
						break;
					default:
						criteria = new Criteria(queryCriteria.getKey()).is(queryCriteria.getValue());
				}
				criteriaList.add(criteria);
			}
		}

		return criteriaList.size() > 0 ? 
				new Criteria().andOperator(criteriaList.toArray(new Criteria[]{})) : null;
	}

	/**
	 * Converts a generic {@link Iterable} to a {@link List}.
	 * 
	 * @param iterable
	 * @param <S>
	 * @return
	 */
	private <S extends T> List<S> iterableToList(Iterable<S> iterable){
		List<S> list = new ArrayList<>();
		for (S entity: iterable){
			list.add(entity);
		}
		return list;
	}

	public MongoOperations getMongoOperations() {
		return mongoOperations;
	}

	public Class<T> getModel() {
		return model;
	}
}
