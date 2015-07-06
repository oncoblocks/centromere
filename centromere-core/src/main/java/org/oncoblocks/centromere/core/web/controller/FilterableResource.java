package org.oncoblocks.centromere.core.web.controller;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

/**
 * Allows filtering of the 'links' attribute using {@link org.oncoblocks.centromere.core.web.util.FilteringJackson2HttpMessageConverter}
 * 
 * @author woemler
 */

@Filterable
public class FilterableResource<T> extends Resource<T> {
	public FilterableResource(T content, Link... links) {
		super(content, links);
	}

	public FilterableResource(T content, Iterable<Link> links) {
		super(content, links);
	}
}
