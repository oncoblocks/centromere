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

import org.oncoblocks.centromere.core.dataimport.job.DataFileProcessor;
import org.oncoblocks.centromere.core.model.support.DataFileMetadata;
import org.oncoblocks.centromere.core.model.support.DataSetMetadata;
import org.springframework.util.Assert;

/**
 * @author woemler
 */
public class QueuedFile {
	
	private DataFileMetadata dataFileMetadata;
	private DataSetMetadata dataSetMetadata;
	private DataFileProcessor dataFileProcessor;

	public QueuedFile(DataFileMetadata dataFileMetadata,
			DataSetMetadata dataSetMetadata,
			DataFileProcessor dataFileProcessor) {
		Assert.notNull(dataFileMetadata);
		Assert.notNull(dataSetMetadata);
		Assert.notNull(dataFileProcessor);
		this.dataFileMetadata = dataFileMetadata;
		this.dataSetMetadata = dataSetMetadata;
		this.dataFileProcessor = dataFileProcessor;
	}

	public DataFileMetadata getDataFileMetadata() {
		return dataFileMetadata;
	}

	public void setDataFileMetadata(
			DataFileMetadata dataFileMetadata) {
		this.dataFileMetadata = dataFileMetadata;
	}

	public DataSetMetadata getDataSetMetadata() {
		return dataSetMetadata;
	}

	public void setDataSetMetadata(
			DataSetMetadata dataSetMetadata) {
		this.dataSetMetadata = dataSetMetadata;
	}

	public DataFileProcessor getDataFileProcessor() {
		return dataFileProcessor;
	}

	public void setDataFileProcessor(
			DataFileProcessor dataFileProcessor) {
		this.dataFileProcessor = dataFileProcessor;
	}
}
