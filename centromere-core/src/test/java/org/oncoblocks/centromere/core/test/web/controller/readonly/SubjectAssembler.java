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

package org.oncoblocks.centromere.core.test.web.controller.readonly;

import org.oncoblocks.centromere.core.test.models.Subject;
import org.oncoblocks.centromere.core.web.controller.FilterableResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * @author woemler
 */

@Component
public class SubjectAssembler extends
		ResourceAssemblerSupport<Subject, FilterableResource<Subject>> {
	
	@Autowired EntityLinks entityLinks;

	public SubjectAssembler() {
		super(SubjectController.class, (Class<FilterableResource<Subject>>)(Class<?>) FilterableResource.class );
	}

	@Override
	public FilterableResource<Subject> toResource(Subject subject) {
		FilterableResource<Subject> resource = new FilterableResource<>(subject);
		resource.add(entityLinks.linkToSingleResource(Subject.class, subject.getId()).withSelfRel());
		return resource;
	}
}