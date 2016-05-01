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

package org.oncoblocks.centromere.dataimport.cli.test.support;

import org.oncoblocks.centromere.core.model.Model;
import org.oncoblocks.centromere.core.model.support.DataFileMetadata;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author woemler
 */
@Document(collection = "data_files")
public class DataFile implements Model<String>, DataFileMetadata<String> {
	
	@Id private String id;
	private String dataType;
	private String dataSetId;
	private String filePath;

	public String getDataType() {
		return dataType;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getDataSetId() {
		return dataSetId;
	}

	public void setDataSetId(String dataSetId) {
		this.dataSetId = dataSetId;
	}
}
