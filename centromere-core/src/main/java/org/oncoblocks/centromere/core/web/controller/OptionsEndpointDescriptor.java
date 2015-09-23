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

package org.oncoblocks.centromere.core.web.controller;

import org.oncoblocks.centromere.core.web.query.AnnotatedQueryParameters;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

/**
 * Describes permitted operations, query parameters, headers, and response objects for a single
 *   web service endpoint, where an endpoint is defined as a unique combination of URL, HTTP method, 
 *   and response media types.
 * 
 * @author woemler
 */
public class OptionsEndpointDescriptor {
	
	private HttpMethod method;
	private String url;
	private List<String> mediaTypes;
	private Map<String, String[]> headers;
	private AnnotatedQueryParameters parameters;
	private Object response;
	private List<HttpStatus> statuses;

	public OptionsEndpointDescriptor() { }

	public OptionsEndpointDescriptor(HttpMethod method, String url,
			List<String> mediaTypes, Map<String, String[]> headers,
			AnnotatedQueryParameters parameters, Object response, List<HttpStatus> statuses) {
		this.method = method;
		this.url = url;
		this.mediaTypes = mediaTypes;
		this.headers = headers;
		this.parameters = parameters;
		this.response = response;
		this.statuses = statuses;
	}
	
	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getMediaTypes() {
		return mediaTypes;
	}

	public void setMediaTypes(List<String> mediaTypes) {
		this.mediaTypes = mediaTypes;
	}

	public Map<String, String[]> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String[]> headers) {
		this.headers = headers;
	}

	public AnnotatedQueryParameters getParameters() {
		return parameters;
	}

	public void setParameters(AnnotatedQueryParameters parameters) {
		this.parameters = parameters;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

	public List<HttpStatus> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<HttpStatus> statuses) {
		this.statuses = statuses;
	}
}
