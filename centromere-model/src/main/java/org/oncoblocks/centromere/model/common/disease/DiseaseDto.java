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

package org.oncoblocks.centromere.model.common.disease;

import org.oncoblocks.centromere.model.support.AbstractModel;

import java.io.Serializable;

/**
 * @author woemler
 */

public class DiseaseDto<ID extends Serializable> extends AbstractModel<ID> {
	
	private String name;
	private String type;
	private String meshId;

	public DiseaseDto() { }

	public DiseaseDto(ID id, String name, String type, String meshId) {
		super(id);
		this.name = name;
		this.type = type;
		this.meshId = meshId;
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
