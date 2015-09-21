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

package org.oncoblocks.centromere.core.dataimport.config;

import org.oncoblocks.centromere.core.dataimport.job.DataImportJob;
import org.oncoblocks.centromere.core.repository.support.DataFileRepositoryOperations;
import org.oncoblocks.centromere.core.repository.support.DataSetRepositoryOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author woemler
 */

@Configuration
public abstract class DataImportJobConfigurer {
	
	@Autowired ApplicationContext applicationContext;
	
	@Bean
	public DataFileQueue dataFileQueue(){
		DataFileQueue dataFileQueue = new DataFileQueue();
		dataFileQueue = configureDataFileQueue(dataFileQueue);
		return dataFileQueue;
	}
	
	@Bean
	public DataImportJob dataImportJob(){
		doBefore();
		DataImportOptions options = new DataImportOptions();
		options = configureDataImportOptions(options);
		return new DataImportJob(options, dataFileQueue(), 
				configureDataFileRepository(), configureDataSetRepository());
	}

	public DataImportOptions configureDataImportOptions(DataImportOptions options){
		return options;	
	}

	public abstract DataFileQueue configureDataFileQueue(DataFileQueue dataFileQueue);
	
	public DataSetRepositoryOperations configureDataSetRepository(){
		return applicationContext.getBean(DataSetRepositoryOperations.class);
	}
	
	public DataFileRepositoryOperations configureDataFileRepository(){
		return applicationContext.getBean(DataFileRepositoryOperations.class);
	}
	
	public void doBefore(){ }
	
}
