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

package org.oncoblocks.centromere.core.test.dataimport;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.oncoblocks.centromere.core.dataimport.validator.EntityValidationException;
import org.oncoblocks.centromere.core.dataimport.writer.MongoImportTempFileWriter;
import org.oncoblocks.centromere.core.test.config.TestMongoConfig;
import org.oncoblocks.centromere.core.test.models.EntrezGene;
import org.oncoblocks.centromere.core.test.repository.mongo.EntrezGeneRepository;
import org.oncoblocks.centromere.core.test.repository.mongo.MongoRepositoryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author woemler
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DataImportConfig.class, TestMongoConfig.class,
		MongoRepositoryConfig.class})
@FixMethodOrder
public class DataImportTests {
	
	@Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();
	private String geneInfoPath = ClassLoader.getSystemClassLoader().getResource("Homo_sapiens.gene_info").getPath();
	
	@Autowired private MongoTemplate mongoTemplate;
	@Autowired private EntrezGeneRepository repository;
	
	@Test
	public void geneInfoReaderTest(){
		GeneInfoReader geneInfoReader = new GeneInfoReader(geneInfoPath);
		List<EntrezGene> genes = new ArrayList<>();
		try {
			geneInfoReader.open();
			EntrezGene gene = geneInfoReader.readRecord();
			while (gene != null){
				genes.add(gene);
				gene = geneInfoReader.readRecord();
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			geneInfoReader.close();
		}
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 5);
		Assert.isTrue(genes.get(4).getEntrezGeneId().equals(10L));
	}
	
	@Test
	public void tempGeneFileTest() throws Exception{
		GeneInfoReader geneInfoReader = new GeneInfoReader(geneInfoPath);
		File tempFile = temporaryFolder.newFile("genes.tmp");
		MongoImportTempFileWriter<EntrezGene> geneWriter = new MongoImportTempFileWriter<>(tempFile, mongoTemplate);
		try {
			geneWriter.open();
			geneInfoReader.open();
			EntrezGene gene = geneInfoReader.readRecord();
			while (gene != null){
				geneWriter.writeRecord(gene);
				gene = geneInfoReader.readRecord();
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			geneInfoReader.close();
			geneWriter.close();
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(tempFile));
			String line = reader.readLine();
			while (line != null){
				System.out.println(line);
				line = reader.readLine();
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if (reader != null){
				reader.close();
			}
		}
		
	}
	
	@Test
	public void entityValidationTest(){
		EntrezGeneValidator validator = new EntrezGeneValidator();
		EntrezGene gene = new EntrezGene();
		gene.setEntrezGeneId(0L);
		gene.setPrimaryGeneSymbol("TEST");
		gene.setTaxId(9606);
		try {
			validator.validate(gene);
		} catch (EntityValidationException e){
			Assert.isTrue(e.getMessage().equals("Entrez Gene ID must be greater than 0"));
		}
		gene.setEntrezGeneId(1L);
		gene.setPrimaryGeneSymbol("");
		try {
			validator.validate(gene);
		} catch (EntityValidationException e){
			Assert.isTrue(e.getMessage().equals("Primary gene symbol must not be an empty string"));
		}
		gene.setPrimaryGeneSymbol("TEST");
		gene.setTaxId(123);
		try {
			validator.validate(gene);
		} catch (EntityValidationException e){
			Assert.isTrue(e.getMessage().equals("Tax ID must be 9606 (Homo sapiens)"));
		}
		gene.setTaxId(9606);
		validator.validate(gene);
	}
	
//	@Test
//	public void importTest() throws Exception {
//		repository.deleteAll();
//		GeneInfoReader geneInfoReader = new GeneInfoReader(geneInfoPath);
//		File tempFile = temporaryFolder.newFile("gene_info.tmp");
//		MongoImportTempFileWriter<EntrezGene> geneWriter = new MongoImportTempFileWriter<>(tempFile, mongoTemplate);
//		try {
//			geneWriter.open();
//			geneInfoReader.open();
//			EntrezGene gene = geneInfoReader.readRecord();
//			while (gene != null){
//				geneWriter.writeRecord(gene);
//				gene = geneInfoReader.readRecord();
//			}
//		} catch (Exception e){
//			e.printStackTrace();
//		} finally {
//			geneInfoReader.close();
//			geneWriter.close();
//		}
//		MongoImportTempFileImporter importer = new MongoImportTempFileImporter(tempFile, "localhost", "centromere-test", "genes", "", "");
//		importer.importFile();
//		List<EntrezGene> genes = repository.findAll();
//		Assert.notNull(genes);
//		Assert.notEmpty(genes);
//		Assert.isTrue(genes.size() == 5);
//	}
	
}
