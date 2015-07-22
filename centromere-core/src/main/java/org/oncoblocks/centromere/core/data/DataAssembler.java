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

package org.oncoblocks.centromere.core.data;

import org.oncoblocks.centromere.core.web.controller.FilterableResource;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

/**
 * @author woemler
 */
public class DataAssembler<T extends Data> extends ResourceAssemblerSupport<T, FilterableResource> {
	
	private EntityLinks entityLinks;
	private Class<T> dataType;

	public DataAssembler(Class<?> controllerClass, Class<T> dataType, EntityLinks entityLinks) {
		super(controllerClass, FilterableResource.class);
		this.entityLinks = entityLinks;
		this.dataType = dataType;
	}

	public FilterableResource toResource(T data) {
		FilterableResource<T> resource = new FilterableResource<>(data);
		resource.add(entityLinks.linkToSingleResource(dataType, data.getId()).withSelfRel());
		resource.add(entityLinks.linkToSingleResource(Gene.class, data.getGeneId()).withRel("gene"));
		resource.add(entityLinks.linkToSingleResource(Sample.class, data.getSampleId()).withRel("sample"));
		resource.add(entityLinks.linkToSingleResource(DataFile.class, data.getDataFileId()).withRel("data_file"));
		return resource;
	}
}
