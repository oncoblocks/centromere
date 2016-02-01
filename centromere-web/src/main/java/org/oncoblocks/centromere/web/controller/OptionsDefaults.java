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

package org.oncoblocks.centromere.web.controller;

import org.oncoblocks.centromere.web.query.AnnotatedQueryParameters;
import org.oncoblocks.centromere.web.util.ApiMediaTypes;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.*;

/**
 * @author woemler
 */
public class OptionsDefaults {
	
	public static Map<String,String[]> getDefaultHeaders(){
		Map<String,String[]> headers = new HashMap<>();
		headers.put("Accept-Encoding", new String[]{ "gzip,deflate" });
		return headers;
	}
	
	public static List<org.oncoblocks.centromere.web.controller.OptionsEndpointDescriptor> getDefaultGetDescriptors(
			Class<?> model, Class<? extends org.oncoblocks.centromere.web.query.AnnotatedQueryParameters> queryParametersClass) 
			throws InstantiationException, IllegalAccessException {
		List<org.oncoblocks.centromere.web.controller.OptionsEndpointDescriptor> descriptors = new ArrayList<>();
		descriptors.add(new org.oncoblocks.centromere.web.controller.OptionsEndpointDescriptor(
				HttpMethod.GET, 
				"/{id}",
				Arrays.asList(new String[]{ MediaType.APPLICATION_JSON_VALUE }),
				OptionsDefaults.getDefaultHeaders(),
				new AnnotatedQueryParameters(),
				model.newInstance(),
				Arrays.asList(new HttpStatus[] {HttpStatus.OK, HttpStatus.NOT_FOUND})
		));
		descriptors.add(new OptionsEndpointDescriptor(
				HttpMethod.GET,
				"/{id}",
				Arrays.asList(new String[]{ ApiMediaTypes.APPLICATION_HAL_JSON_VALUE}),
				OptionsDefaults.getDefaultHeaders(),
				new AnnotatedQueryParameters(),
				new FilterableResource<>(model.newInstance()),
				Arrays.asList(new HttpStatus[] {HttpStatus.OK, HttpStatus.NOT_FOUND})
		));
		descriptors.add(new OptionsEndpointDescriptor(
				HttpMethod.GET,
				"",
				Arrays.asList(new String[]{ MediaType.APPLICATION_JSON_VALUE }),
				OptionsDefaults.getDefaultHeaders(),
				new AnnotatedQueryParameters(),
				Arrays.asList(new Object[] {model.newInstance()}),
				Arrays.asList(new HttpStatus[] {HttpStatus.OK})
		));
		descriptors.add(new OptionsEndpointDescriptor(
				HttpMethod.GET,
				"",
				Arrays.asList(new String[]{ ApiMediaTypes.APPLICATION_HAL_JSON_VALUE}),
				OptionsDefaults.getDefaultHeaders(),
				new AnnotatedQueryParameters(),
				Arrays.asList(new FilterableResource[]{ new FilterableResource<>(model.newInstance())}),
				Arrays.asList(new HttpStatus[] {HttpStatus.OK})
		));
		descriptors.add(new OptionsEndpointDescriptor(
				HttpMethod.GET,
				"?page={int}&size={int}&sort={string,[asc,desc]}",
				Arrays.asList(new String[]{ MediaType.APPLICATION_JSON_VALUE }),
				OptionsDefaults.getDefaultHeaders(),
				new AnnotatedQueryParameters(),
				new PageImpl(Arrays.asList(new Object[]{ model.newInstance() })),
				Arrays.asList(new HttpStatus[] {HttpStatus.OK})
		));
		descriptors.add(new OptionsEndpointDescriptor(
				HttpMethod.GET,
				"?page={int}&size={int}&sort={string,[asc,desc]}",
				Arrays.asList(new String[]{ ApiMediaTypes.APPLICATION_HAL_JSON_VALUE}),
				OptionsDefaults.getDefaultHeaders(),
				new AnnotatedQueryParameters(),
				new PagedResources(
						Arrays.asList(new FilterableResource[]{ new FilterableResource<>(model.newInstance())}),
						new PagedResources.PageMetadata(0,0,0)
				),
				Arrays.asList(new HttpStatus[] {HttpStatus.OK})
		));
		return descriptors;
	}
	
}
