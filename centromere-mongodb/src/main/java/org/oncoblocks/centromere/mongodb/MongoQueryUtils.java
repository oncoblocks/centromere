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

import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Helper class for translation of {@link QueryCriteria} to Spring Data {@link Criteria} query objects.
 * 
 * @author woemler
 * @since 0.4.1
 */
public class MongoQueryUtils {

	/**
	 * Converts a collection of {@link QueryCriteria}
	 *  objects into Spring Data MongoDB {@link Criteria}
	 *  objects, used to build a {@link Query}.
	 *
	 * @param queryCriterias list of query parameters to be converted.
	 * @return {@link Criteria} representation of the dataimport.
	 */
	public static Criteria getQueryFromQueryCriteria(Iterable<QueryCriteria> queryCriterias){
		List<Criteria> criteriaList = new ArrayList<>();
		for (QueryCriteria queryCriteria: queryCriterias){
			Criteria criteria = null;
			if (queryCriteria != null) {
				switch (queryCriteria.getEvaluation()) {
					case EQUALS:
						criteria = new Criteria(queryCriteria.getKey()).is(queryCriteria.getValue());
						break;
					case NOT_EQUALS:
						criteria = new Criteria(queryCriteria.getKey()).not().is(queryCriteria.getValue());
						break;
					case IN:
						criteria = new Criteria(queryCriteria.getKey()).in((Collection) queryCriteria.getValue());
						break;
					case NOT_IN:
						criteria = new Criteria(queryCriteria.getKey()).nin((Collection) queryCriteria.getValue());
						break;
					case IS_NULL:
						criteria = new Criteria(queryCriteria.getKey()).is(null);
						break;
					case NOT_NULL:
						criteria = new Criteria(queryCriteria.getKey()).not().is(null);
						break;
					case GREATER_THAN:
						criteria = new Criteria(queryCriteria.getKey()).gt(queryCriteria.getValue());
						break;
					case GREATER_THAN_EQUALS:
						criteria = new Criteria(queryCriteria.getKey()).gte(queryCriteria.getValue());
						break;
					case LESS_THAN:
						criteria = new Criteria(queryCriteria.getKey()).lt(queryCriteria.getValue());
						break;
					case LESS_THAN_EQUALS:
						criteria = new Criteria(queryCriteria.getKey()).lte(queryCriteria.getValue());
						break;
					case BETWEEN:
						criteria = new Criteria().andOperator(
								Criteria.where(queryCriteria.getKey()).gt(((List) queryCriteria.getValue()).get(0)),
								Criteria.where(queryCriteria.getKey()).lt(((List) queryCriteria.getValue()).get(1)));
						break;
					case OUTSIDE:
						criteria = new Criteria().orOperator(
								Criteria.where(queryCriteria.getKey()).lt(((List) queryCriteria.getValue()).get(0)),
								Criteria.where(queryCriteria.getKey()).gt(((List) queryCriteria.getValue()).get(1)));
						break;
					case BETWEEN_INCLUSIVE:
						criteria = new Criteria().andOperator(
								Criteria.where(queryCriteria.getKey()).gte(((List) queryCriteria.getValue()).get(0)),
								Criteria.where(queryCriteria.getKey()).lte(((List) queryCriteria.getValue()).get(1)));
						break;
					case OUTSIDE_INCLUSIVE:
						criteria = new Criteria().orOperator(
								Criteria.where(queryCriteria.getKey()).lte(((List) queryCriteria.getValue()).get(0)),
								Criteria.where(queryCriteria.getKey()).gte(((List) queryCriteria.getValue()).get(1)));
						break;
					case LIKE:
						criteria = new Criteria(queryCriteria.getKey()).regex((String) queryCriteria.getValue());
						break;
					case NOT_LIKE:
						// TODO
						break;
					case STARTS_WITH:
						criteria = new Criteria(queryCriteria.getKey()).regex("^" + queryCriteria.getValue());
						break;
					case ENDS_WITH:
						criteria = new Criteria(queryCriteria.getKey()).regex(queryCriteria.getValue() + "$");
						break;
					default:
						criteria = new Criteria(queryCriteria.getKey()).is(queryCriteria.getValue());
				}
				criteriaList.add(criteria);
			}
		}
		return criteriaList.size() > 0 ?
				new Criteria().andOperator(criteriaList.toArray(new Criteria[]{})) : null;
	}
	
}
