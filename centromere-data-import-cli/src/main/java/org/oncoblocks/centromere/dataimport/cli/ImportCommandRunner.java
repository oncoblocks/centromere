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

import com.google.common.collect.Iterables;
import org.oncoblocks.centromere.core.dataimport.component.DataImportException;
import org.oncoblocks.centromere.core.dataimport.component.RecordProcessor;
import org.oncoblocks.centromere.core.dataimport.pipeline.BasicImportOptions;
import org.oncoblocks.centromere.core.dataimport.pipeline.DataFileAware;
import org.oncoblocks.centromere.core.dataimport.pipeline.DataSetAware;
import org.oncoblocks.centromere.core.dataimport.pipeline.ImportOptionsAware;
import org.oncoblocks.centromere.core.model.support.BasicDataFileMetadata;
import org.oncoblocks.centromere.core.model.support.DataFileMetadata;
import org.oncoblocks.centromere.core.model.support.DataSetMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author woemler
 */
public class ImportCommandRunner {
	
	private final DataImportManager manager;
	
	private static final Logger logger = LoggerFactory.getLogger(ImportCommandRunner.class);

	public ImportCommandRunner(DataImportManager manager) {
		this.manager = manager;
	}

	/**
	 * Runs the import of the file provided in the input arguments.  Will choose the appropriate 
	 *   {@link RecordProcessor} instance, based on the supplied data type.  
	 * 
	 * @param arguments
	 * @throws Exception
	 */
	public void run(ImportCommandArguments arguments) throws Exception {
		RecordProcessor processor = this.getProcessorByDataType(arguments.getDataType());
		BasicImportOptions options = arguments.getImportOptions();
		DataSetMetadata dataSetMetadata = null;
		DataFileMetadata dataFileMetadata = null;
		String inputFilePath = arguments.getInputFilePath();
		File inputFile = new File(inputFilePath);
		if (!inputFile.exists() || !inputFile.isFile() || !inputFile.canRead()){
			throw new DataImportException(String.format("Input file is not valid: %s", inputFilePath));
		}
		if (processor instanceof DataSetAware){
			dataSetMetadata = this.getDataSetMetadata(arguments.getDataSet());
			((DataSetAware) processor).setDataSetMetadata(dataSetMetadata);
		}
		if (processor instanceof ImportOptionsAware){
			((ImportOptionsAware) processor).setImportOptions(options);
		}
		if (processor instanceof DataFileAware){
			if (Iterables.size(manager.getDataFileRepository().getByFilePath(inputFilePath)) == 0){
				BasicDataFileMetadata df = new BasicDataFileMetadata();
				df.setFilePath(inputFilePath);
				df.setDataType(arguments.getDataType());
				df.setDataSet(dataSetMetadata);
				dataFileMetadata = df;
			} else {
				if (options.isSkipExistingFiles()){
					logger.info(String.format("[CENTROMERE] Skipping existing data file: %s", arguments.getInputFilePath()));
					return;
				} else {
					logger.warn(String.format("Data file already exists: %s", arguments.getInputFilePath()));
					throw new DataImportException(String.format("Data file already exists: %s", arguments.getInputFilePath()));
				}
			}
			((DataFileAware) processor).setDataFileMetadata(dataFileMetadata);
		}
		processor.doBefore();
		processor.run(inputFile.getAbsolutePath());
		processor.doAfter();
	}
	private RecordProcessor getProcessorByDataType(String dataType) throws DataImportException{
		if (!manager.isSupportedDataType(dataType)){
			throw new DataImportException(String.format("Unable to identify appropriate RecordProcessor "
					+ "for data type: %s.  This data type may not be registered, or there may not be a bean "
					+ "for the required processor instantiated.", dataType));
		}
		return manager.getDataTypeProcessor(dataType);
	}
	
	private DataSetMetadata getDataSetMetadata(String label) throws DataImportException {
		DataSetMetadata dataSet = manager.getDataSet(label);
		if (dataSet == null){
			throw new DataImportException(String.format("DataSet for label does not exist: %s", label));
		} else if (dataSet.getId() == null){
			throw new DataImportException(String.format("DataSet record does not have ID, and may not have "
					+ "been persisted to the database: %s", dataSet.getLabel()));
		}
		return dataSet;
	}
	

}
