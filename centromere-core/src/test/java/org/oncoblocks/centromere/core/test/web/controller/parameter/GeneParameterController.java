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

import org.oncoblocks.centromere.core.test.models.EntrezGene;
import org.oncoblocks.centromere.core.test.web.controller.GeneAssembler;
import org.oncoblocks.centromere.core.test.web.service.generic.GeneService;
import org.oncoblocks.centromere.core.web.controller.QueryParameter;
import org.oncoblocks.centromere.core.web.controller.QueryParameterController;
import org.oncoblocks.centromere.core.web.controller.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author woemler
 */

@Controller
@RequestMapping(value = "/param/genes")
public class GeneParameterController
		extends QueryParameterController<EntrezGene, Long, GeneParameterController.GeneParameters> {
	
	@Autowired
	public GeneParameterController(GeneService service) {
		super(service, new GeneAssembler());
	}

	public static class GeneParameters extends QueryParameters {
		
		private String primaryGeneSymbol;
		private Long entrezGeneId;
		@QueryParameter(name = "aliases") private String alias;
		@QueryParameter(name = "attributes.name") private String attributeName;
		@QueryParameter(name = "attributes.value") private String attributeValue;
		private String geneType;

		public String getPrimaryGeneSymbol() {
			return primaryGeneSymbol;
		}

		public void setPrimaryGeneSymbol(String primaryGeneSymbol) {
			this.primaryGeneSymbol = primaryGeneSymbol;
		}

		public Long getEntrezGeneId() {
			return entrezGeneId;
		}

		public void setEntrezGeneId(Long entrezGeneId) {
			this.entrezGeneId = entrezGeneId;
		}

		public String getAlias() {
			return alias;
		}

		public void setAlias(String alias) {
			this.alias = alias;
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

		public String getGeneType() {
			return geneType;
		}

		public void setGeneType(String geneType) {
			this.geneType = geneType;
		}
		
		public void setAttribute(String attribute){
			String[] bits = attribute.split(":");
			this.attributeName = bits[0];
			this.attributeValue = bits[1];
		}
	}
	
}
