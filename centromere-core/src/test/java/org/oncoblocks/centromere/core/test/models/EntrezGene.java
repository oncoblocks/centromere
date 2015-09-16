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

import org.oncoblocks.centromere.core.model.impl.EntrezGeneDto;
import org.oncoblocks.centromere.core.web.query.Attribute;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

/**
 * @author woemler
 */
@Document(collection = "genes")
public class EntrezGene extends EntrezGeneDto<Long> {

	public EntrezGene() { }
	
	@PersistenceConstructor
	public EntrezGene(Long entrezGeneId, String primaryGeneSymbol, Integer taxId, String locusTag,
			String chromosome, String chromosomeLocation, String description, String geneType,
			List<Attribute> attributes, Set<String> aliases, Map<String, Object> dbXrefs) {
		super(entrezGeneId, entrezGeneId, primaryGeneSymbol, taxId, locusTag, chromosome, chromosomeLocation, 
				description, geneType, attributes, dbXrefs, aliases);
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
