package org.oncoblocks.centromere.core.web.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author woemler
 */
public class DisabledEndpointException extends RestException {
	public DisabledEndpointException() {
		super(HttpStatus.NOT_FOUND, 40402, "The requested resource is not available.", "GET requests to this URL have been disabled.", "");
	}
}
