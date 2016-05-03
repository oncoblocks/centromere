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

package org.oncoblocks.centromere.mongodb.test;

import org.oncoblocks.centromere.core.repository.ModelRepository;
import org.oncoblocks.centromere.mongodb.GenericMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author woemler
 */

@ModelRepository(EntrezGene.class)
public class EntrezGeneRepository extends GenericMongoRepository<EntrezGene, Long> {
	
	@Autowired
	public EntrezGeneRepository(MongoTemplate mongoTemplate) {
		super(mongoTemplate);
	}

	public EntrezGene findByEntrezGeneId(Long entrezGeneId) {
		Query query = new Query(Criteria.where("entrezGeneId").is(entrezGeneId));
		return getMongoOperations().findOne(query, getModel());
	}

	public List<EntrezGene> findByPrimaryGeneSymbol(String primaryGeneSymbol) {
		Query query = new Query(Criteria.where("primaryGeneSymbol").is(primaryGeneSymbol));
		return getMongoOperations().find(query, getModel());
	}

	public List<EntrezGene> findByAlias(String alias) {
		Query query = new Query(Criteria.where("aliases").is(alias));
		return getMongoOperations().find(query, getModel());
	}

	public List<EntrezGene> guessGene(String keyword) {
		Query query = new Query(Criteria.where("primaryGeneSymbol").is(keyword));
		Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "entrezGeneId"));
		List<EntrezGene> genes = getMongoOperations().find(query.with(sort), getModel());
		if (genes != null && genes.size() > 0) return genes;
		query = new Query(Criteria.where("aliases").is(keyword));
		genes = getMongoOperations().find(query.with(sort), getModel());
		if (genes != null && genes.size() > 0) return genes;
		return null;
	}
}
