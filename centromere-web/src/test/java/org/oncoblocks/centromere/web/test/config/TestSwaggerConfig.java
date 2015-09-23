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

package org.oncoblocks.centromere.web.test.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * @author woemler
 */

@Configuration
@EnableWebMvc
@EnableSwagger2
@ComponentScan(basePackages = { "org.oncoblocks.centromere.web.test.swagger" })
public class TestSwaggerConfig {

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
				"Centromere Test",
				"Centromere test API", 
				"0.1", 
				"",
				"woemler@blueprintmedicines.com",
				"Apache License 2.0", 
				"https://github.com/oncoblocks/centromere/blob/master/LICENSE"
		);
	}

	private Predicate<String> apiPaths(){
		return regex("/swagger.*");
	}
	
}
