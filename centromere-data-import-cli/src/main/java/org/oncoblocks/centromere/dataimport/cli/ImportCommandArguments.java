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

package org.oncoblocks.centromere.dataimport.cli;

import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.oncoblocks.centromere.core.dataimport.BasicImportOptions;
import org.oncoblocks.centromere.core.model.support.BasicDataSetMetadata;
import org.oncoblocks.centromere.core.model.support.DataSetMetadata;

import java.io.IOException;

/**
 * Command line arguments for the {@code import} command.  Includes arguments for specifying the
 *   input file, associated data type and data sets, and flags for modifying import behavior.  
 * 
 * @author woemler
 */
public class ImportCommandArguments {
	
	@Parameter(names = { "-i", "--input" }, required = true, description = "")
	private String inputFilePath;
	
	@Parameter(names = { "-t", "--data-type" }, required = true, description = "")
	private String dataType;
	
	@Parameter(names = { "-d", "--data-set" }, description = "")
	private String dataSet;
	
	@Parameter(names = { "-T", "--temp-dir" }, description = "")
	private String tempFilePath = "/tmp";
	
	@Parameter(names = {"--skip-invalid-records"}, description = "")
	private boolean skipInvalidRecords = false;

	@Parameter(names = {"--skip-invalid-genes"}, description = "")
	private boolean skipInvalidGenes = false;

	@Parameter(names = {"--skip-invalid-samples"}, description = "")
	private boolean skipInvalidSamples = false;

	@Parameter(names = {"--skip-invalid-data-sets"}, description = "")
	private boolean skipInvalidDataSets = false;

	public String getInputFilePath() {
		return inputFilePath;
	}

	public void setInputFilePath(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	public String getTempFilePath() {
		return tempFilePath;
	}

	public void setTempFilePath(String tempFilePath) {
		this.tempFilePath = tempFilePath;
	}

	public boolean isSkipInvalidRecords() {
		return skipInvalidRecords;
	}

	public void setSkipInvalidRecords(boolean skipInvalidRecords) {
		this.skipInvalidRecords = skipInvalidRecords;
	}

	public boolean isSkipInvalidGenes() {
		return skipInvalidGenes;
	}

	public void setSkipInvalidGenes(boolean skipInvalidGenes) {
		this.skipInvalidGenes = skipInvalidGenes;
	}

	public boolean isSkipInvalidSamples() {
		return skipInvalidSamples;
	}

	public void setSkipInvalidSamples(boolean skipInvalidSamples) {
		this.skipInvalidSamples = skipInvalidSamples;
	}

	public boolean isSkipInvalidDataSets() {
		return skipInvalidDataSets;
	}

	public void setSkipInvalidDataSets(boolean skipInvalidDataSets) {
		this.skipInvalidDataSets = skipInvalidDataSets;
	}

	/**
	 * Uses user-inputted and default flag values to create a {@link org.oncoblocks.centromere.core.dataimport.ImportOptions}
	 *   instance that can be passed to {@link org.oncoblocks.centromere.core.dataimport.RecordProcessor}
	 *   instances to modify their behavior.
	 * 
	 * @return
	 */
	public BasicImportOptions getImportOptions(){
		BasicImportOptions options = new BasicImportOptions();
		options.setSkipInvalidDataSets(this.skipInvalidDataSets);
		options.setSkipInvalidGenes(this.skipInvalidGenes);
		options.setSkipInvalidRecords(this.skipInvalidRecords);
		options.setSkipInvalidSamples(this.skipInvalidSamples);
		options.setTempDirectoryPath(this.tempFilePath);
		return options;
	}

	/**
	 * Attempts to parse the inputted data set argument into a {@link DataSetMetadata} object.
	 * 
	 * @return
	 */
	public DataSetMetadata getDataSetMetadata(){
		ObjectMapper mapper = new ObjectMapper();
		DataSetMetadata metadata = null;
		if (this.dataSet != null && !"".equals(this.dataSet)){
			try {
				metadata = mapper.readValue(this.dataSet, BasicDataSetMetadata.class);
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		return metadata;
	}

	@Override 
	public String toString() {
		return "ImportCommandArguments{" +
				"inputFilePath='" + inputFilePath + '\'' +
				", dataType='" + dataType + '\'' +
				", dataSet='" + dataSet + '\'' +
				", tempFilePath='" + tempFilePath + '\'' +
				", skipInvalidRecords=" + skipInvalidRecords +
				", skipInvalidGenes=" + skipInvalidGenes +
				", skipInvalidSamples=" + skipInvalidSamples +
				", skipInvalidDataSets=" + skipInvalidDataSets +
				'}';
	}
}
