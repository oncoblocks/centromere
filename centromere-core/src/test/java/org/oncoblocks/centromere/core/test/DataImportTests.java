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

package org.oncoblocks.centromere.core.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oncoblocks.centromere.core.dataimport.component.RepositoryRecordWriter;
import org.oncoblocks.centromere.core.dataimport.pipeline.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * @author woemler
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class DataImportTests {
	
	@Autowired private Validator validator;
	private final String geneInfoPath = ClassLoader.getSystemClassLoader().getResource("Homo_sapiens.gene_info").getPath();
	private final String importJobJsonFilePath = ClassLoader.getSystemClassLoader().getResource("example_import.json").getPath();
	@Autowired private GeneInfoProcessor processor;
	@Autowired private TestRepository testRepository;
	@Autowired private ApplicationContext applicationContext;
	@Autowired private BasicImportOptions defaultImportOptions;
	private static final ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setup() throws Exception{
		testRepository.deleteAll();
		testRepository.insert(EntrezGene.createDummyData());
	}

	@Test
	public void geneInfoReaderTest() throws Exception{
		GeneInfoReader reader = new GeneInfoReader();
		List<EntrezGene> genes = new ArrayList<>();
		try {
			reader.open(geneInfoPath);
			EntrezGene gene = reader.readRecord();
			while (gene != null) {
				genes.add(gene);
				gene = reader.readRecord();
			}
		} finally {
			reader.close();
		}
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 5);
		Assert.isTrue(genes.get(4).getEntrezGeneId().equals(10L));
	}
	
	@Test
	public void validationTest() throws Exception {
		EntrezGene gene = new EntrezGene();
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(gene, gene.getClass().getName());
		validator.validate(gene, bindingResult);
		if (bindingResult.hasErrors()){
			for (ObjectError error: bindingResult.getAllErrors()){
				System.out.println(error.toString());
			}
		} else {
			fail("Validation did not catch missing field.");
		}
	}
	
	@Test
	public void recordWriterTest() throws Exception {
		testRepository.deleteAll();
		Assert.isTrue(testRepository.count() == 0);
		RepositoryRecordWriter<EntrezGene> writer = new RepositoryRecordWriter<>(testRepository);
		for (EntrezGene gene: EntrezGene.createDummyData()){
			writer.writeRecord(gene);
		}
		Assert.isTrue(testRepository.count() == 5);
	}
	
	@Test
	public void recordUpdaterTest() throws Exception {
		
	}
	
	@Test
	public void recordProcessorTest() throws Exception {
		testRepository.deleteAll();
		System.out.println(String.format("There are %d records in the test repository.", testRepository.count()));
		Assert.isTrue(testRepository.count() == 0);
		processor.setImportOptions(defaultImportOptions);
		processor.run(geneInfoPath);
		Assert.isTrue(testRepository.count() == 5);
		EntrezGene gene = testRepository.findOne(1L);
		Assert.notNull(gene);
		Assert.isTrue(gene.getId() == 1L);
		System.out.println(String.format("There are %d records in the test repository.", testRepository.count()));
		System.out.println(gene.toString());
	}
	
	@Test
	public void mapInputFileTest() throws Exception {
		String json = "{\"path\": \"/path/to/file.txt\", \"dataType\": \"ENTREZ_GENE\", \"dataSet\":\"metadata\"}";
		InputFile inputFile = mapper.readValue(json, InputFile.class);
		Assert.notNull(inputFile);
		Assert.isTrue(inputFile.getPath().endsWith("file.txt"));
		Assert.isTrue("ENTREZ_GENE".equals(inputFile.getDataType()));
		Assert.isTrue("metadata".equals(inputFile.getDataSet()));
	}
	
	@Test
	public void mapImportJobTest() throws Exception {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(importJobJsonFilePath));
			String line = br.readLine();
			while (line != null){
				sb.append(line);
				line = br.readLine();
			}
		} finally {
			if (br != null) br.close();
		}
		ImportJob job = mapper.readValue(sb.toString(), ImportJob.class);
		Assert.notNull(job);
		Assert.notNull(job.getDataTypes());
		Assert.notEmpty(job.getDataTypes());
		Assert.notNull(job.getFiles());
		Assert.notEmpty(job.getFiles());
	}

	@Test
	public void jsonJobFileParserTest() throws Exception {
		JsonJobFileParser parser = new JsonJobFileParser();
		parser.setObjectMapper(mapper);
		ImportJob job = parser.parseJobFile(importJobJsonFilePath);
		Assert.notNull(job);
		Assert.notNull(job.getDataTypes());
		Assert.notEmpty(job.getDataTypes());
		Assert.notNull(job.getFiles());
		Assert.notEmpty(job.getFiles());
	}
	
	@Test
	public void jobRunnerTest() throws Exception {
		testRepository.deleteAll();
		System.out.println(new File(".").getCanonicalPath());
		Assert.isTrue(testRepository.count() == 0);
		JsonJobFileParser parser = new JsonJobFileParser();
		parser.setObjectMapper(mapper);
		ImportJob job = parser.parseJobFile(importJobJsonFilePath);
		Assert.notNull(job);
		Assert.notNull(job.getOptions());
		ImportJobRunner jobRunner = new ImportJobRunner(applicationContext);
		jobRunner.setImportJob(job);
		Calendar calendar = Calendar.getInstance();
		jobRunner.runImport();
		Assert.isTrue(testRepository.count() == 5);
	}


	
	
}
