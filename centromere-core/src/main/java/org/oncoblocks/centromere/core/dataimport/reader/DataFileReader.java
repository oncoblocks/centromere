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

package org.oncoblocks.centromere.core.dataimport.reader;

import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/**
 * Reads data files and emits a specific entity type.
 * 
* @author woemler
*/
public abstract class DataFileReader<T> implements EntityRecordReader<T> {
	
	private String inputFilePath;
	private BufferedReader reader;
	
	public DataFileReader(String inputFilePath){
		Assert.notNull(inputFilePath);
		this.inputFilePath = inputFilePath;
	}
	
	@Override
	public void before(){
		try {
			reader = new BufferedReader(new java.io.FileReader(new File(inputFilePath)));
		} catch (IOException e){
			e.printStackTrace();
			throw new DataFileReaderException(String.format("Cannot read input file: %s", inputFilePath));
		}
	}

	@Override
	public void after(){
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
