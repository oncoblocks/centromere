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

package org.oncoblocks.centromere.mongodb;

import org.oncoblocks.centromere.core.model.Model;
import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.oncoblocks.centromere.core.repository.RepositoryOperations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link RepositoryOperations} using Spring Data's repository bean factory for
 *   instantiation.  All of the methods that are included in {@link org.springframework.data.repository.PagingAndSortingRepository}
 *   are handled by {@link SimpleMongoRepository}, while the remainder are implemented here.
 *
 * @author woemler
 * @since 0.4.1
 */
public class CentromereMongoRepository<T extends Model<ID>, ID extends Serializable> 
		extends SimpleMongoRepository<T, ID> 
		implements RepositoryOperations<T, ID> {

	private final MongoOperations mongoOperations;
	private final MongoEntityInformation<T, ID> metadata;
	
	public CentromereMongoRepository(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations) {
		super(metadata, mongoOperations);
		this.mongoOperations = mongoOperations;
		this.metadata = metadata;
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
		return mongoOperations.find(query, metadata.getJavaType());
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
		return mongoOperations.find(query.with(sort), metadata.getJavaType());
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
		List<T> entities = mongoOperations.find(query.with(pageable), metadata.getJavaType());
		long count = count(queryCriterias);
		return new PageImpl<>(entities, pageable, count);
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
		return mongoOperations.count(query, metadata.getJavaType());
	}

	/**
	 * {@link RepositoryOperations#distinct(String)}
	 */
	public List<Object> distinct(String field){
		return mongoOperations
				.getCollection(mongoOperations.getCollectionName(metadata.getJavaType()))
				.distinct(field);
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
		return mongoOperations.getCollection(mongoOperations.getCollectionName(metadata.getJavaType()))
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
	
}
