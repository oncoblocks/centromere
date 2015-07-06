package org.oncoblocks.centromere.core.web.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Generic 400 error
 * 
 * @author woemler 
 */
public class RequestFailureException extends RestException {
	public RequestFailureException(Integer code, String message, String developerMessage, String moreInfoUrl) {
		super(HttpStatus.BAD_REQUEST, code, message, developerMessage, moreInfoUrl);
	}
}
