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

import org.oncoblocks.centromere.web.util.FilteringJackson2HttpMessageConverter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

/**
 * Allows filtering of the 'links' attribute using {@link FilteringJackson2HttpMessageConverter}
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
