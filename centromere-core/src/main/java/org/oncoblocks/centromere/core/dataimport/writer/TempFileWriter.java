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

package org.oncoblocks.centromere.core.dataimport.writer;

import org.springframework.util.Assert;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes specific entity type records to temp files.
 * 
* @author woemler
*/
public abstract class TempFileWriter<T> implements EntityRecordWriter<T> {
	
	private File tempFile;
	private FileWriter writer;

	public TempFileWriter(File tempFilePath) {
		Assert.notNull(tempFilePath);
		this.tempFile = tempFilePath;
	}

	public void before(){
		try {
			writer = new FileWriter(tempFile);
		} catch (IOException e){
			e.printStackTrace();
			throw new TempFileWriterException(
					String.format("Unable to create temp file: %s", tempFile.getAbsolutePath()));
		}
	}

	public void after(){
		if (writer != null){
			try{
				writer.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
	protected FileWriter getFileWriter(){
		return this.writer;
	}
	
	public File getTempFile() {
		return tempFile;
	}
}
