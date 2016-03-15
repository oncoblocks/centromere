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

import org.oncoblocks.centromere.core.dataimport.component.DataImportException;
import org.oncoblocks.centromere.core.dataimport.component.DataTypes;
import org.oncoblocks.centromere.core.dataimport.component.RecordProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Configures and runs a data import job, as defined by a {@link ImportJob} object.
 * 
 * @author woemler
 */
public class ImportJobRunner implements ApplicationContextAware {
	
	private ImportJob importJob;
	private ApplicationContext applicationContext;
	private DataTypeManager dataTypeManager;
	private DataSetManager dataSetManager;
	private static final Logger logger = LoggerFactory.getLogger(ImportJobRunner.class);

	public ImportJobRunner() { }

	public ImportJobRunner(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * Executes the import pipeline and processes all of the files in the {@link ImportJob}.
	 * 
	 * @throws DataImportException
	 */
	public void runImport() throws DataImportException {
		this.configurationCheck();
		for (InputFile inputFile: importJob.getFiles()){
			String dataSetName = inputFile.getDataSet();
			String dataTypeName = inputFile.getDataType();
			String inputFilePath = inputFile.getPath();
			DataSetMetadata dataSetMetadata = dataSetManager.getDataSetByName(dataSetName);
			ImportOptions combinedOptions = mergeImportOptions(inputFile.getOptions(), importJob.getOptions());
			this.processDataSet(dataSetMetadata, inputFile);
			RecordProcessor processor = this.getProcessorInstanceByDataType(dataTypeName);
			if (processor instanceof ImportOptionsAware && inputFile.getOptions() != null) {
				((ImportOptionsAware) processor).setImportOptions(combinedOptions);
			}
			if (processor instanceof DataSetAware){
				Object dataSetId = this.getDataSetId(dataSetMetadata, inputFile);
				((DataSetAware) processor).setDataSetId(dataSetId);
			}
			logger.info(String.format("[CENTROMERE] Processing dataimport file: dataSet=%s dataType=%s, processor=%s, filePath=%s",
					dataSetName, dataTypeName, processor.getClass().getName(), inputFilePath));
			processor.doBefore();
			processor.run(inputFilePath);
			processor.doAfter();
			logger.info(String.format("[CENTROMERE] Completed file processing: %s", inputFilePath));
		}
	}

	/**
	 * Merges the data set-level {@link ImportOptions} with the file-level options.  File-level options
	 *   are given precendence over data set-level options, in case a file requires special handling.
	 * 
	 * @param fileOptions
	 * @param jobOptions
	 * @return
	 */
	protected ImportOptions mergeImportOptions(ImportOptions fileOptions, ImportOptions jobOptions){
		Map<String,String> options = new HashMap<>(jobOptions.getOptionsMap());
		options.putAll(new HashMap<>(fileOptions.getOptionsMap()));
		return new ImportOptions(options);
	}

	/**
	 * Runs the required configuration before the import job executes.
	 */
	protected void jobSetup(){
		this.configureDataTypeManager();
		this.configureDataSetManager();
	}

	/**
	 * Processes the current data set record.  Should be overridden to handle record creation or 
	 *   manipulation.
	 * 
	 * @param dataSet
	 */
	protected void processDataSet(DataSetMetadata dataSet, InputFile inputFile) throws DataImportException {
		return;	
	}

	/**
	 * Returns a data set identifier based upon its {@link DataSetMetadata} reference.  This method
	 *   should be overridden along with {@link #processDataSet(DataSetMetadata, InputFile)}, to handle
	 *   implementation-specific data sets.
	 * 
	 * @param dataSet
	 * @return
	 */
	protected Object getDataSetId(DataSetMetadata dataSet, InputFile inputFile){
		return null;
	} 

	/**
	 * Checks that all necessary configurations have taken place place before the import job runs.
	 * 
	 * @throws DataImportException
	 */
	protected void configurationCheck() throws DataImportException {
		if (dataTypeManager == null) throw new DataImportException("DataTypeManager has not been configured.");
	}

	/**
	 * Checks that the submitted {@link ImportJob} object contains all the required components.
	 * 
	 * @throws DataImportException
	 */
	protected void validateImportJob() throws DataImportException {
		try {
			Assert.notNull(importJob, "ImportJob must not be null");
			Assert.isTrue(importJob.getFiles() != null && !importJob.getFiles().isEmpty(),
					"Import file list must not be empty.");
			Assert.isTrue(importJob.getDataSets() != null && !importJob.getDataSets().isEmpty(),
					"Data set list must not be empty.");
//			Assert.isTrue(importJob.getDataTypes() != null && !importJob.getDataTypes().isEmpty(),
//					"Data type list must not be empty.");
		} catch (IllegalArgumentException e){
			e.printStackTrace();
			throw new DataImportException(e.getMessage());
		}
	}

	/**
	 * Creates and populates a {@link DataTypeManager} to handle mapping data types to 
	 *   {@link RecordProcessor} beans.
	 */
	protected void configureDataTypeManager(){
		dataTypeManager = new DataTypeManager();
		for (Map.Entry entry: applicationContext.getBeansWithAnnotation(DataTypes.class).entrySet()){
			Object obj = entry.getValue();
			if (obj instanceof RecordProcessor){
				RecordProcessor p = (RecordProcessor) obj;
				DataTypes dataTypes = p.getClass().getAnnotation(DataTypes.class);
				for (String t: dataTypes.value()){
					dataTypeManager.addDataType(new DataType(t, p.getClass().getName()));
				}
			}
		}
		if (importJob.getDataTypes() != null) dataTypeManager.addDataTypes(importJob.getDataTypes()); // Data type configurations in the job file will override annotations
	}

	protected void configureDataSetManager(){
		dataSetManager = new DataSetManager();
		dataSetManager.addDataSets(importJob.getDataSets());
	}

	/**
	 * Returns reference to existing {@link RecordProcessor} bean, given an associated data type.  
	 *   Throws an exception of the data type is not registered in the configuration, or if the bean 
	 *   has not already been created.
	 * 
	 * @param name
	 * @return
	 * @throws DataImportException
	 */
	protected RecordProcessor getProcessorInstanceByDataType(String name) throws DataImportException {
		if (!dataTypeManager.isSupportedDataType(name)){
			throw new DataImportException(String.format("Requested data type is not supported in the " 
					+ "current configuration: %s", name));
		}
		RecordProcessor processor = null;
		String processorRef = dataTypeManager.getProcessorByDataType(name);
		try {
			Class<? extends RecordProcessor> processorClass = (Class<? extends RecordProcessor>) Class.forName(processorRef);
			processor = applicationContext.getBean(processorClass);
		} catch (ClassNotFoundException e){
			processor = (RecordProcessor) applicationContext.getBean(processorRef);
		}
		
		if (processor == null){
			throw new DataImportException(String.format("RecordProcessor bean does not exist: %s", 
					processorRef));
		}
		return processor;
	}
	
	/* Getters and setters */

	public ImportJob getImportJob() {
		return importJob;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public DataTypeManager getDataTypeManager() {
		return dataTypeManager;
	}

	public DataSetManager getDataSetManager() {
		return dataSetManager;
	}

	public static Logger getLogger() {
		return logger;
	}

	public void setImportJob(ImportJob importJob) throws DataImportException {
		this.importJob = importJob;
		this.validateImportJob();
		this.jobSetup();
	}

	public void setApplicationContext(
			ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}
