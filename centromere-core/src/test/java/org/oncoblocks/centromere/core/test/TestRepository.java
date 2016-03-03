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

import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.oncoblocks.centromere.core.repository.RepositoryOperations;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author woemler
 */

@Component
public class TestRepository implements RepositoryOperations<EntrezGene, Long> {

	private Map<Long, EntrezGene> geneMap;

	public TestRepository() {
		geneMap = new HashMap<>();
		this.setGeneMap(EntrezGene.createDummyData());
	}

	public Map<Long, EntrezGene> getGeneMap() {
		return geneMap;
	}
	
	public void setGeneMap(List<EntrezGene> genes){
		for (EntrezGene gene: genes){
			geneMap.put(gene.getId(), gene);
		}
	}

	public void setGeneMap(Map<Long, EntrezGene> geneMap) {
		this.geneMap = geneMap;
	}

	@Override public EntrezGene findOne(Long aLong) {
		if (geneMap.containsKey(aLong)) return geneMap.get(aLong);
		return null;
	}

	@Override public boolean exists(Long aLong) {
		return geneMap.containsKey(aLong);
	}

	@Override public Iterable<EntrezGene> findAll() {
		return geneMap.values();
	}

	@Override public Iterable<EntrezGene> findAll(Sort sort) {
		return geneMap.values();
	}

	@Override public Page<EntrezGene> findAll(Pageable pageable) {
		return new PageImpl<>(new ArrayList<>(geneMap.values()), pageable, geneMap.size());
	}

	@Override public long count() {
		return geneMap.size();
	}

	@Override public Iterable<EntrezGene> find(Iterable<QueryCriteria> queryCriterias) {
		return geneMap.values();
	}

	@Override public Iterable<EntrezGene> find(Iterable<QueryCriteria> queryCriterias, Sort sort) {
		return geneMap.values();
	}

	@Override
	public Page<EntrezGene> find(Iterable<QueryCriteria> queryCriterias, Pageable pageable) {
		return new PageImpl<>(new ArrayList<>(geneMap.values()), pageable, geneMap.size());
	}

	@Override public long count(Iterable<QueryCriteria> queryCriterias) {
		return geneMap.size();
	}

	@Override public Iterable<Object> distinct(String field) {
		Set<Object> values = new HashSet<>();
		for (EntrezGene gene: geneMap.values()){
			BeanWrapper wrapper = new BeanWrapperImpl(gene);
			if (wrapper.getPropertyValue(field) != null){
				values.add(wrapper.getPropertyValue(field));
			}
		}
		return values;
	}

	@Override public Iterable<Object> distinct(String field, Iterable<QueryCriteria> queryCriterias) {
		return this.distinct(field);
	}

	@Override public <S extends EntrezGene> S insert(S entity) {
		geneMap.put(entity.getId(), entity);
		return entity;
	}

	@Override public <S extends EntrezGene> Iterable<S> insert(Iterable<S> entities) {
		for (EntrezGene gene: entities){
			this.insert(gene);
		}
		return entities;
	}

	@Override public <S extends EntrezGene> S update(S entity) {
		geneMap.put(entity.getId(), entity);
		return entity;
	}

	@Override public <S extends EntrezGene> Iterable<S> update(Iterable<S> entities) {
		for (EntrezGene gene: entities){
			this.update(gene);
		}
		return entities;
	}

	@Override public void delete(Long aLong) {
		geneMap.remove(aLong);
	}

	@Override public void deleteAll() {
		geneMap = new HashMap<>();
	}
}
