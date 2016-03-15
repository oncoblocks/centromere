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

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for {@link ImportJobRunner}, which stores and retrieves references to {@link DataSetMetadata}
 *   for the current pipeline job.
 * 
 * @author woemler
 */
public class DataSetManager {
	
	private final Map<String,DataSetMetadata> dataSetMap = new HashMap<>();
	
	public DataSetManager(){ }
	
	public DataSetManager(Iterable<DataSetMetadata> dataSets){
		this.addDataSets(dataSets);
	}

	/**
	 * Adds or overwrites a {@link DataSetMetadata} record to the internal map.
	 * 
	 * @param dataSet
	 */
	public void addDataSet(DataSetMetadata dataSet){
		Assert.notNull(dataSet);
		Assert.notNull(dataSet.getName());
		dataSetMap.put(dataSet.getName(), dataSet);
	}

	/**
	 * Adds or overwrites multiple {@link DataSetMetadata} records to the map.
	 * 
	 * @param dataSets
	 */
	public void addDataSets(Iterable<DataSetMetadata> dataSets){
		Assert.notNull(dataSets);
		for (DataSetMetadata dataSet: dataSets){
			this.addDataSet(dataSet);
		}
	}

	/**
	 * Retireves a {@link DataSetMetadata} record by name, as stored in the internal map.
	 * 
	 * @param name
	 * @return
	 */
	public DataSetMetadata getDataSetByName(String name){
		DataSetMetadata dataSet = null;
		if (dataSetMap.containsKey(name)){
			dataSet = dataSetMap.get(name);
		}
		return dataSet;
	}
	
}
