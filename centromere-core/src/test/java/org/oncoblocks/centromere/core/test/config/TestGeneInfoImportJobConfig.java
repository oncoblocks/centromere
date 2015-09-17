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

package org.oncoblocks.centromere.core.test.config;

import org.oncoblocks.centromere.core.dataimport.config.DataFileQueue;
import org.oncoblocks.centromere.core.dataimport.config.DataImportJobConfigurer;
import org.oncoblocks.centromere.core.dataimport.config.QueuedFile;
import org.oncoblocks.centromere.core.dataimport.job.DataFileProcessor;
import org.oncoblocks.centromere.core.dataimport.writer.RepositoryRecordWriter;
import org.oncoblocks.centromere.core.repository.support.DataFileRepositoryOperations;
import org.oncoblocks.centromere.core.repository.support.DataSetRepositoryOperations;
import org.oncoblocks.centromere.core.test.dataimport.EntrezGeneValidator;
import org.oncoblocks.centromere.core.test.dataimport.GeneInfoReader;
import org.oncoblocks.centromere.core.test.models.DataFile;
import org.oncoblocks.centromere.core.test.models.DataSet;
import org.oncoblocks.centromere.core.test.models.EntrezGene;
import org.oncoblocks.centromere.core.test.repository.mongo.DataFileRepository;
import org.oncoblocks.centromere.core.test.repository.mongo.DataSetRepository;
import org.oncoblocks.centromere.core.test.repository.mongo.EntrezGeneRepository;
import org.oncoblocks.centromere.core.test.repository.mongo.MongoRepositoryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.Date;

/**
 * @author woemler
 */

@Configuration
@PropertySource({"classpath:test-data-files.properties"})
@Import({MongoRepositoryConfig.class})
public class TestGeneInfoImportJobConfig extends DataImportJobConfigurer {
	
	@Autowired EntrezGeneRepository geneRepository;
	@Autowired DataSetRepository dataSetRepository;
	@Autowired DataFileRepository dataFileRepository;
	@Autowired Environment env;

	@Override 
	public DataFileQueue configureDataFileQueue(DataFileQueue dataFileQueue) {
		
		DataFileProcessor<EntrezGene> geneInfoProcessor = new DataFileProcessor<>(new GeneInfoReader(), 
				new RepositoryRecordWriter<>(geneRepository), new EntrezGeneValidator(), null);
		DataSet geneInfoDataSet = new DataSet(null, "NCBI", "Entrez gene metadata", null);
		DataFile geneInfoDataFile = new DataFile(null, null, ClassLoader.getSystemResource(env.getRequiredProperty("datafiles.geneinfo")).getPath(),
				"gene_info", new Date(), null);
		dataFileQueue.addQueuedFile(new QueuedFile(geneInfoDataFile, geneInfoDataSet, geneInfoProcessor));
		
		return dataFileQueue;
	}

	@Override 
	public DataSetRepositoryOperations configureDataSetRepository() {
		return dataSetRepository;
	}

	@Override 
	public DataFileRepositoryOperations configureDataFileRepository() {
		return dataFileRepository;
	}
	
	
	
}
