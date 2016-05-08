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

package org.oncoblocks.centromere.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * @author woemler
 */
public class CentromereJpaRepositoryFactoryBean<R extends JpaRepository<T, ID>, T, ID extends Serializable>
		extends JpaRepositoryFactoryBean<R, T, ID> {

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new CentromereJpaRepositoryFactory<>(entityManager);
	}

	private static class CentromereJpaRepositoryFactory<T, ID extends Serializable> extends
			JpaRepositoryFactory {

		private final EntityManager entityManager;
		
		public CentromereJpaRepositoryFactory(EntityManager entityManager) {
			super(entityManager);
			this.entityManager = entityManager;
		}

		@Override 
		protected Object getTargetRepository(RepositoryInformation information) {
			JpaEntityInformation entityInformation = getEntityInformation(information.getDomainType());
			return new CentromereJpaRepository<>(entityInformation, entityManager);
		}

		@Override 
		protected Class getRepositoryBaseClass(RepositoryMetadata metadata) {
			return CentromereJpaRepository.class;
		}
	}
	
}
