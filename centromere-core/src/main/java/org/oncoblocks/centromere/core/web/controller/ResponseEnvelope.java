package org.oncoblocks.centromere.core.web.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

/**
 * Wrapper for GET request responses to allow for field filtering via 
 *   {@link org.oncoblocks.centromere.core.web.util.FilteringJackson2HttpMessageConverter}
 * 
 * @author woemler 
 */
public class ResponseEnvelope<T> {

	private Set<String> fieldSet;
	private Set<String> exclude;
	private T entity;

	public ResponseEnvelope(T entity) {
		this.entity = entity;
	}

	public ResponseEnvelope(T entity, Set<String> fieldSet, Set<String> exclude) {
		this.fieldSet = fieldSet;
		this.exclude = exclude;
		this.entity = entity;
	}

	public T getEntity() {
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
