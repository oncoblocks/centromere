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

package org.oncoblocks.centromere.sql.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.oncoblocks.centromere.sql.sqlbuilder.ComplexTableDescription;
import org.oncoblocks.centromere.sql.sqlbuilder.SqlBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.oncoblocks.centromere.sql.sqlbuilder.SqlBuilder.*;

/**
 * @author woemler
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SqlBuilderTests.EmptyContext.class})
public class SqlBuilderTests {
	
	private ComplexTableDescription tableDescription = Subject.getSubjectTableDescription();

	@Test
	public void simpleQueryTest(){

		SqlBuilder sqlBuilder = new SqlBuilder(tableDescription);
		sqlBuilder.where(equal("name", "Joe"));

		String sql = sqlBuilder.toSql();
		System.out.println(sql);
		Assert.notNull(sql);

		List<Object> values = sqlBuilder.getQueryParameterValues();
		Assert.notNull(values);
		Assert.notEmpty(values);
		Assert.isTrue(values.size() == 1);
		String name = (String) values.get(0);
		Assert.isTrue(name.equals("Joe"));

	}

	@Test
	public void simpleAndQueryTest(){

		SqlBuilder sqlBuilder = new SqlBuilder(tableDescription);
		sqlBuilder.where(and(equal("name", "Joe"), equal("species", "human")));

		String sql = sqlBuilder.toSql();
		System.out.println(sql);
		Assert.notNull(sql);

		List<Object> values = sqlBuilder.getQueryParameterValues();
		Assert.notNull(values);
		Assert.notEmpty(values);
		Assert.isTrue(values.size() == 2);
		String name = (String) values.get(0);
		Assert.isTrue(name.equals("Joe"));

	}

	@Test
	public void simpleOrQueryTest(){

		SqlBuilder sqlBuilder = new SqlBuilder(tableDescription);
		sqlBuilder.where(or(equal("name", "Joe"), equal("species", "human")));

		String sql = sqlBuilder.toSql();
		System.out.println(sql);
		Assert.notNull(sql);

		List<Object> values = sqlBuilder.getQueryParameterValues();
		Assert.notNull(values);
		Assert.notEmpty(values);
		Assert.isTrue(values.size() == 2);
		String name = (String) values.get(0);
		Assert.isTrue(name.equals("Joe"));

	}

	@Test
	public void compoundAndOrQueryTest(){

		SqlBuilder sqlBuilder = new SqlBuilder(tableDescription);
		sqlBuilder.where(or(and(equal("name", "Joe"), equal("species", "human")), and(equal("name", "Mittens"), equal("species", "cat"))));

		String sql = sqlBuilder.toSql();
		System.out.println(sql);
		Assert.notNull(sql);

		List<Object> values = sqlBuilder.getQueryParameterValues();
		Assert.notNull(values);
		Assert.notEmpty(values);
		Assert.isTrue(values.size() == 4);
		String name = (String) values.get(2);
		Assert.isTrue("Mittens".equals(name));

	}

	@Test
	public void simpleEvaluationTest(){

		SqlBuilder sqlBuilder = new SqlBuilder(tableDescription);
		sqlBuilder.where(or(equal("name", "Joe"), notEqual("species", "cat"), isNull("notes"), notNull("gender"), in("name", new String[]{ "Henry", "Joe", "Frank" }), notIn("name", new String[]{ "Mittens", "Bolt", "Rhino" })));

		String sql = sqlBuilder.toSql();
		System.out.println(sql);
		Assert.notNull(sql);

		List<Object> values = sqlBuilder.getQueryParameterValues();
		Assert.notNull(values);
		Assert.notEmpty(values);
		Assert.isTrue(values.size() == 4);
		String name = (String) values.get(1);
		Assert.isTrue("cat".equals(name));

	}

	@Test
	public void groupByTest(){

		SqlBuilder sqlBuilder = new SqlBuilder(tableDescription);
		sqlBuilder.groupBy("name", "gender", "species");

		String sql = sqlBuilder.toSql();
		System.out.println(sql);
		Assert.notNull(sql);

	}

	@Test
	public void orderByTest(){

		Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"), new Sort.Order(Sort.Direction.DESC, "gender"));
		SqlBuilder sqlBuilder = new SqlBuilder(tableDescription);
		sqlBuilder.orderBy(sort);

		String sql = sqlBuilder.toSql();
		System.out.println(sql);
		Assert.notNull(sql);

		sqlBuilder.orderBy(new Sort.Order(Sort.Direction.ASC, "name"));

		sql = sqlBuilder.toSql();
		System.out.println(sql);
		Assert.notNull(sql);

		sqlBuilder.orderBy("name");

		sql = sqlBuilder.toSql();
		System.out.println(sql);
		Assert.notNull(sql);

	}

	@Test
	public void limitTest(){

		SqlBuilder sqlBuilder = new SqlBuilder(tableDescription);
		sqlBuilder.limit(10);

		String sql = sqlBuilder.toSql();
		System.out.println(sql);
		Assert.notNull(sql);

		sqlBuilder.limit(50, 10);

		sql = sqlBuilder.toSql();
		System.out.println(sql);
		Assert.notNull(sql);

		PageRequest pageRequest = new PageRequest(5, 10);
		sqlBuilder.limit(pageRequest);

		sql = sqlBuilder.toSql();
		System.out.println(sql);
		Assert.notNull(sql);

	}

	@Test
	public void insertTest(){

		Map<String,Object> parameters = new LinkedHashMap<String,Object>();
		parameters.put("subject_id", 20L);
		parameters.put("name", "Sue");
		parameters.put("species", "human");
		parameters.put("gender", "F");
		SqlBuilder sqlBuilder = new SqlBuilder(tableDescription);
		sqlBuilder.insert(parameters);
		String sql = sqlBuilder.toSql();
		Assert.notNull(sql);
		System.out.println(sql);
		List<Object> values = sqlBuilder.getQueryParameterValues();
		Assert.notNull(values);
		Assert.notEmpty(values);
		Assert.isTrue(values.size() == 4);
		Assert.isTrue(((String) values.get(2)).equals("human"));

	}

	@Test
	public void updateTest(){
		Map<String,Object> parameters = new LinkedHashMap<String,Object>();
		parameters.put("name", "Mittens");
		parameters.put("species", "cat");
		parameters.put("gender", "F");
		SqlBuilder sqlBuilder = new SqlBuilder(tableDescription);
		sqlBuilder.update(parameters).where(equal("subject_id", 1L));
		String sql = sqlBuilder.toSql();
		System.out.println(sql);
		Assert.notNull(sql);
		List<Object> values = sqlBuilder.getQueryParameterValues();
		Assert.notNull(values);
		Assert.notEmpty(values);
		Assert.isTrue(values.size() == 4);
		Assert.isTrue(((Long) values.get(3)).equals(1L));
	}

	@Test
	public void deleteTest(){
		SqlBuilder sqlBuilder = new SqlBuilder(tableDescription);
		sqlBuilder.delete().where(equal("subject_id", 1));
		String sql = sqlBuilder.toSql();
		System.out.println(sql);
		Assert.notNull(sql);
	}

	@Configuration
	public static class EmptyContext {}

}
