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
public class MongoImportTempFileImporter implements EntityRecordImporter {
	
	private MongoImportCredentials credentials;
	private String collection;
	private boolean stopOnError = true;
	private boolean upsertRecords = false;
	private boolean dropCollection = false;

	final static Logger logger = LoggerFactory.getLogger(MongoImportTempFileImporter.class);

	public MongoImportTempFileImporter(MongoImportCredentials credentials, String collection) {
		this.credentials = credentials;
		this.collection = collection;
	}

	@Override 
	public void importFile(String filePath) throws DataImportException {
		
		Process process;
		
		StringBuilder sb = new StringBuilder("mongoimport ");
		if (stopOnError) sb.append(" --stopOnError ");
		if (dropCollection) sb.append(" --drop ");
		if (upsertRecords) sb.append(" --upsert ");
		sb.append(String.format(" --username %s ", credentials.getUsername()));
		sb.append(String.format(" --password %s ", credentials.getPassword()));
		if (credentials.getHost().contains(":")){
			sb.append(String.format(" --host %s ", credentials.getHost()));
		} else if (credentials.getPort() != null){
			sb.append(String.format(" --host %s:%s ", credentials.getHost(), credentials.getPort()));
		} else {
			sb.append(String.format(" --host %s:27017 ", credentials.getHost()));
		}
		sb.append(String.format(" --db %s ", credentials.getDatabase()));
		sb.append(String.format(" --collection %s ", collection));
		sb.append(String.format(" --file %s ", filePath));
		
		String[] commands = new String[]{ "/bin/bash", "-c", sb.toString() }; // TODO: Support for Windows and other shells
		try {
			
			logger.debug(String.format("CENTROMERE: Importing file to MongoDB: %s", filePath));
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

	public static class MongoImportCredentials {
		
		private String username;
		private String password;
		private String host;
		private String port;
		private String database;

		public MongoImportCredentials() { }

		public MongoImportCredentials(String username, String password, String host, String port,
				String database) {
			this.username = username;
			this.password = password;
			this.host = host;
			this.port = port;
			this.database = database;
		}

		public String getUsername() {
			return username;
		}

		public MongoImportCredentials setUsername(String username) {
			this.username = username;
			return this;
		}

		public String getPassword() {
			return password;
		}

		public MongoImportCredentials setPassword(String password) {
			this.password = password;
			return this;
		}

		public String getHost() {
			return host;
		}

		public MongoImportCredentials setHost(String host) {
			this.host = host;
			return this;
		}

		public String getPort() {
			return port;
		}

		public MongoImportCredentials setPort(String port) {
			this.port = port;
			return this;
		}

		public String getDatabase() {
			return database;
		}

		public MongoImportCredentials setDatabase(String database) {
			this.database = database;
			return this;
		}
		
		
		
	}
	
	
}
