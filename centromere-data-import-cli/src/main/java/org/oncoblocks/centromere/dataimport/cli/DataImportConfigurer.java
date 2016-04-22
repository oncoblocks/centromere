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

import org.oncoblocks.centromere.core.dataimport.component.RecordProcessor;
import org.oncoblocks.centromere.core.model.support.DataSetMetadata;
import org.oncoblocks.centromere.core.repository.support.DataFileMetadataRepository;
import org.oncoblocks.centromere.core.repository.support.DataSetMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * @author woemler
 */
public abstract class DataImportConfigurer {
	
	@Autowired private ApplicationContext applicationContext;
	@Autowired private DataSetMetadataRepository dataSetMetadataRepository;
	@Autowired private DataFileMetadataRepository dataFileMetadataRepository;
	
	@Bean
	@Autowired
	public DataImportManager dataImportManager() {
		DataImportManager manager = new DataImportManager(applicationContext, dataSetMetadataRepository, 
				dataFileMetadataRepository);
		manager.setDataSetMap(this.configureDataSetMappings(manager.getDataSetMap()));
		manager.setDataTypeMap(this.configureDataTypeMappings(manager.getDataTypeMap()));
		return manager;
	}

	/**
	 * Allows overriding of the default data set mapping to allow custom behavior.
	 *
	 * @param dataSetMap
	 * @return
	 */
	public Map<String, DataSetMetadata> configureDataSetMappings(Map<String, DataSetMetadata> dataSetMap){
		return dataSetMap;
	}

	/**
	 * Allows overriding the {@code dataTypeMap} initialization or custom additions to the mappings.
	 *
	 * @param dataTypeMap
	 * @return
	 */
	public Map<String, RecordProcessor> configureDataTypeMappings(Map<String, RecordProcessor> dataTypeMap){
		return dataTypeMap;
	}
	
	@Bean 
	public CommandLineRunner commandLineRunner(){
		return new CommandLineRunner(addCommandRunner(), importCommandRunner());
	}
	
	@Bean
	public ImportCommandRunner importCommandRunner(){
		return new ImportCommandRunner(dataImportManager());
	}
	
	@Bean
	public AddCommandRunner addCommandRunner(){
		return new AddCommandRunner(dataImportManager());
	}
	
}
