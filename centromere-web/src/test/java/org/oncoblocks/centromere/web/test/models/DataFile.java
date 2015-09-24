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

package org.oncoblocks.centromere.web.test.models;

import org.oncoblocks.centromere.core.model.Filterable;
import org.oncoblocks.centromere.core.model.support.DataFileMetadata;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author woemler
 */

@Filterable
@Document(collection = "data_files")
public class DataFile implements DataFileMetadata<String> {
	
	private String id;
	private String dataSetId;
	private String filePath;
	private String dataType;
	private Date importDate;
	private String notes;

	public DataFile() { }

	public DataFile(String id, String dataSetId, String filePath, String dataType, Date importDate, String notes) {
		this.id = id;
		this.dataSetId = dataSetId;
		this.filePath = filePath;
		this.dataType = dataType;
		this.importDate = importDate;
		this.notes = notes;
	}

	@Override public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override public String getDataSetId() {
		return dataSetId;
	}

	@Override public void setDataSetId(String dataSetId) {
		this.dataSetId = dataSetId;
	}

	@Override public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
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
