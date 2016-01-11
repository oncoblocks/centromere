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

package org.oncoblocks.centromere.web.test.controller.readonly;

import org.oncoblocks.centromere.web.controller.ReadOnlyApiController;
import org.oncoblocks.centromere.web.test.models.Subject;
import org.oncoblocks.centromere.web.test.repository.jdbc.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author woemler
 */

@Controller
@RequestMapping("/subjects")
@ExposesResourceFor(Subject.class)
public class SubjectController extends ReadOnlyApiController<Subject,Long, SubjectParameters> {
	@Autowired
	public SubjectController(SubjectRepository repository, SubjectAssembler assembler) {
		super(repository, assembler);
	}
}
