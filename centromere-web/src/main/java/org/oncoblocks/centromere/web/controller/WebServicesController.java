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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Basic interface, defining all of the required methods for a web services controller.  
 * 
 * @author woemler
 */
public interface WebServicesController<T extends Model<ID>, ID extends Serializable> {

	/**
	 * {@code  GET  /{id} } - Fetches a single record by its primary ID.
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	HttpEntity<?> findById(ID id, HttpServletRequest request);

	/**
	 * {@code  GET  /distinct } - Fetches a list of distinct field values.
	 * 
	 * @param field
	 * @param request
	 * @return
	 */
	HttpEntity<?> findDistinct(String field, HttpServletRequest request);

	/**
	 * {@code  GET  / } - Fetches one or more records, with optional filtering, paging, or 
	 *   sorting applied.
	 * 
	 * @param pageable
	 * @param pagedResourcesAssembler
	 * @param request
	 * @return
	 */
	HttpEntity<?> find(Pageable pageable, PagedResourcesAssembler<T> pagedResourcesAssembler, HttpServletRequest request);

	/**
	 * {@code  HEAD  /** } - Tests whether the URL endpoint is available.
	 * 
	 * @param request
	 * @return
	 */
	HttpEntity<?> head(HttpServletRequest request);

	/**
	 * {@code  OPTIONS  /** } - Returns basic request information about URL the endpoint.
	 * 
	 * @param request
	 * @return
	 */
	HttpEntity<?> options(HttpServletRequest request);

	/**
	 * {@code  POST  / } - Attempts to create a new entity record.
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	HttpEntity<?> create(T entity, HttpServletRequest request);

	/**
	 * {@code  PUT  /{id} } - Attempts to update an existing entity record.
	 * 
	 * @param entity
	 * @param id
	 * @param request
	 * @return
	 */
	HttpEntity<?> update(T entity, ID id, HttpServletRequest request);

	/**
	 * {@code  DELETE  /{id} } - Attempts to delete a single entity record.
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	HttpEntity<?> delete(ID id, HttpServletRequest request);
	
}
