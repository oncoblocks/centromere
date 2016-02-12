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

import org.oncoblocks.centromere.core.model.Model;
import org.oncoblocks.centromere.web.controller.FilterableResource;
import org.oncoblocks.centromere.web.controller.ResponseEnvelope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Message converter that takes web service response data and converts it to delimited-text in a
 *   tabular format.  Supports field filtering using {@link org.oncoblocks.centromere.web.controller.ResponseEnvelope} attributes.
 * 
 * @author woemler
 */
public class FilteringTextMessageConverter extends AbstractHttpMessageConverter<Object> {
	
	private String delimiter = "\t";
	private final MediaType mediaType;

	public FilteringTextMessageConverter(MediaType supportedMediaType, String delimiter) {
		super(supportedMediaType);
		this.delimiter = delimiter;
		this.mediaType = supportedMediaType;
	}

	public FilteringTextMessageConverter(MediaType supportedMediaType) {
		super(supportedMediaType);
		this.mediaType = supportedMediaType;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	@Override 
	protected boolean supports(Class<?> aClass) {
		return Resource.class.equals(aClass)
				|| FilterableResource.class.equals(aClass)
				|| Resources.class.equals(aClass)
				|| PagedResources.class.equals(aClass)
				|| ResourceSupport.class.equals(aClass)
				|| Model.class.equals(aClass)
				|| ResponseEnvelope.class.equals(aClass);
	}

	@Override 
	protected Object readInternal(Class<?> aClass, HttpInputMessage httpInputMessage)
			throws IOException, HttpMessageNotReadableException {
		return null;
	}

	@Override 
	protected void writeInternal(Object o, HttpOutputMessage httpOutputMessage)
			throws IOException, HttpMessageNotWritableException {
		
		httpOutputMessage.getHeaders().setContentType(this.mediaType);
		OutputStream out = httpOutputMessage.getBody();
		PrintWriter writer = new PrintWriter(out);
		boolean showHeader = true;
		Set<String> includedFields = new HashSet<>();
		Set<String> excludedFields = new HashSet<>();
		
		if (o.getClass().equals(ResponseEnvelope.class)){
			includedFields = ((ResponseEnvelope) o).getFieldSet();
			excludedFields = ((ResponseEnvelope) o).getExclude();
			o = ((ResponseEnvelope) o).getEntity();
		}
		if (o.getClass().equals(PageImpl.class)){
			o = ((Page) o).getContent();
		}
		
		if (!(o instanceof Collection<?>)) {
			o = Arrays.asList(o);
		}
		for (Object entity: (Collection<?>) o){
			String entityString;
			try {
				StringBuilder buffer = new StringBuilder();
				if (showHeader){
					for (Field field: entity.getClass().getDeclaredFields()){
						if (includedFields != null && !includedFields.isEmpty()){
							if (includedFields.contains(field.getName())){
								buffer.append(field.getName()).append(delimiter);
							}
						} else if (excludedFields != null && !excludedFields.isEmpty()){
							if (!excludedFields.contains(field.getName())){
								buffer.append(field.getName()).append(delimiter);
							}
						} else {
							buffer.append(field.getName()).append(delimiter);
						}
					}
					buffer.append("\n");
				}
				for (Field field: entity.getClass().getDeclaredFields()){
					field.setAccessible(true);
					if (includedFields != null && !includedFields.isEmpty()){
						if (includedFields.contains(field.getName())){
							buffer.append(field.get(entity)).append(delimiter);
						}
					} else if (excludedFields != null && !excludedFields.isEmpty()){
						if (!excludedFields.contains(field.getName())){
							buffer.append(field.get(entity)).append(delimiter);
						}
					} else {
						buffer.append(field.get(entity)).append(delimiter);
					}
				}
				buffer.append("\n");
				entityString = buffer.toString();
			} catch (IllegalAccessException e){
				e.printStackTrace();
				entityString = "# Invalid record.";
			}
			writer.write(entityString);
			showHeader = false;
		}
		
		
		writer.close();
		
	}

}
