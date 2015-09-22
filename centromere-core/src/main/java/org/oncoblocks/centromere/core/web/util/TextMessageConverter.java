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

import org.oncoblocks.centromere.core.model.Model;
import org.oncoblocks.centromere.core.web.controller.FilterableResource;
import org.oncoblocks.centromere.core.web.controller.ResponseEnvelope;
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
import java.util.Collection;

/**
 * @author woemler
 */
public class TextMessageConverter extends AbstractHttpMessageConverter<Object> {
	
	private String delimiter = "\t";
	private MediaType mediaType;

	public TextMessageConverter(MediaType supportedMediaType, String delimiter) {
		super(supportedMediaType);
		this.delimiter = delimiter;
		this.mediaType = supportedMediaType;
	}

	public TextMessageConverter(MediaType supportedMediaType) {
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
		
		if (o.getClass().equals(ResponseEnvelope.class)){
			o = ((ResponseEnvelope) o).getEntity();
		}
		if (o.getClass().equals(PageImpl.class)){
			o = ((Page) o).getContent();
		}
		
		if (o instanceof Collection<?>){
			for (Object entity: (Collection<?>) o){
				String entityString;
				try {
					entityString = printEntityRecord(entity, this.delimiter, showHeader);
				} catch (IllegalAccessException e){
					e.printStackTrace();
					entityString = "# Invalid record.";
				}
				writer.write(entityString);
				showHeader = false;
			}
		} else {
			String entityString;
			try {
				entityString = printEntityRecord(o, this.delimiter, true);
			} catch (IllegalAccessException e){
				e.printStackTrace();
				entityString = "# Invalid record.";
			}
			writer.write(entityString);
		}
		
		writer.close();
		
	}

	private String printEntityRecord(Object entity, String delimiter, boolean showHeader) 
			throws IllegalAccessException{
		StringBuffer buffer = new StringBuffer();
		if (showHeader){
			for (Field field: entity.getClass().getDeclaredFields()){
				buffer.append(field.getName() + delimiter);
			}
			buffer.append("\n");
		}
		for (Field field: entity.getClass().getDeclaredFields()){
			field.setAccessible(true);
			buffer.append(field.get(entity) + delimiter);
		}
		buffer.append("\n");
		return buffer.toString();
	}
	
}
