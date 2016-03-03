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

package org.oncoblocks.centromere.core.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * TODO
 * 
 * QueryCriteriaBuilder.where("symbol").is("AKT1").and("signal").between(3.0, 1.2).build();
 * 
 * 
 * @author woemler
 */
public class QueryCriteriaBuilder {

	private String field;
	private List<QueryCriteria> criterias = new ArrayList<>();
	
	public QueryCriteriaBuilder(String field) {
		this.field = field;
	}
	
	public QueryCriteriaBuilder(){ }

	public List<QueryCriteria> build(){
		return criterias;
	}
	
	/* Builder Methods */

	public static QueryCriteriaBuilder where(String field){
		return new QueryCriteriaBuilder(field);
	}
	
	public QueryCriteriaBuilder and(String field){
		this.field = field;
		return this;
	}
	
	public QueryCriteriaBuilder is(Object value){
		criterias.add(new QueryCriteria(this.field, value, Evaluation.EQUALS));
		return this;
	}
	
	public QueryCriteriaBuilder in(Collection<Object> values){
		criterias.add(new QueryCriteria(this.field, values, Evaluation.IN));
		return this;
	}
	
	public QueryCriteriaBuilder isNot(Object value){
		criterias.add(new QueryCriteria(this.field, value, Evaluation.NOT_EQUALS));
		return this;
	}

	public QueryCriteriaBuilder notIn(Collection<Object> values){
		criterias.add(new QueryCriteria(this.field, values, Evaluation.NOT_IN));
		return this;
	}

	public QueryCriteriaBuilder like(String value){
		criterias.add(new QueryCriteria(this.field, value, Evaluation.LIKE));
		return this;
	}

	public QueryCriteriaBuilder notLike(String value){
		criterias.add(new QueryCriteria(this.field, value, Evaluation.NOT_LIKE));
		return this;
	}

	public QueryCriteriaBuilder greaterThan(Object value){
		criterias.add(new QueryCriteria(this.field, value, Evaluation.GREATER_THAN));
		return this;
	}

	public QueryCriteriaBuilder greaterThanOrEqual(Object value){
		criterias.add(new QueryCriteria(this.field, value, Evaluation.GREATER_THAN_EQUALS));
		return this;
	}

	public QueryCriteriaBuilder lessThan(Object value){
		criterias.add(new QueryCriteria(this.field, value, Evaluation.LESS_THAN));
		return this;
	}

	public QueryCriteriaBuilder lessThanOrEqual(Object value){
		criterias.add(new QueryCriteria(this.field, value, Evaluation.LESS_THAN_EQUALS));
		return this;
	}

	public QueryCriteriaBuilder between(Object lowValue, Object highValue){
		criterias.add(new QueryCriteria(this.field, Arrays.asList(lowValue, highValue), Evaluation.BETWEEN));
		return this;
	}

	public QueryCriteriaBuilder betweenIncluding(Object lowValue, Object highValue){
		criterias.add(new QueryCriteria(this.field, Arrays.asList(lowValue, highValue), Evaluation.BETWEEN_INCLUSIVE));
		return this;
	}

	public QueryCriteriaBuilder outside(Object lowValue, Object highValue){
		criterias.add(new QueryCriteria(this.field, Arrays.asList(lowValue, highValue), Evaluation.OUTSIDE));
		return this;
	}

	public QueryCriteriaBuilder outsideIncluding(Object lowValue, Object highValue){
		criterias.add(new QueryCriteria(this.field, Arrays.asList(lowValue, highValue), Evaluation.OUTSIDE_INCLUSIVE));
		return this;
	}

	public QueryCriteriaBuilder isNull(Boolean value){
		criterias.add(new QueryCriteria(this.field, value, Evaluation.IS_NULL));
		return this;
	}

	public QueryCriteriaBuilder notNull(Boolean value){
		criterias.add(new QueryCriteria(this.field, value, Evaluation.NOT_NULL));
		return this;
	}

	public QueryCriteriaBuilder isTrue(Boolean value){
		criterias.add(new QueryCriteria(this.field, value, Evaluation.IS_TRUE));
		return this;
	}

	public QueryCriteriaBuilder isFalse(Boolean value){
		criterias.add(new QueryCriteria(this.field, value, Evaluation.IS_FALSE));
		return this;
	}
	
}
