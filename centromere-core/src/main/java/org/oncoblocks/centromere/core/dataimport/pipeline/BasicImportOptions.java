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

package org.oncoblocks.centromere.core.dataimport.pipeline;

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
		if (options.containsKey("skipInvalidSamples")){
			this.skipInvalidSamples = Boolean.parseBoolean(options.get("skipInvalidSamples"));
		}
		if (options.containsKey("skipInvalidDataSets")){
			this.skipInvalidDataSets = Boolean.parseBoolean(options.get("skipInvalidDataSets"));
		}
		if (options.containsKey("skipInvalidGenes")){
			this.skipInvalidGenes = Boolean.parseBoolean(options.get("skipInvalidGenes"));
		}
		if (options.containsKey("skipInvalidMetadata")){
			this.skipInvalidMetadata = Boolean.parseBoolean(options.get("skipInvalidMetadata"));
		}
		if (options.containsKey("skipInvalidRecords")){
			this.skipInvalidRecords = Boolean.parseBoolean(options.get("skipInvalidRecords"));
		}
		if (options.containsKey("tempDirectoryPath")){
			this.tempDirectoryPath = options.get("tempDirectoryPath");
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

	public String getTempDirectoryPath() {
		return tempDirectoryPath;
	}
}
