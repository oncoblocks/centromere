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

package org.oncoblocks.centromere.core.test.web.controller.parameter;

import org.oncoblocks.centromere.core.test.models.Subject;
import org.oncoblocks.centromere.core.test.web.service.remapping.SubjectService;
import org.oncoblocks.centromere.core.web.controller.FilterableResource;
import org.oncoblocks.centromere.core.web.controller.QueryParameterController;
import org.oncoblocks.centromere.core.web.controller.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * @author woemler
 */

@Controller
@RequestMapping(value = "/param/subjects")
public class SubjectParameterController extends QueryParameterController<Subject,Long, SubjectParameterController.SubjectParameters> {
	
	@Autowired
	public SubjectParameterController(SubjectService service) {
		super(service, new SubjectAssembler());
	}

	public static class SubjectAssembler extends ResourceAssemblerSupport<Subject, FilterableResource<Subject>> {

		public SubjectAssembler() {
			super(SubjectParameterController.class, (Class<FilterableResource<Subject>>)(Class<?>) FilterableResource.class );
		}

		@Override 
		public FilterableResource<Subject> toResource(Subject subject) {
			FilterableResource<Subject> resource = new FilterableResource<>(subject);
			resource.add(linkTo(SubjectParameterController.class).slash(subject.getId()).withSelfRel());
			return resource;
		}
	}
	
	public static class SubjectParameters extends QueryParameters {
		
		private String name;
		private String aliasName;
		private String attributeName;
		private String attributeValue;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAliasName() {
			return aliasName;
		}

		public void setAliasName(String aliasName) {
			this.aliasName = aliasName;
		}

		public String getAttributeName() {
			return attributeName;
		}

		public void setAttributeName(String attributeName) {
			this.attributeName = attributeName;
		}

		public String getAttributeValue() {
			return attributeValue;
		}

		public void setAttributeValue(String attributeValue) {
			this.attributeValue = attributeValue;
		}
	}
	
}
