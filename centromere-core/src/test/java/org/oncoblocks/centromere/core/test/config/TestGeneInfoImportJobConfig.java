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

import org.oncoblocks.centromere.core.dataimport.config.*;
import org.oncoblocks.centromere.core.dataimport.job.DataFileProcessor;
import org.oncoblocks.centromere.core.dataimport.writer.RepositoryRecordWriter;
import org.oncoblocks.centromere.core.test.dataimport.EntrezGeneValidator;
import org.oncoblocks.centromere.core.test.dataimport.GeneInfoReader;
import org.oncoblocks.centromere.core.test.repository.mongo.DataFileRepository;
import org.oncoblocks.centromere.core.test.repository.mongo.DataSetRepository;
import org.oncoblocks.centromere.core.test.repository.mongo.EntrezGeneRepository;
import org.oncoblocks.centromere.core.test.repository.mongo.MongoRepositoryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author woemler
 */

@Configuration
@Import({MongoRepositoryConfig.class})
public class TestGeneInfoImportJobConfig extends DataImportJobConfigurer {
	
	@Autowired EntrezGeneRepository geneRepository;
	@Autowired DataSetRepository dataSetRepository;
	@Autowired DataFileRepository dataFileRepository;

	@Override 
	public JobConfiguration jobConfiguration() {
		return new JobConfiguration()
				//.setFileListPath(ClassLoader.getSystemResource("test-file-list.txt").getPath())
				.setDataSet("Gene info test", "NCBI", "This is a test")
				.setDataFileRepository(dataFileRepository)
				.setDataSetRepository(dataSetRepository);
	}

	@Override 
	public DataFileQueue addFilesToQueue(DataFileQueue dataFileQueue) {
		dataFileQueue.addQueuedFile(
				new QueuedFile(ClassLoader.getSystemResource("Homo_sapiens.gene_info").getPath(), 
						"gene_info", "Entrez gene metadata"));
		return dataFileQueue;
	}

	@Override
	public DataFileProcessorMapper addDataFileProcessorMappings(DataFileProcessorMapper mapper) {
		mapper.addMapping(
				"gene_info",
				new DataFileProcessor<>(
					new GeneInfoReader(), 
					new RepositoryRecordWriter<>(geneRepository),
					new EntrezGeneValidator(),
					null)
		);
		return mapper;
	}
}
