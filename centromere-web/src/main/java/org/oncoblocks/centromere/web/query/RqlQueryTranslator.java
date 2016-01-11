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

package org.oncoblocks.centromere.web.query;

import org.oncoblocks.centromere.core.repository.QueryCriteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author woemler
 */
public class RqlQueryTranslator {
	
	private Map<String,String> parameterMappings;

	public RqlQueryTranslator() {
		this.parameterMappings = new HashMap<>();
	}

	public RqlQueryTranslator(Map<String, String> parameterMappings) {
		this.parameterMappings = parameterMappings;
	}

	public String remapParameter(String param){
		if (parameterMappings.containsKey(param)){
			param = parameterMappings.get(param);
		}
		return param;
	}
	
	public List<QueryCriteria> convertRestQuery(String query){
		List<QueryCriteria> criteriaList = new ArrayList<>();
		String[] components = query.split(";");
		if (components.length > 0){
			for (String component: components) {
				QueryCriteria criteria = translateQueryComponent(component);
				if (criteria != null){
					criteriaList.add(criteria);
				}
			}
		}
		return criteriaList;
	}
	
	private QueryCriteria translateQueryComponent(String component){
		QueryCriteria criteria = null;
		Pattern pattern = Pattern.compile("([A-Za-z0-9._-]+)(==|!=|~=|>>|<<|>=|<=)(.+)");
		Matcher matcher = pattern.matcher(component);
		if (matcher.find()){
			if (matcher.groupCount() == 3){
				criteria = new QueryCriteria(matcher.group(0), matcher.group(2));
			}
		}
		return criteria;
	}
	
}
