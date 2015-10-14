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

import org.oncoblocks.centromere.core.model.Model;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.io.Serializable;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * @author woemler
 */
public class BasicResourceAssembler<T extends Model<ID>, ID extends Serializable> 
		extends ResourceAssemblerSupport<T, FilterableResource> {
	
	private Class<?> controllerClass;

	public BasicResourceAssembler(Class<?> controllerClass,
			Class<FilterableResource> resourceType) {
		super(controllerClass, resourceType);
		this.controllerClass = controllerClass;
	}

	@Override 
	public FilterableResource toResource(T t) {
		FilterableResource<T> resource = new FilterableResource<>(t);
		resource.add(linkTo(controllerClass).slash(t.getId()).withSelfRel());
		return resource;
	}
}
