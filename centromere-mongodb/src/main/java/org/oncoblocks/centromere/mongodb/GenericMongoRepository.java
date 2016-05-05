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

package org.oncoblocks.centromere.mongodb;


import com.google.common.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.oncoblocks.centromere.core.model.Model;
import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.oncoblocks.centromere.core.repository.RepositoryOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;

/**
 * Generic MongoDB implementation of {@link RepositoryOperations}.  Includes all of the methods 
 *   from Spring Data's {@link org.springframework.data.repository.PagingAndSortingRepository}, 
 *   as well as some additional methods that support dynamic query generation.
 * 
 * @author woemler
 */
public class GenericMongoRepository<T extends Model<ID>, ID extends Serializable> 
		implements RepositoryOperations<T, ID>  {
	
	private final MongoOperations mongoOperations;
	private final Class<T> model;
	private static final Logger logger = LoggerFactory.getLogger(GenericMongoRepository.class);

	/**
	 * Creates a new {@link GenericMongoRepository} instance.
	 * @param mongoOperations {@link MongoOperations}
	 * @param model class implementing {@link Model}
	 */
	public GenericMongoRepository(MongoOperations mongoOperations, Class<T> model) {
		Assert.notNull(mongoOperations);
		Assert.notNull(model);
		this.mongoOperations = mongoOperations;
		this.model = model;
	}

	public GenericMongoRepository(MongoOperations mongoOperations) {
		Assert.notNull(mongoOperations);
		this.mongoOperations = mongoOperations;
		TypeToken<T> typeToken = new TypeToken<T>(getClass()){};
		model = (Class<T>) typeToken.getRawType();
	}

	/**
	 * {@link RepositoryOperations#findOne}
	 */
	public T findOne(ID id) {
		return mongoOperations.findById(id, model);
	}

	/**
	 * {@link RepositoryOperations#exists}
	 */
	public boolean exists(ID id) {
		return mongoOperations.findById(id, model) != null;
	}

	/**
	 * {@link RepositoryOperations#findAll}
	 */
	public List<T> findAll() {
		return mongoOperations.findAll(model);
	}

	/**
	 * {@link RepositoryOperations#findAll}
	 */
	public List<T> findAll(Sort sort) {
		return mongoOperations.find(new Query().with(sort), model);
	}

	/**
	 * {@link org.springframework.data.repository.PagingAndSortingRepository#findAll(Iterable)}
	 */
	public List<T> findAll(Iterable<ID> iterable) {
		List<T> found = new ArrayList<>();
		for (ID id: iterable){
			found.add(this.findOne(id));
		}
		return found;
	}

	/**
	 * {@link RepositoryOperations#findAll}
	 */
	public Page<T> findAll(Pageable pageable) {
		List<T> entities = mongoOperations.find(new Query().with(pageable), model);
		long count = count();
		return new PageImpl<T>(entities, pageable, count);
	}

	/**
	 * {@link RepositoryOperations#findAll}
	 */
	public List<T> find(Iterable<QueryCriteria> queryCriterias) {
		Criteria criteria = MongoQueryUtils.getQueryFromQueryCriteria(queryCriterias);
		Query query = new Query();
		if (criteria != null){
			query.addCriteria(criteria);
		}
		return mongoOperations.find(query, model);
	}

	/**
	 * {@link RepositoryOperations#findAll}
	 */
	public List<T> find(Iterable<QueryCriteria> queryCriterias, Sort sort) {
		Criteria criteria = MongoQueryUtils.getQueryFromQueryCriteria(queryCriterias);
		Query query = new Query();
		if (criteria != null){
			query.addCriteria(criteria);
		}
		return mongoOperations.find(query.with(sort), model);
	}

	/**
	 * {@link RepositoryOperations#findAll}
	 */
	public Page<T> find(Iterable<QueryCriteria> queryCriterias, Pageable pageable) {
		Criteria criteria = MongoQueryUtils.getQueryFromQueryCriteria(queryCriterias);
		Query query = new Query();
		if (criteria != null){
			query.addCriteria(criteria);
		}
		List<T> entities = mongoOperations.find(query.with(pageable), model);
		long count = count(queryCriterias);
		return new PageImpl<T>(entities, pageable, count);
	}

	/**
	 * {@link RepositoryOperations#distinct(String)}
	 */
	public List<Object> distinct(String field){
		return mongoOperations.getCollection(mongoOperations.getCollectionName(model)).distinct(field);
	}

	/**
	 * {@link RepositoryOperations#distinct(String, Iterable)}
	 */
	public List<Object> distinct(String field, Iterable<QueryCriteria> queryCriterias){
		Criteria criteria = MongoQueryUtils.getQueryFromQueryCriteria(queryCriterias);
		Query query = new Query();
		if (criteria != null){
			query.addCriteria(criteria);
		}
		return mongoOperations.getCollection(mongoOperations.getCollectionName(model))
				.distinct(field, query.getQueryObject());
	}

	/**
	 * {@link RepositoryOperations#insert}
	 */
	public <S extends T> S insert(S s) {
		mongoOperations.insert(s);
		return s;
	}

	/**
	 * {@link RepositoryOperations#insert}
	 */
	public <S extends T> List<S> insert(Iterable<S> entities) {
		List<S> entityList = iterableToList(entities);
		mongoOperations.insertAll(entityList);
		return entityList;
	}

	/**
	 * {@link RepositoryOperations#update}
	 */
	public <S extends T> S update(S s) {
		if (exists(s.getId())){
			mongoOperations.save(s);
			return s;
		}
		return null;
	}

	/**
	 * {@link RepositoryOperations#update}
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
	 * {@link org.springframework.data.repository.PagingAndSortingRepository#save(Object)}	 */
	public <S extends T> S save(S s) {
		if (this.exists(s.getId())){
			return this.update(s);
		} else {
			return this.insert(s);
		}
	}

	/**
	 * {@link org.springframework.data.repository.PagingAndSortingRepository#save(Iterable)}
	 */
	public <S extends T> List<S> save(Iterable<S> iterable) {
		List<S> saved = new ArrayList<>();
		for (S s: iterable){
			saved.add(this.save(s));
		}
		return saved;
	}

	/**
	 * {@link RepositoryOperations#count}
	 */
	public long count() {
		return mongoOperations.count(new Query(), model);
	}

	/**
	 * {@link RepositoryOperations#count}
	 */
	public long count(Iterable<QueryCriteria> queryCriterias) {
		Criteria criteria = MongoQueryUtils.getQueryFromQueryCriteria(queryCriterias);
		Query query = new Query();
		if (criteria != null){
			query.addCriteria(criteria);
		}
		return mongoOperations.count(query, model);
	}

	/**
	 * {@link RepositoryOperations#delete}
	 */
	public void delete(ID id) {
		mongoOperations.findAndRemove(Query.query(Criteria.where("_id").is(id)), model);
	}

	/**
	 * {@link RepositoryOperations#deleteAll}
	 */
	public void deleteAll() {
		mongoOperations.remove(new Query(), model);
	}

	/**
	 * {@link org.springframework.data.repository.PagingAndSortingRepository#delete(Object)}
	 */
	public void delete(T t) {
		this.delete(t.getId());
	}

	/**
	 * {@link org.springframework.data.repository.PagingAndSortingRepository#delete(Iterable)}
	 */
	public void delete(Iterable<? extends T> iterable) {
		for (T t: iterable){
			this.delete(t.getId());
		}
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
