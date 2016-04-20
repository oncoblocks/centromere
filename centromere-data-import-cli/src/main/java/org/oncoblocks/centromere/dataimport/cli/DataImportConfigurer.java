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

package org.oncoblocks.centromere.dataimport.cli;

import org.oncoblocks.centromere.core.dataimport.pipeline.DataSetManager;
import org.oncoblocks.centromere.core.dataimport.pipeline.DataTypeManager;
import org.oncoblocks.centromere.core.repository.support.DataSetMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Configures required component classes for command line data import tool.
 * 
 * @author woemler
 */
public abstract class DataImportConfigurer {
	
	@Autowired private ApplicationContext applicationContext;
	
	@Bean
	public DataTypeManager dataTypeManager(){
		return new DataTypeManager(applicationContext);
	}
	
	@Bean
	@Autowired
	public DataSetManager dataSetManager(DataSetMetadataRepository<?, ?> dataSetRepository){
		return new DataSetManager(dataSetRepository);
	}
	
	@Bean
	@Autowired
	public AddCommandRunner addCommandRunner(DataTypeManager dataTypeManager, DataSetManager dataSetManager){
		return new AddCommandRunner(dataTypeManager, dataSetManager);
	}
	
	@Bean
	@Autowired
	public ImportCommandRunner importCommandRunner(DataTypeManager dataTypeManager, DataSetManager dataSetManager){
		return new ImportCommandRunner(dataTypeManager, dataSetManager);
	}
	
	@Bean
	@Autowired
	public CommandLineRunner commandLineRunner(AddCommandRunner addCommandRunner, ImportCommandRunner importCommandRunner){
		return new CommandLineRunner(addCommandRunner, importCommandRunner);
	}

}
