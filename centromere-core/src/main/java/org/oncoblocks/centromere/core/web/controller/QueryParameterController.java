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
import org.oncoblocks.centromere.core.web.service.ServiceOperations;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Abstract controller implementation, allowing for dynamic queries based on preset query parameters, 
 *   defined using {@link org.oncoblocks.centromere.core.web.controller.QueryParameters} implementations
 *   that specify allowable parameters. Mapped query parameters get passed to the repository layer 
 *   as {@link org.oncoblocks.centromere.core.repository.QueryCriteria}.
 * 
 * @author woemler
 */
public abstract class QueryParameterController<T extends Model<ID>, ID extends Serializable, Q extends QueryParameters> 
		extends AbstractCrudController<T, ID> {
	
	public QueryParameterController(ServiceOperations<T, ID> service,
			ResourceAssemblerSupport<T, FilterableResource<T>> assembler) {
		super(service, assembler);
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
	public ResponseEntity<ResponseEnvelope<List<T>>> find(
			@ModelAttribute Q params,
			Pageable pageable
	){
		List<QueryCriteria> criterias = QueryParameters.toQueryCriteria(params);
		List<T> entities;
		if (pageable.getSort() != null){
			entities = (List<T>) service.findSorted(criterias, pageable.getSort());
		} else {
			entities = (List<T>) service.find(criterias);
		}
		ResponseEnvelope<List<T>> envelope 
				= new ResponseEnvelope<>(entities, params.getIncludedFields(), params.getExcludedFields());
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
	public ResponseEntity<ResponseEnvelope<Resources<FilterableResource<T>>>> findWithHal(
			@ModelAttribute Q params,
			Pageable pageable,
			HttpServletRequest request
	){
		List<QueryCriteria> criterias = QueryParameters.toQueryCriteria(params);
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
				= new ResponseEnvelope<>(resources, params.getIncludedFields(), params.getExcludedFields());
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
	public ResponseEntity<ResponseEnvelope<Page<T>>> findPaged(
			@ModelAttribute Q params,
			Pageable pageable
	){
		List<QueryCriteria> criterias = QueryParameters.toQueryCriteria(params);
		Page<T> page = service.findPaged(criterias, pageable);
		ResponseEnvelope<Page<T>> envelope 
				= new ResponseEnvelope<>(page, params.getIncludedFields(), params.getExcludedFields());
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
	public ResponseEntity<ResponseEnvelope<PagedResources<FilterableResource<T>>>> findWithHal(
			@ModelAttribute Q params,
			Pageable pageable,
			PagedResourcesAssembler<T> pagedResourcesAssembler,
			HttpServletRequest request
	){
		List<QueryCriteria> criterias = QueryParameters.toQueryCriteria(params);
		Page<T> page = service.findPaged(criterias, pageable);
		Link selfLink = new Link(linkTo(this.getClass()).slash("").toString() +
				(request.getQueryString() != null ? "?" + request.getQueryString() : ""), "self");
		PagedResources<FilterableResource<T>> pagedResources
				= pagedResourcesAssembler.toResource(page, assembler, selfLink);
		ResponseEnvelope<PagedResources<FilterableResource<T>>> envelope
				= new ResponseEnvelope<>(pagedResources, params.getIncludedFields(), params.getExcludedFields());
		return new ResponseEntity<>(envelope, HttpStatus.OK);
	}
	
}
