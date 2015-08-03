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

package org.oncoblocks.centromere.model.common;

import org.oncoblocks.centromere.core.model.Filterable;
import org.oncoblocks.centromere.core.model.Model;

import java.io.Serializable;

/**
 * @author woemler
 */

@Filterable
public abstract class Data<ID extends Serializable> implements Model<ID> {
	
	private ID id;
	private ID sampleId;
	private ID dataFileId;
	private ID geneId;

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public ID getSampleId() {
		return sampleId;
	}

	public void setSampleId(ID sampleId) {
		this.sampleId = sampleId;
	}

	public ID getDataFileId() {
		return dataFileId;
	}

	public void setDataFileId(ID dataFileId) {
		this.dataFileId = dataFileId;
	}

	public ID getGeneId() {
		return geneId;
	}

	public void setGeneId(ID geneId) {
		this.geneId = geneId;
	}
}
