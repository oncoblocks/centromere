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

package org.oncoblocks.centromere.core.repository;

/**
 * Simple representation of a database query evaluation that can be passed to generic 
 *   {@link org.oncoblocks.centromere.core.repository.RepositoryOperations} implementations.
 * 
 * @author woemler
 */
public class QueryCriteria {
	
	private String key;
	private Object value;
	private Evaluation evaluation;

	public QueryCriteria(String key, Object value,
			Evaluation evaluation) {
		this.key = key;
		this.value = value;
		this.evaluation = evaluation;
	}

	public QueryCriteria(String key, Object value) {
		this.key = key;
		this.value = value;
		this.evaluation = Evaluation.EQUALS;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Evaluation getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}

	@Override public String toString() {
		return "QueryCriteria{" +
				"key='" + key + '\'' +
				", value=" + value +
				", evaluation=" + evaluation +
				'}';
	}
}
