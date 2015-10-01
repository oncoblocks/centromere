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

package org.oncoblocks.centromere.web.controller;

import org.oncoblocks.centromere.core.model.Model;
import org.oncoblocks.centromere.core.repository.RepositoryOperations;
import org.oncoblocks.centromere.web.exceptions.MethodNotAllowedException;
import org.oncoblocks.centromere.web.query.QueryParameters;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Serializable;

/**
 * Extension of {@link BaseApiController} that 
 *   throws a {@link MethodNotAllowedException} when
 *   POST, PUT, or DELETE operations are attempted.
 * 
 * @author woemler
 */
public class ReadOnlyApiController<
		T extends Model<ID>, 
		ID extends Serializable, 
		Q extends QueryParameters> extends BaseApiController<T, ID, Q>  {

	public ReadOnlyApiController(RepositoryOperations<T, ID> repository,
			ResourceAssemblerSupport<T, FilterableResource> assembler) {
		super(repository, assembler);
	}

	/**
	 * {@code POST /}
	 * Attempts to create a new record using the submitted entity. Throws an exception if the
	 *   entity already exists.
	 *
	 * @return updated representation of the submitted entity
	 */
	@RequestMapping(value = {"", "/**" }, method = RequestMethod.POST)
	public void create() {
		throw new MethodNotAllowedException();
	}

	/**
	 * {@code PUT /{id}}
	 * Attempts to update an existing entity record, replacing it with the submitted entity. Throws
	 *   an exception if the target entity does not exist.
	 *
	 * @return updated representation of the submitted entity.
	 */
	@RequestMapping(value = {"", "/**" }, method = RequestMethod.PUT)
	public void update() {
		throw new MethodNotAllowedException();
	}

	/**
	 * {@code DELETE /{id}}
	 * Attempts to delete the an entity identified by the submitted primary ID.
	 *
	 * @return {@link org.springframework.http.HttpStatus} indicating success or failure.
	 */
	@RequestMapping(value = {"", "/**" }, method = RequestMethod.DELETE)
	public void delete() {
		throw new MethodNotAllowedException();
	}
	
}
