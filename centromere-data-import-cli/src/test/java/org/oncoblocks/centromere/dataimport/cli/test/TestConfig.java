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

package org.oncoblocks.centromere.dataimport.cli.test;

import org.oncoblocks.centromere.dataimport.cli.AddCommandRunner;
import org.oncoblocks.centromere.dataimport.cli.DataImportManager;
import org.oncoblocks.centromere.dataimport.cli.ImportCommandRunner;
import org.oncoblocks.centromere.dataimport.cli.test.support.DataFileRepository;
import org.oncoblocks.centromere.dataimport.cli.test.support.DataSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author woemler
 */
@Configuration
@ComponentScan(basePackages = { "org.oncoblocks.centromere.dataimport.cli.test" })
public class TestConfig {
	
	@Autowired private ApplicationContext applicationContext;
	@Autowired private DataSetRepository dataSetRepository;
	@Autowired private DataFileRepository dataFileRepository;
	
	@Bean
	public DataImportManager dataImportManager(){
		return new DataImportManager(applicationContext, dataSetRepository, dataFileRepository);
	}
	
	@Bean
	public AddCommandRunner addCommandRunner(){
		return new AddCommandRunner(dataImportManager());
	}
	
	@Bean
	public ImportCommandRunner importCommandRunner(){
		return new ImportCommandRunner(dataImportManager());
	}
	
}
