package org.oncoblocks.centromere.core.test.web.controller;

import org.oncoblocks.centromere.core.test.models.Gene;
import org.oncoblocks.centromere.core.test.web.controller.crud.GeneCrudController;
import org.oncoblocks.centromere.core.web.controller.FilterableResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * @author woemler
 */
public class GeneAssembler extends ResourceAssemblerSupport<Gene, FilterableResource> {
	public GeneAssembler() {
		super(GeneCrudController.class, FilterableResource.class);
	}

	@Override public FilterableResource toResource(Gene gene) {
		FilterableResource<Gene> resource = new FilterableResource<Gene>(gene);
		resource.add(linkTo(GeneCrudController.class).slash(gene.getId()).withSelfRel());
		return resource;
	}
}
