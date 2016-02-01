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

package org.oncoblocks.centromere.web.test.controller;

import org.oncoblocks.centromere.web.controller.FilterableResource;
import org.oncoblocks.centromere.web.test.models.EntrezGene;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * @author woemler
 */

public class EntrezGeneAssembler extends ResourceAssemblerSupport<EntrezGene, FilterableResource> {
	
	private Class<?> controller;
	
	public EntrezGeneAssembler(Class<?> controller) {
		super(controller, FilterableResource.class);
		this.controller = controller;
	}

	@Override 
	public FilterableResource<EntrezGene> toResource(EntrezGene gene) {
		FilterableResource<EntrezGene> resource = new FilterableResource<EntrezGene>(gene);
		resource.add(linkTo(controller).slash(gene.getId()).withSelfRel());
		return resource;
	}
	
}
