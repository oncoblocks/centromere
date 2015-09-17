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

package org.oncoblocks.centromere.core.model.support;

import org.oncoblocks.centromere.core.model.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author woemler
 */

public class DataFileDto<ID extends Serializable> implements Model<ID> {
	
	private ID id;
	private ID dataSetId;
	private String filePath;
	private String dataType;
	private Date importDate;
	private String notes;

	public DataFileDto() { }

	public DataFileDto(ID id, ID dataSetId, String filePath, String dataType, Date importDate, String notes) {
		this.id = id;
		this.dataSetId = dataSetId;
		this.filePath = filePath;
		this.dataType = dataType;
		this.importDate = importDate;
		this.notes = notes;
	}

	@Override public ID getId() {
		return id;
	}

	public DataFileDto setId(ID id) {
		this.id = id;
		return this;
	}

	public ID getDataSetId() {
		return dataSetId;
	}

	public void setDataSetId(ID dataSetId) {
		this.dataSetId = dataSetId;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
