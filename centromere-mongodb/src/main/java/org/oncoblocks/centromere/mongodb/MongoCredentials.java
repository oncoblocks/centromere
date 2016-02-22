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

import org.oncoblocks.centromere.core.input.importer.DatabaseCredentials;

/**
 * @author woemler
 */
public class MongoCredentials implements DatabaseCredentials {

	private String username;
	private String password;
	private String host = "localhost";
	private String port = "27017";
	private String database;

	public MongoCredentials() {
	}

	public MongoCredentials(String username, String password, String host, String port,
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

	public MongoCredentials setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public MongoCredentials setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getHost() {
		return host;
	}

	public MongoCredentials setHost(String host) {
		this.host = host;
		return this;
	}

	public String getPort() {
		return port;
	}

	public MongoCredentials setPort(String port) {
		this.port = port;
		return this;
	}

	public String getDatabase() {
		return database;
	}

	public MongoCredentials setDatabase(String database) {
		this.database = database;
		return this;
	}
	
}
