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

package org.oncoblocks.centromere.jpa.test;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author woemler
 */
public class EntrezGeneRepositoryImpl implements CustomGeneRepository {
	
	private final EntityManager entityManager;

	@Autowired
	public EntrezGeneRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<EntrezGene> guessGene(String keyword) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<EntrezGene> query = builder.createQuery(EntrezGene.class);
		Root<EntrezGene> root = query.from(EntrezGene.class);
		query.where(builder.equal(root.get("primaryGeneSymbol"), keyword));
		List<EntrezGene> genes = entityManager.createQuery(query).getResultList();
		if (genes != null && genes.size() > 0) return genes;
		
		query = builder.createQuery(EntrezGene.class);
		root = query.from(EntrezGene.class);
		Path join = root.join("aliases");
		query.where(builder.equal(join.get("name"), keyword));
		return  entityManager.createQuery(query).getResultList();
	}
	
}
