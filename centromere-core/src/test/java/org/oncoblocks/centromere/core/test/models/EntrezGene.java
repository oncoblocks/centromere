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

package org.oncoblocks.centromere.core.test.models;

import org.oncoblocks.centromere.core.model.Model;
import org.oncoblocks.centromere.core.model.support.Attribute;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

/**
 * @author woemler
 */

@Document(collection = "genes")
public class EntrezGene implements Model<Long> {

	@Id private Long entrezGeneId;
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
	
	public EntrezGene() { }

	public EntrezGene(Long entrezGeneId, String primaryGeneSymbol, Integer taxId,
			String locusTag, String chromosome, String chromosomeLocation, String description,
			String geneType, List<Attribute> attributes,
			Map<String, Object> dbXrefs, Set<String> aliases) {
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

	@Override public Long getId() {
		return entrezGeneId;
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

	public void setAttributes(
			List<Attribute> attributes) {
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

	@Override public String toString() {
		return "EntrezGene{" +
				"entrezGeneId=" + entrezGeneId +
				", primaryGeneSymbol='" + primaryGeneSymbol + '\'' +
				", taxId=" + taxId +
				", locusTag='" + locusTag + '\'' +
				", chromosome='" + chromosome + '\'' +
				", chromosomeLocation='" + chromosomeLocation + '\'' +
				", description='" + description + '\'' +
				", geneType='" + geneType + '\'' +
				", attributes=" + attributes +
				", dbXrefs=" + dbXrefs +
				", aliases=" + aliases +
				'}';
	}

	public static List<EntrezGene> createDummyData(){
		
		List<EntrezGene> genes = new ArrayList<>();
		Set<String> aliases = new HashSet<>();
		List<Attribute> attributes = new ArrayList<>();
		
		EntrezGene gene = new EntrezGene(1L, "GeneA", 9606, null, "1", null, "Test Gene A", "protein-coding", null, null, null);
		attributes.add(new Attribute("isKinase","Y"));
		aliases.add("ABC");
		gene.setAttributes(attributes);
		gene.setAliases(aliases);
		genes.add(gene);
		aliases = new HashSet<>();
		attributes = new ArrayList<>();
		
		gene = new EntrezGene(2L, "GeneB", 9606, null, "3", null, "Test Gene B", "protein-coding", null, null, null);
		attributes.add(new Attribute("isKinase", "N"));
		aliases.add("DEF");
		gene.setAttributes(attributes);
		gene.setAliases(aliases);
		genes.add(gene);
		aliases = new HashSet<>();
		attributes = new ArrayList<>();
		
		
		gene = new EntrezGene(3L, "GeneC", 9606, null, "11", null, "Test Gene C", "pseudo", null, null, null);
		attributes.add(new Attribute("isKinase","N"));
		aliases.add("GHI");
		gene.setAttributes(attributes);
		gene.setAliases(aliases);
		genes.add(gene);
		aliases = new HashSet<>();
		attributes = new ArrayList<>();
		
		
		gene = new EntrezGene(4L, "GeneD", 9606, null, "9", null, "Test Gene D", "protein-coding", null, null, null);
		attributes.add(new Attribute("isKinase","Y"));
		aliases.add("JKL");
		gene.setAttributes(attributes);
		gene.setAliases(aliases);
		genes.add(gene);
		aliases = new HashSet<>();
		attributes = new ArrayList<>();
		
		gene = new EntrezGene(5L, "GeneE", 9606, null, "X", null, "Test Gene E", "pseudo", null, null, null);
		attributes.add(new Attribute("isKinase","N"));
		aliases.add("MNO");
		gene.setAttributes(attributes);
		gene.setAliases(aliases);
		genes.add(gene);
		
		return genes;
		
	}
	
}
