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

package org.oncoblocks.centromere.core.model.support;

/**
 * Simple representation of a data set being processed and imported into the warehouse.  Captures
 *   only the most basic information required for describing the data set.  The {@code dataSetId}
 *   value represents the primary key ID value the record is represented by in the database 
 *   implementation.
 * 
 * @author woemler
 */
public class BasicDataSetMetadata implements DataSetMetadata<String> {

	private String id;
	private String label;
	private Object dataSetId;
	private String name;
	private String source;
	private String notes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Object getDataSetId() {
		return dataSetId;
	}

	public void setDataSetId(Object dataSetId) {
		this.dataSetId = dataSetId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}
