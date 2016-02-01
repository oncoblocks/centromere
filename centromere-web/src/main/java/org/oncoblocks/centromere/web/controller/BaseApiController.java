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

import org.oncoblocks.centromere.core.model.Model;
import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.oncoblocks.centromere.core.repository.RepositoryOperations;
import org.oncoblocks.centromere.web.exceptions.ResourceNotFoundException;
import org.oncoblocks.centromere.web.query.QueryParameters;
import org.oncoblocks.centromere.web.util.ApiMediaTypes;
import org.slf4j.Logger;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Base implementation of web API controller operations for GET, HEAD, and OPTIONS requests.  
 *   Supports dynamic queries of repository resources using {@link QueryParameters} implementations,
 *   field filtering, pagination, and hypermedia support.
 * 
 * @author woemler
 */
public abstract class BaseApiController<
		T extends Model<ID>,
		ID extends Serializable,
		Q extends QueryParameters> {

	private RepositoryOperations<T, ID> repository;
	private ResourceAssemblerSupport<T, FilterableResource> assembler;

	public BaseApiController(
			RepositoryOperations<T, ID> repository,
			ResourceAssemblerSupport<T, FilterableResource> assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(BaseApiController.class);
	
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
	 * @param params {@link QueryParameters}
	 * @param request {@link HttpServletRequest}
	 * @return
	 */
	@RequestMapping(value = "/distinct", method = RequestMethod.GET,
			produces = { ApiMediaTypes.APPLICATION_HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE,
					ApiMediaTypes.APPLICATION_HAL_XML_VALUE, MediaType.APPLICATION_XML_VALUE,
					MediaType.TEXT_PLAIN_VALUE })
	public HttpEntity<?> findDistinct(
			@RequestParam String field, 
			@ModelAttribute Q params,
			HttpServletRequest request)
	{
		List<QueryCriteria> queryCriterias = params.getQueryCriteria();
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
	 * Queries the repository using inputted query string paramters, defined within a custom 
	 *   {@link QueryParameters} implementation.  Supports hypermedia, pagination, sorting, field 
	 *   filtering, and field exclusion.
	 * 
	 * @param params {@link QueryParameters}
	 * @param pagedResourcesAssembler {@link PagedResourcesAssembler}
	 * @param request {@link HttpServletRequest}
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET,
			produces = { MediaType.APPLICATION_JSON_VALUE, ApiMediaTypes.APPLICATION_HAL_JSON_VALUE,
					ApiMediaTypes.APPLICATION_HAL_XML_VALUE, MediaType.APPLICATION_XML_VALUE,
					MediaType.TEXT_PLAIN_VALUE})
	public HttpEntity<?> find(
			@ModelAttribute Q params,
			@RequestParam(required = false) Set<String> fields,
			@RequestParam(required = false) Set<String> exclude,
			@PageableDefault(size = 1000) Pageable pageable,
			PagedResourcesAssembler<T> pagedResourcesAssembler, 
			HttpServletRequest request)
	{
		ResponseEnvelope envelope;
		pageable = this.remapPageable(pageable, params);
		Map<String,String[]> parameterMap = request.getParameterMap();
		List<QueryCriteria> criterias = params.getQueryCriteria();
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
	 * Uses {@link QueryParameters#remapParameterName(String)} to remap any request attribute names in a 
	 *   {@link Pageable} so that they match repository attribute names.
	 *
	 * @param pageable {@link Pageable}
	 * @param params {@link QueryParameters}
	 * @return
	 */
	private Pageable remapPageable(Pageable pageable, QueryParameters params){
		Sort sort = null;
		if (pageable.getSort() != null){
			List<Sort.Order> orders = new ArrayList<>();
			for (Sort.Order order: pageable.getSort()){
				orders.add(new Sort.Order(order.getDirection(), params.remapParameterName(order.getProperty())));
			}
			sort = new Sort(orders);
		}
		return new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
	}
	
	public RepositoryOperations<T, ID> getRepository() {
		return repository;
	}

	public ResourceAssemblerSupport<T, FilterableResource> getAssembler() {
		return assembler;
	}
}
