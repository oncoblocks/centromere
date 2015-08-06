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

package org.oncoblocks.centromere.core.test.web.controller.crud;

import org.oncoblocks.centromere.core.test.models.EntrezGene;
import org.oncoblocks.centromere.core.test.web.controller.crud.GeneController;
import org.oncoblocks.centromere.core.web.controller.FilterableResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * @author woemler
 */

@Component
public class GeneAssembler extends ResourceAssemblerSupport<EntrezGene, FilterableResource<EntrezGene>> {
	
	@Autowired private EntityLinks entityLinks;
	
	public GeneAssembler() {
		super(GeneController.class, (Class<FilterableResource<EntrezGene>>)(Class<?>) FilterableResource.class);
	}

	@Override public FilterableResource<EntrezGene> toResource(EntrezGene gene) {
		FilterableResource<EntrezGene> resource = new FilterableResource<EntrezGene>(gene);
		resource.add(entityLinks.linkToSingleResource(EntrezGene.class, gene.getId()).withSelfRel());
		return resource;
	}
}
