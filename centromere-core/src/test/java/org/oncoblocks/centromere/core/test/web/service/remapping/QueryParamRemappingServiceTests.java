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

package org.oncoblocks.centromere.core.test.web.service.remapping;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oncoblocks.centromere.core.model.Attribute;
import org.oncoblocks.centromere.core.model.SourcedAlias;
import org.oncoblocks.centromere.core.repository.Evaluation;
import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.oncoblocks.centromere.core.test.config.TestJdbcDataSourceConfig;
import org.oncoblocks.centromere.core.test.models.Subject;
import org.oncoblocks.centromere.core.test.repository.jdbc.JdbcRepositoryConfig;
import org.oncoblocks.centromere.core.test.repository.jdbc.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author woemler
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JdbcRepositoryConfig.class, TestJdbcDataSourceConfig.class,
		RemappingServiceConfig.class})
@FixMethodOrder
public class QueryParamRemappingServiceTests {

	@Autowired private SubjectService subjectService;
	@Autowired private SubjectRepository subjectRepository;
	private static boolean isConfigured = false;
	
	@Before
	public void setup(){

		if (isConfigured) return;

		subjectRepository.deleteAll();

		Subject subject = new Subject(1L, "PersonA", "Homo sapiens", "M", null, null, null);
		subject.setAlias(new SourcedAlias("clinic", "patient01"));
		subject.setAttribute(new Attribute("cancerType", "colon"));
		subjectRepository.insert(subject);

		subject = new Subject(2L, "PersonB", "Homo sapiens", "F", null, null, null);
		subject.setAlias(new SourcedAlias("clinic", "patient02"));
		subject.setAttribute(new Attribute("cancerType","breast"));
		subjectRepository.insert(subject);

		subject = new Subject(3L, "PersonC", "Homo sapiens", "M", null, null, null);
		subject.setAlias(new SourcedAlias("clinic","patient03"));
		subject.setAttribute(new Attribute("cancerType","lung"));
		subjectRepository.insert(subject);

		subject = new Subject(4L, "MCF7", "Homo sapiens", "F", null, null, null);
		subject.setAlias(new SourcedAlias("CCLE","MCF7_BREAST"));
		subject.setAttribute(new Attribute("cancerType","breast"));
		subject.setAttribute(new Attribute("isCellLine","Y"));
		subjectRepository.insert(subject);

		subject = new Subject(5L, "A375", "Homo sapiens", "U", null, null, null);
		subject.setAlias(new SourcedAlias("CCLE","A375_SKIN"));
		subject.setAttribute(new Attribute("cancerType","skin"));
		subject.setAttribute(new Attribute("isCellLine","Y"));
		subjectRepository.insert(subject);

		isConfigured = true;
		
	}

	@Test
	public void findOneTest(){
		Subject subject = subjectService.findById(1L);
		Assert.notNull(subject);
		Assert.isTrue(subject.getId().equals(1L));
		Assert.isTrue(subject.getName().equals("PersonA"));
	}

	@Test
	public void findAllTest(){
		List<Subject> subjects = subjectService.findAll();
		Assert.notNull(subjects);
		Assert.notEmpty(subjects);
		Assert.isTrue(subjects.size() == 5);
		Subject subject = subjects.get(0);
		Assert.notNull(subject);
		Assert.isTrue(subject.getId().equals(1L));
		Assert.isTrue(subject.getName().equals("PersonA"));
	}

	@Test
	public void findAllSorted(){
		List<Subject> subjects = subjectService.findAllSorted(
				new Sort(new Sort.Order(Sort.Direction.DESC, "subjectId")));
		Assert.notNull(subjects);
		Assert.notEmpty(subjects);
		Subject subject = subjects.get(0);
		Assert.notNull(subject);
		Assert.isTrue(subject.getId().equals(5L));
		Assert.isTrue(subject.getName().equals("A375"));
	}

	@Test
	public void findAllPaged(){
		PageRequest pageRequest = new PageRequest(1, 3);
		Page<Subject> page = subjectService.findAllPaged(pageRequest);
		Assert.notNull(page);
		Assert.isTrue(page.getTotalPages() == 2);
		Assert.isTrue(page.getTotalElements() == 5);
		List<Subject> subjects = page.getContent();
		Assert.isTrue(subjects.size() == 2);
		Subject subject = subjects.get(0);
		Assert.isTrue(subject.getId().equals(4L));
	}

	@Test
	public void countTest(){
		Long count = subjectService.count();
		Assert.notNull(count);
		Assert.isTrue(count.equals(5L));
	}

	@Test
	public void queryCriteriaTest(){
		List<QueryCriteria> queryCriterias = new ArrayList<>();
		queryCriterias.add(new QueryCriteria("name", "PersonB", Evaluation.EQUALS));
		List<Subject> subjects = subjectService.find(queryCriterias);
		Assert.notNull(subjects);
		Assert.notEmpty(subjects);
		Assert.isTrue(subjects.size() == 1);
		Subject subject = subjects.get(0);
		Assert.notNull(subject);
		Assert.isTrue(subject.getId().equals(2L));
		Assert.isTrue(subject.getName().equals("PersonB"));
	}

	@Test
	public void sortedCriteriaTest(){
		List<QueryCriteria> queryCriterias = new ArrayList<>();
		queryCriterias.add(new QueryCriteria("gender", "F", Evaluation.EQUALS));
		Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "subjectId"));

		List<Subject> subjects = subjectService.findSorted(queryCriterias, sort);
		Assert.notNull(subjects);
		Assert.notEmpty(subjects);
		Assert.isTrue(subjects.size() == 2);

		Subject subject = subjects.get(0);
		Assert.notNull(subject);
		Assert.isTrue(subject.getId().equals(4L));
	}

	@Test
	public void pagedCriteriaTest(){
		List<QueryCriteria> queryCriterias = new ArrayList<>();
		queryCriterias.add(new QueryCriteria("species", "Homo sapiens", Evaluation.EQUALS));
		Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "subjectId"));
		PageRequest pageRequest = new PageRequest(1, 3, sort);

		Page<Subject> page = subjectService.findPaged(queryCriterias, pageRequest);
		Assert.notNull(page);
		Assert.isTrue(page.getTotalPages() == 2);
		Assert.isTrue(page.getTotalElements() == 5);

		List<Subject> subjects = page.getContent();
		Assert.isTrue(subjects.size() == 2);

		Subject subject = subjects.get(0);
		Assert.notNull(subject);
		Assert.isTrue(subject.getId().equals(2L));
	}

	@Test
	public void findAllPagedCriteriaTest(){
		List<QueryCriteria> queryCriterias = new ArrayList<>();
		PageRequest pageRequest = new PageRequest(1, 3);

		Page<Subject> page = subjectService.findPaged(queryCriterias, pageRequest);
		Assert.notNull(page);
		Assert.isTrue(page.getTotalElements() == 5);
		Assert.isTrue(page.getTotalPages() == 2);

		List<Subject> subjects = page.getContent();
		Assert.notNull(subjects);
		Assert.notEmpty(subjects);
		Assert.isTrue(subjects.size() == 2);

		Subject subject = subjects.get(0);
		Assert.isTrue(subject.getId().equals(4L));

	}

	@Test
	public void insertTest(){

		Subject subject = subjectService.insert(
				new Subject(6L, "PersonD", "Homo sapiens", "F", null, null, null));
		Assert.notNull(subject);
		Assert.isTrue(subject.getId().equals(6L));

		subject = subjectService.findById(6L);
		Assert.notNull(subject);
		Assert.isTrue(subject.getId().equals(6L));

		subjectService.delete(6L);

	}

	@Test
	public void updateTest(){

		Subject subject = subjectService.insert(
				new Subject(6L, "PersonD", "Homo sapiens", "F", null, null, null));

		subject.setName("TEST_NAME");

		Subject updated = subjectService.update(subject);
		Assert.notNull(updated);
		Assert.isTrue(updated.getName().equals("TEST_NAME"));

		subject = subjectService.findById(6L);
		Assert.notNull(subject);
		Assert.isTrue(subject.getName().equals("TEST_NAME"));

		subjectService.delete(6L);

	}

	@Test
	public void deleteTest(){

		Subject subject = subjectService.insert(
				new Subject(6L, "PersonD", "Homo sapiens", "F", null, null, null));
		Assert.isTrue(subject.getId().equals(6L));
		Assert.notNull(subject);
		subjectService.delete(6L);
		subject = subjectService.findById(6L);
		Assert.isNull(subject);

	}
	
	
}
