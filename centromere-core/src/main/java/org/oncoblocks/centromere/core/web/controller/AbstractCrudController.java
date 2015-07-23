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
import org.oncoblocks.centromere.core.web.exceptions.RequestFailureException;
import org.oncoblocks.centromere.core.web.exceptions.ResourceNotFoundException;
import org.oncoblocks.centromere.core.web.service.ServiceOperations;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Set;

/**
 * Implementation of standard web service controller operations, with separation 
 *   between request mapping and operation execution, to allow easier overriding
 *   of method functionality without creating mapping conflicts. Implementations 
 *   of the {@link ControllerOperations#find} method will vary depending on means
 *   of performing queries.
 * 
 * @author woemler
 */
public abstract class AbstractCrudController<T extends Model<ID>, ID extends Serializable> {
	
	protected ServiceOperations<T, ID> service;
	protected ResourceAssemblerSupport<T, FilterableResource> assembler;

	public AbstractCrudController(ServiceOperations<T, ID> service, 
			ResourceAssemblerSupport<T, FilterableResource> assembler) {
		this.service = service;
		this.assembler = assembler;
	}

	/**
	 * {@code HEAD /**}
	 * Performs a test on the resource endpoints availability.
	 * 
	 * @return 
	 */
	@RequestMapping(value = { "", "/**" }, method = RequestMethod.HEAD)
	public HttpEntity head(){
		return new ResponseEntity(HttpStatus.OK);
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
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public HttpEntity findById(@PathVariable ID id, @RequestParam(required = false) Set<String> fields, 
			@RequestParam(required = false) Set<String> exclude) {
		return doFindById(id, fields, exclude);
	}

	/**
	 * {@link AbstractCrudController#findById}
	 */
	protected HttpEntity doFindById(ID id, Set<String> fields, Set<String> exclude) {
		T entity = service.findById(id);
		if (entity == null) throw new ResourceNotFoundException();
		FilterableResource resource = assembler.toResource(entity);
		ResponseEnvelope<FilterableResource> envelope = new ResponseEnvelope<>(resource, fields, exclude);
		return new ResponseEntity<>(envelope, HttpStatus.OK);
	}

	/**
	 * {@code POST /}
	 * Attempts to create a new record using the submitted entity. Throws an exception if the
	 *   entity already exists.
	 *
	 * @param entity entity representation to be persisted
	 * @return updated representation of the submitted entity
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public HttpEntity create(@RequestBody T entity) {
		return doCreate(entity);
	}

	/**
	 * {@link AbstractCrudController#create}
	 */
	protected ResponseEntity doCreate(T entity){
		T created = service.insert(entity);
		if (created == null) throw new RequestFailureException(40003, "There was a problem creating the record.", "", "");
		FilterableResource resource = assembler.toResource(created);
		return new ResponseEntity<>(resource, HttpStatus.CREATED);
	}

	/**
	 * {@code PUT /{id}}
	 * Attempts to update an existing entity record, replacing it with the submitted entity. Throws
	 *   an exception if the target entity does not exist.
	 *
	 * @param entity entity representation to update.
	 * @param id primary ID of the target entity
	 * @return updated representation of the submitted entity.
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public HttpEntity update(@RequestBody T entity, @PathVariable ID id) {
		return doUpdate(entity, id);
	}

	/**
	 * {@link AbstractCrudController#update}
	 */
	protected ResponseEntity doUpdate(T entity, ID id){
		if (!service.exists(id)) throw new ResourceNotFoundException();
		T updated = service.update(entity);
		if (updated == null) throw new RequestFailureException(40004, "There was a problem updating the record.", "", "");
		FilterableResource resource = assembler.toResource(updated);
		return new ResponseEntity<>(resource, HttpStatus.CREATED);
	}

	/**
	 * {@code DELETE /{id}}
	 * Attempts to delete the an entity identified by the submitted primary ID.
	 *
	 * @param id primary ID of the target record.
	 * @return {@link org.springframework.http.HttpStatus} indicating success or failure.
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public HttpEntity delete(@PathVariable ID id) {
		return doDelete(id);
	}

	/**
	 * {@link AbstractCrudController#delete}
	 */
	protected ResponseEntity doDelete(ID id){
		service.delete(id);
		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * {@code OPTIONS /}
	 * Returns an information about the endpoint and available parameters.
	 *
	 * @return
	 */
	@RequestMapping(method = RequestMethod.OPTIONS)
	public HttpEntity options() {
		return doOptions();
	}

	/**
	 * {@link AbstractCrudController#options}
	 */
	public HttpEntity doOptions() {
		return null; //TODO
	}
	
}
