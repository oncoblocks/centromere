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

package org.oncoblocks.centromere.core.dataimport.component;

/**
 * Defines basic connection information necessary for accessing a database.  Designed for use with 
 *   {@link RecordImporter} implementations that use specific CLT database import utilities.
 *   TODO: Find better existing class to handle this, preferably without storing the password as plain text.
 * 
 * @author woemler
 */
public interface DatabaseCredentials {
	String getUsername();
	String getPassword();
	String getHost();
	String getPort();
	String getDatabase();
}
