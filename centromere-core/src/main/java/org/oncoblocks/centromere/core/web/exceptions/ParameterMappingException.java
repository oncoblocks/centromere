package org.oncoblocks.centromere.core.web.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author woemler 
 */
public class ParameterMappingException extends RestException {
	public ParameterMappingException(String developerMessage, String moreInfoUrl) {
		super(HttpStatus.BAD_REQUEST, 40002, "One of the query string parameters could not be mapped to the requested domain model.", developerMessage, moreInfoUrl);
	}
}
