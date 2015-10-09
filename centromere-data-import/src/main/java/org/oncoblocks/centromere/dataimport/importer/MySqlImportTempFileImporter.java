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

package org.oncoblocks.centromere.dataimport.importer;

import org.oncoblocks.centromere.dataimport.config.DataImportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author woemler
 */
public class MySqlImportTempFileImporter implements EntityRecordImporter {
	
	private DatabaseCredentials credentials;
	private boolean stopOnError = true;
	private boolean dropCollection = false;
	private String columns;

	final static Logger logger = LoggerFactory.getLogger(MySqlImportTempFileImporter.class);

	public MySqlImportTempFileImporter(DatabaseCredentials credentials, String columns){
		this.credentials = credentials;
		this.columns = columns;
	}
	
	public MySqlImportTempFileImporter(DatabaseCredentials credentials) {
		this(credentials, null);
	}

	@Override 
	public void importFile(String filePath) throws DataImportException {
		
		Process process;
		
		StringBuilder sb = new StringBuilder("mysqlimport --local ");
		if (!stopOnError) sb.append(" --force ");
		if (dropCollection) sb.append(" --delete ");
		if (columns != null) sb.append(String.format(" -c %s ", columns));
		sb.append(String.format(" -u %s ", credentials.getUsername()));
		sb.append(String.format(" -p%s ", credentials.getPassword()));
		sb.append(String.format(" -h %s ", credentials.getHost()));
		sb.append(String.format(" %s %s ", credentials.getDatabase(), filePath));
		String command = sb.toString();
		logger.debug(String.format("CENTROMERE: Executing mysqlimport with command: %s", command));
		
		String[] commands = new String[]{ "/bin/bash", "-c", command }; // TODO: Support for Windows and other shells
		try {
			
			logger.debug(String.format("CENTROMERE: Importing file to MySQL: %s", filePath));
			process = Runtime.getRuntime().exec(commands);
			
			
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(process.getInputStream()));
			StringBuilder outputBuilder = new StringBuilder();
			String line = stdIn.readLine();
			while (line != null){
				logger.debug(line);
				outputBuilder.append(line);
				line = stdIn.readLine();
			}
			stdIn.close();

			BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			StringBuilder errorBuilder = new StringBuilder();
			line = stdErr.readLine();
			while (line != null){
				logger.debug(line);
				errorBuilder.append(line);
				line = stdErr.readLine();
			}
			stdErr.close();

			process.waitFor();
			
			Integer exitValue = process.exitValue();
			if (exitValue != 0){
				throw new DataImportException(String.format("MongoImport failure for temp file: %s \n%s", 
						filePath, errorBuilder.toString()));
			}
			
		} catch (Exception e){
			e.printStackTrace();
			throw new TempFileImportException(String.format("Unable to import temp file: %s", filePath));
		}
		logger.debug(String.format("CENTROMERE: MongoImport complete: %s", filePath));
	}
	
	public MySqlImportTempFileImporter setStopOnError(boolean stopOnError) {
		this.stopOnError = stopOnError;
		return this;
	}

	public MySqlImportTempFileImporter setDropCollection(boolean dropCollection) {
		this.dropCollection = dropCollection;
		return this;
	}


}
