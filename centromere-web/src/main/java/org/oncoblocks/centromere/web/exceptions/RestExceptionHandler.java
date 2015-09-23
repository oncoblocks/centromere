/*
 * Copyright 2015 William Oemler, Blueprint Medicines
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

package org.oncoblocks.centromere.web.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Exception handler for {@link org.oncoblocks.centromere.web.exceptions.RestException} errors. Should be 
 *   implemented as {@link org.springframework.web.bind.annotation.ControllerAdvice}.
 * 
 * @author woemler
 */
public abstract class RestExceptionHandler {

	/**
	 * Catches a {@link org.oncoblocks.centromere.web.exceptions.RestException} thrown bt a web service
	 *   controller and returns an informative message.  
	 * 
	 * @param ex {@link org.oncoblocks.centromere.web.exceptions.RestException}
	 * @param request {@link WebRequest}
	 * @return {@link org.oncoblocks.centromere.web.exceptions.RestError}
	 */
	@ExceptionHandler(value = { org.oncoblocks.centromere.web.exceptions.RestException.class })
	public ResponseEntity<org.oncoblocks.centromere.web.exceptions.RestError> handleRestException(
			org.oncoblocks.centromere.web.exceptions.RestException ex, WebRequest request){
		org.oncoblocks.centromere.web.exceptions.RestError restError = ex.getRestError();
		return new ResponseEntity<org.oncoblocks.centromere.web.exceptions.RestError>(restError, restError.getStatus());
	}
	
}
