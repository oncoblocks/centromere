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
 * @author woemler
 */
public class DataSetManager {
	
	private final Map<String,DataSetMetadata> dataSetMap = new HashMap<>();
	
	public DataSetManager(){ }
	
	public DataSetManager(Iterable<DataSetMetadata> dataSets){
		this.addDataSets(dataSets);
	}
	
	public void addDataSet(DataSetMetadata dataSet){
		Assert.notNull(dataSet);
		Assert.notNull(dataSet.getName());
		dataSetMap.put(dataSet.getName(), dataSet);
	}
	
	public void addDataSets(Iterable<DataSetMetadata> dataSets){
		Assert.notNull(dataSets);
		for (DataSetMetadata dataSet: dataSets){
			this.addDataSet(dataSet);
		}
	}
	
	public DataSetMetadata getDataSetByName(String name){
		DataSetMetadata dataSet = null;
		if (dataSetMap.containsKey(name)){
			dataSet = dataSetMap.get(name);
		}
		return dataSet;
	}
	
}
