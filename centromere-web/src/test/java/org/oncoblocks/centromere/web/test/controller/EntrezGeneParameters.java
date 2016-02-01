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

import org.oncoblocks.centromere.core.model.support.Attribute;
import org.oncoblocks.centromere.core.model.support.Attributes;
import org.oncoblocks.centromere.core.model.support.SimpleAliases;
import org.oncoblocks.centromere.web.query.AnnotatedQueryParameters;
import org.oncoblocks.centromere.web.query.QueryParameter;

/**
 * @author woemler
 */
public class EntrezGeneParameters extends AnnotatedQueryParameters implements SimpleAliases, Attributes {
	
	private Long entrezGeneId;
	@QueryParameter("primaryGeneSymbol") private String geneSymbol;
	private String geneType;
	@QueryParameter("aliases") private String alias;
	@QueryParameter("attributes.name") private String attributeName;
	@QueryParameter("attributes.value") private String attributeValue;

	@Override public void setAttribute(Attribute attribute) {
		this.attributeName = attribute.getName();
		this.attributeValue = attribute.getValue();
	}

	public Long getEntrezGeneId() {
		return entrezGeneId;
	}

	public void setEntrezGeneId(Long entrezGeneId) {
		this.entrezGeneId = entrezGeneId;
	}

	public String getGeneSymbol() {
		return geneSymbol;
	}

	public void setGeneSymbol(String geneSymbol) {
		this.geneSymbol = geneSymbol;
	}

	public String getGeneType() {
		return geneType;
	}

	public void setGeneType(String geneType) {
		this.geneType = geneType;
	}

	public String getAlias() {
		return alias;
	}

	@Override public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAttributeName() {
		return attributeName;
	}

	@Override public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	@Override public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
}
