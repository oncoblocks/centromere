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

package org.oncoblocks.centromere.web.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

/**
 * Wrapper for GET request responses to allow for field filtering via 
 *   {@link org.oncoblocks.centromere.web.util.FilteringJackson2HttpMessageConverter}
 * 
 * @author woemler 
 */
public class ResponseEnvelope {

	private Set<String> fieldSet;
	private Set<String> exclude;
	private Object entity;

	public ResponseEnvelope(Object entity) {
		this.entity = entity;
	}

	public ResponseEnvelope(Object entity, Set<String> fieldSet, Set<String> exclude) {
		this.fieldSet = fieldSet;
		this.exclude = exclude;
		this.entity = entity;
	}

	public Object getEntity() {
		return entity;
	}

	@JsonIgnore
	public Set<String> getFieldSet() {
		return fieldSet;
	}

	@JsonIgnore
	public Set<String> getExclude() {
		return exclude;
	}

	public void setExclude(Set<String> exclude) {
		this.exclude = exclude;
	}

	public void setFieldSet(Set<String> fieldSet) {
		this.fieldSet = fieldSet;
	}

	public void setFields(String fields) {
		Set<String> fieldSet = new HashSet<String>();
		if (fields != null) {
			for (String field : fields.split(",")) {
				fieldSet.add(field);
			}
		}
		this.fieldSet = fieldSet;
	}
}
