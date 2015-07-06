package org.oncoblocks.centromere.core.test.web.controller;

import org.oncoblocks.centromere.core.test.models.Gene;
import org.oncoblocks.centromere.core.test.web.service.GeneService;
import org.oncoblocks.centromere.core.web.controller.AbstractCrudController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author woemler
 */
@Controller
@RequestMapping(value = "/crud/genes")
public class GeneCrudController extends AbstractCrudController<Gene, Long> {
	@Autowired
	public GeneCrudController(GeneService service) {
		super(service, new GeneAssembler());
	}

}
