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

package org.oncoblocks.centromere.web.test.controller.readonly;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oncoblocks.centromere.web.test.config.TestJdbcDataSourceConfig;
import org.oncoblocks.centromere.web.test.config.TestWebConfig;
import org.oncoblocks.centromere.web.test.models.Subject;
import org.oncoblocks.centromere.web.test.repository.jdbc.JdbcRepositoryConfig;
import org.oncoblocks.centromere.web.test.repository.jdbc.SubjectRepository;
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
@ContextConfiguration(classes = {TestJdbcDataSourceConfig.class, JdbcRepositoryConfig.class,
		TestReadOnlyControllerConfig.class, TestWebConfig.class})
@WebAppConfiguration
@FixMethodOrder
public class ReadOnlyControllerTests {

	@Autowired private SubjectRepository subjectRepository;
	private MockMvc mockMvc;
	@Autowired private WebApplicationContext webApplicationContext;
	private static boolean isConfigured = false;

	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		if (isConfigured) return;
		subjectRepository.deleteAll();
		for (Subject subject: Subject.createDummyData()){
			subjectRepository.insert(subject);
		}
		isConfigured = true;
	}

	
	@Test
	public void findById() throws Exception {
		mockMvc.perform(get("/subjects/1").accept(HalMediaType.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("id")))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("PersonA")))
				.andExpect(jsonPath("$", hasKey("links")))
				.andExpect(jsonPath("$.links", hasSize(1)))
				.andExpect(jsonPath("$.links[0].rel", is("self")))
				.andExpect(jsonPath("$.links[0].href", endsWith("/subjects/1")));
	}

	@Test
	public void findByIdWithoutLinks() throws Exception {
		mockMvc.perform(get("/subjects/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("id")))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("PersonA")))
				.andExpect(jsonPath("$", not(hasKey("links"))));
	}

	@Test
	public void findByIdFiltered() throws Exception {
		mockMvc.perform(get("/subjects/1?exclude=links,gender").accept(HalMediaType.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("id")))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("PersonA")))
				.andExpect(jsonPath("$", not(hasKey("links"))))
				.andExpect(jsonPath("$", not(hasKey("gender"))));
	}

	@Test
	public void findByIdWithoutLinksFiltered() throws Exception {
		mockMvc.perform(get("/subjects/1?fields=id,name").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("id")))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("PersonA")))
				.andExpect(jsonPath("$", not(hasKey("links"))))
				.andExpect(jsonPath("$", not(hasKey("gender"))))
				.andExpect(jsonPath("$", not(hasKey("species"))));;
	}

	@Test
	public void findAll() throws Exception {
		mockMvc.perform(get("/subjects").accept(HalMediaType.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(5)))
				.andExpect(jsonPath("$.content[0]", hasKey("id")))
				.andExpect(jsonPath("$.content[0].id", is(1)))
				.andExpect(jsonPath("$", hasKey("links")))
				.andExpect(jsonPath("$.links", hasSize(1)))
				.andExpect(jsonPath("$.links[0].rel", is("self")))
				.andExpect(jsonPath("$.links[0].href", endsWith("/subjects")))
				.andExpect(jsonPath("$", not(hasKey("pageMetadata"))));
	}

	@Test
	public void findAllWithoutLinks() throws Exception {
		mockMvc.perform(get("/subjects").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)))
				.andExpect(jsonPath("$[0]", hasKey("id")))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0]", not(hasKey("links"))))
				.andExpect(jsonPath("$", not(hasKey("pageMetadata"))));
	}

	@Test
	public void findFiltered() throws Exception {
		mockMvc.perform(get("/subjects?exclude=links,name")
				.accept(HalMediaType.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(5)))
				.andExpect(jsonPath("$.content[0]", hasKey("id")))
				.andExpect(jsonPath("$.content[0].id", is(1)))
				.andExpect(jsonPath("$.content[0]", not(hasKey("name"))))
				.andExpect(jsonPath("$.content[0]", not(hasKey("links"))));
	}

	@Test
	public void findFieldFiltered() throws Exception {
		mockMvc.perform(get("/subjects?fields=links,name")
				.accept(HalMediaType.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(5)))
				.andExpect(jsonPath("$.content[0]", not(hasKey("id"))))
				.andExpect(jsonPath("$.content[0]", hasKey("name")))
				.andExpect(jsonPath("$.content[0]", hasKey("links")));
	}

	@Test
	public void findMultipleBySingleParam() throws Exception {
		mockMvc.perform(get("/subjects?name=MCF7").accept(HalMediaType.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(jsonPath("$.content[0]", hasKey("id")))
				.andExpect(jsonPath("$.content[0].id", is(4)))
				.andExpect(jsonPath("$", hasKey("links")))
				.andExpect(jsonPath("$.links", hasSize(1)))
				.andExpect(jsonPath("$.links[0].rel", is("self")))
				.andExpect(jsonPath("$.links[0].href", endsWith("/subjects?name=MCF7")))
				.andExpect(jsonPath("$", not(hasKey("pageMetadata"))));
	}

	//	@Test
	//	public void findMultipleByAlias() throws Exception {
	//		mockMvc.perform(get("/subjects?aliasName=MCF7_BREAST").accept(HalMediaType.APPLICATION_HAL_JSON_VALUE))
	//				.andExpect(status().isOk())
	//				.andExpect(jsonPath("$", hasKey("content")))
	//				.andExpect(jsonPath("$.content", hasSize(1)))
	//				.andExpect(jsonPath("$.content[0]", hasKey("id")))
	//				.andExpect(jsonPath("$.content[0].id", is(4)))
	//				.andExpect(jsonPath("$", hasKey("links")))
	//				.andExpect(jsonPath("$.links", hasSize(1)))
	//				.andExpect(jsonPath("$.links[0].rel", is("self")))
	//				.andExpect(jsonPath("$.links[0].href", endsWith("/subjects?aliasName=MCF7_BREAST")))
	//				.andExpect(jsonPath("$", not(hasKey("pageMetadata"))));
	//	}

	//	@Test
	//	public void findMultipleByAttribute() throws Exception {
	//		mockMvc.perform(get("/subjects?attributeName=isCellLine&attributeValue=Y").accept(HalMediaType.APPLICATION_HAL_JSON_VALUE))
	//				.andExpect(status().isOk())
	//				.andExpect(jsonPath("$", hasKey("content")))
	//				.andExpect(jsonPath("$.content", hasSize(2)))
	//				.andExpect(jsonPath("$.content[0]", hasKey("id")))
	//				.andExpect(jsonPath("$.content[0].id", is(4)))
	//				.andExpect(jsonPath("$", hasKey("links")))
	//				.andExpect(jsonPath("$.links", hasSize(1)))
	//				.andExpect(jsonPath("$.links[0].rel", is("self")))
	//				.andExpect(jsonPath("$.links[0].href", endsWith("/subjects?attributeName=isCellLine&attributeValue=Y")))
	//				.andExpect(jsonPath("$", not(hasKey("pageMetadata"))));
	//	}

	@Test
	public void findPaged() throws Exception {
		mockMvc.perform(get("/subjects?page=1&size=3").accept(HalMediaType.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(jsonPath("$.content[0]", hasKey("id")))
				.andExpect(jsonPath("$.content[0].id", is(4)))
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
		mockMvc.perform(get("/subjects?page=1&size=3").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(jsonPath("$.content[0]", hasKey("id")))
				.andExpect(jsonPath("$.content[0].id", is(4)))
				.andExpect(jsonPath("$", not(hasKey("links"))));
	}

	@Test
	public void findSorted() throws Exception {
		mockMvc.perform(get("/subjects?sort=subjectId,desc").accept(HalMediaType.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(5)))
				.andExpect(jsonPath("$.content[0]", hasKey("id")))
				.andExpect(jsonPath("$.content[0].id", is(5)));
	}
	
	@Test
	public void postTest() throws Exception {
		mockMvc.perform(post("/subjects"))
				.andExpect(status().isMethodNotAllowed());
	}

	@Test
	public void putTest() throws Exception {
		mockMvc.perform(put("/subjects/1"))
				.andExpect(status().isMethodNotAllowed());
	}

	@Test
	public void deleteTest() throws Exception {
		mockMvc.perform(delete("/subjects/1"))
				.andExpect(status().isMethodNotAllowed());
	}
	
	@Test
	public void headTest() throws Exception {
		mockMvc.perform(head("/subjects"))
				.andExpect(status().isOk());
		mockMvc.perform(head("/subjects/1"))
				.andExpect(status().isOk());
	}
	
}
