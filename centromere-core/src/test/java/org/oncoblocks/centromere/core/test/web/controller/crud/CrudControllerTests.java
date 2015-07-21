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

package org.oncoblocks.centromere.core.test.web.controller.crud;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oncoblocks.centromere.core.test.config.TestMongoConfig;
import org.oncoblocks.centromere.core.test.config.TestWebConfig;
import org.oncoblocks.centromere.core.test.models.EntrezGene;
import org.oncoblocks.centromere.core.test.repository.mongo.GeneRepository;
import org.oncoblocks.centromere.core.test.repository.mongo.MongoRepositoryConfig;
import org.oncoblocks.centromere.core.test.web.service.generic.GenericServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author woemler
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
		TestMongoConfig.class, TestWebConfig.class, MongoRepositoryConfig.class, 
		GenericServiceConfig.class, CrudControllerConfig.class})
@WebAppConfiguration
@FixMethodOrder
public class CrudControllerTests {

	@Autowired private GeneRepository geneRepository;
	private MockMvc mockMvc;
	@Autowired private WebApplicationContext webApplicationContext;
	private static boolean isConfigured = false;

	@Before
	public void setup(){

		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		if (isConfigured) return;

		geneRepository.deleteAll();
		EntrezGene
				geneA = new EntrezGene(1L, "GeneA", 9606, null, "1", null, "Test Gene A", "protein-coding", null, null, null);
		geneA.setAttribute("isKinase:Y");
		geneA.setAlias("ABC");
		EntrezGene
				geneB = new EntrezGene(2L, "GeneB", 9606, null, "3", null, "Test Gene B", "protein-coding", null, null, null);
		geneB.setAttribute("isKinase:N");
		geneB.setAlias("DEF");
		EntrezGene
				geneC = new EntrezGene(3L, "GeneC", 9606, null, "11", null, "Test Gene C", "pseudo", null, null, null);
		geneC.setAttribute("isKinase:N");
		geneC.setAlias("GHI");
		EntrezGene
				geneD = new EntrezGene(4L, "GeneD", 9606, null, "9", null, "Test Gene D", "protein-coding", null, null, null);
		geneD.setAttribute("isKinase:Y");
		geneD.setAlias("JKL");
		EntrezGene
				geneE = new EntrezGene(5L, "GeneE", 9606, null, "X", null, "Test Gene E", "pseudo", null, null, null);
		geneE.setAttribute("isKinase:N");
		geneE.setAlias("MNO");
		geneRepository.insert(Arrays.asList(new EntrezGene[] {geneA, geneB, geneC, geneD, geneE}));

		isConfigured = true;

	}

	@Test
	public void findById() throws Exception {

		mockMvc.perform(get("/crud/genes/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.entrezGeneId", is(1)))
				.andExpect(jsonPath("$.primaryGeneSymbol", is("GeneA")))
				.andExpect(jsonPath("$.links", hasSize(1)))
				.andExpect(jsonPath("$.links[0].rel", is("self")))
				.andExpect(jsonPath("$.links[0].href", endsWith("/genes/1")));
	}

	@Test
	public void findByIdNotFound() throws Exception{
		mockMvc.perform(get("/crud/genes/{id}", 99L))
				.andExpect(status().isNotFound());
	}

	@Test
	public void findByIdFiltered() throws Exception {
		mockMvc.perform(get("/crud/genes/{id}?exclude=links,primaryGeneSymbol", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.entrezGeneId", is(1)))
				.andExpect(jsonPath("$", not(hasKey("primaryGeneSymbol"))))
				.andExpect(jsonPath("$", not(hasKey("links"))));
	}

	@Test
	public void createTest() throws Exception {

		EntrezGene
				gene = new EntrezGene(6L, "GeneF", 9606, "", "10", "", "", "protein-coding", null, null, null);
		ObjectMapper mapper = new ObjectMapper();
		mapper.setFilters(new SimpleFilterProvider().addFilter("fieldFilter",
				SimpleBeanPropertyFilter.serializeAllExcept()).setFailOnUnknownId(false));
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		mockMvc.perform(post("/crud/genes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(gene)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$.entrezGeneId", is(6)));
		
		mockMvc.perform(get("/crud/genes/{id}", 6L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.entrezGeneId", is(6)));

		geneRepository.delete(6L);

	}

	@Test
	public void updateTest() throws Exception {

		EntrezGene
				gene = new EntrezGene(7L, "GeneG", 9606, "", "10", "", "", "protein-coding", null, null, null);
		ObjectMapper mapper = new ObjectMapper();
				mapper.setFilters(new SimpleFilterProvider().addFilter("fieldFilter",
						SimpleBeanPropertyFilter.serializeAllExcept()).setFailOnUnknownId(false));
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		mockMvc.perform(post("/crud/genes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(gene)))
				.andExpect(status().isCreated());

		gene.setPrimaryGeneSymbol("TEST_GENE");

		mockMvc.perform(put("/crud/genes/{id}", 7L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(gene)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$.entrezGeneId", is(7)))
				.andExpect(jsonPath("$.primaryGeneSymbol", is("TEST_GENE")));

		mockMvc.perform(get("/crud/genes/{id}", 7L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.entrezGeneId", is(7)))
				.andExpect(jsonPath("$.primaryGeneSymbol", is("TEST_GENE")));

		geneRepository.delete(7L);

	}

	@Test
	public void deleteTest() throws Exception {

		EntrezGene
				gene = new EntrezGene(8L, "GeneH", 9606, "", "10", "", "", "protein-coding", null, null, null);
		geneRepository.insert(gene);

		mockMvc.perform(delete("/crud/genes/{id}", 8L))
				.andExpect(status().isOk());

		mockMvc.perform(get("/crud/genes/{id}", 8L))
				.andExpect(status().isNotFound());

	}

	@Test
	public void optionsTest() throws Exception {

	}
	
}
