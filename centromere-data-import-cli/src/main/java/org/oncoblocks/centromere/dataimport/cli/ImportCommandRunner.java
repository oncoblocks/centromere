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
import org.oncoblocks.centromere.core.dataimport.component.RecordProcessor;
import org.oncoblocks.centromere.core.dataimport.pipeline.*;
import org.oncoblocks.centromere.core.dataimport.pipeline.DataSetMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;

/**
 * @author woemler
 */
public class ImportCommandRunner {
	
	private DataTypeManager dataTypeManager;
	private DataSetManager dataSetManager;
	
	private static final Logger logger = LoggerFactory.getLogger(ImportCommandRunner.class);

	public ImportCommandRunner() { }

	public ImportCommandRunner(
			DataTypeManager dataTypeManager,
			DataSetManager dataSetManager) {
		this.dataTypeManager = dataTypeManager;
		this.dataSetManager = dataSetManager;
	}

	public void run(ImportCommandArguments arguments) throws Exception {
		Assert.notNull(dataSetManager, "DataSetManager must not be null!");
		Assert.notNull(dataTypeManager, "DataTypeManager must not be null!");
		Assert.notNull(arguments, "arguments must not be null!");
		RecordProcessor<?> processor = dataTypeManager.getProcessorByDataType(arguments.getDataType());
		if (processor == null){
			throw new DataImportException(String.format("Unable to identify appropriate RecordProcessor " 
					+ "for data type: %s.  This data type may not be registered, or there may not be a bean " 
					+ "for the required processor instantiated.", arguments.getDataType()));
		}
		if (processor instanceof DataSetAware){
			DataSetMetadata dataSet = dataSetManager.getDataSet(arguments.getDataSet());
			if (dataSet == null){
				throw new DataImportException(String.format("DataSet for label does not exist: %s",
						arguments.getDataSet()));
			} else if (dataSet.getDataSetId() == null){
				throw new DataImportException(String.format("DataSet record does not have ID, and may not have "
						+ "been persisted to the database: ", dataSet.getLabel()));
			}
			((DataSetAware) processor).setDataSetId(dataSet.getDataSetId());
		}
		ImportOptions options = arguments.getImportOptions();
		processor.setImportOptions(options);
		File inputFile = new File(arguments.getInputFilePath());
		if (!inputFile.exists() || !inputFile.isFile() || !inputFile.canRead()){
			throw new DataImportException(String.format("Input file is not valid: %s", arguments.getInputFilePath()));
		}
		processor.run(inputFile.getAbsolutePath());
	}

	public DataTypeManager getDataTypeManager() {
		return dataTypeManager;
	}

	public void setDataTypeManager(
			DataTypeManager dataTypeManager) {
		this.dataTypeManager = dataTypeManager;
	}

	public DataSetManager getDataSetManager() {
		return dataSetManager;
	}

	public void setDataSetManager(
			DataSetManager dataSetManager) {
		this.dataSetManager = dataSetManager;
	}
}
