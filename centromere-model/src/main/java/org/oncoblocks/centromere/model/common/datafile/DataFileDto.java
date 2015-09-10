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

package org.oncoblocks.centromere.model.common.datafile;

import org.oncoblocks.centromere.model.support.AbstractModel;

import java.io.Serializable;
import java.util.Date;

/**
 * @author woemler
 */

public class DataFileDto<ID extends Serializable> extends AbstractModel<ID> {
	
	private ID dataSetId;
	private String filePath;
	private Date importDate;
	private String notes;

	public DataFileDto() { }

	public DataFileDto(ID id, ID dataSetId, String filePath, Date importDate, String notes) {
		super(id);
		this.dataSetId = dataSetId;
		this.filePath = filePath;
		this.importDate = importDate;
		this.notes = notes;
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
