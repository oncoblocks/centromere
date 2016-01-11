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

package org.oncoblocks.centromere.sql.sqlbuilder;

import org.oncoblocks.centromere.core.repository.Evaluation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.swing.*;
import java.util.*;

/**
 * Uses basic table and query definitions set in {@link ComplexTableDescription} to generate SQL
 *   queries that can be executed in a JdbcTemplate, or by another SQL execution method.
 * 
 * @author woemler
 * 
 * Example usage:
 * 
 * SqlBuilder sqlBuilder = new SqlBuilder(tableDescription);
 * String sql = sqlBuilder.select("*")
 *   .from("users")
 *   .where(
 *     and(
 *       eq("category", "active"),
 *       notEq("name", "Joe")
 *     )
 *   )
 *   .groupBy("name", "address")
 *   .orderBy(
 *     new Sort(
 *       new Sort.Order(Sort.Direction.ASC, "name"), 
 *       new Sort.Order(Sort.Direction.DESC, "address")
 *     )
 *   )
 *   .limit(10, 50)
 *   .toSql();
 *   
 *  Should produce the string:
 *  
 *   SELECT * FROM users 
 *   WHERE category = ? AND name != ? 
 *   GROUP BY name, address
 *   ORDER BY name ASC, address DESC
 *   LIMIT 10, 50
 *   
 *  List<Object> parameters = sqlBuilder.getQueryParameterValues;
 * 
 *  Should return a list with { 'active', 'Joe' }
 * 
 */
public class SqlBuilder {
	
	private String tableName;
	private String selectClause = "*";
	private String fromClause = "";
	private String whereClause = "";
	private String groupByClause = "";
	private String orderByClause = "";
	private String limitClause = "";
	private String insertClause = "";
	private String updateClause = "";
	private enum Mode { SELECT, INSERT, UPDATE, DELETE };
	private Mode mode = Mode.SELECT;
	
	private List<String> idColumns;
	private LinkedHashMap<String, SortOrder> sorts;
	private List<Object> queryParameterValues;

	final static Logger logger = LoggerFactory.getLogger(SqlBuilder.class);
	
	public SqlBuilder(ComplexTableDescription tableDescription){
		
		this.tableName = tableDescription.getTableName();
		this.selectClause = tableDescription.getSelectClause();
		this.fromClause = tableDescription.getFromClause();
		this.groupByClause = tableDescription.getGroupByClause();
		this.idColumns = tableDescription.getIdColumns();
		this.queryParameterValues = new ArrayList<>();
		
		this.sorts = new LinkedHashMap<>();
		
	}
	
	public SqlBuilder(String tableName){
		this.tableName = tableName;
		this.fromClause = tableName;
	}
	
	public SqlBuilder(){ }
	
	//// SELECT

	public String getSelectClause() {
		return "SELECT " + selectClause;
	}
	
	public void setSelectClause(String selectClause){
		this.selectClause = selectClause;
	}

	public SqlBuilder select(String clause){
		this.selectClause = clause;
		return this;
	}

	//// INSERT

	public SqlBuilder insert(Map<String,Object> parameters){
		StringBuilder columnString = new StringBuilder(" (");
		StringBuilder valueString = new StringBuilder(" VALUES (");
		boolean flag = false;
		for (Map.Entry param: parameters.entrySet()){
			if (flag){
				columnString.append(",");
				valueString.append(",");
			}
			flag = true;
			columnString.append(param.getKey());
			valueString.append("?");
			queryParameterValues.add(param.getValue());
		}
		columnString.append(") ");
		valueString.append(") ");
		insertClause = tableName + columnString.toString() + valueString.toString(); 
		mode = Mode.INSERT;
		return this;
	}
	
	public String getInsertClause(){
		return "INSERT INTO " + insertClause;
	}
	
	//// UPDATE

	public SqlBuilder update(Map<String,Object> parameters){
		StringBuilder stringBuilder = new StringBuilder();
		boolean flag = false;
		for (Map.Entry param: parameters.entrySet()){
			if (flag){
				stringBuilder.append(", ");
			}
			flag = true;
			stringBuilder.append(param.getKey() + " = ?");
			queryParameterValues.add(param.getValue());
		}
		updateClause = tableName + " SET " + stringBuilder.toString();
		mode = Mode.UPDATE;
		return this;
	}
	
	public String getUpdateClause(){
		return "UPDATE " + updateClause;
	}

	//// DELETE

	public SqlBuilder delete(){
		fromClause = " " + tableName + " ";
		mode = Mode.DELETE;
		return this;
	}
	
	//// FROM

	public String getFromClause() {
		return " FROM " + fromClause;
	}
	
	public void setFromClause(String fromClause){
		this.fromClause = fromClause;
	}

	public SqlBuilder from(String clause) {
		this.fromClause =  clause;
		return this;
	}
	
	//// WHERE
	
	public String getWhereClause(){
		return whereClause.equals("") ? whereClause : " WHERE " + whereClause;
	}
	
	public void setWhereClause(String whereClause){
		this.whereClause = whereClause;
	}
	
	public SqlBuilder where(Conditions conditions){
		whereClause = conditions.getSql();
		queryParameterValues.addAll(conditions.getValues());
		return this;
	}
	
	public SqlBuilder where(Condition condition){
		whereClause = condition.getClause();
		queryParameterValues.add(condition.getValue());
		return this;
	}
	
	public static Conditions and(Condition... conditions){
		return new Conditions(Conditions.Operation.AND, conditions);
	}

	public static Conditions and(Conditions... conditions){
		return new Conditions(Conditions.Operation.AND, conditions);
	}
	
	public static Conditions or(Condition... conditions){
		return new Conditions(Conditions.Operation.OR, conditions);
	}

	public static Conditions or(Conditions... conditions){
		return new Conditions(Conditions.Operation.OR, conditions);
	}
	
	public static Condition equal(String column, Object value){
		return new Condition(column, value, Evaluation.EQUALS);
	}
	
	public static Condition notEqual(String column, Object value){
		return new Condition(column, value, Evaluation.NOT_EQUALS);
	}

	public static Condition in(String column, Object[] value){
		return new Condition(column, value, Evaluation.IN);
	}

	public static Condition notIn(String column, Object[] value){
		return new Condition(column, value, Evaluation.NOT_IN);
	}

	public static Condition isNull(String column){
		return new Condition(column, null, Evaluation.IS_NULL);
	}
	
	public static Condition notNull(String column){
		return new Condition(column, null, Evaluation.NOT_NULL);
	}
	
	//// GROUP BY
	
	public String getGroupByClause(){
		return groupByClause.equals("") ? groupByClause : " GROUP BY " + groupByClause;
	}
	
	public void setGroupByClause(String groupByClause){
		this.groupByClause = groupByClause;
	}
	
	public SqlBuilder groupBy(String... columns){
		StringBuilder builder = new StringBuilder();
		boolean flag = false;
		for (String column: columns){
			if (flag){
				builder.append(", ");
			}
			flag = true;
			builder.append(column);
		}
		groupByClause = builder.toString();
		return this;
	}

	//// ORDER BY
	
	public String getOrderByClause(){
		return orderByClause.equals("") ? orderByClause : " ORDER BY " + orderByClause;
	}
	
	public void setOrderByClause(String orderByClause){
		this.orderByClause = orderByClause;
	}
	
	public SqlBuilder orderBy(Sort sort){
		StringBuilder builder = new StringBuilder();
		boolean flag = false;
		Iterator<Sort.Order> orders = sort.iterator();
		while (orders.hasNext()){
			Sort.Order order = orders.next();
			if (flag){
				builder.append(", ");
			}
			flag = true;
			builder.append(order.getProperty() + " " + order.getDirection().toString());
		}
		orderByClause = builder.toString();
		return this;
	}
	
	public SqlBuilder orderBy(Sort.Order... orders){
		StringBuilder builder = new StringBuilder();
		boolean flag = false;
		for (Sort.Order order: orders){
			if (flag){
				builder.append(", ");
			}
			flag = true;
			builder.append(order.getProperty() + " " + order.getDirection().toString());
		}
		orderByClause = builder.toString();
		return this;
	}
	
	public SqlBuilder orderBy(String column, Sort.Direction direction){
		orderByClause = column + " " + direction.toString();
		return this;
	}

	public SqlBuilder orderBy(String column){
		orderByClause = column + " ASC";
		return this;
	}
	
	//// LIMIT
	
	public String getLimitClause(){
		return limitClause.equals("") ? limitClause : " LIMIT " + limitClause;
	}
	
	public void setLimitClause(String limitClause){
		this.limitClause = limitClause;
	}
	
	public SqlBuilder limit(Integer offset, Integer count){
		limitClause = offset.toString() + "," + count.toString();
		return this;
	}
	
	public SqlBuilder limit(Integer count){
		limitClause = count.toString();
		return this;
	}
	
	public SqlBuilder limit(Pageable pageable){
		limitClause = String.valueOf(pageable.getOffset()) + "," + String.valueOf(pageable.getPageSize());
		return this;
	}
	
	public String getDeleteClause(){
		return "DELETE ";
	}
	
	//// Output
	
	public String toSql(){
		String sql;
		switch (mode){
			case SELECT:
				sql = getSelectClause() + getFromClause() + getWhereClause() + getGroupByClause() + getOrderByClause() + getLimitClause();
				break;
			case INSERT:
				sql = getInsertClause();
				break;
			case UPDATE:
				sql = getUpdateClause() + getWhereClause();
				break;
			case DELETE:
				sql = getDeleteClause() + getFromClause() + getWhereClause();
				break;
			default:
				sql = getSelectClause() + getFromClause() + getWhereClause() + getGroupByClause() + getOrderByClause() + getLimitClause();
		}
		//logger.debug(sql);
		return sql;
	}
	
	public List<Object> getQueryParameterValues(){
		return queryParameterValues;
	}
	
}
