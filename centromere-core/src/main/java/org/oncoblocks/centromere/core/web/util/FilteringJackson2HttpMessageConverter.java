package org.oncoblocks.centromere.core.web.util;

import org.oncoblocks.centromere.core.web.controller.ResponseEnvelope;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.util.Set;

/**
 * Uses {@link org.oncoblocks.centromere.core.web.controller.ResponseEnvelope} to identify filterable entities and 
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
	protected void writeInternal(Object object, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		ObjectMapper objectMapper = getObjectMapper();
		JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(outputMessage.getBody());

		try {

			if (this.prefixJson) {
				jsonGenerator.writeRaw("{} && ");
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
				
				objectMapper.setFilters(filters);
				objectMapper.writeValue(jsonGenerator, entity);

			} else if (object == null){
				jsonGenerator.writeNull();
			} else {
				FilterProvider filters = new SimpleFilterProvider().setFailOnUnknownId(false);
				objectMapper.setFilters(filters);
				objectMapper.writeValue(jsonGenerator, object);
			}

		} catch (JsonProcessingException e){
			e.printStackTrace();
			throw new HttpMessageNotWritableException("Could not write JSON: " + e.getMessage());
		}

	}
}
