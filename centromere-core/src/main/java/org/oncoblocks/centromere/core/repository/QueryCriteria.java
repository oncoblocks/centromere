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

	public static enum Evaluation {
		EQUALS,
		IN,
		NOT_EQUALS,
		NOT_IN,
		LIKE,
		NOT_LIKE,
		GREATER_THAN,
		LESS_THAN,
		GREATER_THAN_EQUALS,
		LESS_THAN_EQUALS,
		IS_NULL,
		NOT_NULL,
		IS_TRUE,
		IS_FALSE
		;
	}
	
}
