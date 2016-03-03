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

package org.oncoblocks.centromere.core.dataimport.pipeline;

import java.util.Map;

/**
 * Simple representation of a file to be imported.  The {@code dataType} attribute should map to
 *   a corresponding {@link DataType} definition, which links the file to the {@link org.oncoblocks.centromere.core.dataimport.component.RecordProcessor}
 *   to be used to handle the file import.
 * 
 * @author woemler
 */
public class InputFile {
	
	private String path;
	private String dataType;
	private String dataSet;
	private ImportOptions options = new ImportOptions();

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	public ImportOptions getOptions() {
		return options;
	}

	public void setOptions(Map<String,String> options) {
		this.options = new ImportOptions(options);
	}
}
