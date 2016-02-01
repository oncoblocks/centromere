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

package org.oncoblocks.centromere.web.test.controller;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oncoblocks.centromere.web.test.config.TestMongoConfig;
import org.oncoblocks.centromere.web.test.config.TestWebConfig;
import org.oncoblocks.centromere.web.test.models.EntrezGene;
import org.oncoblocks.centromere.web.test.repository.mongo.EntrezGeneRepository;
import org.oncoblocks.centromere.web.test.repository.mongo.MongoRepositoryConfig;
import org.oncoblocks.centromere.web.util.HalMediaType;
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
@ContextConfiguration(classes = {TestMongoConfig.class, MongoRepositoryConfig.class,
		ControllerIntegrationTestConfig.class, TestWebConfig.class})
@WebAppConfiguration
@FixMethodOrder
public class ReadOnlyControllerIntegrationTests {

	@Autowired private EntrezGeneRepository repository;
	private MockMvc mockMvc;
	@Autowired private WebApplicationContext webApplicationContext;
	private static boolean isConfigured = false;
	
	private static final String BASE_URL = "/genes/read";

	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		if (isConfigured) return;
		repository.deleteAll();
		for (EntrezGene gene: EntrezGene.createDummyData()){
			repository.insert(gene);
		}
		isConfigured = true;
	}
	
	@Test
	public void findById() throws Exception {

		mockMvc.perform(get(BASE_URL + "/{id}", 1L)
				.accept(HalMediaType.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.entrezGeneId", is(1)))
				.andExpect(jsonPath("$.primaryGeneSymbol", is("GeneA")))
				.andExpect(jsonPath("$.links", hasSize(1)))
				.andExpect(jsonPath("$.links[0].rel", is("self")))
				.andExpect(jsonPath("$.links[0].href", endsWith(BASE_URL + "/1")));
	}

	@Test
	public void findByIdNoHal() throws Exception {

		mockMvc.perform(get(BASE_URL + "/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.entrezGeneId", is(1)))
				.andExpect(jsonPath("$.primaryGeneSymbol", is("GeneA")))
				.andExpect(jsonPath("$", not(hasKey("links"))));
	}

	@Test
	public void findByIdNotFound() throws Exception{
		mockMvc.perform(get(BASE_URL + "/{id}", 99L))
				.andExpect(status().isNotFound());
	}

	@Test
	public void findByIdFiltered() throws Exception {
		mockMvc.perform(get(BASE_URL + "/{id}?exclude=links,primaryGeneSymbol", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.entrezGeneId", is(1)))
				.andExpect(jsonPath("$", not(hasKey("primaryGeneSymbol"))))
				.andExpect(jsonPath("$", not(hasKey("links"))));
	}

	@Test
	public void findByIdWithoutLinks() throws Exception {

		mockMvc.perform(get(BASE_URL + "/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.entrezGeneId", is(1)))
				.andExpect(jsonPath("$.primaryGeneSymbol", is("GeneA")))
				.andExpect(jsonPath("$", not(hasKey("links"))));
	}

	@Test
	public void findAll() throws Exception {
		mockMvc.perform(get(BASE_URL).accept(HalMediaType.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(5)))
				.andExpect(jsonPath("$.content[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$.content[0].entrezGeneId", is(1)))
				.andExpect(jsonPath("$", hasKey("links")))
				.andExpect(jsonPath("$.links", hasSize(1)))
				.andExpect(jsonPath("$.links[0].rel", is("self")))
				.andExpect(jsonPath("$.links[0].href", endsWith(BASE_URL)))
				.andExpect(jsonPath("$", not(hasKey("pageMetadata"))));
	}

	@Test
	public void findAllWithoutLinks() throws Exception {
		mockMvc.perform(get(BASE_URL))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)))
				.andExpect(jsonPath("$[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$[0].entrezGeneId", is(1)))
				.andExpect(jsonPath("$[0]", not(hasKey("links"))))
				.andExpect(jsonPath("$", not(hasKey("pageMetadata"))));
	}

	@Test
	public void findFiltered() throws Exception {
		mockMvc.perform(get(BASE_URL + "?exclude=links,primaryGeneSymbol").accept(
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
		mockMvc.perform(get(BASE_URL + "?fields=links,primaryGeneSymbol").accept(
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
		mockMvc.perform(get(BASE_URL + "?geneType=pseudo").accept(HalMediaType.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(jsonPath("$.content[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$.content[0].entrezGeneId", is(3)))
				.andExpect(jsonPath("$", hasKey("links")))
				.andExpect(jsonPath("$.links", hasSize(1)))
				.andExpect(jsonPath("$.links[0].rel", is("self")))
				.andExpect(jsonPath("$.links[0].href", endsWith(BASE_URL + "?geneType=pseudo")))
				.andExpect(jsonPath("$", not(hasKey("pageMetadata"))));
	}

	@Test
	public void findByAlias() throws Exception {
		mockMvc.perform(get(BASE_URL + "?alias=MNO").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$[0].entrezGeneId", is(5)))
				.andExpect(jsonPath("$[0]", not(hasKey("links"))));
	}

	@Test
	public void findByKeyValueAttributes() throws Exception {
		mockMvc.perform(get(BASE_URL + "?attribute=isKinase:Y").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$[0].entrezGeneId", is(1)))
				.andExpect(jsonPath("$[0]", not(hasKey("links"))));
	}

	@Test
	public void findPaged() throws Exception {
		mockMvc.perform(get(BASE_URL + "?page=1&size=3").accept(HalMediaType.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(jsonPath("$.content[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$.content[0].entrezGeneId", is(4)))
				.andExpect(jsonPath("$", hasKey("links")))
				.andExpect(jsonPath("$.links", hasSize(4)))
				.andExpect(jsonPath("$.links[0].rel", is("first")))
				.andExpect(jsonPath("$", hasKey("page")))
				.andExpect(jsonPath("$.page.totalElements", is(5)))
				.andExpect(jsonPath("$.page.number", is(1)))
				.andExpect(jsonPath("$.page.size", is(3)))
				.andExpect(jsonPath("$.page.totalPages", is(2)));
	}

	@Test
	public void findPagedWithoutLinks() throws Exception {
		mockMvc.perform(get(BASE_URL + "?page=1&size=3"))
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
		mockMvc.perform(get(BASE_URL + "?sort=geneSymbol,desc").accept(
				HalMediaType.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(5)))
				.andExpect(jsonPath("$.content[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$.content[0].entrezGeneId", is(5)));
	}

	@Test
	public void findDistinct() throws Exception {
		mockMvc.perform(get(BASE_URL + "/distinct?field=geneType"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	public void findDistinctFiltered() throws Exception {
		mockMvc.perform(get(BASE_URL + "/distinct?field=primaryGeneSymbol&geneType=protein-coding"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[2]", is("GeneD")));
	}
	
	@Test
	public void postTest() throws Exception {
		mockMvc.perform(post(BASE_URL))
				.andExpect(status().isMethodNotAllowed());
	}

	@Test
	public void putTest() throws Exception {
		mockMvc.perform(put(BASE_URL + "/1"))
				.andExpect(status().isMethodNotAllowed());
	}

	@Test
	public void deleteTest() throws Exception {
		mockMvc.perform(delete(BASE_URL + "/1"))
				.andExpect(status().isMethodNotAllowed());
	}
	
	@Test
	public void headTest() throws Exception {
		mockMvc.perform(head(BASE_URL))
				.andExpect(status().isOk());
		mockMvc.perform(head(BASE_URL + "/1"))
				.andExpect(status().isOk());
	}
	
}
