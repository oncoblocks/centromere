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

import org.oncoblocks.centromere.core.model.support.Attribute;
import org.oncoblocks.centromere.core.model.support.Attributes;
import org.oncoblocks.centromere.core.model.support.SourcedAlias;
import org.oncoblocks.centromere.core.model.support.SourcedAliases;
import org.oncoblocks.centromere.core.web.query.*;

/**
 * @author woemler
 */
public class SubjectParameters extends AnnotatedQueryParameters
		implements SourcedAliases, Attributes {

	@QueryParameter("subjects.subject_id") private Long subjectId;
	@QueryParameter("subjects.name") private String name;
	@QueryParameter("subject_attributes.name") private String attributeName;
	@QueryParameter("subject_attributes.value") private String attributeValue;
	@QueryParameter("subject_aliases.source") private String aliasSource;
	@QueryParameter("subject_aliases.name") private String aliasName;

	@Override
	public void setAttribute(Attribute attribute) {
		this.attributeName = attribute.getName();
		this.attributeValue = attribute.getValue();
	}

	@Override 
	public void setAlias(SourcedAlias alias) {
		this.aliasSource = alias.getSource();
		this.aliasName = alias.getName();
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAttributeName() {
		return attributeName;
	}

	@Override 
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	@Override 
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getAliasSource() {
		return aliasSource;
	}

	@Override 
	public void setAliasSource(String aliasSource) {
		this.aliasSource = aliasSource;
	}

	public String getAliasName() {
		return aliasName;
	}

	@Override 
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
}
