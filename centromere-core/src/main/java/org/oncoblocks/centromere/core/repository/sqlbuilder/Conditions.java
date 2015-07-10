package org.oncoblocks.centromere.core.repository.sqlbuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Wraps a series of conditions and joins them with AND/OR operators
 * 
 * @author woemler 
 */
public class Conditions {
	
	public static enum Operation { AND, OR }
	
	private Operation operation;
	private String sql;
	private List<Object> values;
	
	public Conditions(Operation operation, Condition... conditions){
		
		this.operation = operation;
		this.values = new ArrayList<>();

		StringBuilder builder = new StringBuilder();
		String separator = " AND ";
		if (operation.equals(Operation.OR)){
			separator = " OR ";
		}
		boolean flag = false;
		for (Condition condition: conditions){
			if (flag){
				builder.append(separator);
			}
			flag = true;
			builder.append(condition.getClause());
			if (condition.getValue() != null){
				values.add(condition.getValue());
			}
		}
		
		this.sql = builder.toString();
		
	}
	
	public Conditions(Operation operation, Conditions... conditions){
		
		this.operation = operation;
		this.values = new ArrayList<>();

		StringBuilder builder = new StringBuilder();
		String separator = " AND ";
		if (operation.equals(Operation.OR)){
			separator = " OR ";
		}
		boolean flag = false;
		
		for (Conditions c: conditions){
			if (flag) {
				builder.append(separator);
			}
			flag = true;
			builder.append(" ( " + c.getSql() + " ) ");
			this.values.addAll(c.getValues());
		}
		
		this.sql = builder.toString();
		
	}
	
	public String getSql(){
		return sql;
	}
	
	public List<Object> getValues(){
		return values;
	}
	
	public Operation getOperation(){
		return operation;
	}
	
}
