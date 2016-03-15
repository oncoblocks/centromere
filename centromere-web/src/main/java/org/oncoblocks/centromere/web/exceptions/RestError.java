/*
 * Copyright 2016 William Oemler, Blueprint Medicines
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

import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

/**
 * Simple representation of a web services exception.  Returns to the user an HTTP status code,
 *   API-specific error code, user message, developer message, and URL for more information.
 * 
 * @author woemler 
 */
public class RestError {

	private final HttpStatus status;
	private final Integer code;
	private final String message;
	private final String developerMessage;
	private final String moreInfoUrl;

	public RestError(HttpStatus status, Integer code, String message, String developerMessage,
			String moreInfoUrl) {
		Assert.notNull(status,"HttpStatus argument cannot be null.");
		this.status = status;
		this.code = code;
		this.message = message;
		this.developerMessage = developerMessage;
		this.moreInfoUrl = moreInfoUrl;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public String getMoreInfoUrl() {
		return moreInfoUrl;
	}
	
}
