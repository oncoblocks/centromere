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

package org.oncoblocks.centromere.core.model.support;

import org.oncoblocks.centromere.core.model.Model;
import org.oncoblocks.centromere.core.web.query.Attribute;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author woemler
 */

public class EntrezGeneDto<ID extends Serializable> implements Model<ID> {

	@Id private ID id;
	private Long entrezGeneId;
	private String primaryGeneSymbol;
	private Integer taxId;
	private String locusTag;
	private String chromosome;
	private String chromosomeLocation;
	private String description;
	private String geneType;
	private List<Attribute> attributes;
	private Map<String, Object> dbXrefs;
	private Set<String> aliases;

	public EntrezGeneDto() { }

	public EntrezGeneDto(ID id, Long entrezGeneId, String primaryGeneSymbol, Integer taxId,
			String locusTag, String chromosome, String chromosomeLocation, String description,
			String geneType, List<Attribute> attributes,
			Map<String, Object> dbXrefs, Set<String> aliases) {
		this.id = id;
		this.entrezGeneId = entrezGeneId;
		this.primaryGeneSymbol = primaryGeneSymbol;
		this.taxId = taxId;
		this.locusTag = locusTag;
		this.chromosome = chromosome;
		this.chromosomeLocation = chromosomeLocation;
		this.description = description;
		this.geneType = geneType;
		this.attributes = attributes;
		this.dbXrefs = dbXrefs;
		this.aliases = aliases;
	}

	@Override public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public Long getEntrezGeneId() {
		return entrezGeneId;
	}

	public void setEntrezGeneId(Long entrezGeneId) {
		this.entrezGeneId = entrezGeneId;
	}

	public String getPrimaryGeneSymbol() {
		return primaryGeneSymbol;
	}

	public void setPrimaryGeneSymbol(String primaryGeneSymbol) {
		this.primaryGeneSymbol = primaryGeneSymbol;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public String getLocusTag() {
		return locusTag;
	}

	public void setLocusTag(String locusTag) {
		this.locusTag = locusTag;
	}

	public String getChromosome() {
		return chromosome;
	}

	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}

	public String getChromosomeLocation() {
		return chromosomeLocation;
	}

	public void setChromosomeLocation(String chromosomeLocation) {
		this.chromosomeLocation = chromosomeLocation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGeneType() {
		return geneType;
	}

	public void setGeneType(String geneType) {
		this.geneType = geneType;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public Map<String, Object> getDbXrefs() {
		return dbXrefs;
	}

	public void setDbXrefs(Map<String, Object> dbXrefs) {
		this.dbXrefs = dbXrefs;
	}

	public Set<String> getAliases() {
		return aliases;
	}

	public void setAliases(Set<String> aliases) {
		this.aliases = aliases;
	}
}
