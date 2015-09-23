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

/**
 * @author woemler
 */
public class DataImportOptions {

	private String tempFileDirectory;
	private String logFilePath;
	private boolean failOnMissingFile = true;
	private boolean failOnExistingFile = true;

	public DataImportOptions() { }

	public String getTempFileDirectory() {
		return tempFileDirectory;
	}

	public DataImportOptions setTempFileDirectory(String tempFileDirectory) {
		this.tempFileDirectory = tempFileDirectory;
		return this;
	}

	public String getLogFilePath() {
		return logFilePath;
	}

	public DataImportOptions setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
		return this;
	}

	public boolean isFailOnMissingFile() {
		return failOnMissingFile;
	}

	public DataImportOptions setFailOnMissingFile(boolean failOnMissingFile) {
		this.failOnMissingFile = failOnMissingFile;
		return this;
	}

	public boolean isFailOnExistingFile() {
		return failOnExistingFile;
	}

	public DataImportOptions setFailOnExistingFile(boolean failOnExistingFile) {
		this.failOnExistingFile = failOnExistingFile;
		return this;
	}

}
