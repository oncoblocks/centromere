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

package org.oncoblocks.centromere.web.controller;

import org.oncoblocks.centromere.core.model.ForeignKey;
import org.oncoblocks.centromere.core.model.Model;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author woemler
 */
public class DefaultResourceAssembler<T extends Model<?>> 
		extends ResourceAssemblerSupport<T, FilterableResource> {

	private EntityLinks entityLinks;
	private Class<?> modelController;
	private Class<T> model;
	
	public DefaultResourceAssembler(Class<?> controllerClass, Class<T> model,  EntityLinks entityLinks) {
		super(controllerClass, FilterableResource.class);
		this.modelController = controllerClass;
		this.model = model;
		this.entityLinks = entityLinks;
	}

	@Override 
	public FilterableResource toResource(T t) {
		FilterableResource<T> resource = new FilterableResource<>(t);
		resource.add(entityLinks.linkToSingleResource(model, t.getId()).withSelfRel());
		List<Link> links = addLinks(new ArrayList<>());
		links.addAll(this.addForeignKeyLinks(t));
		resource.add(links);
		return resource;
	}
	
	private List<Link> addForeignKeyLinks(T t){
		List<Link> links = new ArrayList<>();
		for (Field field: model.getDeclaredFields()){
			ForeignKey fk = field.getAnnotation(ForeignKey.class);
			if (fk == null) continue;
			if (fk.value() == null) throw new RuntimeException(String.format("ForeignKey annotation for " 
					+ "class %s does not contain any class reference!", model.getName()));
			Class<?> fkCLass = fk.value();
			String relName = fk.rel().equals("") ? field.getName() : fk.rel();
			String fieldName = fk.field().equals("") ? field.getName() : fk.field();
			if (!Model.class.isAssignableFrom(fkCLass)) continue;
			Link link = null;
			try {
				field.setAccessible(true);
				if (fk.relationship().equals(ForeignKey.Relationship.MANY_TO_ONE)
						&& (!field.getType().isArray() && !Collection.class
						.isAssignableFrom(field.getType()))) {
					link = entityLinks.linkToSingleResource(fkCLass, field.get(t)).withRel(relName);
				} else if (fk.relationship().equals(ForeignKey.Relationship.ONE_TO_MANY)
						&& (field.getType().isArray() || Collection.class.isAssignableFrom(field.getType()))) {
					Map<String,Object> map = new HashMap<>();
					map.put(fieldName, field.get(t));
					link = entityLinks.linkToCollectionResource(fkCLass).expand(map).withRel(relName);   
				} else if (fk.relationship().equals(ForeignKey.Relationship.MANY_TO_MANY)
						&& (field.getType().isArray() || Collection.class.isAssignableFrom(field.getType()))){
					Map<String,Object> map = new HashMap<>();
					map.put(fieldName, field.get(t));
					link = entityLinks.linkToCollectionResource(fkCLass).expand(map).withRel(relName);  
				} else {
					throw new RuntimeException(String.format("Unable to determine correct link format for " 
							+ "field %s of class %s", field.getName(), model.getName()));
				}
			} catch (IllegalAccessException e){
				e.printStackTrace();
			}
			if (link != null) links.add(link);
		}
		return links;
	}
	
	private List<Link> addLinks(List<Link> links){
		return links;
	}

	public EntityLinks getEntityLinks() {
		return entityLinks;
	}

	public Class<?> getModelController() {
		return modelController;
	}

	public Class<T> getModel() {
		return model;
	}
}
