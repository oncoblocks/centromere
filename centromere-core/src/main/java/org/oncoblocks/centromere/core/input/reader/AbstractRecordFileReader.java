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

package org.oncoblocks.centromere.core.input.reader;

import org.oncoblocks.centromere.core.input.DataImportException;
import org.oncoblocks.centromere.core.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author woemler
 */
public abstract class AbstractRecordFileReader<T extends Model<?>> implements RecordReader<T> {
	
	private BufferedReader reader;
	private static final Logger logger = LoggerFactory.getLogger(AbstractRecordFileReader.class);

	@Override public void doBefore(String input) throws DataImportException{
		this.close();
		this.open(input);
	}

	@Override public void doAfter() {
		this.close();
	}

	public void open(String inputFilePath) throws DataImportException{
		File file = new File(inputFilePath);
		if (!file.canRead() || !file.isFile()){
			try {
				file = new File(ClassLoader.getSystemClassLoader().getResource(inputFilePath).getPath());
			} catch (NullPointerException e){
				throw new DataImportException(String.format("Cannot locate input file: %s", inputFilePath));
			}
		}
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (IOException e){
			e.printStackTrace();
			throw new DataImportException(String.format("Cannot read input file: %s", inputFilePath));
		}
	}
	
	public void close(){
		if (reader != null){
			try {
				reader.close();
			} catch (IOException e){
				logger.debug(e.getMessage());
			}
		}
	}

	protected BufferedReader getReader() {
		return reader;
	}
	
}
