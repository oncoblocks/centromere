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

import org.oncoblocks.centromere.core.dataimport.DataImportException;
import org.oncoblocks.centromere.core.model.support.DataSetMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Executes the {@code add} command, based upon input arguments.  The {@code category} argument will
 *   determine how the {@code label} and {@code body} objects are processed.
 * 
 * @author woemler
 */
public class AddCommandRunner {
	
	private final DataImportManager manager;
	
	private static final Logger logger = LoggerFactory.getLogger(AddCommandRunner.class);

	public AddCommandRunner(DataImportManager manager) {
		this.manager = manager;
	}

	/**
	 * Takes parsed {@link AddCommandArguments} and delegates the submitted arguments based upon the 
	 *   {@code category} argument.  The {@code label} and {@code body} are passed to methods that 
	 *   handle their specific content and use-cases.
	 * 
	 * @param arguments
	 * @throws Exception
	 */
	public void run(AddCommandArguments arguments) throws Exception {
		logger.debug(String.format("[CENTROMERE] Running AddCommandRunner with arguments: %s", arguments.toString()));
		Assert.notNull(arguments, "AddCommandArguments must not be null!");
		switch (arguments.getCategory().toLowerCase()){
			case "data_type":
				logger.debug(String.format("[CENTROMERE] Adding data type: %s", arguments.getLabel()));
				this.addDataType(arguments.getLabel(), arguments.getBody());
				break;
			case "data_set":
				DataSetMetadata dataSetMetadata = arguments.getDataSetMetadata();
				if (dataSetMetadata != null){
					logger.debug(String.format("[CENTROMERE] Adding data set: %s", dataSetMetadata.toString()));
					this.addDataSet(dataSetMetadata);
				} else {
					throw new CommandLineRunnerException("Could not parse DataSetMetadata from input: " + arguments.getBody());
				}
				break;
			default:
				throw new CommandLineRunnerException(String.format("Invalid add mode category: %s", arguments.getCategory()));
		}
		logger.debug("[CENTROMERE] Add task complete.");
	}

	/**
	 * Registers a new data type mapping with the {@link DataImportManager}.
	 * 
	 * @param dataType
	 * @param processorRef
	 * @throws DataImportException
	 */
	private void addDataType(String dataType, String processorRef) throws DataImportException{
		manager.addDataTypeMapping(dataType, processorRef);
	}

	/**
	 * Registers a new data set mapping with the {@link DataImportManager}.
	 * 
	 * @param dataSetMetadata
	 */
	private void addDataSet(DataSetMetadata dataSetMetadata){
		manager.addDataSetMapping(dataSetMetadata);
	}

}
