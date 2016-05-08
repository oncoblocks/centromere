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

package org.oncoblocks.centromere.jpa.test;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author woemler
 */
@Entity
@Table(name = "gene_attributes")
@IdClass(GeneAttribute.GeneAttributeId.class)
public class GeneAttribute {
	
	@Id @Column(name = "name") private String name;
	@Column(name = "value") private String value;
	@Id @Column(name = "entrez_gene_id") private Long entrezGeneId;

	public GeneAttribute() { }

	public GeneAttribute(Long entrezGeneId, String name, String value) {
		this.entrezGeneId = entrezGeneId;
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getEntrezGeneId() {
		return entrezGeneId;
	}

	public void setEntrezGeneId(Long entrezGeneId) {
		this.entrezGeneId = entrezGeneId;
	}
	
	public static class GeneAttributeId implements Serializable{
		private String name;
		private Long entrezGeneId;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Long getEntrezGeneId() {
			return entrezGeneId;
		}

		public void setEntrezGeneId(Long entrezGeneId) {
			this.entrezGeneId = entrezGeneId;
		}
	}
}
