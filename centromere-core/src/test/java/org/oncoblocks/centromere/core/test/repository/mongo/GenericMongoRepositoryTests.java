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

package org.oncoblocks.centromere.core.test.repository.mongo;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oncoblocks.centromere.core.model.Attribute;
import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.oncoblocks.centromere.core.test.config.TestMongoConfig;
import org.oncoblocks.centromere.core.test.models.EntrezGene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author woemler
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestMongoConfig.class, MongoRepositoryConfig.class})
@FixMethodOrder
public class GenericMongoRepositoryTests {
		
	@Autowired private EntrezGeneRepository geneRepository;
	
	private static boolean isConfigured = false;
	
	@Before
	public void setup(){
		
		if (isConfigured) return;
		
		geneRepository.deleteAll();
		EntrezGene
				geneA = new EntrezGene(1L, "GeneA", 9606, null, "1", null, "Test Gene A", "protein-coding", null, null, null);
		geneA.setAttribute(new Attribute("isKinase","Y"));
		geneA.setAlias("ABC");
		EntrezGene
				geneB = new EntrezGene(2L, "GeneB", 9606, null, "3", null, "Test Gene B", "protein-coding", null, null, null);
		geneB.setAttribute(new Attribute("isKinase","N"));
		geneB.setAlias("DEF");
		EntrezGene
				geneC = new EntrezGene(3L, "GeneC", 9606, null, "11", null, "Test Gene C", "pseudo", null, null, null);
		geneC.setAttribute(new Attribute("isKinase","N"));
		geneC.setAlias("GHI");
		EntrezGene
				geneD = new EntrezGene(4L, "GeneD", 9606, null, "9", null, "Test Gene D", "protein-coding", null, null, null);
		geneD.setAttribute(new Attribute("isKinase","Y"));
		geneD.setAlias("JKL");
		EntrezGene
				geneE = new EntrezGene(5L, "GeneE", 9606, null, "X", null, "Test Gene E", "pseudo", null, null, null);
		geneE.setAttribute(new Attribute("isKinase","N"));
		geneE.setAlias("MNO");
		geneRepository.insert(Arrays.asList(new EntrezGene[]{ geneA, geneB, geneC, geneD, geneE }));
		
		isConfigured = true;
		
	}

	@Test
	public void findByIdTest(){
		
		EntrezGene gene = geneRepository.findById(1L);
		Assert.notNull(gene);
		Assert.isTrue(gene.getEntrezGeneId().equals(1L));
		Assert.isTrue(gene.getPrimaryGeneSymbol().equals("GeneA"));
		Assert.notNull(gene.getAliases());
		Assert.notEmpty(gene.getAliases());
		Assert.isTrue(gene.getAliases().size() == 1);
		
	}

	@Test
	public void findAllTest(){
		
		List<EntrezGene> genes = geneRepository.findAll();
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 5);

		EntrezGene gene = genes.get(0);
		Assert.notNull(gene);
		Assert.isTrue(gene.getEntrezGeneId().equals(1L));
		Assert.isTrue(gene.getPrimaryGeneSymbol().equals("GeneA"));
		Assert.notNull(gene.getAliases());
		Assert.notEmpty(gene.getAliases());
		Assert.isTrue(gene.getAliases().size() == 1);
		System.out.println(gene.toString());

	}

	@Test
	public void countTest(){

		long count = geneRepository.count();
		Assert.notNull(count);
		Assert.isTrue(count == 5L);

	}

	@Test
	public void findBySimpleCriteriaTest(){

		List<QueryCriteria> searchCriterias = new ArrayList<>();
		searchCriterias.add(new QueryCriteria("primaryGeneSymbol", "GeneB"));
		List<EntrezGene> genes = geneRepository.find(searchCriterias);
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 1);

		EntrezGene gene = genes.get(0);
		Assert.notNull(gene);
		Assert.isTrue(gene.getEntrezGeneId().equals(2L));
		Assert.isTrue(gene.getPrimaryGeneSymbol().equals("GeneB"));

	}

	@Test
	public void findByNestedArrayCriteriaTest(){

		List<QueryCriteria> searchCriterias = new ArrayList<>();
		searchCriterias.add(new QueryCriteria("aliases", "DEF"));
		List<EntrezGene> genes = geneRepository.find(searchCriterias);
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 1);

		EntrezGene gene = genes.get(0);
		Assert.notNull(gene);
		Assert.isTrue(gene.getEntrezGeneId().equals(2L));
		Assert.isTrue(gene.getPrimaryGeneSymbol().equals("GeneB"));
		Assert.isTrue(gene.getAliases().contains("DEF"));

	}

	@Test
	public void findByNestedObjectCriteriaTest(){

		List<QueryCriteria> searchCriterias = new ArrayList<>();
		searchCriterias.add(new QueryCriteria("attributes.name", "isKinase"));
		searchCriterias.add(new QueryCriteria("attributes.value", "Y"));
		List<EntrezGene> genes = geneRepository.find(searchCriterias);
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 2);

		EntrezGene gene = genes.get(0);
		Assert.notNull(gene);
		Assert.isTrue(gene.getEntrezGeneId().equals(1L));
		Assert.isTrue(gene.getPrimaryGeneSymbol().equals("GeneA"));
		Assert.isTrue(gene.getAttributes().size() == 1);
		Assert.isTrue(gene.getAttributes().get(0).getName().equals("isKinase"));
		Assert.isTrue(gene.getAttributes().get(0).getValue().equals("Y"));

	}

	@Test
	public void findSortedTest(){

		Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "primaryGeneSymbol"));
		List<EntrezGene> genes = geneRepository.findAllSorted(sort);
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 5);

		EntrezGene gene = genes.get(0);
		Assert.notNull(gene);
		Assert.isTrue(gene.getEntrezGeneId().equals(5L));
		Assert.isTrue(gene.getPrimaryGeneSymbol().equals("GeneE"));

	}

	@Test
	public void findPagedTest(){

		PageRequest pageRequest = new PageRequest(1, 2);
		Page<EntrezGene> page = geneRepository.findAllPaged(pageRequest);
		Assert.notNull(page);
		Assert.isTrue(page.getTotalPages() == 3);
		Assert.isTrue(page.getTotalElements() == 5);

		List<EntrezGene> genes = page.getContent();
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 2);

		EntrezGene gene = genes.get(0);
		Assert.notNull(gene);
		Assert.isTrue(gene.getEntrezGeneId().equals(3l));

	}

	@Test
	public void findByCriteriaPagedTest(){

		List<QueryCriteria> searchCriterias = new ArrayList<>();
		searchCriterias.add(new QueryCriteria("geneType", "protein-coding"));
		PageRequest pageRequest = new PageRequest(1, 2);
		Page<EntrezGene> page = geneRepository.findPaged(searchCriterias, pageRequest);
		Assert.notNull(page);
		Assert.isTrue(page.getTotalElements() == 3);
		Assert.isTrue(page.getTotalPages() == 2);

		List<EntrezGene> genes = page.getContent();
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 1);

		EntrezGene gene = genes.get(0);
		Assert.notNull(gene);
		Assert.isTrue(gene.getEntrezGeneId().equals(4L));

	}

	@Test
	public void insertTest(){

		EntrezGene
				gene = new EntrezGene(100L, "TEST", 9606, null, "1", "1", "Test gene", "protein-coding", null, null, null);
		geneRepository.insert(gene);

		EntrezGene created = geneRepository.findById(100L);
		Assert.notNull(created);
		Assert.isTrue(created.getId().equals(100L));
		Assert.isTrue(created.getPrimaryGeneSymbol().equals("TEST"));

		geneRepository.delete(100L);

	}

	@Test
	public void updateTest(){

		EntrezGene
				gene = new EntrezGene(100L, "TEST", 9606, null, "1", "1", "Test gene", "protein-coding", null, null, null);
		geneRepository.insert(gene);

		gene.setPrimaryGeneSymbol("TEST_TEST");
		gene.setGeneType("pseudogene");
		geneRepository.update(gene);

		EntrezGene updated = geneRepository.findById(100L);
		Assert.notNull(updated);
		Assert.isTrue(updated.getPrimaryGeneSymbol().equals("TEST_TEST"));
		Assert.isTrue(updated.getGeneType().equals("pseudogene"));

		geneRepository.delete(100L);

	}

	@Test
	public void deleteTest(){

		EntrezGene
				gene = new EntrezGene(100L, "TEST", 9606, null, "1", "1", "Test gene", "protein-coding", null, null, null);
		geneRepository.insert(gene);

		EntrezGene created = geneRepository.findById(100L);
		Assert.notNull(created);
		Assert.isTrue(created.getId().equals(100L));

		geneRepository.delete(100L);
		EntrezGene deleted = geneRepository.findById(100L);
		Assert.isNull(deleted);

	}

	@Test
	public void rawJsonTestById() throws Exception {

		EntrezGene gene = new EntrezGene();
		gene.setEntrezGeneId(1L);
		List<EntrezGene> genes = geneRepository.find(gene);
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.get(0).getEntrezGeneId().equals(1L));


	}

	@Test
	public void rawJsonTestByGeneType() throws Exception {

		EntrezGene gene = new EntrezGene();
		gene.setGeneType("protein-coding");
		List<EntrezGene> genes = geneRepository.find(gene);
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 3);
		Assert.isTrue(genes.get(2).getEntrezGeneId().equals(4L));


	}

	@Test
	public void rawJsonTestByConflictingAttributes() throws Exception {

		EntrezGene gene = new EntrezGene();
		gene.setGeneType("protein-coding");
		gene.setEntrezGeneId(11L);

		List<EntrezGene> genes = geneRepository.find(gene);
		Assert.notNull(genes);
		Assert.isTrue(genes.size() == 0);


	}

	@Test
	public void rawJsonTestByNestedAttributes() throws Exception {

		EntrezGene gene = new EntrezGene();
		gene.setAttribute(new Attribute("isKinase","Y"));

		List<EntrezGene> genes = geneRepository.find(gene);
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 2);
		Assert.isTrue(genes.get(0).getEntrezGeneId().equals(1L));

	}

	@Test
	public void rawJsonTestByPartialNestedAttributes() throws Exception {

		EntrezGene gene = new EntrezGene();
		gene.setAttributeName("isKinase");

		List<EntrezGene> genes = geneRepository.find(gene);

		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 5);
		Assert.isTrue(genes.get(0).getEntrezGeneId().equals(1L));

	}
	
	@Test
	public void findByPrimaryIdTest() throws Exception {
		
		EntrezGene gene = geneRepository.findByPrimaryGeneId(1L);
		Assert.notNull(gene);
		Assert.isTrue(gene.getEntrezGeneId().equals(1L));
		
	}
	
	@Test
	public void findByPrimaryGeneSymbolTest() throws Exception {
		
		List<EntrezGene> genes = geneRepository.findByPrimaryGeneSymbol("GeneA");
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 1);
		EntrezGene gene = genes.get(0);
		Assert.isTrue(gene.getPrimaryGeneSymbol().equals("GeneA"));
		
	}

	@Test
	public void findByAliasTest() throws Exception {

		List<EntrezGene> genes = geneRepository.findByAlias("ABC");
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 1);
		EntrezGene gene = genes.get(0);
		Assert.isTrue(gene.getEntrezGeneId().equals(1L));

	}
	
	@Test
	public void guessGeneTest() throws Exception {
		
		EntrezGene gene = geneRepository.guessGene("GeneA");
		Assert.notNull(gene);
		Assert.isTrue(gene.getEntrezGeneId().equals(1L));
		
		gene = geneRepository.guessGene("MNO");
		Assert.notNull(gene);
		Assert.isTrue(gene.getEntrezGeneId().equals(5L));
		
		gene = geneRepository.guessGene("XYZ");
		Assert.isNull(gene);
		
	}
	
}
