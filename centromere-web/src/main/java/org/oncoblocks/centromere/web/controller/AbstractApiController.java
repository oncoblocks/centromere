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
import org.oncoblocks.centromere.core.repository.RepositoryOperations;
import org.oncoblocks.centromere.web.exceptions.InvalidParameterException;
import org.oncoblocks.centromere.web.exceptions.ResourceNotFoundException;
import org.oncoblocks.centromere.web.util.ApiMediaTypes;
import org.slf4j.Logger;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Base implementation of web API controller operations for GET, HEAD, and OPTIONS requests.  
 *   Supports dynamic queries of repository resources using annotated {@link Model} classes,
 *   field filtering, pagination, and hypermedia support.
 * 
 * @author woemler
 */
public abstract class AbstractApiController<T extends Model<ID>, ID extends Serializable> {

	private final RepositoryOperations<T, ID> repository;
	private final ResourceAssemblerSupport<T, FilterableResource> assembler;
	private final Class<T> model;
	private final ConversionService conversionService = new DefaultConversionService();
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AbstractApiController.class);

	public AbstractApiController(
			RepositoryOperations<T, ID> repository,
			Class<T> model,
			ResourceAssemblerSupport<T, FilterableResource> assembler) {
		this.repository = repository;
		this.model = model;
		this.assembler = assembler;
	}

	/**
	 * {@code GET /{id}}
	 * Fetches a single record by its primary ID and returns it, or a {@code Not Found} exception if not.
	 *
	 * @param id primary ID for the target record.
	 * @param fields set of field names to be included in response object.
	 * @param exclude set of field names to be excluded from the response object.
	 * @return {@code T} instance
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET,
			produces = { ApiMediaTypes.APPLICATION_HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE, 
					ApiMediaTypes.APPLICATION_HAL_XML_VALUE, MediaType.APPLICATION_XML_VALUE, 
					MediaType.TEXT_PLAIN_VALUE })
	public HttpEntity<?> findById(
			@PathVariable ID id,
			@RequestParam(required = false) Set<String> fields,
			@RequestParam(required = false) Set<String> exclude,
			HttpServletRequest request
	) {
		T entity = repository.findOne(id);
		if (entity == null) throw new ResourceNotFoundException();
		ResponseEnvelope envelope = null;
		if (ApiMediaTypes.isHalMediaType(request.getHeader("Accept"))){
			FilterableResource resource = assembler.toResource(entity);
			envelope = new ResponseEnvelope(resource, fields, exclude);
		} else {
			envelope = new ResponseEnvelope(entity, fields, exclude);
		}
		return new ResponseEntity<>(envelope, HttpStatus.OK);
	}

	/**
	 * {@code GET /distinct}
	 * Fetches the distinct values of the model attribute, {@code field}, which fulfill the given 
	 *   query parameters.
	 * 
	 * @param field Name of the model attribute to retrieve unique values of.
	 * @param request {@link HttpServletRequest}
	 * @return
	 */
	@RequestMapping(value = "/distinct", method = RequestMethod.GET,
			produces = { ApiMediaTypes.APPLICATION_HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE,
					ApiMediaTypes.APPLICATION_HAL_XML_VALUE, MediaType.APPLICATION_XML_VALUE,
					MediaType.TEXT_PLAIN_VALUE })
	public HttpEntity<?> findDistinct(
			@RequestParam String field, 
			HttpServletRequest request)
	{
		List<QueryCriteria> queryCriterias = this.getQueryCriteriaFromRequest(request);
		List<Object> distinct = (List<Object>) repository.distinct(field, queryCriterias);
		ResponseEnvelope envelope = null;
		if (ApiMediaTypes.isHalMediaType(request.getHeader("Accept"))){
			Link selfLink = new Link(linkTo(this.getClass()).slash("distinct").toString() + 
					(request.getQueryString() != null ? "?" + request.getQueryString() : ""), "self");
			Resources resources = new Resources(distinct);
			resources.add(selfLink);
			envelope = new ResponseEnvelope(resources);
		} else {
			envelope = new ResponseEnvelope(distinct);
		}
		return new ResponseEntity<>(envelope, HttpStatus.OK);
	}

	/**
	 * Queries the repository using inputted query string paramters, defined within a annotated 
	 *   {@link Model} classes.  Supports hypermedia, pagination, sorting, field 
	 *   filtering, and field exclusion.
	 * 
	 * @param pagedResourcesAssembler {@link PagedResourcesAssembler}
	 * @param request {@link HttpServletRequest}
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET,
			produces = { MediaType.APPLICATION_JSON_VALUE, ApiMediaTypes.APPLICATION_HAL_JSON_VALUE,
					ApiMediaTypes.APPLICATION_HAL_XML_VALUE, MediaType.APPLICATION_XML_VALUE,
					MediaType.TEXT_PLAIN_VALUE})
	public HttpEntity<?> find(
			@RequestParam(required = false) Set<String> fields,
			@RequestParam(required = false) Set<String> exclude,
			@PageableDefault(size = 1000) Pageable pageable,
			PagedResourcesAssembler<T> pagedResourcesAssembler, 
			HttpServletRequest request)
	{
		ResponseEnvelope envelope;
		pageable = this.remapPageable(pageable);
		Map<String,String[]> parameterMap = request.getParameterMap();
		List<QueryCriteria> criterias = this.getQueryCriteriaFromRequest(request);
		String mediaType = request.getHeader("Accept");
		Link selfLink = new Link(linkTo(this.getClass()).slash("").toString() +
				(request.getQueryString() != null ? "?" + request.getQueryString() : ""), "self");
		if (parameterMap.containsKey("page") || parameterMap.containsKey("size")){
			Page<T> page = repository.find(criterias, pageable);
			if (ApiMediaTypes.isHalMediaType(mediaType)){
				PagedResources<FilterableResource> pagedResources
						= pagedResourcesAssembler.toResource(page, assembler, selfLink);
				envelope = new ResponseEnvelope(pagedResources, fields, exclude);
			} else {
				envelope = new ResponseEnvelope(page, fields, exclude);
			}
		} else {
			Sort sort = pageable.getSort();
			List<T> entities = null;
			if (sort != null){
				entities = (List<T>) repository.find(criterias, sort);
			} else {
				entities = (List<T>) repository.find(criterias);
			}
			if (ApiMediaTypes.isHalMediaType(mediaType)){
				List<FilterableResource> resourceList = assembler.toResources(entities);
				Resources<FilterableResource> resources = new Resources<>(resourceList);
				resources.add(selfLink);
				envelope = new ResponseEnvelope(resources, fields, exclude);
			} else {
				envelope = new ResponseEnvelope(entities, fields, exclude);
			}
		}
		return new ResponseEntity<>(envelope, HttpStatus.OK);
	}

	/**
	 * {@code HEAD /**}
	 * Performs a test on the resource endpoints availability.
	 *
	 * @return
	 */
	@RequestMapping(value = { "", "/**" }, method = RequestMethod.HEAD)
	public HttpEntity<?> head(){
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * {@code OPTIONS /}
	 * Returns an information about the endpoint and available parameters.
	 *
	 * @return
	 */
//	@RequestMapping(method = RequestMethod.OPTIONS)
//	public HttpEntity options() throws Exception {
//		OptionsResponse response = doOptions();
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
//	
//	protected OptionsResponse doOptions() throws Exception {
//		OptionsResponse optionsResponse = new OptionsResponse();
//		List<OptionsEndpointDescriptor> descriptors = new ArrayList<>();
//		descriptors.addAll(OptionsDefaults.getDefaultGetDescriptors(model, queryParametersClass));
//		optionsResponse.setEndpoints(descriptors);
//		return optionsResponse;
//	}

	/**
	 * Extracts request parameters and matches them to available database query parameters, as defined
	 *   in the {@code model} class definition.
	 * 
	 * @param request {@link HttpServletRequest}
	 * @return
	 */
	private List<QueryCriteria> getQueryCriteriaFromRequest(HttpServletRequest request){
		logger.debug(String.format("Generating QueryCriteria for request parameters: model=%s params=%s", 
				model.getName(), request.getQueryString()));
		List<QueryCriteria> criterias = new ArrayList<>();
		for (Map.Entry entry: request.getParameterMap().entrySet()){
			QueryCriteria criteria = null;
			String paramName = (String) entry.getKey();
			String[] paramValue = ((String[]) entry.getValue())[0].split(",");
			Class<?> type;
			for (Field field: this.model.getDeclaredFields()){
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
	 * Converts an object into the appropriate type defined by the model field being queried.
	 * 
	 * @param param
	 * @param type
	 * @return
	 */
	private Object convertParameter(Object param, Class<?> type){
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

	/**
	 * Converts an array of objects into the appropriate type defined by the model field being queried
	 * 
	 * @param params
	 * @param type
	 * @return
	 */
	private List<Object> convertParameterArray(Object[] params, Class<?> type){
		List<Object> objects = new ArrayList<>();
		for (Object param: params){
			objects.add(this.convertParameter(param, type));
		}
		return objects;
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
	private QueryCriteria createCriteriaFromRequestParameter(String param, Object[] values, Class<?> type, Evaluation evaluation){
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
	 * Uses annotated {@link Model} class definitions to remap any request attribute names in a 
	 *   {@link Pageable} so that they match repository attribute names.
	 *
	 * @param pageable {@link Pageable}
	 * @return
	 */
	private Pageable remapPageable(Pageable pageable){
		logger.debug("Attempting to remap Pageable parameter names.");
		Sort sort = null;
		if (pageable.getSort() != null){
			List<Sort.Order> orders = new ArrayList<>();
			for (Sort.Order order: pageable.getSort()){
				orders.add(new Sort.Order(order.getDirection(), this.remapParameterName(order.getProperty())));
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
	private String remapParameterName(String param){
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
	
	public RepositoryOperations<T, ID> getRepository() {
		return repository;
	}

	public ResourceAssemblerSupport<T, FilterableResource> getAssembler() {
		return assembler;
	}

	public Class<T> getModel() {
		return model;
	}
}
