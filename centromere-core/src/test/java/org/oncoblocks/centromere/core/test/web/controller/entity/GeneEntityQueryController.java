package org.oncoblocks.centromere.core.test.web.controller.entity;

import org.oncoblocks.centromere.core.test.models.Gene;
import org.oncoblocks.centromere.core.test.web.controller.GeneAssembler;
import org.oncoblocks.centromere.core.test.web.service.generic.GeneService;
import org.oncoblocks.centromere.core.web.controller.EntityQueryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author woemler
 */

@Controller
@RequestMapping(value = "/eq/genes")
public class GeneEntityQueryController extends EntityQueryController<Gene, Long> {
	@Autowired
	public GeneEntityQueryController(GeneService service) {
		super(service, new GeneAssembler());
	}
}
