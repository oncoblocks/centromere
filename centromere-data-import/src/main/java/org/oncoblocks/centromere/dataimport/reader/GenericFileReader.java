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

package org.oncoblocks.centromere.dataimport.reader;

import org.oncoblocks.centromere.core.input.DataImportException;
import org.oncoblocks.centromere.core.input.reader.RecordReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/**
 * @author woemler
 */
public abstract class GenericFileReader<T> implements RecordReader<T> {

	private BufferedReader reader;
	private final String filePath;
	
	public GenericFileReader(String filePath){ 
		this.filePath = filePath;
	}

	public void open() throws DataImportException{
		try {
			reader = new BufferedReader(new java.io.FileReader(new File(filePath)));
		} catch (IOException e){
			e.printStackTrace();
			throw new EntityReaderException(String.format("Cannot read input file: %s", filePath));
		}
	}

	public void close(){
		if (reader != null){
			try {
				reader.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	public BufferedReader getReader(){
		return this.reader;
	}
	
}
