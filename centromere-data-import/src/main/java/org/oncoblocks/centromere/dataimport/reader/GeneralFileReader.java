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

package org.oncoblocks.centromere.dataimport.reader;

import org.oncoblocks.centromere.core.model.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author woemler
 */
public abstract class GeneralFileReader<T extends Model<ID>, ID extends Serializable>
		implements EntityRecordReader<T, ID> {

	private BufferedReader reader;
	private ID dataSetId;
	private ID dataFileId;
	private boolean failOnUnknownSample = true;

	public GeneralFileReader(){ }

	@Override
	public void open(String inputFilePath){
		this.close();
		try {
			reader = new BufferedReader(new java.io.FileReader(new File(inputFilePath)));
		} catch (IOException e){
			e.printStackTrace();
			throw new DataFileReaderException(String.format("Cannot read input file: %s", inputFilePath));
		}
	}

	@Override
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

	public void setDataSetId(ID dataSetId) {
		this.dataSetId = dataSetId;
	}

	public void setDataFileId(ID dataFileId) {
		this.dataFileId = dataFileId;
	}

	public ID getDataSetId() {
		return dataSetId;
	}

	public ID getDataFileId() {
		return dataFileId;
	}

	public boolean isFailOnUnknownSample() {
		return failOnUnknownSample;
	}

	public GeneralFileReader setFailOnUnknownSample(boolean failOnUnknownSample) {
		this.failOnUnknownSample = failOnUnknownSample;
		return this;
	}

}
