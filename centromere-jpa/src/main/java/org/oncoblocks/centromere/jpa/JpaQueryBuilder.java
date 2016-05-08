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

import org.oncoblocks.centromere.core.model.Model;
import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.persistence.EntityManager;

/**
 * @author woemler
 */
public class JpaQueryBuilder<T extends Model<?>> {
	
	private final EntityManager entityManager;

	public JpaQueryBuilder(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Specification<T> createSpecification(Iterable<QueryCriteria> queryCriterias){
		Specifications<T> specifications = null;
		for (QueryCriteria queryCriteria: queryCriterias){
			if (queryCriteria != null){
				Specification<T> specification = new QueryCriteriaSpecification<>(queryCriteria);
				if (specifications == null){
					specifications = Specifications.where(specification);
				} else {
					specifications = specifications.and(specification);
				}
			}
		}
		return specifications;
	}
	
}
