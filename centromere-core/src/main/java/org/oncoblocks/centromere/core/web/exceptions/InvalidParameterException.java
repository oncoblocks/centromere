package org.oncoblocks.centromere.core.web.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown by invalid query string parameters for GET request queries.
 * 
 * @author woemler 
 */
public class InvalidParameterException extends RestException {
	public InvalidParameterException() {
		super(HttpStatus.BAD_REQUEST, 40001, "Invalid query string parameter.", "", "");
	}
}
