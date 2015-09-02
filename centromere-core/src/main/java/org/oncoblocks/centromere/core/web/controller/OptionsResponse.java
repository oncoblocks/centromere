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

import java.util.List;

/**
 * Response object for OPTIONS requests, which describes the API controller operations, parameters,
 *   and responses.
 * 
 * @author woemler
 */
public class OptionsResponse {

	private String description;
	private Class<?> model;
	private Class<?> queryParametersClass;
	private List<OptionsEndpointDescriptor> endpoints;

	public OptionsResponse() { }

	public OptionsResponse(
			Class<?> model,
			Class<?> queryParametersClass,
			List<OptionsEndpointDescriptor> endpoints) {
		this.model = model;
		this.queryParametersClass = queryParametersClass;
		this.endpoints = endpoints;
		this.description = String.format("Resource operations for model %s", model.getName());
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Class<?> getModel() {
		return model;
	}

	public void setModel(Class<?> model) {
		this.model = model;
	}

	public Class<?> getQueryParametersClass() {
		return queryParametersClass;
	}

	public void setQueryParametersClass(Class<?> queryParametersClass) {
		this.queryParametersClass = queryParametersClass;
	}

	public List<OptionsEndpointDescriptor> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(
			List<OptionsEndpointDescriptor> endpoints) {
		this.endpoints = endpoints;
	}
}
