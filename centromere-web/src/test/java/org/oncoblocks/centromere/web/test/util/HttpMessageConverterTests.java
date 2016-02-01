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

package org.oncoblocks.centromere.web.test.util;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oncoblocks.centromere.web.controller.ResponseEnvelope;
import org.oncoblocks.centromere.web.test.models.EntrezGene;
import org.oncoblocks.centromere.web.util.FilteringJackson2HttpMessageConverter;
import org.oncoblocks.centromere.web.util.FilteringTextMessageConverter;
import org.oncoblocks.centromere.web.util.ApiMediaTypes;
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author woemler
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { UtilTestConfig.class })
public class HttpMessageConverterTests {
	
	private FilteringJackson2HttpMessageConverter jsonConverter;
	private FilteringTextMessageConverter textConverter;
	private final MediaType textMediaType = new MediaType("text", "plain", Charset.forName("utf-8"));
	
	@Before
	public void setup(){
		jsonConverter = new FilteringJackson2HttpMessageConverter();
		jsonConverter.setSupportedMediaTypes(ApiMediaTypes.getJsonMediaTypes());
		jsonConverter.setPrettyPrint(true);
		textConverter = new FilteringTextMessageConverter(textMediaType);
		textConverter.setDelimiter("\t");
	}

	@Test
	public void writeToJsonNoFilter() throws Exception {
		List<EntrezGene> genes = EntrezGene.createDummyData();
		ResponseEnvelope envelope = new ResponseEnvelope(genes);
		Assert.isTrue(jsonConverter.canWrite(envelope.getClass(), MediaType.APPLICATION_JSON));
		MockHttpOutputMessage message =  new MockHttpOutputMessage();
		jsonConverter.write(envelope, MediaType.APPLICATION_JSON, message);
		Object document = Configuration.defaultConfiguration()
				.jsonProvider().parse(message.getBodyAsString());
		Assert.isTrue(!((List<String>) JsonPath.read(document, "$")).isEmpty());
		Assert.isTrue(((List<String>) JsonPath.read(document, "$")).size() == 5);
		Assert.notNull(JsonPath.read(document, "$[0].entrezGeneId"));
		Assert.isTrue( (Integer) JsonPath.read(document, "$[0].entrezGeneId") == 1);
	}

	@Test
	public void writeToJsonWithFieldFilter() throws Exception {
		List<EntrezGene> genes = EntrezGene.createDummyData();
		Set<String> fields = new HashSet<>();
		fields.add("primaryGeneSymbol");
		ResponseEnvelope envelope = new ResponseEnvelope(genes, fields, new HashSet<>());
		Assert.isTrue(jsonConverter.canWrite(envelope.getClass(), MediaType.APPLICATION_JSON));
		MockHttpOutputMessage message =  new MockHttpOutputMessage();
		jsonConverter.write(envelope, MediaType.APPLICATION_JSON, message);
		Object document = Configuration.defaultConfiguration()
				.jsonProvider().parse(message.getBodyAsString());
		Assert.isTrue(!((List<String>) JsonPath.read(document, "$")).isEmpty());
		Assert.isTrue(((List<String>) JsonPath.read(document, "$")).size() == 5);
		Map<String,Object> gene = JsonPath.read(document, "$[0]");
		Assert.isTrue(!gene.containsKey("entrezGeneId"));
		Assert.isTrue(!gene.containsKey("aliases"));
		Assert.isTrue(!gene.containsKey("attributes"));
		Assert.isTrue(gene.containsKey("primaryGeneSymbol"));
		Assert.isTrue(gene.get("primaryGeneSymbol").equals("GeneA"));
	}

	@Test
	public void writeToJsonWithExcludeFilter() throws Exception {
		List<EntrezGene> genes = EntrezGene.createDummyData();
		Set<String> exclude = new HashSet<>();
		exclude.add("primaryGeneSymbol");
		ResponseEnvelope envelope = new ResponseEnvelope(genes, new HashSet<>(), exclude);
		Assert.isTrue(jsonConverter.canWrite(envelope.getClass(), MediaType.APPLICATION_JSON));
		MockHttpOutputMessage message =  new MockHttpOutputMessage();
		jsonConverter.write(envelope, MediaType.APPLICATION_JSON, message);
		Object document = Configuration.defaultConfiguration()
				.jsonProvider().parse(message.getBodyAsString());
		Assert.isTrue(!((List<String>) JsonPath.read(document, "$")).isEmpty());
		Assert.isTrue(((List<String>) JsonPath.read(document, "$")).size() == 5);
		Map<String,Object> gene = JsonPath.read(document, "$[0]");
		Assert.isTrue(gene.containsKey("entrezGeneId"));
		Assert.isTrue(gene.containsKey("aliases"));
		Assert.isTrue(gene.containsKey("attributes"));
		Assert.isTrue(!gene.containsKey("primaryGeneSymbol"));
		Assert.isTrue(((Integer) gene.get("entrezGeneId")) == 1);
	}
	
	@Test
	public void writeToTextNoFilter() throws Exception {
		List<EntrezGene> genes = EntrezGene.createDummyData();
		ResponseEnvelope envelope = new ResponseEnvelope(genes);
		Assert.isTrue(textConverter.canWrite(envelope.getClass(), textMediaType));
		MockHttpOutputMessage message = new MockHttpOutputMessage();
		textConverter.write(envelope, textMediaType, message);
		String text = message.getBodyAsString();
		Assert.notNull(text);
		System.out.println(text);
		// TODO
	}
	
}
