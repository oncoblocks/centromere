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

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.oncoblocks.centromere.core.dataimport.job.DataImportJob;
import org.oncoblocks.centromere.core.test.config.TestGeneInfoImportJobConfig;
import org.oncoblocks.centromere.core.test.config.TestMongoConfig;
import org.oncoblocks.centromere.core.test.repository.mongo.DataFileRepository;
import org.oncoblocks.centromere.core.test.repository.mongo.DataSetRepository;
import org.oncoblocks.centromere.core.test.repository.mongo.EntrezGeneRepository;
import org.oncoblocks.centromere.core.test.repository.mongo.MongoRepositoryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author woemler
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DataImportConfig.class, TestMongoConfig.class,
		MongoRepositoryConfig.class, TestGeneInfoImportJobConfig.class})
@FixMethodOrder
public class DataImportJobTests {

	@Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();
	
	@Autowired DataSetRepository dataSetRepository;
	@Autowired DataFileRepository dataFileRepository;
	@Autowired EntrezGeneRepository entrezGeneRepository;
	@Autowired DataImportJob dataImportJob;
	
	public static boolean isConfigured = false;
	
	@Before
	public void setup(){
		
		if (!isConfigured){
			dataSetRepository.deleteAll();
			dataFileRepository.deleteAll();
			entrezGeneRepository.deleteAll();
		}
		isConfigured = true;
		
	}
//	
//	@Test
//	public void dataImportTest(){
//		Assert.isTrue(dataFileRepository.count() == 0);
//		Assert.isTrue(dataSetRepository.count() == 0);
//		Assert.isTrue(entrezGeneRepository.count() == 0);
//		dataImportJob.run();
//		Assert.isTrue(dataFileRepository.count() == 1);
//		Assert.isTrue(dataSetRepository.count() == 1);
//		Assert.isTrue(entrezGeneRepository.count() == 5);
//	}
	
}
