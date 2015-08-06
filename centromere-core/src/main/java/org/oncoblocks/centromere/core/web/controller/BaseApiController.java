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
import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.oncoblocks.centromere.core.repository.RepositoryOperations;
import org.oncoblocks.centromere.core.web.exceptions.ResourceNotFoundException;
import org.oncoblocks.centromere.core.web.query.QueryParameters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Base implementation of web API controller operations for GET, HEAD, and OPTIONS requests.  
 *   Supports dynamic queries of repository resources using {@link org.oncoblocks.centromere.core.web.query.QueryParameters},
 *   field filtering, pagination, and HAL support.
 * 
 * @author woemler
 */
public abstract class BaseApiController<
		T extends Model<ID>,
		ID extends Serializable,
		Q extends QueryParameters> {

	protected RepositoryOperations<T, ID> repository;
	protected ResourceAssemblerSupport<T, FilterableResource> assembler;

	public BaseApiController(
			RepositoryOperations<T, ID> repository,
			ResourceAssemblerSupport<T, FilterableResource> assembler) {
		this.repository = repository;
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
			produces = { HalMediaType.APPLICATION_JSON_HAL_VALUE })
	public HttpEntity findByIdWithHal(
			@PathVariable ID id,
			@RequestParam(required = false) Set<String> fields,
			@RequestParam(required = false) Set<String> exclude
	) {
		T entity = repository.findById(id);
		if (entity == null) throw new ResourceNotFoundException();
		FilterableResource resource = assembler.toResource(entity);
		ResponseEnvelope envelope = new ResponseEnvelope(resource, fields, exclude);
		return new ResponseEntity<>(envelope, HttpStatus.OK);
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
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public HttpEntity findById(@PathVariable ID id,
			@RequestParam(required = false) Set<String> fields,
			@RequestParam(required = false) Set<String> exclude
	) {
		T entity = repository.findById(id);
		if (entity == null) throw new ResourceNotFoundException();
		ResponseEnvelope envelope = new ResponseEnvelope(entity, fields, exclude);
		return new ResponseEntity<>(envelope, HttpStatus.OK);
	}

	/**
	 * GET request for a collection of entities. Can be sorted using {@link org.springframework.data.domain.Pageable},
	 *   or just return a default-ordered list (typically by primary key ID). Results can be filtered 
	 *   using query string parameters that map to {@code T} entity attributes.  Supports field filtering.
	 *
	 * @param params object that defines acceptable query parameters. 
	 * @param pageable {@link org.springframework.data.domain.Pageable} request object, created using 
	 *   'page', 'size', or 'sort' as query string parameters. 
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET, params = { "!page", "!size" },
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public HttpEntity find(@ModelAttribute Q params, Pageable pageable){
		pageable = QueryParameters.remapPageable(pageable, params);
		List<QueryCriteria> criterias = QueryParameters.toQueryCriteria(params);
		List<T> entities;
		if (pageable.getSort() != null){
			entities = (List<T>) repository.findSorted(criterias, pageable.getSort());
		} else {
			entities = (List<T>) repository.find(criterias);
		}
		ResponseEnvelope envelope
				= new ResponseEnvelope(entities, params.getIncludedFields(), params.getExcludedFields());
		return new ResponseEntity<>(envelope, HttpStatus.OK);
	}

	/**
	 * GET request for a collection of entities. Can be sorted using {@link org.springframework.data.domain.Pageable},
	 *   or just return a default-ordered list (typically by primary key ID). Results can be filtered 
	 *   using query string parameters that map to {@code T} entity attributes.  Supports field filtering.
	 *   Response objects are wrapped and annotated with HAL-formatted links.
	 *
	 * @param params object that defines acceptable query parameters. 
	 * @param pageable {@link org.springframework.data.domain.Pageable} request object, created using 
	 *   'page', 'size', or 'sort' as query string parameters. 
	 * @param request {@link javax.servlet.http.HttpServletRequest}
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET, params = { "!page", "!size" },
			produces = {HalMediaType.APPLICATION_JSON_HAL_VALUE})
	public HttpEntity findWithHal(@ModelAttribute Q params, Pageable pageable, HttpServletRequest request){
		pageable = QueryParameters.remapPageable(pageable, params);
		List<QueryCriteria> criterias = QueryParameters.toQueryCriteria(params);
		List<T> entities;
		if (pageable.getSort() != null){
			entities = (List<T>) repository.findSorted(criterias, pageable.getSort());
		} else {
			entities = (List<T>) repository.find(criterias);
		}
		List<FilterableResource> resourceList = assembler.toResources(entities);
		Resources<FilterableResource> resources = new Resources<>(resourceList);
		Link selfLink = new Link(linkTo(this.getClass()).slash("").toString() +
				(request.getQueryString() != null ? "?" + request.getQueryString() : ""), "self");
		resources.add(selfLink);
		ResponseEnvelope envelope
				= new ResponseEnvelope(resources, params.getIncludedFields(), params.getExcludedFields());
		return new ResponseEntity<>(envelope, HttpStatus.OK);
	}

	/**
	 * GET request for a paged collection of entities. Can be sorted using {@link org.springframework.data.domain.Pageable},
	 *   or just return a default-ordered list (typically by primary key ID). Results can be filtered 
	 *   using query string parameters that map to {@code T} entity attributes.  Supports field filtering.
	 *
	 * @param params object that defines acceptable query parameters.  
	 * @param pageable {@link org.springframework.data.domain.Pageable} request object, created using 
	 *   'page', 'size', or 'sort' as query string parameters. 
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public HttpEntity findPaged(@ModelAttribute Q params, Pageable pageable){
		pageable = QueryParameters.remapPageable(pageable, params);
		List<QueryCriteria> criterias = QueryParameters.toQueryCriteria(params);
		Page<T> page = repository.findPaged(criterias, pageable);
		ResponseEnvelope envelope
				= new ResponseEnvelope(page, params.getIncludedFields(), params.getExcludedFields());
		return new ResponseEntity<>(envelope, HttpStatus.OK);
	}

	/**
	 * GET request for a paged collection of entities. Can be sorted using {@link org.springframework.data.domain.Pageable},
	 *   or just return a default-ordered list (typically by primary key ID). Results can be filtered 
	 *   using query string parameters that map to {@code T} entity attributes.  Supports field filtering.
	 *   Response objects are wrapped and annotated with HAL-formatted links.
	 *
	 * @param params object that defines acceptable query parameters. 
	 * @param pageable {@link org.springframework.data.domain.Pageable} request object, created using 
	 *   'page', 'size', or 'sort' as query string parameters. 
	 * @param pagedResourcesAssembler {@link org.springframework.data.web.PagedResourcesAssembler} for 
	 *   assembling paged entities into a wrapped response object with hypermedia links. 
	 * @param request {@link javax.servlet.http.HttpServletRequest}
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET,
			produces = {HalMediaType.APPLICATION_JSON_HAL_VALUE})
	public HttpEntity findWithHal(
			@ModelAttribute Q params,
			Pageable pageable,
			PagedResourcesAssembler<T> pagedResourcesAssembler,
			HttpServletRequest request
	){
		pageable = QueryParameters.remapPageable(pageable, params);
		List<QueryCriteria> criterias = QueryParameters.toQueryCriteria(params);
		Page<T> page = repository.findPaged(criterias, pageable);
		Link selfLink = new Link(linkTo(this.getClass()).slash("").toString() +
				(request.getQueryString() != null ? "?" + request.getQueryString() : ""), "self");
		PagedResources<FilterableResource> pagedResources
				= pagedResourcesAssembler.toResource(page, assembler, selfLink);
		ResponseEnvelope envelope
				= new ResponseEnvelope(pagedResources, params.getIncludedFields(), params.getExcludedFields());
		return new ResponseEntity<>(envelope, HttpStatus.OK);
	}

	/**
	 * {@code HEAD /**}
	 * Performs a test on the resource endpoints availability.
	 *
	 * @return
	 */
	@RequestMapping(value = { "", "/**" }, method = RequestMethod.HEAD)
	public HttpEntity head(){
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * {@code OPTIONS /}
	 * Returns an information about the endpoint and available parameters.
	 *
	 * @return
	 */
	@RequestMapping(value = { "", "/**" }, method = RequestMethod.OPTIONS,
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public HttpEntity options() {
		return null; //TODO
	}
	
	
}
