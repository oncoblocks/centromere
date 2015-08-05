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

package org.oncoblocks.centromere.core.web.controller;

import org.oncoblocks.centromere.core.model.Model;
import org.oncoblocks.centromere.core.model.Parameter;
import org.oncoblocks.centromere.core.model.Queryable;
import org.oncoblocks.centromere.core.repository.Evaluation;
import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.oncoblocks.centromere.core.web.service.ServiceOperations;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Abstract controller implementation, allowing for dynamic queries based on preset query parameters, 
 *   which get passed to the repository layer as {@link org.oncoblocks.centromere.core.repository.QueryCriteria}
 * 
 * @author woemler
 */
public abstract class QueryCriteriaController<T extends Model<ID>, ID extends Serializable> 
		extends AbstractCrudController<T, ID> {
	
	private ConversionService conversionService;
	private Class<T> model;

	public QueryCriteriaController(ServiceOperations<T, ID> service,
			ResourceAssemblerSupport<T, FilterableResource<T>> assembler,
			ConversionService conversionService, Class<T> model) {
		super(service, assembler);
		this.conversionService = conversionService;
		this.model = model;
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET, params = { "!page", "!size" },
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ResponseEnvelope<List<T>>> find(
			@RequestParam(value = "fields", required = false) Set<String> fields,
			@RequestParam(value = "exclude", required = false) Set<String> exclude,
			Pageable pageable,
			HttpServletRequest request
	){
		List<QueryCriteria> criterias = convertRequestParameters(request.getParameterMap());
		List<T> entities;
		if (pageable.getSort() != null){
			entities = (List<T>) service.findSorted(criterias, pageable.getSort());
		} else {
			entities = (List<T>) service.find(criterias);
		}
		ResponseEnvelope<List<T>> envelope = new ResponseEnvelope<>(entities, fields, exclude);
		return new ResponseEntity<>(envelope, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.GET, params = { "!page", "!size" },
			produces = {HalMediaType.APPLICATION_JSON_HAL_VALUE})
	public ResponseEntity<ResponseEnvelope<Resources<FilterableResource<T>>>> findWithHal(
			@RequestParam(value = "fields", required = false) Set<String> fields,
			@RequestParam(value = "exclude", required = false) Set<String> exclude,
			Pageable pageable,
			HttpServletRequest request
	){
		List<QueryCriteria> criterias = convertRequestParameters(request.getParameterMap());
		List<T> entities;
		if (pageable.getSort() != null){
			entities = (List<T>) service.findSorted(criterias, pageable.getSort());
		} else {
			entities = (List<T>) service.find(criterias);
		}
		List<FilterableResource<T>> resourceList = assembler.toResources(entities);
		Resources<FilterableResource<T>> resources = new Resources<>(resourceList);
		Link selfLink = new Link(linkTo(this.getClass()).slash("").toString() +
				(request.getQueryString() != null ? "?" + request.getQueryString() : ""), "self");
		resources.add(selfLink);
		ResponseEnvelope<Resources<FilterableResource<T>>> envelope
				= new ResponseEnvelope<>(resources, fields, exclude);
		return new ResponseEntity<>(envelope, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ResponseEnvelope<Page<T>>> findPaged(
			@RequestParam(value = "fields", required = false) Set<String> fields,
			@RequestParam(value = "exclude", required = false) Set<String> exclude,
			Pageable pageable,
			PagedResourcesAssembler<T> pagedResourcesAssembler,
			HttpServletRequest request
	){
		List<QueryCriteria> criterias = convertRequestParameters(request.getParameterMap());
		Page<T> page = service.findPaged(criterias, pageable);
		ResponseEnvelope<Page<T>> envelope = new ResponseEnvelope<>(page, fields, exclude);
		return new ResponseEntity<>(envelope, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.GET,
			produces = {HalMediaType.APPLICATION_JSON_HAL_VALUE})
	public ResponseEntity<ResponseEnvelope<PagedResources<FilterableResource<T>>>> findWithHal(
			@RequestParam(value = "fields", required = false) Set<String> fields,
			@RequestParam(value = "exclude", required = false) Set<String> exclude,
			Pageable pageable,
			PagedResourcesAssembler<T> pagedResourcesAssembler,
			HttpServletRequest request
	){
		List<QueryCriteria> criterias = convertRequestParameters(request.getParameterMap());
		Page<T> page = service.findPaged(criterias, pageable);
		Link selfLink = new Link(linkTo(this.getClass()).slash("").toString() +
				(request.getQueryString() != null ? "?" + request.getQueryString() : ""), "self");
		PagedResources<FilterableResource<T>> pagedResources
				= pagedResourcesAssembler.toResource(page, assembler, selfLink);
		ResponseEnvelope<PagedResources<FilterableResource<T>>> envelope
				= new ResponseEnvelope<>(pagedResources, fields, exclude);
		return new ResponseEntity<>(envelope, HttpStatus.OK);
	}
//	
//	private List<QueryCriteria> convertRequestParameters(Map<String,String[]> params){
//		List<QueryCriteria> criterias = new ArrayList<>();
//		List<QueryParameter> queryParameters = registerQueryParameters(new ArrayList<>());
//		for (QueryParameter queryParameter: queryParameters){
//			String key = queryParameter.getQueryStringParameter();
//			Class<?> type = queryParameter.getType();
//			if (params.containsKey(key)){
//				if (type.equals(String.class)){
//					criterias.add(queryParameter.toQueryCriteria((String) params.get(key)[0]));
//				} else if (conversionService.canConvert(String.class, type)){
//					criterias.add(queryParameter.toQueryCriteria(conversionService.convert(params.get(key)[0], type)));
//				}
//			}
//		}
//		return criterias;
//	}
//	
//	protected List<QueryParameter> registerQueryParameters(List<QueryParameter> queryParameters){
//		return queryParameters;
//	}


	private List<QueryCriteria> convertRequestParameters(Map<String, String[]> parameterMap){
		List<QueryCriteria> criterias = new ArrayList<>();
		for (Field field: model.getDeclaredFields()){
			String paramName = field.getName();
			Class<?> fieldType = field.getType();
			if (field.isAnnotationPresent(Queryable.class)){
				Queryable queryable = field.getAnnotation(Queryable.class);
				if (queryable.value().length > 0){
					for (Parameter param: queryable.value()){
						paramName = param.value();
						if (!param.type().equals(Object.class)) fieldType = param.type();
						Evaluation evaluation = param.evalutation();
						if (parameterMap.containsKey(paramName)) {
							criterias.add(
									new QueryCriteria(
											paramName,
											conversionService.convert(parameterMap.get(paramName)[0], fieldType),
											evaluation
									)
							);
						}
					}
				} else {
					if (parameterMap.containsKey(paramName)){
						criterias.add(
								new QueryCriteria(
										paramName,
										conversionService.convert(parameterMap.get(paramName)[0], fieldType)
								)
						);
					}
				}
			}
		}
		return criterias;
	}

}
