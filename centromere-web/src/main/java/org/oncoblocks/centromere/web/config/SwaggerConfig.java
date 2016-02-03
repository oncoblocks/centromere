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

package org.oncoblocks.centromere.web.config;

import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Performs configuration for Swagger spec 2.0 API documentation via SpringFox.  API documentation 
 *   parameters are set in the {@code centromere.properties} file.
 * 
 * @author woemler
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Autowired private Environment env;
	
	@Bean
	public Docket api(){
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(apiPaths())
				.build()
				.apiInfo(apiInfo())
				.enableUrlTemplating(true);
	}

	private ApiInfo apiInfo(){
		return new ApiInfo(
				env.getRequiredProperty("centromere.api.name"),
				env.getRequiredProperty("centromere.api.description"),
				env.getRequiredProperty("centromere.api.version"),
				env.getRequiredProperty("centromere.api.tos"),
				env.getRequiredProperty("centromere.api.contact-email"),
				env.getRequiredProperty("centromere.api.license"),
				env.getRequiredProperty("centromere.api.license-url")
		);
	}

	private Predicate<String> apiPaths(){
		return regex(env.getRequiredProperty("centromere.api.regex-url"));
	}
	
}
