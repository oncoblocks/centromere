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

package org.oncoblocks.centromere.web.controller;

import org.oncoblocks.centromere.core.model.Alias;
import org.oncoblocks.centromere.core.model.Aliases;
import org.oncoblocks.centromere.core.model.Ignored;
import org.oncoblocks.centromere.core.model.Model;
import org.oncoblocks.centromere.core.repository.Evaluation;
import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.oncoblocks.centromere.web.exceptions.InvalidParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Helper methods for processing API controller requests.
 * 
 * @author woemler
 */
public class RequestUtils {

	private static final Logger logger = LoggerFactory.getLogger(RequestUtils.class);

	/**
	 * Extracts request parameters and matches them to available database query parameters, as defined
	 *   in the {@code model} class definition.
	 *
	 * @param request {@link HttpServletRequest}
	 * @return
	 */
	public static List<QueryCriteria> getQueryCriteriaFromRequest(
			Class<? extends Model<?>> model, HttpServletRequest request
	){
		logger.debug(String.format("Generating QueryCriteria for request parameters: model=%s params=%s",
				model.getName(), request.getQueryString()));
		List<QueryCriteria> criterias = new ArrayList<>();
		for (Map.Entry entry: request.getParameterMap().entrySet()){
			QueryCriteria criteria = null;
			String paramName = (String) entry.getKey();
			String[] paramValue = ((String[]) entry.getValue())[0].split(",");
			Class<?> type;
			for (Field field: model.getDeclaredFields()){
				if (Collection.class.isAssignableFrom(field.getType())){
					ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
					type = (Class<?>) parameterizedType.getActualTypeArguments()[0];
				} else {
					type = field.getType();
				}
				String fieldName = field.getName();
				if (field.isAnnotationPresent(Ignored.class)) continue;
				if (field.getName().equals(paramName)) {
					if (paramValue.length > 1){
						criteria = createCriteriaFromRequestParameter(fieldName, paramValue, type, Evaluation.IN);
					} else {
						criteria = createCriteriaFromRequestParameter(fieldName, paramValue, type, Evaluation.EQUALS);
					}
				} else if (field.isAnnotationPresent(Aliases.class)){
					Aliases aliases = field.getAnnotation(Aliases.class);
					for (Alias alias: aliases.value()){
						if (alias.value().equals(paramName)){
							if (!alias.fieldName().equals("")) fieldName = alias.fieldName();
							criteria = createCriteriaFromRequestParameter(fieldName, paramValue, type, alias.evaluation());
						}
					}
				} else if (field.isAnnotationPresent(Alias.class)){
					Alias alias = field.getAnnotation(Alias.class);
					if (alias.value().equals(paramName)){
						if (!alias.fieldName().equals("")) fieldName = alias.fieldName();
						criteria = createCriteriaFromRequestParameter(fieldName, paramValue, type, alias.evaluation());
					}
				}
			}
			if (criteria != null) criterias.add(criteria);
		}
		return criterias;
	}
	
	/**
	 * Creates a {@link QueryCriteria} object based upon a request parameter and {@link Evaluation}
	 *   value.
	 *
	 * @param param
	 * @param values
	 * @param type
	 * @param evaluation
	 * @return
	 */
	public static QueryCriteria createCriteriaFromRequestParameter(String param, Object[] values, Class<?> type, Evaluation evaluation){
		logger.debug(String.format("Generating QueryCriteria object for query string parameter: "
				+ "param=%s values=%s type=%s eval=%s", param, values.toString(), type.getName(), evaluation.toString()));
		switch (evaluation){
			case EQUALS:
				return new QueryCriteria(param, convertParameter(values[0], type), Evaluation.EQUALS);
			case NOT_EQUALS:
				return new QueryCriteria(param, convertParameter(values[0], type), Evaluation.NOT_EQUALS);
			case IN:
				return new QueryCriteria(param, convertParameterArray(values, type), Evaluation.IN);
			case NOT_IN:
				return new QueryCriteria(param, Arrays.asList(values), Evaluation.NOT_IN);
			case IS_NULL:
				return new QueryCriteria(param, true, Evaluation.IS_NULL);
			case NOT_NULL:
				return new QueryCriteria(param, true, Evaluation.NOT_NULL);
			case IS_TRUE:
				return new QueryCriteria(param, true, Evaluation.IS_TRUE);
			case IS_FALSE:
				return new QueryCriteria(param, true, Evaluation.IS_FALSE);
			case GREATER_THAN:
				return new QueryCriteria(param, convertParameter(values[0], type), Evaluation.GREATER_THAN);
			case GREATER_THAN_EQUALS:
				return new QueryCriteria(param, convertParameter(values[0], type), Evaluation.GREATER_THAN_EQUALS);
			case LESS_THAN:
				return new QueryCriteria(param, convertParameter(values[0], type), Evaluation.LESS_THAN);
			case LESS_THAN_EQUALS:
				return new QueryCriteria(param, convertParameter(values[0], type), Evaluation.LESS_THAN_EQUALS);
			case BETWEEN:
				return new QueryCriteria(param, Arrays.asList(convertParameter(values[0], type),
						convertParameter(values[1], type)), Evaluation.BETWEEN);
			case OUTSIDE:
				return new QueryCriteria(param, Arrays.asList(convertParameter(values[0], type),
						convertParameter(values[1], type)), Evaluation.OUTSIDE);
			case BETWEEN_INCLUSIVE:
				return new QueryCriteria(param, Arrays.asList(convertParameter(values[0], type),
						convertParameter(values[1], type)), Evaluation.BETWEEN_INCLUSIVE);
			case OUTSIDE_INCLUSIVE:
				return new QueryCriteria(param, Arrays.asList(convertParameter(values[0], type),
						convertParameter(values[1], type)), Evaluation.OUTSIDE_INCLUSIVE);
			default:
				return null;
		}
	}

	/**
	 * Converts an object into the appropriate type defined by the model field being queried.
	 *
	 * @param param
	 * @param type
	 * @return
	 */
	public static Object convertParameter(Object param, Class<?> type, ConversionService conversionService){
		logger.debug(String.format("Attempting to convert parameter: from=%s to=%s",
				param.getClass().getName(), type.getName()));
		if (conversionService.canConvert(param.getClass(), type)){
			try {
				return conversionService.convert(param, type);
			} catch (ConversionFailedException e){
				e.printStackTrace();
				throw new InvalidParameterException();
			}
		} else {
			return param;
		}
	}
	
	public static Object convertParameter(Object param, Class<?> type){
		ConversionService conversionService = new DefaultConversionService();
		return convertParameter(param, type, conversionService);
	}

	/**
	 * Converts an array of objects into the appropriate type defined by the model field being queried
	 *
	 * @param params
	 * @param type
	 * @return
	 */
	public static List<Object> convertParameterArray(Object[] params, Class<?> type){
		List<Object> objects = new ArrayList<>();
		for (Object param: params){
			objects.add(convertParameter(param, type));
		}
		return objects;
	}

	/**
	 * Extracts the requested filtered fields parameter from a request.
	 * 
	 * @param request
	 * @return
	 */
	public static Set<String> getFilteredFieldsFromRequest(HttpServletRequest request){
		Set<String> fields = null;
		if (request.getParameterMap().containsKey("fields")){
			fields = new HashSet<>();
			String[] params = request.getParameter("fields").split(",");
			for (String field: params){
				fields.add(field.trim());
			}
		}
		return fields;
	}

	/**
	 * Extracts the requested filtered fields parameter from a request.
	 *
	 * @param request
	 * @return
	 */
	public static Set<String> getExcludedFieldsFromRequest(HttpServletRequest request){
		Set<String> exclude = null;
		if (request.getParameterMap().containsKey("exclude")){
			exclude = new HashSet<>();
			String[] params = request.getParameter("exclude").split(",");
			for (String field: params){
				exclude.add(field.trim());
			}
		}
		return exclude;
	}

	/**
	 * Uses annotated {@link Model} class definitions to remap any request attribute names in a 
	 *   {@link Pageable} so that they match repository attribute names.
	 *
	 * @param pageable {@link Pageable}
	 * @return
	 */
	public static Pageable remapPageable(Pageable pageable, Class<? extends Model<?>> model){
		logger.debug("Attempting to remap Pageable parameter names.");
		Sort sort = null;
		if (pageable.getSort() != null){
			List<Sort.Order> orders = new ArrayList<>();
			for (Sort.Order order: pageable.getSort()){
				orders.add(new Sort.Order(order.getDirection(), remapParameterName(order.getProperty(), model)));
			}
			sort = new Sort(orders);
		}
		return new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
	}

	/**
	 * Checks a request parameter name against all possible {@link Model} attributes, converting it to
	 *   the appropriate repository field name for querying and sorting.
	 *
	 * @param param
	 * @return
	 */
	public static String remapParameterName(String param, Class<? extends Model<?>> model){
		logger.debug(String.format("Attempting to remap query string parameter: %s", param));
		for (Field field: model.getDeclaredFields()){
			String fieldName = field.getName();
			if (field.isAnnotationPresent(Aliases.class)){
				Aliases aliases = field.getAnnotation(Aliases.class);
				for (Alias alias: aliases.value()){
					if (alias.value().equals(param)) return fieldName;
				}
			} else if (field.isAnnotationPresent(Alias.class)){
				Alias alias = field.getAnnotation(Alias.class);
				if (alias.value().equals(param)) return fieldName;
			}
		}
		logger.debug(String.format("Parameter remapped to: %s", param));
		return param;
	}
	
}
