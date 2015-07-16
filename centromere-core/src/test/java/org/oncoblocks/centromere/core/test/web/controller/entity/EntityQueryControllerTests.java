package org.oncoblocks.centromere.core.test.web.controller.entity;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oncoblocks.centromere.core.test.config.TestMongoConfig;
import org.oncoblocks.centromere.core.test.config.TestWebConfig;
import org.oncoblocks.centromere.core.test.models.Gene;
import org.oncoblocks.centromere.core.test.repository.mongo.GeneRepository;
import org.oncoblocks.centromere.core.test.repository.mongo.MongoRepositoryConfig;
import org.oncoblocks.centromere.core.test.web.service.generic.GenericServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author woemler
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
		TestMongoConfig.class, TestWebConfig.class, MongoRepositoryConfig.class,
		GenericServiceConfig.class, EntityControllerConfig.class})
@WebAppConfiguration
@FixMethodOrder
public class EntityQueryControllerTests {

	@Autowired private GeneRepository geneRepository;
	private MockMvc mockMvc;
	@Autowired private WebApplicationContext webApplicationContext;
	private static boolean isConfigured = false;

	@Before
	public void setup(){

		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

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
		geneRepository.insert(Arrays.asList(new Gene[] {geneA, geneB, geneC, geneD, geneE}));

		isConfigured = true;

	}

	@Test
	public void findAll() throws Exception {
		mockMvc.perform(get("/eq/genes"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(5)))
				.andExpect(jsonPath("$.content[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$.content[0].entrezGeneId", is(1)))
				.andExpect(jsonPath("$", hasKey("links")))
				.andExpect(jsonPath("$.links", hasSize(1)))
				.andExpect(jsonPath("$.links[0].rel", is("self")))
				.andExpect(jsonPath("$.links[0].href", endsWith("/genes")))
				.andExpect(jsonPath("$", not(hasKey("pageMetadata"))));
	}

	@Test
	public void findFiltered() throws Exception {
		mockMvc.perform(get("/eq/genes?exclude=links,primaryGeneSymbol"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(5)))
				.andExpect(jsonPath("$.content[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$.content[0].entrezGeneId", is(1)))
				.andExpect(jsonPath("$.content[0]", not(hasKey("primaryGeneSymbol"))))
				.andExpect(jsonPath("$.content[0]", not(hasKey("links"))));
	}

	@Test
	public void findFieldFiltered() throws Exception {
		mockMvc.perform(get("/eq/genes?fields=links,primaryGeneSymbol"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(5)))
				.andExpect(jsonPath("$.content[0]", not(hasKey("entrezGeneId"))))
				.andExpect(jsonPath("$.content[0]", hasKey("primaryGeneSymbol")))
				.andExpect(jsonPath("$.content[0]", hasKey("links")));
	}

	@Test
	public void findMultipleByParams() throws Exception {
		mockMvc.perform(get("/eq/genes?geneType=pseudo"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(jsonPath("$.content[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$.content[0].entrezGeneId", is(3)))
				.andExpect(jsonPath("$", hasKey("links")))
				.andExpect(jsonPath("$.links", hasSize(1)))
				.andExpect(jsonPath("$.links[0].rel", is("self")))
				.andExpect(jsonPath("$.links[0].href", endsWith("/genes?geneType=pseudo")))
				.andExpect(jsonPath("$", not(hasKey("pageMetadata"))));
	}

	@Test
	public void findPaged() throws Exception {
		mockMvc.perform(get("/eq/genes?page=1&size=3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(jsonPath("$.content[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$.content[0].entrezGeneId", is(4)))
				.andExpect(jsonPath("$", hasKey("links")))
				.andExpect(jsonPath("$.links", hasSize(2)))
				.andExpect(jsonPath("$.links[0].rel", is("self")))
				.andExpect(jsonPath("$", hasKey("page")))
				.andExpect(jsonPath("$.page.totalElements", is(5)))
				.andExpect(jsonPath("$.page.number", is(1)))
				.andExpect(jsonPath("$.page.size", is(3)))
				.andExpect(jsonPath("$.page.totalPages", is(2)));
	}

	@Test
	public void findSorted() throws Exception {
		mockMvc.perform(get("/eq/genes?sort=primaryGeneSymbol,desc"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(5)))
				.andExpect(jsonPath("$.content[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$.content[0].entrezGeneId", is(5)));
	}
	
}
