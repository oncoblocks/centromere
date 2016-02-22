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
import java.util.List;

/**
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

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	
	public List<QueryCriteria> build(){
		return criterias;
	}
	
	/* Builder Methods */

	public static QueryCriteriaBuilder where(String field){
		return new QueryCriteriaBuilder(field);
	}
	
	public void and(String field){
		this.field = field;
	}
	
	public void is(Object value){
		criterias.add(new QueryCriteria(this.field, value, Evaluation.EQUALS));
	}
	
	
}
