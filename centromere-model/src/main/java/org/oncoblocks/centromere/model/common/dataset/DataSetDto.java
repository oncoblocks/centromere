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

package org.oncoblocks.centromere.model.common.dataset;

import org.oncoblocks.centromere.model.support.AbstractModel;

import java.io.Serializable;

/**
 * @author woemler
 */

public class DataSetDto<ID extends Serializable> extends AbstractModel<ID> {
	
	private String source;
	private String name;
	private String dataType;
	private String notes;

	public DataSetDto() { }

	public DataSetDto(ID id, String source, String name, String dataType, String notes) {
		super(id);
		this.source = source;
		this.name = name;
		this.dataType = dataType;
		this.notes = notes;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
