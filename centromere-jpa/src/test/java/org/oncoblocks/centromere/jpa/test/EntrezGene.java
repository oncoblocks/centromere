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

import org.oncoblocks.centromere.core.model.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author woemler
 */

@Entity
@Table(name = "entrez_gene")
public class EntrezGene implements Model<Long> {

	@Id @Column(name = "entrez_gene_id") private Long entrezGeneId;
	@Column(name = "primary_gene_symbol") private String primaryGeneSymbol;
	@Column(name = "tax_id") private Integer taxId;
	@Column(name = "locus_tag") private String locusTag;
	@Column(name = "chromosome") private String chromosome;
	@Column(name = "chromosome_location") private String chromosomeLocation;
	@Column(name = "description") private String description;
	@Column(name = "gene_type") private String geneType;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, 
			targetEntity = GeneAttribute.class) 
	@JoinColumn(name = "entrez_gene_id")
	private List<GeneAttribute> attributes;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, 
			targetEntity = GeneAlias.class)
	@JoinColumn(name = "entrez_gene_id")
	private List<GeneAlias> aliases;

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

	public List<GeneAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<GeneAttribute> attributes) {
		this.attributes = attributes;
	}

	public List<GeneAlias> getAliases() {
		return aliases;
	}

	public void setAliases(List<GeneAlias> aliases) {
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
				", aliases=" + aliases +
				'}';
	}

	public static List<EntrezGene> createDummyData(){
		
		List<EntrezGene> genes = new ArrayList<>();
		List<GeneAlias> aliases = new ArrayList<>();
		List<GeneAttribute> attributes = new ArrayList<>();
		
		EntrezGene gene = new EntrezGene();
		gene.setEntrezGeneId(1L);
		gene.setPrimaryGeneSymbol("GeneA");
		gene.setTaxId(9606);
		gene.setChromosome("1");
		gene.setDescription("Test Gene A");
		gene.setGeneType("protein-coding");
		attributes.add(new GeneAttribute(1L, "isKinase","Y"));
		aliases.add(new GeneAlias(1L, "ABC"));
		gene.setAttributes(attributes);
		gene.setAliases(aliases);
		genes.add(gene);
		aliases = new ArrayList<>();
		attributes = new ArrayList<>();

		gene = new EntrezGene();
		gene.setEntrezGeneId(2L);
		gene.setPrimaryGeneSymbol("GeneB");
		gene.setTaxId(9606);
		gene.setChromosome("3");
		gene.setDescription("Test Gene B");
		gene.setGeneType("protein-coding");
		attributes.add(new GeneAttribute(2L, "isKinase", "N"));
		aliases.add(new GeneAlias(2L, "DEF"));
		gene.setAttributes(attributes);
		gene.setAliases(aliases);
		genes.add(gene);
		aliases = new ArrayList<>();
		attributes = new ArrayList<>();

		gene = new EntrezGene();
		gene.setEntrezGeneId(3L);
		gene.setPrimaryGeneSymbol("GeneC");
		gene.setTaxId(9606);
		gene.setChromosome("3");
		gene.setDescription("Test Gene C");
		gene.setGeneType("pseudo");
		attributes.add(new GeneAttribute(3L, "isKinase", "N"));
		aliases.add(new GeneAlias(3L, "GHI"));
		gene.setAttributes(attributes);
		gene.setAliases(aliases);
		genes.add(gene);
		aliases = new ArrayList<>();
		attributes = new ArrayList<>();

		gene = new EntrezGene();
		gene.setEntrezGeneId(4L);
		gene.setPrimaryGeneSymbol("GeneD");
		gene.setTaxId(9606);
		gene.setChromosome("9");
		gene.setDescription("Test Gene D");
		gene.setGeneType("protein-coding");
		attributes.add(new GeneAttribute(4L, "isKinase", "Y"));
		aliases.add(new GeneAlias(4L, "JKL"));
		gene.setAttributes(attributes);
		gene.setAliases(aliases);
		genes.add(gene);
		aliases = new ArrayList<>();
		attributes = new ArrayList<>();

		gene = new EntrezGene();
		gene.setEntrezGeneId(5L);
		gene.setPrimaryGeneSymbol("GeneE");
		gene.setTaxId(9606);
		gene.setChromosome("X");
		gene.setDescription("Test Gene E");
		gene.setGeneType("pseudo");
		attributes.add(new GeneAttribute(5L, "isKinase", "N"));
		aliases.add(new GeneAlias(5L, "MNO"));
		gene.setAttributes(attributes);
		gene.setAliases(aliases);
		genes.add(gene);
		
		return genes;
		
	}
	
}
