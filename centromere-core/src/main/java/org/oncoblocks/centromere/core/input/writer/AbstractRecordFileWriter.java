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

package org.oncoblocks.centromere.core.input.writer;

import org.oncoblocks.centromere.core.input.DataImportException;
import org.oncoblocks.centromere.core.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @author woemler
 */
public abstract class AbstractRecordFileWriter<T extends Model<?>> implements RecordWriter<T> {
	
	private FileWriter writer;
	private static final Logger logger = LoggerFactory.getLogger(AbstractRecordFileWriter.class);

	@Override 
	public void doBefore(String destination) throws DataImportException {
		this.open(destination);
	}

	@Override 
	public void doAfter() throws DataImportException {
		this.close();
	}

	public void open(String outputFilePath) throws DataImportException{
		this.close();
		try {
			writer = new FileWriter(outputFilePath);
		} catch (IOException e){
			e.printStackTrace();
			throw new DataImportException(String.format("Cannot open output file: %s", outputFilePath));
		}
	}
	
	public void close(){
		try {
			writer.flush();
			writer.close();
		} catch (Exception e){
			logger.debug(e.getMessage());
		}
	}

	protected FileWriter getWriter() {
		return writer;
	}
}
