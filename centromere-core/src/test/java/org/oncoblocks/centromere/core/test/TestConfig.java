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

package org.oncoblocks.centromere.core.test;

import org.oncoblocks.centromere.core.input.pipeline.ImportOptions;
import org.oncoblocks.centromere.core.input.pipeline.BasicImportOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author woemler
 */

@Configuration
@ComponentScan(basePackages = { "org.oncoblocks.centromere.core.test" })
@PropertySource({"classpath:test.properties"})
public class TestConfig {
	
	@Autowired Environment environment;

	@Bean
	public LocalValidatorFactoryBean validator(){
		return new LocalValidatorFactoryBean();
	}
	
	@Bean
	public ImportOptions importOptions(){
		BasicImportOptions importOptions = new BasicImportOptions();
		importOptions.setTempDirectoryPath(environment.getRequiredProperty("tmp.dir"));
		importOptions.setFailOnInvalidRecord(false);
		return importOptions;
	}
	
}