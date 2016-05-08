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
import org.oncoblocks.centromere.core.repository.Evaluation;
import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * @author woemler
 */
public class QueryCriteriaSpecification<T extends Model<?>> implements Specification<T> {
	
	private final QueryCriteria queryCriteria;
	private static final Logger logger = LoggerFactory.getLogger(QueryCriteriaSpecification.class);

	public QueryCriteriaSpecification(QueryCriteria queryCriteria) {
		this.queryCriteria = queryCriteria;
	}

	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery,
			CriteriaBuilder criteriaBuilder) {
		String key = queryCriteria.getKey();
		Object value = queryCriteria.getValue();
		Evaluation eval = queryCriteria.getEvaluation();
		Path path = null;
		if (key.contains(".")){
			String[] bits = key.split("\\.");
			path = root.join(bits[0]).get(bits[1]);
		} else {
			path = root.get(key);
		}
		logger.debug(String.format("[CENTROMERE] Converting QueryCriteria to JPA specification: %s", queryCriteria.toString()));
		switch (eval){
			case EQUALS:
				return criteriaBuilder.equal(path, value);
			case NOT_EQUALS:
				return criteriaBuilder.notEqual(path, value);
			case IN:
				return path.in(value);
			case NOT_IN:
				return criteriaBuilder.not(path.in(value));
			case IS_NULL:
				return criteriaBuilder.isNull(path);
			case NOT_NULL:
				return criteriaBuilder.isNotNull(path);
			case GREATER_THAN:
				return criteriaBuilder.greaterThan(path, value.toString());
			case GREATER_THAN_EQUALS:
				return criteriaBuilder.greaterThanOrEqualTo(path, value.toString());
			case LESS_THAN:
				return criteriaBuilder.lessThan(path, value.toString());
			case LESS_THAN_EQUALS:
				return criteriaBuilder.lessThanOrEqualTo(path, value.toString());
			case BETWEEN:
				return criteriaBuilder
						.and(criteriaBuilder.greaterThan(path, ((List<?>) value).get(0).toString()),
						criteriaBuilder.lessThan(path, ((List<?>) value).get(1).toString()));
			case OUTSIDE:
				return criteriaBuilder
						.or(criteriaBuilder.greaterThan(path, ((List<?>) value).get(1).toString()),
								criteriaBuilder.lessThan(path, ((List<?>) value).get(0).toString()));
			case BETWEEN_INCLUSIVE:
				return criteriaBuilder
					.and(criteriaBuilder.greaterThanOrEqualTo(path, ((List<?>) value).get(0).toString()),
							criteriaBuilder.lessThanOrEqualTo(path, ((List<?>) value).get(1).toString()));
			case OUTSIDE_INCLUSIVE:
				return criteriaBuilder
						.or(criteriaBuilder.greaterThanOrEqualTo(path, ((List<?>) value).get(1).toString()),
								criteriaBuilder.lessThanOrEqualTo(path, ((List<?>) value).get(0).toString()));
			case LIKE:
				return criteriaBuilder.like(path, "%" + value.toString() + "%");
			case NOT_LIKE:
				return criteriaBuilder.notLike(path, "%" + value.toString() + "%");
			case STARTS_WITH:
				return criteriaBuilder.like(path, value.toString() + "%");
			case ENDS_WITH:
				return criteriaBuilder.like(path, "%" + value.toString());
			default:
				return criteriaBuilder.equal(root.get(queryCriteria.getKey()), queryCriteria.getValue());
		}
	}
}
