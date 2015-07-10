package org.oncoblocks.centromere.core.repository.sqlbuilder;

import org.oncoblocks.centromere.core.repository.QueryCriteria.Evaluation;

/**
 * Maps a {@link org.oncoblocks.centromere.core.repository.QueryCriteria} to SQL operations.
 * 
 * @author woemler 
 */
public class Condition {
	private String clause;
	private Object value;

	public Condition(String column, Object value, Evaluation evalutation){
		StringBuilder builder = new StringBuilder(" " + column);
		switch (evalutation) {
			case EQUALS:
				builder.append(" = ? ");
				break;
			case NOT_EQUALS:
				builder.append(" != ? ");
				break;
			case IS_NULL:
				builder.append(" is null ");
				break;
			case NOT_NULL:
				builder.append(" is not null ");
				break;
			case IN:
				builder.append(" in (?) ");
				break;
			case NOT_IN:
				builder.append(" not in (?) ");
				break;
			default:
				builder.append(" = ? ");
		}
		this.clause = builder.toString();
		this.value = value;
	}

	public String getClause() {
		return clause;
	}

	public Object getValue() {
		return value;
	}

}
