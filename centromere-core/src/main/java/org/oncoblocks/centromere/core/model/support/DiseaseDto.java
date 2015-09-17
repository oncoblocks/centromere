/*
 * Copyright 2015 William Oemler, Blueprint Medicines
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

package org.oncoblocks.centromere.core.model.support;

import org.oncoblocks.centromere.core.model.Model;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @author woemler
 */

public class DiseaseDto<ID extends Serializable> implements Model<ID> {
	
	@Id private ID id;
	private String name;
	private String type;
	private String meshId;

	public DiseaseDto() { }

	public DiseaseDto(ID id, String name, String type, String meshId) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.meshId = meshId;
	}

	@Override public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMeshId() {
		return meshId;
	}

	public void setMeshId(String meshId) {
		this.meshId = meshId;
	}
}
