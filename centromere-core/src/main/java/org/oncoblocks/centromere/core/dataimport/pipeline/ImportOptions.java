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
public class ImportOptions {

	private Map<String,String> options = new HashMap<>();
	
	public ImportOptions(){ }

	public ImportOptions(Map<String, String> options) {
		this.options = options;
	}
	
	public Map<String,String> getOptionsMap(){
		return options;
	}
	
	public String getString(String name){
		return options.containsKey(name) ? options.get(name) : null;
	}
	
	public boolean getBoolean(String name){
		if (!options.containsKey(name)) throw new NullPointerException("Invalid option: " + name);
		return Boolean.parseBoolean(options.get(name));
	}
	
	public void setOption(String name, String value){
		options.put(name, value);
	}
	
	public void setOptions(Map<String,String> options){
		this.options.putAll(options);
	}
	
	public boolean hasOption(String name){
		return options.containsKey(name);
	}

}
