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

import org.oncoblocks.centromere.core.repository.Evaluation;
import org.oncoblocks.centromere.core.repository.QueryCriteria;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link QueryParameters} that assumes that subclass implementations will use
 *   basic parameter types, annotated with {@link org.oncoblocks.centromere.web.query.QueryParameter} to add extra customization to the 
 *   web service query parameters, and handle parameter name remapping.
 * 
 * @author woemler
 */
public class AnnotatedQueryParameters implements QueryParameters {

	@Override 
	public List<QueryCriteria> getQueryCriteria() {
		return AnnotatedQueryParameters.toQueryCriteria(this);
	}

	/**
	 * Remaps parameter names by inspecting attributes with {@link org.oncoblocks.centromere.web.query.QueryParameter} annotations and 
	 *   using those as mapping references.
	 * 
	 * @param name
	 * @return
	 */
	@Override 
	public String remapParameterName(String name) {
		for (Field field : this.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(QueryParameter.class)) {
				QueryParameter queryParameter = field.getAnnotation(
						QueryParameter.class);
				if (field.getName().equals(name) && !queryParameter.value().equals(""))
					name = queryParameter.value();
			}
		}
		return name;
	}

	/**
	 * Converts a {@link org.oncoblocks.centromere.web.query.AnnotatedQueryParameters} object into
	 *   a list of {@link QueryCriteria}, to be passed to 
	 *   the repository layer as query parameters.
	 *
	 * @param queryParameters mapped query object from the servlet request.
	 * @return
	 */
	public static List<QueryCriteria> toQueryCriteria(
			AnnotatedQueryParameters queryParameters){
		
		List<QueryCriteria> criterias = new ArrayList<>();
		for (Field field: queryParameters.getClass().getDeclaredFields()){
			try {
				field.setAccessible(true);
				if (field.get(queryParameters) != null){
					String name = field.getName();
					Evaluation evaluation = Evaluation.EQUALS;
					if (field.isAnnotationPresent(QueryParameter.class)){
						QueryParameter
								queryParameter = field.getAnnotation(QueryParameter.class);
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
	
}
