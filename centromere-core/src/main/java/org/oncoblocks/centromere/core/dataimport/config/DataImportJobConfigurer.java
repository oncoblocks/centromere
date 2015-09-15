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
import org.springframework.context.annotation.Bean;

/**
 * @author woemler
 */

public abstract class DataImportJobConfigurer {
	
	@Bean
	public DataFileQueue dataFileQueue(){
		return new DataFileQueue(jobConfiguration().getFileListPath());
	}
	
	@Bean
	public DataFileProcessorMapper dataFileProcessorMapper(){
		DataFileProcessorMapper mapper = new DataFileProcessorMapper();
		mapper = this.addDataFileProcessorMappings(mapper);
		return mapper;
	}
	
	@Bean
	public DataImportJob dataImportJob(){
		return new DataImportJob(jobConfiguration(), dataFileQueue(), dataFileProcessorMapper());
	}
	
	public abstract JobConfiguration jobConfiguration();
	
	public abstract DataFileProcessorMapper addDataFileProcessorMappings(DataFileProcessorMapper mapper);


}
