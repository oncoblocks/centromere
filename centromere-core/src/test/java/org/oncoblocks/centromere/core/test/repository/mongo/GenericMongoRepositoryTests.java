package org.oncoblocks.centromere.core.test.repository.mongo;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.oncoblocks.centromere.core.test.config.TestMongoConfig;
import org.oncoblocks.centromere.core.test.models.Gene;
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
		
	@Autowired private GeneRepository geneRepository;
	
	private static boolean isConfigured = false;
	
	@Before
	public void setup(){
		
		if (isConfigured) return;
		
		geneRepository.deleteAll();
		Gene geneA = new Gene(1L, "GeneA", 9606, null, "1", null, "Test Gene A", "protein-coding", null, null, null);
		geneA.setAttribute("isKinase:Y");
		geneA.setAlias("ABC");
		Gene geneB = new Gene(2L, "GeneB", 9606, null, "3", null, "Test Gene B", "protein-coding", null, null, null);
		geneB.setAttribute("isKinase:N");
		geneB.setAlias("DEF");
		Gene geneC = new Gene(3L, "GeneC", 9606, null, "11", null, "Test Gene C", "pseudo", null, null, null);
		geneC.setAttribute("isKinase:N");
		geneC.setAlias("GHI");
		Gene geneD = new Gene(4L, "GeneD", 9606, null, "9", null, "Test Gene D", "protein-coding", null, null, null);
		geneD.setAttribute("isKinase:Y");
		geneD.setAlias("JKL");
		Gene geneE = new Gene(5L, "GeneE", 9606, null, "X", null, "Test Gene E", "pseudo", null, null, null);
		geneE.setAttribute("isKinase:N");
		geneE.setAlias("MNO");
		geneRepository.insert(Arrays.asList(new Gene[]{ geneA, geneB, geneC, geneD, geneE }));
		
		isConfigured = true;
		
	}

	@Test
	public void findByIdTest(){
		
		Gene gene = geneRepository.findById(1L);
		Assert.notNull(gene);
		Assert.isTrue(gene.getEntrezGeneId().equals(1L));
		Assert.isTrue(gene.getPrimaryGeneSymbol().equals("GeneA"));
		Assert.notNull(gene.getAliases());
		Assert.notEmpty(gene.getAliases());
		Assert.isTrue(gene.getAliases().size() == 1);
		
	}

	@Test
	public void findAllTest(){
		
		List<Gene> genes = geneRepository.findAll();
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 5);

		Gene gene = genes.get(0);
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
		List<Gene> genes = geneRepository.find(searchCriterias);
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 1);

		Gene gene = genes.get(0);
		Assert.notNull(gene);
		Assert.isTrue(gene.getEntrezGeneId().equals(2L));
		Assert.isTrue(gene.getPrimaryGeneSymbol().equals("GeneB"));

	}

	@Test
	public void findByNestedArrayCriteriaTest(){

		List<QueryCriteria> searchCriterias = new ArrayList<>();
		searchCriterias.add(new QueryCriteria("aliases", "DEF"));
		List<Gene> genes = geneRepository.find(searchCriterias);
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 1);

		Gene gene = genes.get(0);
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
		List<Gene> genes = geneRepository.find(searchCriterias);
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 2);

		Gene gene = genes.get(0);
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
		List<Gene> genes = geneRepository.findAllSorted(sort);
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 5);

		Gene gene = genes.get(0);
		Assert.notNull(gene);
		Assert.isTrue(gene.getEntrezGeneId().equals(5L));
		Assert.isTrue(gene.getPrimaryGeneSymbol().equals("GeneE"));

	}

	@Test
	public void findPagedTest(){

		PageRequest pageRequest = new PageRequest(1, 2);
		Page<Gene> page = geneRepository.findAllPaged(pageRequest);
		Assert.notNull(page);
		Assert.isTrue(page.getTotalPages() == 3);
		Assert.isTrue(page.getTotalElements() == 5);

		List<Gene> genes = page.getContent();
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 2);

		Gene gene = genes.get(0);
		Assert.notNull(gene);
		Assert.isTrue(gene.getEntrezGeneId().equals(3l));

	}

	@Test
	public void findByCriteriaPagedTest(){

		List<QueryCriteria> searchCriterias = new ArrayList<>();
		searchCriterias.add(new QueryCriteria("geneType", "protein-coding"));
		PageRequest pageRequest = new PageRequest(1, 2);
		Page<Gene> page = geneRepository.findPaged(searchCriterias, pageRequest);
		Assert.notNull(page);
		Assert.isTrue(page.getTotalElements() == 3);
		Assert.isTrue(page.getTotalPages() == 2);

		List<Gene> genes = page.getContent();
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 1);

		Gene gene = genes.get(0);
		Assert.notNull(gene);
		Assert.isTrue(gene.getEntrezGeneId().equals(4L));

	}

	@Test
	public void insertTest(){

		Gene gene = new Gene(100L, "TEST", 9606, null, "1", "1", "Test gene", "protein-coding", null, null, null);
		geneRepository.insert(gene);

		Gene created = geneRepository.findById(100L);
		Assert.notNull(created);
		Assert.isTrue(created.getId().equals(100L));
		Assert.isTrue(created.getPrimaryGeneSymbol().equals("TEST"));

		geneRepository.delete(100L);

	}

	@Test
	public void updateTest(){

		Gene gene = new Gene(100L, "TEST", 9606, null, "1", "1", "Test gene", "protein-coding", null, null, null);
		geneRepository.insert(gene);

		gene.setPrimaryGeneSymbol("TEST_TEST");
		gene.setGeneType("pseudogene");
		geneRepository.update(gene);

		Gene updated = geneRepository.findById(100L);
		Assert.notNull(updated);
		Assert.isTrue(updated.getPrimaryGeneSymbol().equals("TEST_TEST"));
		Assert.isTrue(updated.getGeneType().equals("pseudogene"));

		geneRepository.delete(100L);

	}

	@Test
	public void deleteTest(){

		Gene gene = new Gene(100L, "TEST", 9606, null, "1", "1", "Test gene", "protein-coding", null, null, null);
		geneRepository.insert(gene);

		Gene created = geneRepository.findById(100L);
		Assert.notNull(created);
		Assert.isTrue(created.getId().equals(100L));

		geneRepository.delete(100L);
		Gene deleted = geneRepository.findById(100L);
		Assert.isNull(deleted);

	}

	@Test
	public void rawJsonTestById() throws Exception {

		Gene gene = new Gene();
		gene.setEntrezGeneId(1L);
		List<Gene> genes = geneRepository.find(gene);
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.get(0).getEntrezGeneId().equals(1L));


	}

	@Test
	public void rawJsonTestByGeneType() throws Exception {

		Gene gene = new Gene();
		gene.setGeneType("protein-coding");
		List<Gene> genes = geneRepository.find(gene);
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 3);
		Assert.isTrue(genes.get(2).getEntrezGeneId().equals(4L));


	}

	@Test
	public void rawJsonTestByConflictingAttributes() throws Exception {

		Gene gene = new Gene();
		gene.setGeneType("protein-coding");
		gene.setEntrezGeneId(11L);

		List<Gene> genes = geneRepository.find(gene);
		Assert.notNull(genes);
		Assert.isTrue(genes.size() == 0);


	}

	@Test
	public void rawJsonTestByNestedAttributes() throws Exception {

		Gene gene = new Gene();
		gene.setAttribute("isKinase:Y");

		List<Gene> genes = geneRepository.find(gene);
		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 2);
		Assert.isTrue(genes.get(0).getEntrezGeneId().equals(1L));

	}

	@Test
	public void rawJsonTestByPartialNestedAttributes() throws Exception {

		Gene gene = new Gene();
		gene.setAttributeName("isKinase");

		List<Gene> genes = geneRepository.find(gene);

		Assert.notNull(genes);
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 5);
		Assert.isTrue(genes.get(0).getEntrezGeneId().equals(1L));



	}
	
}
