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

package org.oncoblocks.centromere.sql.sqlbuilder;

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
