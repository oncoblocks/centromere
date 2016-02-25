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

import org.oncoblocks.centromere.core.dataimport.component.RecordProcessor;

/**
 * Simple POJO that maps a user-defined data type name to a {@link RecordProcessor} bean and an
 *   instance of {@link ImportOptions} to dictate job parameters.  This object can be generated
 *   by import job description file parsing, or by {@link org.oncoblocks.centromere.core.dataimport.component.DataTypes}
 *   annotation inspection at runtime.
 * 
 * @author woemler
 */
public class DataType {
	
	private String name;
	private Class<? extends RecordProcessor> processor;
	private ImportOptions options = new BasicImportOptions();

	public DataType() { }

	public DataType(String name,
			Class<? extends RecordProcessor> processor) {
		this.name = name;
		this.processor = processor;
	}

	public DataType(String name,
			Class<? extends RecordProcessor> processor,
			ImportOptions options) {
		this.name = name;
		this.processor = processor;
		this.options = options;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<? extends RecordProcessor> getProcessor() {
		return processor;
	}

	public void setProcessor(
			Class<? extends RecordProcessor> processor) {
		this.processor = processor;
	}

	public ImportOptions getOptions() {
		return options;
	}

	public void setOptions(ImportOptions options) {
		this.options = options;
	}

}


