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

package org.oncoblocks.centromere.core.repository;

/**
 * @author woemler
 */
public class QueryParameterDescriptor {
	
	private String paramName;
	private String fieldName;
	private Class<?> type;
	private Evaluation evaluation;

	public QueryParameterDescriptor() { }

	public QueryParameterDescriptor(String paramName, String fieldName, Class<?> type,
			Evaluation evaluation) {
		this.paramName = paramName;
		this.fieldName = fieldName;
		this.type = type;
		this.evaluation = evaluation;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public Evaluation getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}
	
	public QueryCriteria createQueryCriteria(Object value){
		return new QueryCriteria(fieldName, value, evaluation);
	}

	@Override public String toString() {
		return "QueryParameterDescriptor{" +
				"paramName='" + paramName + '\'' +
				", fieldName='" + fieldName + '\'' +
				", type=" + type +
				", evaluation=" + evaluation +
				'}';
	}
}
