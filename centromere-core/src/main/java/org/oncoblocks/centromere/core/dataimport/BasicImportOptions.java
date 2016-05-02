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

import java.util.HashMap;
import java.util.Map;

/**
 * Captures basic parameters required for a data import pipeline.
 * 
 * @author woemler
 */
public class BasicImportOptions implements ImportOptions {

	private Map<String,String> options = new HashMap<>();
	private boolean skipInvalidSamples = false;
	private boolean skipInvalidDataSets = false;
	private boolean skipInvalidGenes = false;
	private boolean skipInvalidMetadata = false;
	private boolean skipInvalidRecords = false;
	private boolean skipExistingFiles = false;
	private String tempDirectoryPath = "/tmp";
	
	public BasicImportOptions(){ }

	public BasicImportOptions(Map<String, String> options) {
		this.options = options;
		this.setDefaultParameters();
	}
	
	public BasicImportOptions(ImportOptions importOptions){
		this(importOptions.getOptions());
	}
	
	private void setDefaultParameters(){
		if (options.containsKey(ImportOptions.SKIP_INVALID_SAMPLES)){
			this.skipInvalidSamples = Boolean.parseBoolean(options.get(ImportOptions.SKIP_INVALID_SAMPLES));
		}
		if (options.containsKey(ImportOptions.SKIP_INVALID_DATA_SETS)){
			this.skipInvalidDataSets = Boolean.parseBoolean(options.get(ImportOptions.SKIP_INVALID_DATA_SETS));
		}
		if (options.containsKey(ImportOptions.SKIP_INVALID_GENES)){
			this.skipInvalidGenes = Boolean.parseBoolean(options.get(ImportOptions.SKIP_INVALID_GENES));
		}
		if (options.containsKey(ImportOptions.SKIP_INVALID_METADATA)){
			this.skipInvalidMetadata = Boolean.parseBoolean(options.get(ImportOptions.SKIP_INVALID_METADATA));
		}
		if (options.containsKey(ImportOptions.SKIP_INVALID_RECORDS)){
			this.skipInvalidRecords = Boolean.parseBoolean(options.get(ImportOptions.SKIP_INVALID_RECORDS));
		}
		if (options.containsKey(ImportOptions.SKIP_EXISTING_FILES)){
			this.skipExistingFiles = Boolean.parseBoolean(options.get(ImportOptions.SKIP_EXISTING_FILES));
		}
		if (options.containsKey(ImportOptions.TEMP_DIRECTORY_PATH)){
			this.tempDirectoryPath = options.get(ImportOptions.TEMP_DIRECTORY_PATH);
		}
	}
	
	public Map<String,String> getOptions(){
		return options;
	}
	
	public String getOption(String name){
		return options.containsKey(name) ? options.get(name) : null;
	}
	
	public boolean getBoolean(String name){
		if (!options.containsKey(name)) throw new NullPointerException("Invalid option: " + name);
		return Boolean.parseBoolean(options.get(name));
	}
	
	public void setOption(String name, String value){
		options.put(name, value);
		this.setDefaultParameters();
	}
	
	public void setOptions(Map<String,String> options){
		this.options.putAll(options);
		this.setDefaultParameters();
	}
	
	public boolean hasOption(String name){
		return options.containsKey(name);
	}

	public boolean isSkipInvalidSamples() {
		return skipInvalidSamples;
	}

	public boolean isSkipInvalidDataSets() {
		return skipInvalidDataSets;
	}

	public boolean isSkipInvalidGenes() {
		return skipInvalidGenes;
	}

	public boolean isSkipInvalidMetadata() {
		return skipInvalidMetadata;
	}

	public boolean isSkipInvalidRecords() {
		return skipInvalidRecords;
	}

	public void setSkipInvalidSamples(boolean skipInvalidSamples) {
		this.skipInvalidSamples = skipInvalidSamples;
		this.options.put(ImportOptions.SKIP_INVALID_SAMPLES, Boolean.toString(skipInvalidSamples));
	}

	public void setSkipInvalidDataSets(boolean skipInvalidDataSets) {
		this.skipInvalidDataSets = skipInvalidDataSets;
		this.options.put(ImportOptions.SKIP_INVALID_DATA_SETS, Boolean.toString(skipInvalidDataSets));
	}

	public void setSkipInvalidGenes(boolean skipInvalidGenes) {
		this.skipInvalidGenes = skipInvalidGenes;
		this.options.put(ImportOptions.SKIP_INVALID_GENES, Boolean.toString(skipInvalidGenes));
	}

	public void setSkipInvalidMetadata(boolean skipInvalidMetadata) {
		this.skipInvalidMetadata = skipInvalidMetadata;
		this.options.put(ImportOptions.SKIP_INVALID_METADATA, Boolean.toString(skipInvalidMetadata));
	}

	public void setSkipInvalidRecords(boolean skipInvalidRecords) {
		this.skipInvalidRecords = skipInvalidRecords;
		this.options.put(ImportOptions.SKIP_INVALID_RECORDS, Boolean.toString(skipInvalidRecords));
	}

	public void setTempDirectoryPath(String tempDirectoryPath) {
		this.tempDirectoryPath = tempDirectoryPath;
		this.options.put(ImportOptions.TEMP_DIRECTORY_PATH, tempDirectoryPath);
	}

	public boolean isSkipExistingFiles() {
		return skipExistingFiles;
	}

	public void setSkipExistingFiles(boolean skipExistingFiles) {
		this.skipExistingFiles = skipExistingFiles;
		this.options.put(ImportOptions.SKIP_EXISTING_FILES, Boolean.toString(skipExistingFiles));
	}

	public String getTempDirectoryPath() {
		return tempDirectoryPath;
	}

	@Override 
	public String toString() {
		return "BasicImportOptions{" +
				"options=" + options +
				", skipInvalidSamples=" + skipInvalidSamples +
				", skipInvalidDataSets=" + skipInvalidDataSets +
				", skipInvalidGenes=" + skipInvalidGenes +
				", skipInvalidMetadata=" + skipInvalidMetadata +
				", skipInvalidRecords=" + skipInvalidRecords +
				", skipExistingFiles=" + skipExistingFiles +
				", tempDirectoryPath='" + tempDirectoryPath + '\'' +
				'}';
	}
}
