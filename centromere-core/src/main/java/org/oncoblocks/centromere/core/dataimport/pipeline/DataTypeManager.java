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

import org.oncoblocks.centromere.core.dataimport.component.DataTypes;
import org.oncoblocks.centromere.core.dataimport.component.RecordProcessor;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple object that handles mapping of user-defined data types to {@link RecordProcessor} beans.
 * 
 * @author woemler
 */
public class DataTypeManager {
	
	private final Map<String, RecordProcessor> dataTypeMap;
	private final ApplicationContext applicationContext;
	
	public DataTypeManager(ApplicationContext applicationContext){
		this.applicationContext = applicationContext;
		this.dataTypeMap = new HashMap<>();
		for (Map.Entry entry: applicationContext.getBeansWithAnnotation(DataTypes.class).entrySet()){
			Object obj = entry.getValue();
			if (obj instanceof RecordProcessor){
				RecordProcessor p = (RecordProcessor) obj;
				DataTypes dataTypes = p.getClass().getAnnotation(DataTypes.class);
				for (String t: dataTypes.value()){
					this.dataTypeMap.put(t, p);
				}
			}
		}
	}
	
	public void addDataType(String label, String processorBeanName) {
		RecordProcessor processor = null;
		try {
			Class<? extends RecordProcessor> processorClass
					= (Class<? extends RecordProcessor>) Class.forName(processorBeanName);
			processor = applicationContext.getBean(processorClass);
		} catch (ClassNotFoundException e){
			processor = (RecordProcessor) applicationContext.getBean(processorBeanName);
		}
		this.dataTypeMap.put(label, processor);
	}

	/**
	 * Adds a data type mapping, using the data type name and {@link RecordProcessor} bean reference.
	 * 
	 * @param label
	 */
	public void addDataType(String label, RecordProcessor processor){
		dataTypeMap.put(label, processor);
	}

	/**
	 * Returns reference to a {@link RecordProcessor} bean class, if one of that type has been mapped
	 *   to a data set.  Returns null if no mapping exists.
	 * 
	 * @param label
	 * @return
	 */
	public RecordProcessor getProcessorByDataType(String label){
		if (!dataTypeMap.containsKey(label)) return null;
		return dataTypeMap.get(label);
	}

	/**
	 * Tests whether a data type mapping has been registered.
	 * 
	 * @param label
	 * @return
	 */
	public boolean isSupportedDataType(String label){
		return dataTypeMap.containsKey(label);
	}

	
}
