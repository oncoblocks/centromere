package org.oncoblocks.centromere.core.repository;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.oncoblocks.centromere.core.model.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generic MongoDB implementation of {@link org.oncoblocks.centromere.core.repository.RepositoryOperations}.
 * 
 * @author woemler
 */
public class GenericMongoRepository<T extends Model<ID>, ID extends Serializable> 
		implements RepositoryOperations<T, ID>  {
	
	protected final MongoOperations mongoOperations;
	protected final Class<T> model;

	/**
	 * Creates a new {@link GenericMongoRepository} instance.
	 * @param mongoOperations {@link org.springframework.data.mongodb.core.MongoOperations}
	 * @param model class implementing {@link org.oncoblocks.centromere.core.model.Model}
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
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#findAllSorted}
	 */
	public List<T> findAllSorted(Sort sort) {
		return mongoOperations.find(new Query().with(sort), model);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#findAllPaged}
	 */
	public Page<T> findAllPaged(Pageable pageable) {
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
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#findSorted}
	 */
	public List<T> findSorted(Iterable<QueryCriteria> queryCriterias, Sort sort) {
		Criteria criteria = getQueryFromQueryCriteria(queryCriterias);
		Query query = new Query();
		if (criteria != null){
			query.addCriteria(criteria);
		}
		return mongoOperations.find(query.with(sort), model);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#findPaged}
	 */
	public Page<T> findPaged(Iterable<QueryCriteria> queryCriterias, Pageable pageable) {
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
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#find}
	 */
	public List<T> find(T entityQuery) {
		DBObject dbObject = entityToJsonQuery(entityQuery);
		Query query = new BasicQuery(dbObject);
		return mongoOperations.find(query, model);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#findSorted}
	 */
	public List<T> findSorted(T entityQuery, Sort sort) {
		DBObject dbObject = entityToJsonQuery(entityQuery);
		Query query = new BasicQuery(dbObject);
		return mongoOperations.find(query.with(sort), model);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#findPaged}
	 */
	public Page<T> findPaged(T entityQuery, Pageable pageable) {
		DBObject dbObject = entityToJsonQuery(entityQuery);
		Query query = new BasicQuery(dbObject);
		List<T> entities = mongoOperations.find(query.with(pageable), model);
		long count = count(entityQuery);
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
		List<S> inserted = new ArrayList<>();
		for (S entity: entities){
			entity = this.insert(entity);
			if (entity != null) inserted.add(entity);
		}
		return inserted;
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
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#count}
	 */
	public long count(T entityQuery) {
		DBObject dbObject = entityToJsonQuery(entityQuery);
		Query query = new BasicQuery(dbObject);
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
	 * Converts a collection of {@link org.oncoblocks.centromere.core.repository.QueryCriteria}
	 *  objects into Spring Data MongoDB {@link org.springframework.data.mongodb.core.query.Criteria}
	 *  objects, used to build a {@link org.springframework.data.mongodb.core.query.Query}.
	 * 
	 * @param queryCriterias list of query parameters to be converted.
	 * @return {@link org.springframework.data.mongodb.core.query.Criteria} representation of the input.
	 */
	protected Criteria getQueryFromQueryCriteria(Iterable<QueryCriteria> queryCriterias){
		boolean flag = false;
		Criteria criteria = null;
		for (QueryCriteria queryCriteria: queryCriterias){
			if (queryCriteria != null) {
				if (flag) {
					criteria = criteria.and(queryCriteria.getKey());
				} else {
					criteria = Criteria.where(queryCriteria.getKey());
				}
				flag = true;
				switch (queryCriteria.getEvaluation()) {
					case EQUALS:
						criteria.is(queryCriteria.getValue());
						break;
					case NOT_EQUALS:
						criteria.not().is(queryCriteria.getValue());
						break;
					case IN:
						criteria.in((Object[]) queryCriteria.getValue());
						break;
					case NOT_IN:
						criteria.nin((Object[]) queryCriteria.getValue());
						break;
					case IS_NULL:
						criteria.is(null);
						break;
					case NOT_NULL:
						criteria.not().is(null);
						break;
					case GREATER_THAN:
						criteria.gt(queryCriteria.getValue());
						break;
					case GREATER_THAN_EQUALS:
						criteria.gte(queryCriteria.getValue());
						break;
					case LESS_THAN:
						criteria.lt(queryCriteria.getValue());
						break;
					case LESS_THAN_EQUALS:
						criteria.lte(queryCriteria.getValue());
						break;
					default:
						criteria.is(queryCriteria.getValue());
				}
			}
		}

		return criteria;
	}

	/**
	 * Converts a partial entity object into a dot-notation-formatted {@link com.mongodb.DBObject},
	 *   to be used in building a {@link org.springframework.data.mongodb.core.query.BasicQuery}.
	 * 
	 * @param entity partial entity object
	 * @return {@link com.mongodb.BasicDBObject} 
	 */
	protected DBObject entityToJsonQuery(T entity){
		MongoConverter converter = mongoOperations.getConverter();
		DBObject dbObject = new BasicDBObject();
		converter.write(entity, dbObject);
		dbObject.removeField("_class");
		if (dbObject.get("_id") == null) dbObject.removeField("_id");
		JsonDoterizer doterizer = new JsonDoterizer(dbObject.toMap());
		return new BasicDBObject(doterizer.getNewMap());
	}

	/**
	 * Convenience class for converting {@code Map} objects into dot-notation maps for MongoDB queries.
	 */
	public static class JsonDoterizer {

		private Map<String,Object> newMap;

		public JsonDoterizer(Map<String,Object> oldMap) {
			this.newMap = new HashMap<>();
			for (Map.Entry entry: oldMap.entrySet()){
				Map<String,Object> newEntry = recurse(entry.getValue(), (String) entry.getKey());
				if (newEntry != null) newMap.putAll(newEntry);
			}
		}

		private Map<String,Object> recurse(Object obj, String prefix){
			Map<String,Object> entry = new HashMap<>();
			if (obj instanceof Iterable){
				for (Object o: (Iterable) obj){
					entry.putAll(recurse(o, prefix));
				}
				return entry;
			} else if (obj instanceof Map){
				for (Map.Entry o: ((Map<String,Object>) obj).entrySet()){
					entry.putAll(recurse(o.getValue(), prefix + "." + o.getKey()));
				}
				return entry;
			} else {
				entry.put(prefix, obj);
				return entry;
			}

		}

		public Map<String,Object> getNewMap(){ return this.newMap; }

	}
	
}
