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

import com.nurkiewicz.jdbcrepository.MissingRowUnmapper;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import org.oncoblocks.centromere.core.model.Model;
import org.oncoblocks.centromere.core.repository.sqlbuilder.ComplexTableDescription;
import org.oncoblocks.centromere.core.repository.sqlbuilder.Condition;
import org.oncoblocks.centromere.core.repository.sqlbuilder.SqlBuilder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static org.oncoblocks.centromere.core.repository.sqlbuilder.SqlBuilder.*;

/**
 * Generic implementation of {@link org.oncoblocks.centromere.core.repository.RepositoryOperations}, 
 *   designed for use with a {@link org.oncoblocks.centromere.core.repository.sqlbuilder.SqlBuilder}
 *   that generates SQL queries to be executed in a {@link org.springframework.jdbc.core.JdbcTemplate}.
 * 
 * @author woemler
 */
public class GenericJdbcRepository<T extends Model<ID>, ID extends Serializable>  implements RepositoryOperations<T, ID> {

	private JdbcTemplate jdbcTemplate;
	private ComplexTableDescription tableDescription;
	private RowMapper<T> rowMapper;
	private RowUnmapper<T> rowUnmapper;

	/**
	 * Creates a new repository instance using a {@link javax.sql.DataSource} to generate a new 
	 *   {@link org.springframework.jdbc.core.JdbcTemplate}, a {@link org.oncoblocks.centromere.core.repository.sqlbuilder.ComplexTableDescription} 
	 *   used to create a {@link org.oncoblocks.centromere.core.repository.sqlbuilder.SqlBuilder} for
	 *   query generation, a {@link org.springframework.jdbc.core.RowMapper} for mapping database records
	 *   to {@link Model} objects, and a {@link com.nurkiewicz.jdbcrepository.RowUnmapper}
	 *   for mapping model objects back to database records.
	 * 
	 * @param dataSource {@link javax.sql.DataSource}
	 * @param tableDescription {@link org.oncoblocks.centromere.core.repository.sqlbuilder.ComplexTableDescription}
	 * @param rowMapper {@link org.springframework.jdbc.core.RowMapper}
	 * @param rowUnmapper {@link com.nurkiewicz.jdbcrepository.RowUnmapper}
	 */
	public GenericJdbcRepository(DataSource dataSource,
			ComplexTableDescription tableDescription, RowMapper<T> rowMapper,
			RowUnmapper<T> rowUnmapper) {
		
		Assert.notNull(dataSource);
		Assert.notNull(tableDescription);
		Assert.notNull(rowMapper);
		Assert.notNull(rowUnmapper);
		
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.tableDescription = tableDescription;
		this.rowMapper = rowMapper;
		this.rowUnmapper = rowUnmapper;
		
	}

	/**
	 * Creates a new repository instance using the default constructor, but with a {@link com.nurkiewicz.jdbcrepository.MissingRowUnmapper}
	 *   to enforce read-only functionality.
	 * 
	 * @param dataSource {@link javax.sql.DataSource}
	 * @param tableDescription {@link org.oncoblocks.centromere.core.repository.sqlbuilder.ComplexTableDescription}
	 * @param rowMapper {@link org.springframework.jdbc.core.RowMapper}
	 */
	public GenericJdbcRepository(DataSource dataSource,
			ComplexTableDescription tableDescription, RowMapper<T> rowMapper) {
		this(dataSource, tableDescription, rowMapper, new MissingRowUnmapper<T>());
	}

	/**
	 * Creates a new {@link org.oncoblocks.centromere.core.repository.sqlbuilder.SqlBuilder} instance.
	 * 
	 * @return
	 */
	protected SqlBuilder getSqlBuilder(){
		return new SqlBuilder(tableDescription);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#findById}
	 */
	public T findById(ID id) {
		SqlBuilder sqlBuilder = getSqlBuilder();
		List<Object> identifiers = idToObjectList(id);
		List<String> idColumns = tableDescription.getIdColumns();
		List<Condition> conditions = new ArrayList<>();
		for (int i = 0; i < identifiers.size(); i++){
			conditions.add(equal(idColumns.get(i), identifiers.get(i)));
		}
		sqlBuilder.where(and(conditions.toArray(new Condition[]{})));
		String sql = sqlBuilder.toSql();
		Object[] parameters = sqlBuilder.getQueryParameterValues().toArray();

		try {
			return jdbcTemplate.queryForObject(sql, parameters, rowMapper);
		} catch (EmptyResultDataAccessException e){
			return null;
		}
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#exists}
	 */
	public boolean exists(ID id) {
		return findById(id) != null;
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#findAll}
	 */
	public List<T> findAll() {
		return jdbcTemplate.query(getSqlBuilder().toSql(), rowMapper);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#findAllSorted}
	 */
	public List<T> findAllSorted(Sort sort) {
		SqlBuilder sqlBuilder = getSqlBuilder().orderBy(sort);
		return jdbcTemplate.query(sqlBuilder.toSql(), rowMapper);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#findAllPaged}
	 */
	public Page<T> findAllPaged(Pageable pageable) {
		SqlBuilder sqlBuilder = getSqlBuilder();
		if (pageable.getSort() != null){
			sqlBuilder.orderBy(pageable.getSort());
		}
		sqlBuilder.limit(pageable);
		List<T> objects = jdbcTemplate.query(sqlBuilder.toSql(), rowMapper);
		Long rowCount = count();
		return new PageImpl<>(objects, pageable, rowCount);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#count}
	 */
	public long count() {
		String selectAll = getSqlBuilder().toSql();
		SqlBuilder sqlBuilder = new SqlBuilder()
				.select("count(*)")
				.from("(" + selectAll + ") a");
		return jdbcTemplate.queryForObject(sqlBuilder.toSql(), Long.class);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#find}
	 */
	public List<T> find(Iterable<QueryCriteria> queryCriterias) {
		SqlBuilder sqlBuilder = getSqlBuilder();
		List<Condition> conditionList = new ArrayList<>();
		for (QueryCriteria criteria: queryCriterias){
			if (criteria != null) conditionList.add(getConditionFromQueryCriteria(criteria));
		}
		sqlBuilder.where(and(conditionList.toArray(new Condition[] {})));
		return jdbcTemplate.query(sqlBuilder.toSql(), sqlBuilder.getQueryParameterValues().toArray(),
				rowMapper);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#findSorted}
	 */
	public List<T> findSorted(Iterable<QueryCriteria> queryCriterias, Sort sort) {
		SqlBuilder sqlBuilder = getSqlBuilder();
		List<Condition> conditionList = new ArrayList<>();
		for (QueryCriteria criteria: queryCriterias){
			if (criteria != null) conditionList.add(getConditionFromQueryCriteria(criteria));
		}
		sqlBuilder
				.where(and(conditionList.toArray(new Condition[]{})))
				.orderBy(sort);
		return jdbcTemplate.query(sqlBuilder.toSql(), sqlBuilder.getQueryParameterValues().toArray(), rowMapper);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#findPaged}
	 */
	public Page<T> findPaged(Iterable<QueryCriteria> queryCriterias, Pageable pageable) {
		SqlBuilder sqlBuilder = getSqlBuilder();
		List<Condition> conditionList = new ArrayList<>();
		for (QueryCriteria criteria: queryCriterias){
			if (criteria != null) conditionList.add(getConditionFromQueryCriteria(criteria));
		}
		sqlBuilder.where(and(conditionList.toArray(new Condition[] {})));
		if (pageable.getSort() != null) sqlBuilder.orderBy(pageable.getSort());
		sqlBuilder.limit(pageable);
		List<T> objects = jdbcTemplate.query(sqlBuilder.toSql(),
				sqlBuilder.getQueryParameterValues().toArray(), rowMapper);
		Long rowCount = count(queryCriterias);
		return new PageImpl<>(objects, pageable, rowCount);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#count}
	 */
	public long count(Iterable<QueryCriteria> queryCriterias) {
		SqlBuilder sqlBuilder = getSqlBuilder();
		List<Condition> conditionList = new ArrayList<>();
		for (QueryCriteria criteria: queryCriterias){
			if (criteria != null) conditionList.add(getConditionFromQueryCriteria(criteria));
		}
		sqlBuilder.where(and(conditionList.toArray(new Condition[] {})));
		String selectWhere = sqlBuilder.toSql();
		SqlBuilder sqlBuilder2 = new SqlBuilder()
				.select("count(*)")
				.from("(" + selectWhere + ") a");
		return jdbcTemplate.queryForObject(sqlBuilder2.toSql(), sqlBuilder.getQueryParameterValues().toArray(), Long.class);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#insert}
	 */
	public <S extends T> S insert(S entity) {
		SqlBuilder sqlBuilder = getSqlBuilder();
		Map<String,Object> mappings = rowUnmapper.mapColumns(entity);
		final String sql = sqlBuilder.insert(mappings).toSql();
		final Object[] values = sqlBuilder.getQueryParameterValues().toArray();  
		final String[] columns = mappings.keySet().toArray(new String[]{});
		sqlBuilder.insert(mappings);
		S created = null;

		if (entity.getId() == null){
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override public PreparedStatement createPreparedStatement(Connection connection)
						throws SQLException {
					PreparedStatement preparedStatement = connection.prepareStatement(sql, columns);
					for (int i = 0; i < values.length; i++){
						preparedStatement.setObject(i+1, values[i]);
					}
					return preparedStatement;
				}
			}, keyHolder);
			ID newId = (ID) keyHolder.getKey();
			created = (S) findById(newId);
		} else {
			jdbcTemplate.update(sql, values);
			created = entity;
		}

		return created;
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#insert}
	 */
	public <S extends T> List<S> insert(Iterable<S> entities) {
		List<S> insertedList = new ArrayList<>();
		for (S entity: entities){
			insertedList.add(insert(entity));
		}
		return insertedList;
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#update}
	 */
	public <S extends T> S update(S entity) {
		Map<String,Object> mappings = rowUnmapper.mapColumns(entity);
		List<String> idColumns = tableDescription.getIdColumns();
		List<Object> identifiers = idToObjectList(entity.getId());
		SqlBuilder sqlBuilder = getSqlBuilder();
		sqlBuilder.update(mappings);
		List<Condition> conditions = new ArrayList<>();
		for (int i = 0; i < identifiers.size(); i++){
			conditions.add(equal(idColumns.get(i), identifiers.get(i)));
		}
		sqlBuilder.where(and(conditions.toArray(new Condition[] {})));
		jdbcTemplate.update(sqlBuilder.toSql(), sqlBuilder.getQueryParameterValues().toArray());
		return entity;
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#update}
	 */
	public <S extends T> List<S> update(Iterable<S> entities) {
		List<S> updatedList = new ArrayList<>();
		for (S entity: entities){
			updatedList.add(update(entity));
		}
		return updatedList;
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#delete}
	 */
	public void delete(ID id) {
		SqlBuilder sqlBuilder = getSqlBuilder();
		List<Object> identifiers = idToObjectList(id);
		List<String> idColumns = tableDescription.getIdColumns();
		sqlBuilder.delete();
		for (int i = 0; i < identifiers.size(); i++){
			sqlBuilder.where(equal(idColumns.get(i), identifiers.get(i)));
		}
		String sql = sqlBuilder.toSql();
		Object[] parameters = sqlBuilder.getQueryParameterValues().toArray();
		jdbcTemplate.update(sql, parameters);
	}

	/**
	 * {@link org.oncoblocks.centromere.core.repository.RepositoryOperations#deleteAll}
	 */
	public void deleteAll() {
		jdbcTemplate.execute("DELETE FROM " + tableDescription.getTableName());
	}

	/**
	 * Truncates the target table, dropping all records.
	 */
	public void truncateTable() {
		jdbcTemplate.execute("TRUNCATE TABLE " + tableDescription.getTableName());
	}

	/**
	 * Converts an {@code ID} instance into a collection of objects.
	 * 
	 * @param id
	 * @return
	 */
	protected static <ID> List<Object> idToObjectList(ID id) {
		if (id instanceof Object[])
			return Arrays.asList((Object[]) id);
		else
			return Collections.<Object>singletonList(id);
	}

	/**
	 * Creates a {@link org.oncoblocks.centromere.core.repository.sqlbuilder.Condition} instance from
	 *   a {@link org.oncoblocks.centromere.core.repository.QueryCriteria} search parameter for use
	 *   in SQL query generation.
	 * 
	 * @param criteria {@link org.oncoblocks.centromere.core.repository.QueryCriteria}
	 * @return {@link org.oncoblocks.centromere.core.repository.sqlbuilder.Condition}
	 */
	protected Condition getConditionFromQueryCriteria(QueryCriteria criteria){
		switch (criteria.getEvaluation()){
			case EQUALS:
				return equal(criteria.getKey(), criteria.getValue());
			case NOT_EQUALS:
				return notEqual(criteria.getKey(), criteria.getValue());
			case IN :
				return in(criteria.getKey(), (Object[]) criteria.getValue());
			case NOT_IN:
				return notIn(criteria.getKey(), (Object[]) criteria.getValue());
			case IS_NULL:
				return isNull(criteria.getKey());
			case NOT_NULL:
				return notNull(criteria.getKey());
			default:
				return equal(criteria.getKey(), criteria.getValue());
		}
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public ComplexTableDescription getTableDescription() {
		return tableDescription;
	}

	public RowMapper<T> getRowMapper() {
		return rowMapper;
	}

	public RowUnmapper<T> getRowUnmapper() {
		return rowUnmapper;
	}
}
