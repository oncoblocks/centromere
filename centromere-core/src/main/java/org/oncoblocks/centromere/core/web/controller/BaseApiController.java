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
import org.oncoblocks.centromere.core.web.util.HalMediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

	private RepositoryOperations<T, ID> repository;
	private ResourceAssemblerSupport<T, FilterableResource> assembler;

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
			produces = { HalMediaType.APPLICATION_HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE, 
					HalMediaType.APPLICATION_HAL_XML_VALUE, MediaType.APPLICATION_XML_VALUE, 
					MediaType.TEXT_PLAIN_VALUE })
	public HttpEntity findById(
			@PathVariable ID id,
			@RequestParam(required = false) Set<String> fields,
			@RequestParam(required = false) Set<String> exclude,
			HttpServletRequest request
	) {
		T entity = repository.findById(id);
		if (entity == null) throw new ResourceNotFoundException();
		ResponseEnvelope envelope = null;
		if (HalMediaType.isHalMediaType(request.getHeader("Accept"))){
			FilterableResource resource = assembler.toResource(entity);
			envelope = new ResponseEnvelope(resource, fields, exclude);
		} else {
			envelope = new ResponseEnvelope(entity, fields, exclude);
		}
		return new ResponseEntity<>(envelope, HttpStatus.OK);
	}

	/**
	 * Queries the repository using inputted query string paramters, defined within a custom 
	 *   {@link QueryParameters}.  Supports pagination, sorting, field filtering, and field exclusion.
	 * 
	 * @param params
	 * @param pagedResourcesAssembler
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET,
			produces = { MediaType.APPLICATION_JSON_VALUE, HalMediaType.APPLICATION_HAL_JSON_VALUE,
					HalMediaType.APPLICATION_HAL_XML_VALUE, MediaType.APPLICATION_XML_VALUE,
					MediaType.TEXT_PLAIN_VALUE})
	public HttpEntity find(
			@ModelAttribute Q params, 
			PagedResourcesAssembler<T> pagedResourcesAssembler, 
			HttpServletRequest request)
	{
		ResponseEnvelope envelope = null;
		List<QueryCriteria> criterias = QueryParameters.toQueryCriteria(params);
		String mediaType = request.getHeader("Accept");
		Link selfLink = new Link(linkTo(this.getClass()).slash("").toString() +
				(request.getQueryString() != null ? "?" + request.getQueryString() : ""), "self");
		if (params.isPaged()){
			Pageable pageable = QueryParameters.remapPageable(params);
			Page<T> page = repository.find(criterias, pageable);
			if (HalMediaType.isHalMediaType(mediaType)){
				PagedResources<FilterableResource> pagedResources
						= pagedResourcesAssembler.toResource(page, assembler, selfLink);
				envelope = new ResponseEnvelope(pagedResources, params.getIncludedFields(), params.getExcludedFields());
			} else {
				envelope = new ResponseEnvelope(page, params.getIncludedFields(), params.getExcludedFields());
			}
		} else {
			Sort sort = params.getSort();
			List<T> entities = null;
			if (sort != null){
				entities = (List<T>) repository.find(criterias, QueryParameters.remapSort(params));
			} else {
				entities = (List<T>) repository.find(criterias);
			}
			if (HalMediaType.isHalMediaType(mediaType)){
				List<FilterableResource> resourceList = assembler.toResources(entities);
				Resources<FilterableResource> resources = new Resources<>(resourceList);
				resources.add(selfLink);
				envelope = new ResponseEnvelope(resources, params.getIncludedFields(), params.getExcludedFields());
			} else {
				envelope = new ResponseEnvelope(entities, params.getIncludedFields(), params.getExcludedFields());
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
	public HttpEntity head(){
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
	
	public RepositoryOperations<T, ID> getRepository() {
		return repository;
	}

	public ResourceAssemblerSupport<T, FilterableResource> getAssembler() {
		return assembler;
	}
}
