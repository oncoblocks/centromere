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

package org.oncoblocks.centromere.core.web.query;

import org.oncoblocks.centromere.core.repository.Evaluation;
import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Base class for capturing web service query string parameters.  Allows for simple and strict query
 *   parameter definitions, and simpler API documentation via Swagger inspection.
 * 
 * @author woemler
 */
public class QueryParameters {
	
	private Set<String> fields;
	private Set<String> exclude;

	public Set<String> getIncludedFields() {
		return fields;
	}

	public void setFields(Set<String> fields) {
		this.fields = fields;
	}

	public Set<String> getExcludedFields() {
		return exclude;
	}

	public void setExclude(Set<String> exclude) {
		this.exclude = exclude;
	}

	/**
	 * Converts a {@link QueryParameters} object into
	 *   a list of {@link org.oncoblocks.centromere.core.repository.QueryCriteria}, to be passed to 
	 *   the repository layer as query parameters.
	 *
	 * @param queryParameters mapped query object from the servlet request.
	 * @return
	 */
	public static List<QueryCriteria> toQueryCriteria(QueryParameters queryParameters){
		List<QueryCriteria> criterias = new ArrayList<>();
		for (Field field: queryParameters.getClass().getDeclaredFields()){
			try {
				field.setAccessible(true);
				if (field.get(queryParameters) != null){
					String name = field.getName();
					Evaluation evaluation = Evaluation.EQUALS;
					if (field.isAnnotationPresent(QueryParameter.class)){
						QueryParameter queryParameter = field.getAnnotation(QueryParameter.class);
						if (!queryParameter.value().equals("")) name = queryParameter.value();
						evaluation = queryParameter.evalutation();
					}
					criterias.add(new QueryCriteria(name, field.get(queryParameters), evaluation));
				}
			} catch (IllegalAccessException e){
				e.printStackTrace();
			}
		}
		return criterias;
	}

	/**
	 * Remaps sort field names in {@link org.springframework.data.domain.Pageable} objects.
	 * 
	 * @param pageable
	 * @return
	 */
	public static Pageable remapPageable(Pageable pageable, QueryParameters queryParameters){
		if (pageable.getSort() != null) {
			Map<String, String> mappings = new HashMap<>();
			for (Field field : queryParameters.getClass().getDeclaredFields()) {
				if (field.isAnnotationPresent(QueryParameter.class)) {
					QueryParameter queryParameter = field.getAnnotation(QueryParameter.class);
					if (!queryParameter.value().equals(""))
						mappings.put(field.getName(), queryParameter.value());
				}
			}
			List<Sort.Order> orders = new ArrayList<>();
			for (Sort.Order order : pageable.getSort()) {
				if (mappings.containsKey(order.getProperty())) {
					order = new Sort.Order(order.getDirection(), mappings.get(order.getProperty()));
				}
				orders.add(order);
			}
			pageable =
					new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(orders));
		}
		return pageable;
	}
	
}
