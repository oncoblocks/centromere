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

import org.oncoblocks.centromere.core.repository.support.DataSetMetadataRepository;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for data import, which stores and retrieves references to {@link DataSetMetadata}
 *   for the current pipeline job.
 * 
 * @author woemler
 */
public class DataSetManager {
	
	private final Map<String,DataSetMetadata> dataSetMap = new HashMap<>();
	private final DataSetMetadataRepository<?, ?> repository;
	
	public DataSetManager(DataSetMetadataRepository<?, ?> repository){
		this.repository = repository;
		this.addDataSetMappings(repository.getAllMetadata());
	}

	/**
	 * Creates a new record in the data warehouse for the {@link DataSetMetadata} record and then adds
	 *   the mapping.
	 * 
	 * @param dataSet
	 */
	public void createDataSet(DataSetMetadata dataSet){
		repository.createFromMetdata(dataSet);
		this.addDataSetMapping(dataSet);
	}

	/**
	 * Adds or overwrites a {@link DataSetMetadata} record to the internal map.
	 * 
	 * @param dataSet
	 */
	public void addDataSetMapping(DataSetMetadata dataSet){
		Assert.notNull(dataSet);
		Assert.notNull(dataSet.getLabel());
		Assert.notNull(dataSet.getDataSetId());
		dataSetMap.put(dataSet.getLabel(), dataSet);
	}

	/**
	 * Adds or overwrites multiple {@link DataSetMetadata} records to the map.
	 * 
	 * @param dataSets
	 */
	public void addDataSetMappings(Iterable<DataSetMetadata> dataSets){
		Assert.notNull(dataSets);
		for (DataSetMetadata dataSet: dataSets){
			this.addDataSetMapping(dataSet);
		}
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
	
	public Object getDataSetId(String label){
		Object dataSetId = null;
		if (dataSetMap.containsKey(label)){
			DataSetMetadata dataSet = dataSetMap.get(label);
			dataSetId = dataSet.getDataSetId();
		}
		return dataSetId;
	}
	
}
