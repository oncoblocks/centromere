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

package org.oncoblocks.centromere.mongodb.test;

import com.mongodb.Mongo;
import cz.jirutka.spring.embedmongo.EmbeddedMongoBuilder;
import org.oncoblocks.centromere.mongodb.CentromereMongoRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.IOException;

/**
 * @author woemler
 */
@Configuration
@PropertySource({ "classpath:test-mongo-data-source.properties" })
@EnableMongoRepositories(basePackages = {"org.oncoblocks.centromere.mongodb.test"},
		repositoryFactoryBeanClass = CentromereMongoRepositoryFactoryBean.class)
public class CentromereMongoConfig {

	@Autowired private Environment env;

	@Bean(destroyMethod = "close")
	public Mongo mongo() throws IOException {
		return new EmbeddedMongoBuilder().build();
	}

	@Bean
	public MongoTemplate mongoTemplate(Mongo mongo){
		return new MongoTemplate(mongo, env.getRequiredProperty("mongo.name"));
	}
	
}
