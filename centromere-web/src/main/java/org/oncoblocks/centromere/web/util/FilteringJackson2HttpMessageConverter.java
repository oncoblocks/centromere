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

package org.oncoblocks.centromere.web.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.oncoblocks.centromere.web.controller.ResponseEnvelope;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * Uses {@link org.oncoblocks.centromere.web.controller.ResponseEnvelope} to identify filterable entities and 
 *   filters or includes fields based upon request parameters.
 * 
 * @author woemler 
 */

public class FilteringJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

	private boolean prefixJson = false;

	@Override
	public void setPrefixJson(boolean prefixJson) {
		this.prefixJson = prefixJson;
		super.setPrefixJson(prefixJson);
	}

	@Override
	protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		ObjectMapper objectMapper = getObjectMapper();
		JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(outputMessage.getBody());

		try {

			if (this.prefixJson) {
				jsonGenerator.writeRaw(")]}', ");
			}

			if (object instanceof ResponseEnvelope) {
				
				ResponseEnvelope envelope = (ResponseEnvelope) object;
				Object entity = envelope.getEntity();
				Set<String> fieldSet = envelope.getFieldSet();
				Set<String> exclude = envelope.getExclude();
				FilterProvider filters = null;
				
				if (fieldSet != null && !fieldSet.isEmpty()) {
					if (entity instanceof ResourceSupport){
						fieldSet.add("content"); // Don't filter out the wrapped content.
					}
					filters = new SimpleFilterProvider()
							.addFilter("fieldFilter", SimpleBeanPropertyFilter.filterOutAllExcept(fieldSet))
								.setFailOnUnknownId(false);
				} else if (exclude != null && !exclude.isEmpty()) {
					filters = new SimpleFilterProvider()
							.addFilter("fieldFilter", SimpleBeanPropertyFilter.serializeAllExcept(exclude))
								.setFailOnUnknownId(false);
				} else {
					filters = new SimpleFilterProvider()
							.addFilter("fieldFilter", SimpleBeanPropertyFilter.serializeAllExcept())
								.setFailOnUnknownId(false);
				}
				
				objectMapper.setFilterProvider(filters);
				objectMapper.writeValue(jsonGenerator, entity);

			} else if (object == null){
				jsonGenerator.writeNull();
			} else {
				FilterProvider filters = new SimpleFilterProvider().setFailOnUnknownId(false);
				objectMapper.setFilterProvider(filters);
				objectMapper.writeValue(jsonGenerator, object);
			}

		} catch (JsonProcessingException e){
			e.printStackTrace();
			throw new HttpMessageNotWritableException("Could not write JSON: " + e.getMessage());
		}

	}
}
