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

import org.oncoblocks.centromere.core.dataimport.component.RecordProcessor;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple object that handles mapping of user-defined data types to {@link RecordProcessor} beans,
 *   as defined by {@link DataType} instances.
 * 
 * @author woemler
 */
public class DataTypeManager {
	
	private final Map<String, DataType> dataTypeMap = new HashMap<>();

	public DataTypeManager() { }
	
	public DataTypeManager(Iterable<DataType> dataTypes){
		this.addDataTypes(dataTypes);
	}
	
	public DataTypeManager(DataType... dataTypes){
		for (DataType dataType: dataTypes){
			this.addDataType(dataType);
		}
	}

	/**
	 * Adds a data type mapping, overwriting existing ones with the same name.
	 * 
	 * @param dataType
	 */
	public void addDataType(DataType dataType){
		Assert.notNull(dataType);
		Assert.notNull(dataType.getName());
		Assert.notNull(dataType.getProcessor());
		Assert.notNull(dataType.getOptions());
		dataTypeMap.put(dataType.getName(), dataType);
	}

	/**
	 * Adds multiple data type mappings at once.
	 * 
	 * @param dataTypes
	 */
	public void addDataTypes(Iterable<DataType> dataTypes){
		for (DataType dataType: dataTypes){
			this.addDataType(dataType);
		}
	}

	/**
	 * Returns reference to a {@link RecordProcessor} bean class, if one of that type has been mapped
	 *   to a data set.  Returns null if no mapping exists.
	 * 
	 * @param name
	 * @return
	 */
	public String getProcessorByDataType(String name){
		if (!dataTypeMap.containsKey(name)) return null;
		DataType dataType = dataTypeMap.get(name);
		return dataType.getProcessor();
	}

	/**
	 * Tests whether a data type mapping has been registered.
	 * 
	 * @param name
	 * @return
	 */
	public boolean isSupportedDataType(String name){
		return dataTypeMap.containsKey(name);
	}
	
}
