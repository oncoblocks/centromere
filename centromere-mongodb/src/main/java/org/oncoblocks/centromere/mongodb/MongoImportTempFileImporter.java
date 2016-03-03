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

package org.oncoblocks.centromere.mongodb;

import org.oncoblocks.centromere.core.dataimport.component.DataImportException;
import org.oncoblocks.centromere.core.dataimport.component.DatabaseCredentials;
import org.oncoblocks.centromere.core.dataimport.component.RecordImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author woemler
 */
public class MongoImportTempFileImporter implements RecordImporter {

	private final DatabaseCredentials credentials;
	private final String collection;
	private boolean stopOnError = true;
	private boolean upsertRecords = false;
	private boolean dropCollection = false;

	private final static Logger logger = LoggerFactory.getLogger(MongoImportTempFileImporter.class);

	public MongoImportTempFileImporter(DatabaseCredentials credentials, String collection) {
		this.credentials = credentials;
		this.collection = collection;
	}

	@Override
	public void importFile(String filePath) throws DataImportException {

		Process process;
		String[] commands = new String[]{ "/bin/bash", "-c", buildImportCommand(filePath) }; // TODO: Support for Windows and other shells
		try {

			logger.debug(String.format("[CENTROMERE] Importing file to MongoDB: %s", filePath));
			for (String cmd: commands) {
				logger.debug(cmd);
			}
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
			throw new DataImportException(String.format("Unable to import temp file: %s", filePath));
		}
		logger.debug(String.format("CENTROMERE: MongoImport complete: %s", filePath));
	}
	
	private String buildImportCommand(String filePath){
		StringBuilder sb = new StringBuilder("mongoimport ");
		if (stopOnError) sb.append(" --stopOnError ");
		if (dropCollection) sb.append(" --drop ");
		if (upsertRecords) sb.append(" --upsert ");
		if (credentials.getUsername() != null) {
			sb.append(String.format(" --username %s ", credentials.getUsername()));
		}
		if (credentials.getPassword() != null){
			sb.append(String.format(" --password %s ", credentials.getPassword()));
		}
		if (credentials.getHost() != null) {
			if (credentials.getHost().contains(":")) {
				sb.append(String.format(" --host %s ", credentials.getHost()));
			} else if (credentials.getPort() != null) {
				sb.append(String.format(" --host %s:%s ", credentials.getHost(), credentials.getPort()));
			} else {
				sb.append(String.format(" --host %s:27017 ", credentials.getHost()));
			}
		}
		sb.append(String.format(" --db %s ", credentials.getDatabase()));
		sb.append(String.format(" --collection %s ", collection));
		sb.append(String.format(" --file %s ", filePath));
		return sb.toString();
	}

	public MongoImportTempFileImporter setStopOnError(boolean stopOnError) {
		this.stopOnError = stopOnError;
		return this;
	}

	public MongoImportTempFileImporter setUpsertRecords(boolean upsertRecords) {
		this.upsertRecords = upsertRecords;
		return this;
	}

	public MongoImportTempFileImporter setDropCollection(boolean dropCollection) {
		this.dropCollection = dropCollection;
		return this;
	}
	
}
