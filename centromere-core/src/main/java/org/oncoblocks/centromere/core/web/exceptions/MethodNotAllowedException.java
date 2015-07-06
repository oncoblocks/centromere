package org.oncoblocks.centromere.core.web.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author woemler 
 */
public class MethodNotAllowedException extends RestException {
	
	public MethodNotAllowedException(){
		super(HttpStatus.METHOD_NOT_ALLOWED, 40501, "The requested HTTP method is not allowed for this resource.", "", "");
	}
	
}
