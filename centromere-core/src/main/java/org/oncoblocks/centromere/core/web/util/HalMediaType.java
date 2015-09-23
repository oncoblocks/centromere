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

package org.oncoblocks.centromere.core.web.util;

import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

/**
 * Support class for defining and identifying HAL-supporting {@link MediaType} values, which will
 *   trigger Spring HATEOAS hypermedia annotation.
 * 
 * @author woemler
 */
public class HalMediaType {
	
	public static final String APPLICATION_HAL_JSON_VALUE = "application/hal+json";
	public static final MediaType APPLICATION_HAL_JSON = new MediaType("application", "hal+json");
	public static final String APPLICATION_HAL_XML_VALUE = "application/hal+xml";
	public static final MediaType APPLICATION_HAL_XML = new MediaType("application", "hal+xml");
	
	public static boolean isHalMediaType(String mediaType){
		return getHalMediaTypeValues().contains(mediaType);
	}
	
	public static boolean isHalMediaType(MediaType mediaType){
		return getHalMediaTypes().contains(mediaType);
	}
	
	public static List<String> getHalMediaTypeValues(){
		List<String> mediaTypes = new ArrayList<>();
		mediaTypes.add(APPLICATION_HAL_JSON_VALUE);
		mediaTypes.add(APPLICATION_HAL_XML_VALUE);
		return mediaTypes;
	}
	
	public static List<MediaType> getHalMediaTypes(){
		List<MediaType> mediaTypes = new ArrayList<>();
		mediaTypes.add(APPLICATION_HAL_JSON);
		mediaTypes.add(APPLICATION_HAL_XML);
		return mediaTypes;
	}
	
}
