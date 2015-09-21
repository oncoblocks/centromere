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

package org.oncoblocks.centromere.core.dataimport.importer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author woemler
 */
public class MongoImportTempFileImporter implements EntityRecordImporter {
	
	private String host;
	private String db;
	private String collection;
	private String username;
	private String password;

	public MongoImportTempFileImporter(String host, String db, String collection,
			String username, String password) {
		this.host = host;
		this.db = db;
		this.collection = collection;
		this.username = username;
		this.password = password;
	}

	@Override 
	public void importFile(String filePath) {
		Process process;
		String command = String.format("mongoimport --quiet --username %s --password %s --host %s --db %s --collection %s --file %s", 
				username, password, host, db, collection, filePath);
		String[] commands = new String[]{ "/bin/bash", "-c", command };
		try {
			
			System.out.println(String.format("Importing file to MongoDB: %s", filePath));
			process = Runtime.getRuntime().exec(commands);
			process.waitFor();
			
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = stdIn.readLine();
			while (line != null){
				System.out.println("MongoImport output: " + line);
				line = stdIn.readLine();
			}

			BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getInputStream()));
			line = stdErr.readLine();
			while (line != null){
				System.out.println("MongoImport error output: " + line);
				line = stdErr.readLine();
			}

		} catch (Exception e){
			e.printStackTrace();
			throw new TempFileImportException(
					String.format("Unable to import temp file: %s", filePath));
		}
		System.out.println(String.format("MongoImport complete: %s", filePath));
	}
}
