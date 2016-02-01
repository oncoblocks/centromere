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

import org.oncoblocks.centromere.web.controller.CrudApiController;
import org.oncoblocks.centromere.web.test.models.EntrezGene;
import org.oncoblocks.centromere.web.test.repository.EntrezGeneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author woemler
 */
@Controller
@RequestMapping(value = "/genes/crud")
@ExposesResourceFor(EntrezGene.class)
public class EntrezGeneCrudController extends CrudApiController<EntrezGene, Long, EntrezGeneParameters> {
	@Autowired
	public EntrezGeneCrudController(EntrezGeneRepository repository) {
		super(repository, new EntrezGeneAssembler(EntrezGeneCrudController.class));
	}

}
