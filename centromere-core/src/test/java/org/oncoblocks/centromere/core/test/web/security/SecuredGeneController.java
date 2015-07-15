package org.oncoblocks.centromere.core.test.web.security;

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
@RequestMapping(value = "/secured/genes")
public class SecuredGeneController extends EntityQueryController<Gene, Long> {
	@Autowired
	public SecuredGeneController(GeneService service) {
		super(service, new GeneAssembler());
	}
}
