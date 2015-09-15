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

package org.oncoblocks.centromere.core.dataimport.config;

import org.oncoblocks.centromere.core.model.impl.DataFileDto;
import org.oncoblocks.centromere.core.model.impl.DataSetDto;
import org.oncoblocks.centromere.core.repository.impl.DataFileRepositoryOperations;
import org.oncoblocks.centromere.core.repository.impl.DataSetRepositoryOperations;

/**
 * @author woemler
 */
public class JobConfiguration {

	private String fileListPath;
	private String tempFileDirectory;
	private String logFilePath;
	private DataSetDto<?> dataSet;
	private DataSetRepositoryOperations<DataSetDto<?>, ?> dataSetRepository;
	private DataFileRepositoryOperations<DataFileDto<?>, ?> dataFileRepository;
	private boolean failOnMissingFile = true;
	private boolean failOnMissingFileType = true;
	private boolean failOnExistingFile = true;
	private boolean failOnExistingDataSet = true;

	public JobConfiguration() { }

	public String getFileListPath() {
		return fileListPath;
	}

	public JobConfiguration setFileListPath(String fileListPath) {
		this.fileListPath = fileListPath;
		return this;
	}

	public String getTempFileDirectory() {
		return tempFileDirectory;
	}

	public JobConfiguration setTempFileDirectory(String tempFileDirectory) {
		this.tempFileDirectory = tempFileDirectory;
		return this;
	}

	public String getLogFilePath() {
		return logFilePath;
	}

	public JobConfiguration setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
		return this;
	}

	public DataSetDto getDataSet() {
		return dataSet;
	}
	
	public JobConfiguration setDataSet(String name, String source, String notes){
		this.dataSet = new DataSetDto<>(null, source, name, notes);
		return this;
	}
	
	public JobConfiguration setDataSet(String name, String source){
		this.setDataSet(name, source, null);
		return this;
	}
	
	public JobConfiguration setDataSet(String name){
		this.setDataSet(name, null, null);
		return this;
	}

	public JobConfiguration setDataSet(
			DataSetDto<?> dataSet) {
		this.dataSet = dataSet;
		return this;
	}

	public DataSetRepositoryOperations<DataSetDto<?>, ?> getDataSetRepository() {
		return dataSetRepository;
	}

	public JobConfiguration setDataSetRepository(
			DataSetRepositoryOperations<DataSetDto<?>, ?> dataSetRepository) {
		this.dataSetRepository = dataSetRepository;
		return this;
	}

	public DataFileRepositoryOperations<DataFileDto<?>, ?> getDataFileRepository() {
		return dataFileRepository;
	}

	public JobConfiguration setDataFileRepository(
			DataFileRepositoryOperations<DataFileDto<?>, ?> dataFileRepository) {
		this.dataFileRepository = dataFileRepository;
		return this;
	}

	public boolean isFailOnMissingFile() {
		return failOnMissingFile;
	}

	public JobConfiguration setFailOnMissingFile(boolean failOnMissingFile) {
		this.failOnMissingFile = failOnMissingFile;
		return this;
	}

	public boolean isFailOnMissingFileType() {
		return failOnMissingFileType;
	}

	public JobConfiguration setFailOnMissingFileType(boolean failOnMissingFileType) {
		this.failOnMissingFileType = failOnMissingFileType;
		return this;
	}

	public boolean isFailOnExistingFile() {
		return failOnExistingFile;
	}

	public JobConfiguration setFailOnExistingFile(boolean failOnExistingFile) {
		this.failOnExistingFile = failOnExistingFile;
		return this;
	}

	public boolean isFailOnExistingDataSet() {
		return failOnExistingDataSet;
	}

	public JobConfiguration setFailOnExistingDataSet(boolean failOnExistingDataSet) {
		this.failOnExistingDataSet = failOnExistingDataSet;
		return this;
	}
}
