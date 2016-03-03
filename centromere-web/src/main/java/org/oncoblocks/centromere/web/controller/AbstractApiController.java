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
import org.oncoblocks.centromere.web.util.ApiMediaTypes;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Base abstract implementation of {@link WebServicesController} for GET, HEAD, and OPTIONS requests.  
 *   Supports dynamic queries of repository resources using annotated {@link Model} classes,
 *   field filtering, pagination, and hypermedia support.
 * 
 * @author woemler
 */
public abstract class AbstractApiController<T extends Model<ID>, ID extends Serializable> 
		implements WebServicesController<T,ID>, ApplicationContextAware {

	private final RepositoryOperations<T, ID> repository;
	private final ResourceAssemblerSupport<T, FilterableResource> assembler;
	private final Class<T> model;
	private ApplicationContext applicationContext;
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
	 * @return {@code T} instance
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET,
			produces = { ApiMediaTypes.APPLICATION_HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE, 
					ApiMediaTypes.APPLICATION_HAL_XML_VALUE, MediaType.APPLICATION_XML_VALUE, 
					MediaType.TEXT_PLAIN_VALUE })
	public HttpEntity<?> findById(
			@PathVariable ID id,
			HttpServletRequest request
	) {
		Set<String> fields = RequestUtils.getFilteredFieldsFromRequest(request);
		Set<String> exclude = RequestUtils.getExcludedFieldsFromRequest(request);
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
		List<QueryCriteria> queryCriterias = RequestUtils.getQueryCriteriaFromRequest(model, request);
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
			@PageableDefault(size = 1000) Pageable pageable,
			PagedResourcesAssembler<T> pagedResourcesAssembler, 
			HttpServletRequest request)
	{
		ResponseEnvelope envelope;
		Set<String> fields = RequestUtils.getFilteredFieldsFromRequest(request);
		Set<String> exclude = RequestUtils.getExcludedFieldsFromRequest(request);
		pageable = RequestUtils.remapPageable(pageable, model);
		Map<String,String[]> parameterMap = request.getParameterMap();
		List<QueryCriteria> criterias = RequestUtils.getQueryCriteriaFromRequest(model, request);
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
	public HttpEntity<?> head(HttpServletRequest request){
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * {@code OPTIONS /}
	 * Returns an information about the endpoint and available parameters.
	 * TODO
	 *
	 * @return
	 */
	@RequestMapping(method = RequestMethod.OPTIONS)
	public HttpEntity<?> options(HttpServletRequest request) {
		return new ResponseEntity<>(HttpStatus.OK);
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

	@Autowired 
	public void setApplicationContext(
			ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
