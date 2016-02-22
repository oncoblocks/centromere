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

package org.oncoblocks.centromere.dataimport.writer;

import org.oncoblocks.centromere.core.model.Model;
import org.oncoblocks.centromere.core.input.DataImportException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

/**
 * Writes specific entity type records to temp files.
 * 
* @author woemler
*/
public abstract class TempFileWriter<T extends Model<ID>, ID extends Serializable> 
		implements EntityRecordWriter<T, ID> {
	
	private FileWriter writer;

	public TempFileWriter() { }

	@Override
	public void open(String tempFilePath) throws DataImportException{
		this.close();
		try {
			writer = new FileWriter(tempFilePath);
		} catch (IOException e){
			e.printStackTrace();
			throw new TempFileWriterException(
					String.format("Unable to create temp file: %s", tempFilePath));
		}
	}

	@Override
	public void close(){
		if (writer != null){
			try{
				writer.flush();
				writer.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
	public FileWriter getFileWriter(){
		return this.writer;
	}
	
}
