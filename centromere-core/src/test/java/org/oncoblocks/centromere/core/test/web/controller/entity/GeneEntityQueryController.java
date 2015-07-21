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

package org.oncoblocks.centromere.core.test.web.controller.entity;

import org.oncoblocks.centromere.core.model.Gene;
import org.oncoblocks.centromere.core.test.models.EntrezGene;
import org.oncoblocks.centromere.core.test.web.controller.GeneAssembler;
import org.oncoblocks.centromere.core.test.web.service.generic.GeneService;
import org.oncoblocks.centromere.core.web.controller.EntityQueryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author woemler
 */
@ExposesResourceFor(Gene.class)
@Controller
@RequestMapping(value = "/eq/genes")
public class GeneEntityQueryController extends EntityQueryController<EntrezGene, Long> {
	@Autowired
	public GeneEntityQueryController(GeneService service) {
		super(service, new GeneAssembler());
	}
}
