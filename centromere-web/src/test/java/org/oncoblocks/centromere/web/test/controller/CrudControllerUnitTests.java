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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.oncoblocks.centromere.web.exceptions.RestExceptionHandler;
import org.oncoblocks.centromere.web.test.config.TestMongoConfig;
import org.oncoblocks.centromere.web.test.config.TestWebConfig;
import org.oncoblocks.centromere.web.test.models.EntrezGene;
import org.oncoblocks.centromere.web.test.repository.EntrezGeneRepository;
import org.oncoblocks.centromere.web.test.repository.MongoRepositoryConfig;
import org.oncoblocks.centromere.web.util.FilteringJackson2HttpMessageConverter;
import org.oncoblocks.centromere.web.util.ApiMediaTypes;
import org.oncoblocks.centromere.web.util.StringToAttributeConverter;
import org.oncoblocks.centromere.web.util.StringToSourcedAliasConverter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssemblerArgumentResolver;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author woemler
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestMongoConfig.class, TestWebConfig.class, MongoRepositoryConfig.class})
@FixMethodOrder
public class CrudControllerUnitTests {

	@Mock private EntrezGeneRepository repository;
	private MockMvc mockMvc;
	private List<EntrezGene> genes;
	private static final String BASE_URL = "/genes/crud";
	
	private EntrezGene createdGene = new EntrezGene(99L, "GeneF", 9606, "", "10", "", "", "protein-coding", null, null, null);
	private EntrezGene updatedGene = new EntrezGene(99L, "TEST_GENE", 9606, "", "10", "", "", "protein-coding", null, null, null);
	
	@Before
	public void setup(){
		genes = EntrezGene.createDummyData();
		FormattingConversionService conversionService = new FormattingConversionService();
		conversionService.addConverter(new StringToAttributeConverter());
		conversionService.addConverter(new StringToSourcedAliasConverter());
		
		MockitoAnnotations.initMocks(this);
		
		mockMvc = MockMvcBuilders.standaloneSetup(
				new EntrezGeneCrudController(repository))
				.setMessageConverters(createMessageConverters())
				.setCustomArgumentResolvers(
						new PageableHandlerMethodArgumentResolver(), 
						new HateoasPageableHandlerMethodArgumentResolver(),
						new PagedResourcesAssemblerArgumentResolver(new HateoasPageableHandlerMethodArgumentResolver(), 
								new ControllerLinkBuilderFactory()))
				.setViewResolvers(new ViewResolver() {
					@Override
					public View resolveViewName(String viewName, Locale locale) throws Exception {
						return new MappingJackson2JsonView();
					}
				}).setConversionService(conversionService)
				.setHandlerExceptionResolvers(createExceptionResolver())
				.build();
		
		Mockito.when(repository.findOne(1L)).thenReturn(genes.get(0));
		Mockito.when(repository.findOne(99L)).thenReturn(null);
		Mockito.when(repository.findAll()).thenReturn(genes);
		Mockito.when(repository.find(Matchers.anyCollectionOf(QueryCriteria.class))).thenReturn(genes);
		Mockito.when(repository.find(Matchers.anyCollectionOf(QueryCriteria.class), Matchers.any(Sort.class)))
				.thenReturn(genes);
		Mockito.when(repository.find(Matchers.anyCollectionOf(QueryCriteria.class), Matchers.any(
				Pageable.class))).thenReturn(new PageImpl<>(genes, new PageRequest(0,5), 5));
		Mockito.when(repository.distinct(Matchers.anyString()))
				.thenReturn(Arrays.asList("GeneA", "GeneB", "GeneC", "GeneD", "GeneE"));
		Mockito.when(repository.distinct(Matchers.anyString(), Matchers.anyCollectionOf(QueryCriteria.class)))
				.thenReturn(Arrays.asList("GeneA", "GeneB", "GeneC", "GeneD", "GeneE"));
		Mockito.when(repository.insert(Matchers.any(EntrezGene.class))).thenReturn(createdGene);
		Mockito.when(repository.update(Matchers.any(EntrezGene.class))).thenReturn(updatedGene);
		Mockito.when(repository.exists(Matchers.anyLong())).thenReturn(true);
		
	}
	
	private HttpMessageConverter<?> createMessageConverters(){
		FilteringJackson2HttpMessageConverter jsonConverter = new FilteringJackson2HttpMessageConverter();
		jsonConverter.setSupportedMediaTypes(ApiMediaTypes.getJsonMediaTypes());
		return jsonConverter;
	}

	private ExceptionHandlerExceptionResolver createExceptionResolver() {
		ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
			protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
				Method method = new ExceptionHandlerMethodResolver(RestExceptionHandler.class).resolveMethod(exception);
				return new ServletInvocableHandlerMethod(new RestExceptionHandler(), method);
			}
		};
		exceptionResolver.afterPropertiesSet();
		return exceptionResolver;
	}

	@Test
	public void headTest() throws Exception {
		mockMvc.perform(head(BASE_URL))
				.andExpect(status().isOk());
	}

	@Test
	public void findById() throws Exception {

		mockMvc.perform(get(BASE_URL + "/{id}", 1L)
				.accept(ApiMediaTypes.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.entrezGeneId", is(1)))
				.andExpect(jsonPath("$.primaryGeneSymbol", is("GeneA")))
				.andExpect(jsonPath("$.links", hasSize(1)))
				.andExpect(jsonPath("$.links[0].rel", is("self")))
				.andExpect(jsonPath("$.links[0].href", endsWith(BASE_URL + "/1")));
	}

	@Test
	public void findByIdNoHal() throws Exception {

		mockMvc.perform(get(BASE_URL + "/{id}", 1L).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.entrezGeneId", is(1)))
				.andExpect(jsonPath("$.primaryGeneSymbol", is("GeneA")))
				.andExpect(jsonPath("$", not(hasKey("links"))));
	}

	@Test
	public void findByIdWithoutLinks() throws Exception {

		mockMvc.perform(get(BASE_URL + "/{id}", 1L).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.entrezGeneId", is(1)))
				.andExpect(jsonPath("$.primaryGeneSymbol", is("GeneA")))
				.andExpect(jsonPath("$", not(hasKey("links"))));
	}

	@Test
	public void findAll() throws Exception {
		mockMvc.perform(get(BASE_URL).accept(ApiMediaTypes.APPLICATION_HAL_JSON_VALUE))
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
		mockMvc.perform(get(BASE_URL).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)))
				.andExpect(jsonPath("$[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$[0].entrezGeneId", is(1)))
				.andExpect(jsonPath("$[0]", not(hasKey("links"))))
				.andExpect(jsonPath("$", not(hasKey("pageMetadata"))));
	}

	@Test
	public void findByAlias() throws Exception {
		mockMvc.perform(get(BASE_URL + "?alias=MNO").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)))
				.andExpect(jsonPath("$[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$[0].entrezGeneId", is(1)))
				.andExpect(jsonPath("$[0]", not(hasKey("links"))));
	}

	@Test
	public void findByKeyValueAttributes() throws Exception {
		mockMvc.perform(get(BASE_URL + "?attribute=isKinase:Y").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)))
				.andExpect(jsonPath("$[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$[0].entrezGeneId", is(1)))
				.andExpect(jsonPath("$[0]", not(hasKey("links"))));
	}

	@Test
	public void findPaged() throws Exception {
		mockMvc.perform(get(BASE_URL + "?page=0&size=5").accept(ApiMediaTypes.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(5)))
				.andExpect(jsonPath("$.content[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$.content[0].entrezGeneId", is(1)))
				.andExpect(jsonPath("$", hasKey("links")))
				.andExpect(jsonPath("$.links", hasSize(1)))
				.andExpect(jsonPath("$.links[0].rel", is("self")))
				.andExpect(jsonPath("$", hasKey("page")))
				.andExpect(jsonPath("$.page.totalElements", is(5)))
				.andExpect(jsonPath("$.page.number", is(0)))
				.andExpect(jsonPath("$.page.size", is(5)))
				.andExpect(jsonPath("$.page.totalPages", is(1)));
	}

	@Test
	public void findPagedWithoutLinks() throws Exception {
		mockMvc.perform(get(BASE_URL + "?page=0&size=5").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(5)))
				.andExpect(jsonPath("$.content[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$.content[0].entrezGeneId", is(1)))
				.andExpect(jsonPath("$.content[0]", not(hasKey("links"))))
				.andExpect(jsonPath("$", not(hasKey("links"))));
	}

	@Test
	public void findSorted() throws Exception {
		mockMvc.perform(get(BASE_URL + "?sort=geneSymbol,asc").accept(
				ApiMediaTypes.APPLICATION_HAL_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("content")))
				.andExpect(jsonPath("$.content", hasSize(5)))
				.andExpect(jsonPath("$.content[0]", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$.content[0].entrezGeneId", is(1)));
	}
	
	@Test
	public void findDistinct() throws Exception {
		mockMvc.perform(get(BASE_URL + "/distinct?field=primaryGeneSymbol").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)))
				.andExpect(jsonPath("$[0]", is("GeneA")));
	}

	@Test
	public void findDistinctFiltered() throws Exception {
		mockMvc.perform(get(BASE_URL + "/distinct?field=primaryGeneSymbol&geneType=protein-coding")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)))
				.andExpect(jsonPath("$[2]", is("GeneC")));
	}

	@Test
	public void createTest() throws Exception {

		EntrezGene
				gene = new EntrezGene(99L, "GeneF", 9606, "", "10", "", "", "protein-coding", null, null, null);
		ObjectMapper mapper = new ObjectMapper();
		mapper.setFilters(new SimpleFilterProvider().addFilter("fieldFilter",
				SimpleBeanPropertyFilter.serializeAllExcept()).setFailOnUnknownId(false));
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		mockMvc.perform(post(BASE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(gene)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$.entrezGeneId", is(99)));
		
		Mockito.when(repository.findOne(99L)).thenReturn(createdGene);
		
		mockMvc.perform(get(BASE_URL + "/{id}", 99L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.entrezGeneId", is(99)));

	}

	@Test
	public void updateTest() throws Exception {

		EntrezGene gene = createdGene;
		ObjectMapper mapper = new ObjectMapper();
				mapper.setFilters(new SimpleFilterProvider().addFilter("fieldFilter",
						SimpleBeanPropertyFilter.serializeAllExcept()).setFailOnUnknownId(false));
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		gene.setPrimaryGeneSymbol("TEST_GENE");
		Mockito.when(repository.exists(99L)).thenReturn(true);

		mockMvc.perform(put(BASE_URL + "/{id}", 99L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(gene)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$", hasKey("entrezGeneId")))
				.andExpect(jsonPath("$.entrezGeneId", is(99)))
				.andExpect(jsonPath("$.primaryGeneSymbol", is("TEST_GENE")));
		
		Mockito.when(repository.findOne(99L)).thenReturn(updatedGene);

		mockMvc.perform(get(BASE_URL + "/{id}", 99L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.entrezGeneId", is(99)))
				.andExpect(jsonPath("$.primaryGeneSymbol", is("TEST_GENE")));

	}

	@Test
	public void deleteTest() throws Exception {
		mockMvc.perform(delete(BASE_URL + "/{id}", 1L))
				.andExpect(status().isOk());
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
