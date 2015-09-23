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

package org.oncoblocks.centromere.web.query;

import org.oncoblocks.centromere.core.repository.QueryCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link org.oncoblocks.centromere.web.query.QueryParameters} that assumes that subclass implementations will manually
 *   create {@link QueryCriteria} based on setter methods and will manually remap parameters based
 *   upon a predefined mapping set.  This implementation can be useful by taking advantage of Spring's 
 *   default controller model attribute parameter mapping, whereby a web service query parameter can 
 *   be defined as one type using a setter method, and the user input can be directly mapped to a 
 *   {@link QueryCriteria} instance, allowing for greater flexibility in query parameter definitions.
 * 
 * @author woemler
 */
public abstract class QueryCriteriaParameters implements
		org.oncoblocks.centromere.web.query.QueryParameters {

	private List<QueryCriteria> queryCriterias = new ArrayList<>();
	private Map<String,String> parameterMap;
	
	@Override 
	public List<QueryCriteria> getQueryCriteria() {
		return queryCriterias;
	}

	@Override 
	public String remapParameterName(String name) {
		if (this.getParameterMap().containsKey(name)){
			name = this.getParameterMap().get(name);
		}
		return name;
	}
	
	public void addQueryCriteria(QueryCriteria criteria){
		this.queryCriterias.add(criteria);
	}
	
	public abstract Map<String,String> getParameterMap();
	
}
