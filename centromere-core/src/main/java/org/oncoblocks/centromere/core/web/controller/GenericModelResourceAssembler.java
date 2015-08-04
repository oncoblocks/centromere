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

package org.oncoblocks.centromere.core.web.controller;

import org.oncoblocks.centromere.core.model.ForeignKey;
import org.oncoblocks.centromere.core.model.Model;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Convenience class for automatically generating links for ???
 *   subclasses.  Uses the {@link org.oncoblocks.centromere.core.model.ForeignKey} annotation to 
 *   identify referenced fields and generate appropriate links, based on their relationship.
 * 
 * @author woemler
 */
public class GenericModelResourceAssembler<T extends Model> 
		extends ResourceAssemblerSupport<T, FilterableResource> {

	private final EntityLinks entityLinks;
	private final Class<T> model;
	
	public GenericModelResourceAssembler(Class<?> controllerClass, Class<T> model,
			EntityLinks entityLinks) {
		super(controllerClass, FilterableResource.class);
		this.model = model;
		this.entityLinks = entityLinks;
	}

	public FilterableResource toResource(T t) {
		FilterableResource<T> resource = new FilterableResource<T>(t);
		resource.add(entityLinks.linkToSingleResource(model, t.getId()).withSelfRel());
		for (Field field: model.getDeclaredFields()){
			if (field.isAnnotationPresent(ForeignKey.class)){
				field.setAccessible(true);
				ForeignKey fk = field.getAnnotation(ForeignKey.class);
				Class<?> relation = fk.type();
				String rel = !fk.relString().equals("") ? fk.relString() : field.getName();
				String param = !fk.qsParameter().equals("") ? fk.qsParameter() : field.getName();
				if (Collection.class.isAssignableFrom(field.getType()) || field.getType().isArray()){
					resource.add(new Link(entityLinks.linkFor(relation).toString() + "?" + param + "=" + t.getId()).withRel(rel));
				} else {
					try {
						resource.add(entityLinks.linkToSingleResource(relation, field.get(t)).withRel(rel));
					} catch (IllegalAccessException e) {
						// pass
					}
				}
			}
		}
		return resource;
	}
}
