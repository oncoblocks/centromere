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
import org.oncoblocks.centromere.core.repository.RepositoryOperations;
import org.oncoblocks.centromere.core.web.exceptions.RequestFailureException;
import org.oncoblocks.centromere.core.web.exceptions.ResourceNotFoundException;
import org.oncoblocks.centromere.core.web.query.QueryParameters;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Serializable;

/**
 * Extension of {@link org.oncoblocks.centromere.core.web.controller.BaseApiController} that allows for 
 *   PUT, POST, and DELETE operations.
 * 
 * @author woemler
 */
public class CrudApiController<
		T extends Model<ID>,
		ID extends Serializable,
		Q extends QueryParameters> 
		extends BaseApiController<T, ID, Q> {
	
	public CrudApiController(RepositoryOperations<T, ID> service,
			ResourceAssemblerSupport<T, FilterableResource> assembler) {
		super(service, assembler);
	}

	/**
	 * {@code POST /}
	 * Attempts to create a new record using the submitted entity. Throws an exception if the
	 *   entity already exists.
	 *
	 * @param entity entity representation to be persisted
	 * @return updated representation of the submitted entity
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, 
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<T> create(@RequestBody T entity) {
		T created = repository.insert(entity);
		if (created == null) throw new RequestFailureException(40003, "There was a problem creating the record.", "", "");
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	/**
	 * {@code POST /}
	 * Attempts to create a new record using the submitted entity. Throws an exception if the
	 *   entity already exists.
	 *
	 * @param entity entity representation to be persisted
	 * @return updated representation of the submitted entity
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, 
			produces = { HalMediaType.APPLICATION_JSON_HAL_VALUE })
	public ResponseEntity<FilterableResource<T>> createWithHal(@RequestBody T entity) {
		T created = repository.insert(entity);
		if (created == null) throw new RequestFailureException(40003, "There was a problem creating the record.", "", "");
		FilterableResource<T> resource = assembler.toResource(created);
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
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, 
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<T> update(@RequestBody T entity, @PathVariable ID id) {
		if (!repository.exists(id)) throw new ResourceNotFoundException();
		T updated = repository.update(entity);
		if (updated == null) throw new RequestFailureException(40004, "There was a problem updating the record.", "", "");
		return new ResponseEntity<>(updated, HttpStatus.CREATED);
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
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, 
			produces = { HalMediaType.APPLICATION_JSON_HAL_VALUE })
	public ResponseEntity<FilterableResource<T>> updateWithHal(@RequestBody T entity, @PathVariable ID id) {
		if (!repository.exists(id)) throw new ResourceNotFoundException();
		T updated = repository.update(entity);
		if (updated == null) throw new RequestFailureException(40004, "There was a problem updating the record.", "", "");
		FilterableResource<T> resource = assembler.toResource(updated);
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
	public ResponseEntity<?> delete(@PathVariable ID id) {
		repository.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
