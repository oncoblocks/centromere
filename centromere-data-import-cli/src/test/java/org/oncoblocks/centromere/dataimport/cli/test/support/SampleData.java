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

package org.oncoblocks.centromere.dataimport.cli.test.support;

import org.oncoblocks.centromere.core.model.Model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author woemler
 */
@Document(collection = "sample_data")
public class SampleData implements Model<String> {
	
	@Id private String id;
	private String dataFileId;
	private String name;
	private Double value;

	@Override public String getId() {
		return id;
	}

	public SampleData() { }

	public SampleData(String id, String dataFileId, String name, Double value) {
		this.id = id;
		this.dataFileId = dataFileId;
		this.name = name;
		this.value = value;
	}

	public SampleData(String name, Double value) {
		this.name = name;
		this.value = value;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDataFileId() {
		return dataFileId;
	}

	public void setDataFileId(String dataFileId) {
		this.dataFileId = dataFileId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	public static List<SampleData> createSampleData(){
		List<SampleData> dataList = new ArrayList<>();
		dataList.add(new SampleData("SampleA", 1.2));
		dataList.add(new SampleData("SampleB", 2.3));
		dataList.add(new SampleData("SampleC", 4.5));
		dataList.add(new SampleData("SampleD", 6.7));
		dataList.add(new SampleData("SampleE", 8.9));
		return dataList;
	}
	
}
