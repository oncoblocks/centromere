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
import org.oncoblocks.centromere.core.test.repository.mongo.EntrezGeneRepository;
import org.oncoblocks.centromere.core.test.repository.mongo.MongoRepositoryConfig;
import org.oncoblocks.centromere.core.web.util.HalMediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author woemler
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
		TestMongoConfig.class, TestWebConfig.class, MongoRepositoryConfig.class, CrudControllerConfig.class})
@WebAppConfiguration
@FixMethodOrder
public class CrudControllerTests {

	@Autowired private EntrezGeneRepository geneRepository;
	private MockMvc mockMvc;
	@Autowired private WebApplicationContext webApplicationContext;
	private static boolean isConfigured = false;

	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		if (isConfigured) return;
		geneRepository.deleteAll();
		for (EntrezGene gene: EntrezGene.createDummyData()){
			geneRepository.insert(gene);
		}
		isConfigured = true;
	}
	
	@Test
	public void headTest() throws Exception {
		mockMvc.perform(head("/genes"))
				.andExpect(status().isOk());
	}

	@Test
	public void findById() throws Exception {

		mockMvc.perform(get("/genes/{id}", 1L)
				.accept(HalMediaType.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.entrezGeneId", is(1)))
				.andExpect(jsonPath("$.primaryGeneSymbol", is("GeneA")))
				.andExpect(jsonPath("$.links", hasSize(1)))
				.andExpect(jsonPath("$.links[0].rel", is("self")))
				.andExpect(jsonPath("$.links[0].href", endsWith("/genes/1")));
	}

	@Test
	public void findByIdNoHal() throws Exception {

		mockMvc.perform(get("/genes/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.entrezGeneId", is(1)))
				.andExpect(jsonPath("$.primaryGeneSymbol", is("GeneA")))
				.andExpect(jsonPath("$", not(hasKey("links"))));
	}

	@Test
	public void findByIdNotFound() throws Exception{
		mockMvc.perform(get("/genes/{id}", 99L))
				.andExpect(status().isNotFound());
	}

	@Test
	public void findByIdFiltered() throws Exception {
		mockMvc.perform(get("/genes/{id}?exclude=links,primaryGeneSymbol", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.entrezGeneId", is(1)))
				.andExpect(jsonPath("$", not(hasKey("primaryGeneSymbol"))))
				.andExpect(jsonPath("$", not(hasKey("links"))));
	}

	@Test
	public void findByIdWithoutLinks() throws Exception {

		mockMvc.perform(get("/genes/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.entrezGeneId", is(1)))
				.andExpect(jsonPath("$.primaryGeneSymbol", is("GeneA")))
				.andExpect(jsonPath("$", not(hasKey("links"))));
	}

	@Test
	public void findAll() throws Exception {
		mockMvc.perform(get("/genes").accept(HalMediaType.APPLICATION_HAL_JSON_VALUE))
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
	public void findAllWithoutLinks() throws Exception {
		mockMvc.perform(get("/genes"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)))
				.andExpect(jsonPath("$[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$[0].entrezGeneId", is(1)))
				.andExpect(jsonPath("$[0]", not(hasKey("links"))))
				.andExpect(jsonPath("$", not(hasKey("pageMetadata"))));
	}

	@Test
	public void findFiltered() throws Exception {
		mockMvc.perform(get("/genes?exclude=links,primaryGeneSymbol").accept(
				HalMediaType.APPLICATION_HAL_JSON_VALUE))
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
		mockMvc.perform(get("/genes?fields=links,primaryGeneSymbol").accept(
				HalMediaType.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(5)))
				.andExpect(jsonPath("$.content[0]", not(hasKey("entrezGeneId"))))
				.andExpect(jsonPath("$.content[0]", hasKey("primaryGeneSymbol")))
				.andExpect(jsonPath("$.content[0]", hasKey("links")));
	}

	@Test
	public void findMultipleByParams() throws Exception {
		mockMvc.perform(get("/genes?geneType=pseudo").accept(HalMediaType.APPLICATION_HAL_JSON_VALUE))
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
	public void findByAlias() throws Exception {
		mockMvc.perform(get("/genes?alias=MNO").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$[0].entrezGeneId", is(5)))
				.andExpect(jsonPath("$[0]", not(hasKey("links"))));
	}

	@Test
	public void findByKeyValueAttributes() throws Exception {
		mockMvc.perform(get("/genes?attribute=isKinase:Y").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$[0].entrezGeneId", is(1)))
				.andExpect(jsonPath("$[0]", not(hasKey("links"))));
	}

	@Test
	public void findPaged() throws Exception {
		mockMvc.perform(get("/genes?page=1&size=3").accept(HalMediaType.APPLICATION_HAL_JSON_VALUE))
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
	public void findPagedWithoutLinks() throws Exception {
		mockMvc.perform(get("/genes?page=1&size=3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(jsonPath("$.content[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$.content[0].entrezGeneId", is(4)))
				.andExpect(jsonPath("$.content[0]", not(hasKey("links"))))
				.andExpect(jsonPath("$", not(hasKey("links"))));
	}

	@Test
	public void findSorted() throws Exception {
		mockMvc.perform(get("/genes?sort=primaryGeneSymbol+desc").accept(
				HalMediaType.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(5)))
				.andExpect(jsonPath("$.content[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$.content[0].entrezGeneId", is(5)));
	}

	@Test
	public void createTest() throws Exception {

		EntrezGene
				gene = new EntrezGene(6L, "GeneF", 9606, "", "10", "", "", "protein-coding", null, null, null);
		ObjectMapper mapper = new ObjectMapper();
		mapper.setFilters(new SimpleFilterProvider().addFilter("fieldFilter",
				SimpleBeanPropertyFilter.serializeAllExcept()).setFailOnUnknownId(false));
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		mockMvc.perform(post("/genes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(gene)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$.entrezGeneId", is(6)));
		
		mockMvc.perform(get("/genes/{id}", 6L))
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

		mockMvc.perform(post("/genes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(gene)))
				.andExpect(status().isCreated());

		gene.setPrimaryGeneSymbol("TEST_GENE");

		mockMvc.perform(put("/genes/{id}", 7L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(gene)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$.entrezGeneId", is(7)))
				.andExpect(jsonPath("$.primaryGeneSymbol", is("TEST_GENE")));

		mockMvc.perform(get("/genes/{id}", 7L))
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

		mockMvc.perform(delete("/genes/{id}", 8L))
				.andExpect(status().isOk());

		mockMvc.perform(get("/genes/{id}", 8L))
				.andExpect(status().isNotFound());

	}

//	@Test
//	public void optionsTest() throws Exception {
//		MvcResult result = mockMvc.perform(request(HttpMethod.OPTIONS, "/genes").accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$", hasKey("description")))
//				.andReturn();
//		System.out.println("Response: " + result.getResponse().getContentAsString());
//	}
	
}
