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

package org.oncoblocks.centromere.dataimport.cli;

import org.oncoblocks.centromere.core.dataimport.component.DataImportException;
import org.oncoblocks.centromere.core.model.support.DataSetMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author woemler
 */
public class AddCommandRunner {
	
	private final DataImportManager manager;
	
	private static final Logger logger = LoggerFactory.getLogger(AddCommandRunner.class);

	public AddCommandRunner(DataImportManager manager) {
		this.manager = manager;
	}

	public void run(AddCommandArguments arguments) throws Exception {
		Assert.notNull(arguments, "AddCommandArguments must not be null!");
		switch (arguments.getCategory().toLowerCase()){
			case "data_type":
				this.addDataType(arguments.getLabel(), arguments.getBody());
				break;
			case "data_set":
				DataSetMetadata dataSetMetadata = arguments.getDataSetMetadata();
				if (dataSetMetadata != null){
					this.addDataSet(dataSetMetadata);
				} else {
					throw new DataImportException("Could not parse DataSetMetadata from input: " + arguments.getBody());
				}
				break;
			default:
				throw new DataImportException(String.format("Invalid add mode category: %s", arguments.getCategory()));
		}
		
	}
	
	private void addDataType(String dataType, String processorRef) throws DataImportException{
		manager.addDataTypeMapping(dataType, processorRef);
	} 
	
	private void addDataSet(DataSetMetadata dataSetMetadata){
		manager.addDataSetMapping(dataSetMetadata);
	}

}
