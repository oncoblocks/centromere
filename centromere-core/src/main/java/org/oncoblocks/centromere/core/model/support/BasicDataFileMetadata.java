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
 * @author woemler
 */
public class BasicDataFileMetadata implements DataFileMetadata<String> {
	
	private String id;
	private String dataType;
	private String filePath;
	private Object dataFileId;
	private DataSetMetadata dataSet;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Object getDataFileId() {
		return dataFileId;
	}

	public void setDataFileId(Object dataFileId) {
		this.dataFileId = dataFileId;
	}

	public DataSetMetadata getDataSet() {
		return dataSet;
	}

	public void setDataSet(DataSetMetadata dataSet) {
		this.dataSet = dataSet;
	}

	@Override 
	public String toString() {
		return "BasicDataFileMetadata{" +
				"id='" + id + '\'' +
				", dataType='" + dataType + '\'' +
				", filePath='" + filePath + '\'' +
				", dataFileId=" + dataFileId +
				", dataSet=" + dataSet +
				'}';
	}
	
}
