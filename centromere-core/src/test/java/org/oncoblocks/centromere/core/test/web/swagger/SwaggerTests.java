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

package org.oncoblocks.centromere.core.test.web.swagger;

import io.github.robwin.markup.builder.MarkupLanguage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oncoblocks.centromere.core.test.config.TestMongoConfig;
import org.oncoblocks.centromere.core.test.config.TestSwaggerConfig;
import org.oncoblocks.centromere.core.test.config.TestWebConfig;
import org.oncoblocks.centromere.core.test.repository.mongo.MongoRepositoryConfig;
import org.oncoblocks.centromere.core.test.web.controller.crud.CrudControllerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import springfox.documentation.staticdocs.Swagger2MarkupResultHandler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author woemler
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
		TestMongoConfig.class, TestWebConfig.class, MongoRepositoryConfig.class,
		CrudControllerConfig.class, TestSwaggerConfig.class})
@WebAppConfiguration
public class SwaggerTests {

	private MockMvc mockMvc;
	@Autowired private WebApplicationContext webApplicationContext;
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void findTest() throws Exception {
		mockMvc.perform(get("/swagger"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void swaggerTest() throws Exception {
		mockMvc.perform(get("/v2/api-docs")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void generateAsciiStaticDocs() throws Exception {
		mockMvc.perform(get("/v2/api-docs")
		.accept(MediaType.APPLICATION_JSON))
				.andDo(Swagger2MarkupResultHandler.outputDirectory("src/docs/asciidoc/generated").build())
				.andExpect(status().isOk());
	}
	
	@Test
	public void generateMarkdownStaticDocs() throws Exception {
		mockMvc.perform(get("/v2/api-docs")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(Swagger2MarkupResultHandler.outputDirectory("src/docs/markdown/generated")
						.withMarkupLanguage(MarkupLanguage.MARKDOWN)
						.build())
				.andExpect(status().isOk());
	}
	
}
