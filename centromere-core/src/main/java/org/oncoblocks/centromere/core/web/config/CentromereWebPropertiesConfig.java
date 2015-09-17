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

package org.oncoblocks.centromere.core.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Enables autoconfiguration of required configuration parameters with a properties file.  Uses 
 *   default values if the file does not exist or cannot be found.
 * 
 * @author woemler
 */

@Configuration
@PropertySource(value = "classpath:centromere.properties", ignoreResourceNotFound = true)
public class CentromereWebPropertiesConfig {
	
	@Autowired Environment env;
	
	@Bean
	public CentromereWebProperties properties(){
		
		CentromereWebProperties properties = new CentromereWebProperties();
		String rootUrl = env.getProperty("api.rooturl") != null ?
				env.getRequiredProperty("api.rooturl") : "/api";
		properties.setApiRootUrl(rootUrl);
		return properties;
		
	}
	
}
