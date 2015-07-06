package org.oncoblocks.centromere.core.web.exceptions;

import org.springframework.http.HttpStatus;

/**
 * 404 error when a single resource request returns null
 * 
 * @author woemler 
 */
public class ResourceNotFoundException extends RestException {
	public ResourceNotFoundException(){
		super(HttpStatus.NOT_FOUND, 40401, "The requested resource could not be found.", "", "");
	}
}
