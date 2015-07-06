package org.oncoblocks.centromere.core.web.exceptions;

import org.springframework.http.HttpStatus;

/**
 * 406 error
 * 
 * @author woemler 
 */
public class MalformedEntityException extends RestException {
	public MalformedEntityException(Integer code, String message, String developerMessage, String moreInfoUrl) {
		super(HttpStatus.NOT_ACCEPTABLE, code, message, developerMessage, moreInfoUrl);
	}
}
