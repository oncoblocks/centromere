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

import org.oncoblocks.centromere.core.dataimport.component.DataImportException;
import org.oncoblocks.centromere.core.dataimport.component.DataTypes;
import org.oncoblocks.centromere.core.dataimport.component.RecordProcessor;
import org.springframework.context.ApplicationContext;
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
	
	private final Map<String, RecordProcessor> dataTypeMap = new HashMap<>();
	private final ApplicationContext applicationContext;

	public DataTypeManager(ApplicationContext applicationContext) { 
		this.applicationContext = applicationContext;
		for (Map.Entry entry: applicationContext.getBeansWithAnnotation(DataTypes.class).entrySet()){
			Object obj = entry.getValue();
			if (obj instanceof RecordProcessor){
				RecordProcessor p = (RecordProcessor) obj;
				DataTypes dataTypes = p.getClass().getAnnotation(DataTypes.class);
				for (String t: dataTypes.value()){
					dataTypeMap.put(t, p);
				}
			}
		}
	}
	
	private RecordProcessor getProcessorByName(String processorBeanName) throws DataImportException{
		RecordProcessor processor = null;
		try {
			Class<? extends RecordProcessor> processorClass
					= (Class<? extends RecordProcessor>) Class.forName(processorBeanName);
			processor = applicationContext.getBean(processorClass);
		} catch (ClassNotFoundException e){
			processor = (RecordProcessor) applicationContext.getBean(processorBeanName);
		}
		if (processor == null){
			throw new DataImportException(String.format("RecordProcessor bean does not exist: %s",
					processorBeanName));
		}
		return processor;
	}

	/**
	 * Adds a data type mapping, using the data type name to find a 
	 * 
	 * @param dataTypeName
	 */
	public void addDataType(String dataTypeName, String processorBeanName) throws DataImportException {
		dataTypeMap.put(dataTypeName, this.getProcessorByName(processorBeanName));
	}
	
	public void addDataType(String dataTypeName, RecordProcessor processor){
		dataTypeMap.put(dataTypeName, processor);
	}

	/**
	 * Adds a data type mapping, overwriting existing ones with the same name.
	 * 
	 * @param dataType
	 */
	public void addDataType(DataType dataType) throws DataImportException{
		Assert.notNull(dataType);
		Assert.notNull(dataType.getName());
		Assert.notNull(dataType.getProcessor());
		Assert.notNull(dataType.getOptions());
		this.addDataType(dataType.getName(), dataType.getProcessor());
	}

	/**
	 * Adds multiple data type mappings at once.
	 * 
	 * @param dataTypes
	 */
	public void addDataTypes(Iterable<DataType> dataTypes) throws DataImportException{
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
	public RecordProcessor getProcessorByDataType(String name){
		if (!dataTypeMap.containsKey(name)) return null;
		return dataTypeMap.get(name);
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
