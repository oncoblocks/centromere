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

import org.oncoblocks.centromere.core.model.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

/**
 * @author woemler
 */
@Filterable
@Document(collection = "genes")
public class EntrezGene implements Model<Long>, SimpleAliases, Attributes {

	@Id private Long entrezGeneId;
	@Indexed private String primaryGeneSymbol;
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
	
	@PersistenceConstructor
	public EntrezGene(Long entrezGeneId, String primaryGeneSymbol, Integer taxId, String locusTag,
			String chromosome, String chromosomeLocation, String description, String geneType,
			List<Attribute> attributes, Set<String> aliases, Map<String, Object> dbXrefs) {
		this.entrezGeneId = entrezGeneId;
		this.primaryGeneSymbol = primaryGeneSymbol;
		this.taxId = taxId;
		this.locusTag = locusTag;
		this.chromosome = chromosome;
		this.chromosomeLocation = chromosomeLocation;
		this.description = description;
		this.geneType = geneType;
		this.attributes = attributes;
		this.aliases = aliases;
		this.dbXrefs = dbXrefs;
	}
	
	public Long getId() {
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
	
	public void setAttributes(Collection<Attribute> attributes) {
		this.attributes = (List) attributes;
	}
	
	public Set<String> getAliases() {
		return aliases;
	}
	
	public void setAliases(Set<String> aliases) {
		this.aliases = aliases;
	}
	
	public Map<String, Object> getDbXrefs() {
		return dbXrefs;
	}
	
	public void setDbXrefs(Map<String, Object> dbXrefs) {
		this.dbXrefs = dbXrefs;
	}
	
	public void setAttributeName(String attributeName) {
		if (attributes == null) attributes = new ArrayList<>();
		attributes.add(new Attribute(attributeName, null));
	}
	
	public void setAttributeValue(String attributeValue) {
		if (attributes == null) attributes = new ArrayList<>();
		attributes.add(new Attribute(null, attributeValue));
	}
	
	public void setAttribute(String attribute) {
		if (attributes == null) attributes = new ArrayList<>();
		String[] bits = attribute.split(":");
		if (bits.length == 2) attributes.add(new Attribute(bits[0], bits[1]));
	}
	
	public boolean hasAttribute(String name) {
		for (Attribute attribute: attributes){
			if (attribute.getName().equals(name)) return true;
		}
		return false;
	}
	
	public void setAlias(String alias) {
		if (aliases == null) aliases = new HashSet<>();
		aliases.add(alias);
	}

	public boolean hasAlias(String alias) {
		return aliases.contains(alias);
	}

	@Override public String toString() {
		return "Gene{" +
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
}
