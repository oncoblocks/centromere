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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oncoblocks.centromere.web.test.config.TestMongoConfig;
import org.oncoblocks.centromere.web.test.config.TestWebConfig;
import org.oncoblocks.centromere.web.test.models.CopyNumber;
import org.oncoblocks.centromere.web.test.repository.CopyNumberRepository;
import org.oncoblocks.centromere.web.test.repository.MongoRepositoryConfig;
import org.oncoblocks.centromere.web.util.ApiMediaTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author woemler
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestWebConfig.class, TestMongoConfig.class, 
		MongoRepositoryConfig.class, ControllerIntegrationTestConfig.class})
@WebAppConfiguration
public class AbstractControllerIntegrationTests {
	
	@Autowired private WebApplicationContext webApplicationContext;
	@Autowired private CopyNumberRepository repository;
	private MockMvc mockMvc;
	private static final String BASE_URL = "/cnv";
	
	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		repository.deleteAll();
		repository.insert(new CopyNumber("1", "33", "GeneA", 1.20, "Y"));
		repository.insert(new CopyNumber("2", "101", "GeneB", 2.11, "N"));
		repository.insert(new CopyNumber("3", "45", "GeneC", 3.45, "Y"));
		repository.insert(new CopyNumber("4", "4453", "GeneD", 1.03, "Y"));
		repository.insert(new CopyNumber("5", "435", "GeneE", 3.00, "Y"));
	}
	
	@Test
	public void findById() throws Exception {
		mockMvc.perform(get(BASE_URL + "/{id}", "1")
				.accept(ApiMediaTypes.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is("1")))
				.andExpect(jsonPath("$.geneId", is("33")))
				.andExpect(jsonPath("$.geneSymbol", is("GeneA")))
				.andExpect(jsonPath("$.links", hasSize(2)))
				.andExpect(jsonPath("$.links[0].rel", is("self")))
				.andExpect(jsonPath("$.links[0].href", endsWith(BASE_URL + "/1")))
				.andExpect(jsonPath("$.links[1].rel", is("gene")))
				.andExpect(jsonPath("$.links[1].href", endsWith("/33")));
				
	}

	@Test
	public void findByIdNoHal() throws Exception {
		mockMvc.perform(get(BASE_URL + "/{id}", "1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is("1")))
				.andExpect(jsonPath("$.geneSymbol", is("GeneA")))
				.andExpect(jsonPath("$", not(hasKey("links"))));
	}

	@Test
	public void findByIdNotFound() throws Exception{
		mockMvc.perform(get(BASE_URL + "/{id}", "99"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void findByIdFiltered() throws Exception {
		mockMvc.perform(get(BASE_URL + "/{id}?exclude=links,geneSymbol", "1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is("1")))
				.andExpect(jsonPath("$", not(hasKey("geneSymbol"))))
				.andExpect(jsonPath("$", not(hasKey("links"))));
	}

	@Test
	public void findByIdWithoutLinks() throws Exception {
		mockMvc.perform(get(BASE_URL + "/{id}", "1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is("1")))
				.andExpect(jsonPath("$.geneSymbol", is("GeneA")))
				.andExpect(jsonPath("$", not(hasKey("links"))));
	}

	@Test
	public void findAll() throws Exception {
		mockMvc.perform(get(BASE_URL).accept(ApiMediaTypes.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(5)))
				.andExpect(jsonPath("$.content[0]", hasKey("id")))
				.andExpect(jsonPath("$.content[0].id", is("1")))
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
				.andExpect(jsonPath("$[0]", hasKey("id")))
				.andExpect(jsonPath("$[0].id", is("1")))
				.andExpect(jsonPath("$[0]", not(hasKey("links"))))
				.andExpect(jsonPath("$", not(hasKey("pageMetadata"))));
	}

	@Test
	public void findFiltered() throws Exception {
		mockMvc.perform(get(BASE_URL + "?exclude=links,geneSymbol").accept(
				ApiMediaTypes.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(5)))
				.andExpect(jsonPath("$.content[0]", hasKey("id")))
				.andExpect(jsonPath("$.content[0].id", is("1")))
				.andExpect(jsonPath("$.content[0]", not(hasKey("geneSymbol"))))
				.andExpect(jsonPath("$.content[0]", not(hasKey("links"))));
	}

	@Test
	public void findFieldFiltered() throws Exception {
		mockMvc.perform(get(BASE_URL + "?fields=links,geneSymbol").accept(
				ApiMediaTypes.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(5)))
				.andExpect(jsonPath("$.content[0]", not(hasKey("id"))))
				.andExpect(jsonPath("$.content[0]", hasKey("geneSymbol")))
				.andExpect(jsonPath("$.content[0]", hasKey("links")));
	}

	@Test
	public void findPaged() throws Exception {
		mockMvc.perform(get(BASE_URL + "?page=1&size=3").accept(ApiMediaTypes.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(jsonPath("$.content[0]", hasKey("id")))
				.andExpect(jsonPath("$.content[0].id", is("4")))
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
				.andExpect(jsonPath("$.content[0]", hasKey("id")))
				.andExpect(jsonPath("$.content[0].id", is("4")))
				.andExpect(jsonPath("$.content[0]", not(hasKey("links"))))
				.andExpect(jsonPath("$", not(hasKey("links"))));
	}

	@Test
	public void findSorted() throws Exception {
		mockMvc.perform(get(BASE_URL + "?sort=gene,desc")
				.accept(ApiMediaTypes.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(5)))
				.andExpect(jsonPath("$.content[0]", hasKey("id")))
				.andExpect(jsonPath("$.content[0].id", is("5")));
	}
	
	@Test
	public void findByFieldName() throws Exception {
		mockMvc.perform(get(BASE_URL + "?geneId=45")
				.accept(ApiMediaTypes.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(jsonPath("$.content[0]", hasKey("id")))
				.andExpect(jsonPath("$.content[0].id", is("3")))
				.andExpect(jsonPath("$.content[0]", hasKey("geneId")))
				.andExpect(jsonPath("$.content[0].geneId", is("45")))
				.andExpect(jsonPath("$.content[0]", hasKey("links")))
				.andExpect(jsonPath("$.content[0].links[0].rel", is("self")))
				.andExpect(jsonPath("$.content[0].links[0].href", endsWith(BASE_URL + "/3")))
				.andExpect(jsonPath("$.content[0].links[1].rel", is("gene")))
				.andExpect(jsonPath("$.content[0].links[1].href", endsWith("/45")));
	}

	@Test
	public void findByFieldAlias() throws Exception {
		mockMvc.perform(get(BASE_URL + "?gene=GeneC")
				.accept(ApiMediaTypes.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(jsonPath("$.content[0]", hasKey("id")))
				.andExpect(jsonPath("$.content[0].id", is("3")))
				.andExpect(jsonPath("$.content[0]", hasKey("geneSymbol")))
				.andExpect(jsonPath("$.content[0].geneSymbol", is("GeneC")))
				.andExpect(jsonPath("$.content[0]", hasKey("links")))
				.andExpect(jsonPath("$.content[0].links[0].rel", is("self")))
				.andExpect(jsonPath("$.content[0].links[0].href", endsWith(BASE_URL + "/3")))
				.andExpect(jsonPath("$.content[0].links[1].rel", is("gene")))
				.andExpect(jsonPath("$.content[0].links[1].href", endsWith("/45")));
	}
	
	@Test
	public void findByInvalidParameter() throws Exception {
		mockMvc.perform(get(BASE_URL + "?bad=param")) // Invalid parameters are ignored by default
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)));
	}

	@Test
	public void findByIgnoredParameter() throws Exception {
		mockMvc.perform(get(BASE_URL + "?flag=N")) // Would expect one record, but the field is marked as ignored.
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)));
	}

	@Test
	public void findByValueGreaterThan() throws Exception {
		mockMvc.perform(get(BASE_URL + "?signalGreaterThan=2.0&sort=signal,desc"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0]", hasKey("id")))
				.andExpect(jsonPath("$[0].id", is("3")))
				.andExpect(jsonPath("$[0]", hasKey("signal")))
				.andExpect(jsonPath("$[0].signal", is(3.45)));
	}

	@Test
	public void findByValueBetween() throws Exception {
		mockMvc.perform(get(BASE_URL + "?signalBetween=1.2,3.0"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0]", hasKey("id")))
				.andExpect(jsonPath("$[0].id", is("2")))
				.andExpect(jsonPath("$[0]", hasKey("signal")))
				.andExpect(jsonPath("$[0].signal", is(2.11)));
	}

	@Test
	public void findByValueOutside() throws Exception {
		mockMvc.perform(get(BASE_URL + "?signalOutside=1.2,3.0&sort=signal,asc"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[0]", hasKey("id")))
				.andExpect(jsonPath("$[0].id", is("4")))
				.andExpect(jsonPath("$[0]", hasKey("geneSymbol")))
				.andExpect(jsonPath("$[0].geneSymbol", is("GeneD")));
	}

}
