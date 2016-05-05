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

package org.oncoblocks.centromere.mongodb;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.io.Serializable;

/**
 * Overrides the default {@link MongoRepositoryFactoryBean} behavior, to allow the creation of 
 *   {@link CentromereMongoRepository} instances, instead of {@link org.springframework.data.mongodb.repository.support.SimpleMongoRepository}.
 * 
 * @author woemler
 * @since 0.4.1
 */
public class CentromereMongoRepositoryFactoryBean<R extends MongoRepository<T, ID>, T, ID extends Serializable> 
		extends MongoRepositoryFactoryBean<R, T, ID> {

	@Override 
	protected RepositoryFactorySupport getFactoryInstance(MongoOperations operations) {
		return new CentromereMongoRepositoryFactory<>(operations);
	}
	
	private static class CentromereMongoRepositoryFactory<T, ID extends Serializable> 
			extends MongoRepositoryFactory {

		private final MongoOperations mongoOperations;
		
		public CentromereMongoRepositoryFactory(MongoOperations mongoOperations) {
			super(mongoOperations);
			this.mongoOperations = mongoOperations;
		}

		@Override 
		protected Object getTargetRepository(RepositoryInformation information) {
			MongoEntityInformation entityInformation = getEntityInformation(information.getDomainType());
			return new CentromereMongoRepository<>(entityInformation, mongoOperations);
		}

		@Override 
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			return CentromereMongoRepository.class;
		}
	}
	
}
