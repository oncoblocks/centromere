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

import org.oncoblocks.centromere.core.dataimport.component.DataTypes;
import org.oncoblocks.centromere.core.dataimport.component.RecordProcessor;
import org.oncoblocks.centromere.core.model.support.DataSetMetadata;
import org.oncoblocks.centromere.core.repository.support.DataFileMetadataRepository;
import org.oncoblocks.centromere.core.repository.support.DataSetMetadataRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Configures required component classes for command line data import tool.
 * 
 * @author woemler
 */
public class DataImportManager {
	
	private ApplicationContext applicationContext;
	private DataSetMetadataRepository dataSetRepository;
	private DataFileMetadataRepository dataFileRepository;
	
	private Map<String, RecordProcessor> dataTypeMap = new HashMap<>(); 
	private Map<String, DataSetMetadata> dataSetMap = new HashMap<>();
	
	public DataImportManager(ApplicationContext applicationContext,
			DataSetMetadataRepository dataSetRepository, 
			DataFileMetadataRepository dataFileRepository){
		this.applicationContext = applicationContext;
		this.dataSetRepository = dataSetRepository;
		this.dataFileRepository = dataFileRepository;
		dataTypeMap = initializeDataTypeMap();
		dataSetMap = initializeDataSetMap();
	}
	
	/* Data Set and File Management */
	
	/**
	 * Builds the {@code dataSetMap} that associates data set labels and their corresponding metadata 
	 *   and database IDs, by pulling the records from the database.
	 * 
	 * @return
	 */
	private Map<String, DataSetMetadata> initializeDataSetMap(){
		Map<String, DataSetMetadata> map = new HashMap<>();
		for (DataSetMetadata metadata: (Iterable<DataSetMetadata>) dataSetRepository.findAll()){
			map.put(metadata.getLabel(), metadata);
		}
		return map;
	}


	/**
	 * Adds or overwrites a {@link DataSetMetadata} record to the internal map.
	 *
	 * @param dataSet
	 */
	public void addDataSetMapping(DataSetMetadata dataSet){
		Assert.notNull(dataSet);
		Assert.notNull(dataSet.getLabel());
		dataSetMap.put(dataSet.getLabel(), dataSet);
	}

	/**
	 * Retireves a {@link DataSetMetadata} record by name, as stored in the internal map.
	 *
	 * @param label
	 * @return
	 */
	public DataSetMetadata getDataSet(String label){
		DataSetMetadata dataSet = null;
		if (dataSetMap.containsKey(label)){
			dataSet = dataSetMap.get(label);
		}
		return dataSet;
	}
	
	/* Data Type Management */

	/**
	 * Builds the {@code dataTypeMap} by inspecting registered {@link RecordProcessor} beans and their
	 *   {@link DataTypes} annotations.
	 */
	private Map<String, RecordProcessor> initializeDataTypeMap(){
		Map<String, RecordProcessor> map = new HashMap<>();
		for (Map.Entry entry: applicationContext.getBeansWithAnnotation(DataTypes.class).entrySet()){
			Object obj = entry.getValue();
			if (obj instanceof RecordProcessor){
				RecordProcessor p = (RecordProcessor) obj;
				DataTypes dataTypes = p.getClass().getAnnotation(DataTypes.class);
				for (String t: dataTypes.value()){
					map.put(t, p);
				}
			}
		}
		return map;
	}

	/**
	 * Adds a data type mapping, using the name of a {@link RecordProcessor} class or instance name to
	 *   locate a usable bean.
	 * 
	 * @param label
	 * @param beanReference
	 */
	public void addDataTypeMapping(String label, String beanReference){
		RecordProcessor processor = null;
		try {
			Class<? extends RecordProcessor> processorClass
					= (Class<? extends RecordProcessor>) Class.forName(beanReference);
			processor = applicationContext.getBean(processorClass);
		} catch (ClassNotFoundException e){
			processor = (RecordProcessor) applicationContext.getBean(beanReference);
		}
		addDataTypeMapping(label, processor);
	}

	/**
	 * Adds a data type mapping, using the data type name and {@link RecordProcessor} bean reference.
	 *
	 * @param label
	 */
	public void addDataTypeMapping(String label, RecordProcessor processor){
		dataTypeMap.put(label, processor);
	}

	/**
	 * Returns reference to a {@link RecordProcessor} bean class, if one of that type has been mapped
	 *   to a data set.  Returns null if no mapping exists.
	 *
	 * @param label
	 * @return
	 */
	public RecordProcessor getDataTypeProcessor(String label){
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
		return dataTypeMap.containsKey(label) && dataTypeMap.get(label) != null;
	}

	public Map<String, RecordProcessor> getDataTypeMap() {
		return dataTypeMap;
	}

	public void setDataTypeMap(
			Map<String, RecordProcessor> dataTypeMap) {
		this.dataTypeMap = dataTypeMap;
	}

	public Map<String, DataSetMetadata> getDataSetMap() {
		return dataSetMap;
	}

	public void setDataSetMap(
			Map<String, DataSetMetadata> dataSetMap) {
		this.dataSetMap = dataSetMap;
	}

	public DataSetMetadataRepository getDataSetRepository() {
		return dataSetRepository;
	}

	public DataFileMetadataRepository getDataFileRepository() {
		return dataFileRepository;
	}
}
