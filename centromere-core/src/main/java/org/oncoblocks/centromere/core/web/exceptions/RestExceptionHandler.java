package org.oncoblocks.centromere.core.web.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Exception handler for {@link org.oncoblocks.centromere.core.web.exceptions.RestException} errors. Should be 
 *   implemented as {@link org.springframework.web.bind.annotation.ControllerAdvice}.
 * 
 * @author woemler
 */
public abstract class RestExceptionHandler {

	/**
	 * Catches a {@link org.oncoblocks.centromere.core.web.exceptions.RestException} thrown bt a web service
	 *   controller and returns an informative message.  
	 * 
	 * @param ex {@link org.oncoblocks.centromere.core.web.exceptions.RestException}
	 * @param request {@link org.springframework.web.context.request.WebRequest}
	 * @return {@link org.oncoblocks.centromere.core.web.exceptions.RestError}
	 */
	@ExceptionHandler(value = { RestException.class })
	public ResponseEntity<RestError> handleRestException(RestException ex, WebRequest request){
		RestError restError = ex.getRestError();
		return new ResponseEntity<RestError>(restError, restError.getStatus());
	}
	
}
