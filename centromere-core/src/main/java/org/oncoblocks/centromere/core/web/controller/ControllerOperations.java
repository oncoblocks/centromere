package org.oncoblocks.centromere.core.web.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Standard web service controller methods.
 * 
 * @author woemler
 */
public interface ControllerOperations<T, ID extends Serializable> {

	/**
	 * {@code GET /{id}}
	 * Fetches a single record by its primary ID and returns it, or a {@code Not Found} exception if not.
	 * 
	 * @param id primary ID for the target record.
	 * @param fields set of field names to be included in response object.
	 * @param exclude set of field names to be excluded from the response object.
	 * @return {@code T} instance
	 */
	public HttpEntity findById(ID id, Iterable<String> fields, Iterable<String> exclude);

	/**
	 * {@code GET /}
	 * Fetches one or more records based on the input search parameters.
	 * 
	 * @param args query object
	 * @param fields set of field names to be included in response object.
	 * @param exclude set of field names to be excluded from the response object.
	 * @param pageable {@link org.springframework.data.domain.Pageable}
	 * @param resourcesAssembler {@link org.springframework.data.web.PagedResourcesAssembler}
	 * @param request {@link javax.servlet.http.HttpServletRequest}
	 * @return one or more {@code T} instances wrapped in a response object.
	 */
	public HttpEntity find(Object args, Iterable<String> fields, Iterable<String> exclude,
			Pageable pageable, PagedResourcesAssembler resourcesAssembler, HttpServletRequest request);

	/**
	 * {@code POST /}
	 * Attempts to create a new record using the submitted entity. Throws an exception if the
	 *   entity already exists.
	 * 
	 * @param entity entity representation to be persisted
	 * @return updated representation of the submitted entity
	 */
	public HttpEntity create(T entity);

	/**
	 * {@code PUT /{id}}
	 * Attempts to update an existing entity record, replacing it with the submitted entity. Throws
	 *   an exception if the target entity does not exist.
	 * 
	 * @param entity entity representation to update.
	 * @param id primary ID of the target entity
	 * @return updated representation of the submitted entity.
	 */
	public HttpEntity update(T entity, ID id);

	/**
	 * {@code DELETE /{id}}
	 * Attempts to delete the an entity identified by the submitted primary ID.
	 * 
	 * @param id primary ID of the target record.
	 * @return {@link org.springframework.http.HttpStatus} indicating success or failure.
	 */
	public HttpEntity delete(ID id);

	/**
	 * {@code OPTIONS /}
	 * Returns an information about the endpoint and available parameters.
	 * 
	 * @return
	 */
	public HttpEntity options();
}
