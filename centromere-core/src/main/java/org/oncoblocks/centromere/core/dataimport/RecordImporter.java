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

package org.oncoblocks.centromere.core.dataimport;

/**
 * Data import component designed to take a temporary record file and import it directly into the 
 *   database, via a specified utility (eg. MySQLImport of MongoImport).  
 * 
 * @author woemler
 */
public interface RecordImporter {

	/**
	 * Runs the data import on the specified temp file.
	 * 
	 * @param filePath
	 * @throws DataImportException
	 */
	void importFile(String filePath) throws DataImportException;
}
