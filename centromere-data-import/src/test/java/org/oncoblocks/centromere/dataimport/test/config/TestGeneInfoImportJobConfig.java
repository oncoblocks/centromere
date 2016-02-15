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

package org.oncoblocks.centromere.dataimport.test.config;

import org.oncoblocks.centromere.dataimport.config.DataFileQueue;
import org.oncoblocks.centromere.dataimport.config.DataImportJob;
import org.oncoblocks.centromere.dataimport.config.DataImportOptions;
import org.oncoblocks.centromere.dataimport.config.QueuedFile;
import org.oncoblocks.centromere.dataimport.test.GeneInfoProcessor;
import org.oncoblocks.centromere.dataimport.test.models.DataFile;
import org.oncoblocks.centromere.dataimport.test.models.DataSet;
import org.oncoblocks.centromere.dataimport.test.repositories.DataFileRepository;
import org.oncoblocks.centromere.dataimport.test.repositories.DataSetRepository;
import org.oncoblocks.centromere.dataimport.test.repositories.EntrezGeneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import java.util.Date;

/**
 * @author woemler
 */

@Configuration
@PropertySource({"classpath:test-data-files.properties"})
@ComponentScan(basePackages = { "org.oncoblocks.centromere.dataimport.test" })
@Import({TestMongoConfig.class})
public class TestGeneInfoImportJobConfig {
	
	@Autowired private EntrezGeneRepository geneRepository;
	@Autowired private DataSetRepository dataSetRepository;
	@Autowired private DataFileRepository dataFileRepository;
	@Autowired private GeneInfoProcessor geneInfoProcessor;
	@Autowired private Environment env;

	private DataFileQueue dataFileQueue() {
		
		DataFileQueue dataFileQueue = new DataFileQueue();
		DataSet geneInfoDataSet = new DataSet(null, "NCBI", "Entrez gene metadata", null);
		DataFile geneInfoDataFile = new DataFile(null, null, ClassLoader.getSystemResource(env.getRequiredProperty("datafiles.geneinfo")).getPath(),
				"gene_info", new Date(), null);
		dataFileQueue.addQueuedFile(new QueuedFile(geneInfoDataFile, geneInfoDataSet, geneInfoProcessor));
		
		return dataFileQueue;
	}

	private DataImportOptions dataImportOptions() {
		DataImportOptions options = new DataImportOptions();
		options.setFailOnExistingFile(true)
				.setFailOnMissingFile(true);
		return options;
	}
	
	@Bean
	public DataImportJob geneInfoImportJob(){
		return new DataImportJob(dataImportOptions(), dataFileQueue(), dataFileRepository, dataSetRepository);
	}
	
	
}
