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
