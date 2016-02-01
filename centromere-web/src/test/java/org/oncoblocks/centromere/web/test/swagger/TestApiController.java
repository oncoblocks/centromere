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

package org.oncoblocks.centromere.web.test.swagger;

import org.oncoblocks.centromere.core.model.Model;
import org.oncoblocks.centromere.core.repository.RepositoryOperations;
import org.oncoblocks.centromere.web.test.models.EntrezGene;
import org.oncoblocks.centromere.web.util.ApiMediaTypes;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author woemler
 */
public abstract class TestApiController<T extends Model<ID>, ID extends Serializable> {
	
	private RepositoryOperations<T, ID> repository;

	public TestApiController(RepositoryOperations<T, ID> repository) {
		this.repository = repository;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET,
			produces = {ApiMediaTypes.APPLICATION_HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public HttpEntity<?> findById(@PathVariable ID id) {
		EntrezGene gene = new EntrezGene();
		return new ResponseEntity<>(gene, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, ApiMediaTypes.APPLICATION_HAL_JSON_VALUE})
	public HttpEntity<?> find(@RequestParam(value = "symbol", defaultValue = "AKT1") String geneSymbol) {
		return new ResponseEntity<>(new ArrayList<T>(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.HEAD)
	public HttpEntity<?> head() {
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST,
			produces = {MediaType.APPLICATION_JSON_VALUE, ApiMediaTypes.APPLICATION_HAL_JSON_VALUE})
	public HttpEntity<?> post() {
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.PUT,
			produces = {MediaType.APPLICATION_JSON_VALUE, ApiMediaTypes.APPLICATION_HAL_JSON_VALUE})
	public HttpEntity<?> put() {
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.DELETE,
			produces = {MediaType.APPLICATION_JSON_VALUE, ApiMediaTypes.APPLICATION_HAL_JSON_VALUE})
	public HttpEntity<?> delete() {
		return new ResponseEntity(HttpStatus.OK);
	}

}
