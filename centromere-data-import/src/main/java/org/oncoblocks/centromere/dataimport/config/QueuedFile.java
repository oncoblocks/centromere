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

package org.oncoblocks.centromere.dataimport.config;

import org.oncoblocks.centromere.core.model.support.DataFileMetadata;
import org.oncoblocks.centromere.core.model.support.DataSetMetadata;
import org.oncoblocks.centromere.dataimport.processor.GeneralFileProcessor;
import org.springframework.util.Assert;

/**
 * @author woemler
 */
public class QueuedFile {
	
	private DataFileMetadata dataFile;
	private DataSetMetadata dataSet;
	private GeneralFileProcessor processor;
	
	private String tempFileName;

	public QueuedFile() { }

	public QueuedFile(DataFileMetadata dataFile,
			DataSetMetadata dataSet,
			GeneralFileProcessor processor) {
		Assert.notNull(dataFile);
		Assert.notNull(dataSet);
		Assert.notNull(processor);
		this.dataFile = dataFile;
		this.dataSet = dataSet;
		this.processor = processor;
	}

	public DataFileMetadata getDataFile() {
		return dataFile;
	}

	public QueuedFile setDataFile(
			DataFileMetadata dataFile) {
		this.dataFile = dataFile;
		return this;
	}

	public DataSetMetadata getDataSet() {
		return dataSet;
	}

	public QueuedFile setDataSet(
			DataSetMetadata dataSet) {
		this.dataSet = dataSet;
		return this;
	}

	public GeneralFileProcessor getProcessor() {
		return processor;
	}

	public QueuedFile setProcessor(
			GeneralFileProcessor processor) {
		this.processor = processor;
		return this;
	}

	public String getTempFileName() {
		return tempFileName;
	}

	public QueuedFile setTempFileName(String tempFileName) {
		this.tempFileName = tempFileName;
		return this;
	}
}
