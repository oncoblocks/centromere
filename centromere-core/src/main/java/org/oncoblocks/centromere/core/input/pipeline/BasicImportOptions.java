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

package org.oncoblocks.centromere.core.input.pipeline;

/**
 * @author woemler
 */
public class BasicImportOptions implements ImportOptions {
	
	private String tempDirectoryPath;
	private boolean failOnInvalidRecord = true;
	private boolean failOnDataImportException = true;
	private boolean failOnMissingFile = true;

	public String getTempDirectoryPath() {
		return tempDirectoryPath;
	}

	public void setTempDirectoryPath(String tempDirectoryPath) {
		this.tempDirectoryPath = tempDirectoryPath;
	}

	public boolean failOnInvalidRecord() {
		return failOnInvalidRecord;
	}

	public void setFailOnInvalidRecord(boolean failOnInvalidRecord) {
		this.failOnInvalidRecord = failOnInvalidRecord;
	}

	public boolean failOnDataImportException() {
		return failOnDataImportException;
	}

	public void setFailOnDataImportException(boolean failOnDataImportException) {
		this.failOnDataImportException = failOnDataImportException;
	}

	public boolean failOnMissingFile() {
		return failOnMissingFile;
	}

	public void setFailOnMissingFile(boolean failOnMissingFile) {
		this.failOnMissingFile = failOnMissingFile;
	}
}
