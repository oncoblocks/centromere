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
import java.util.Set;

/**
 * @author woemler
 */

public class SampleDto<ID extends Serializable> implements Model<ID> {

	@Id private ID id;
	private ID subjectId;
	private ID dataSetId;
	private String name;
	private String type;
	private String tissue;
	private String histology;
	private String notes;
	private Set<ID> studyIds;

	public SampleDto() { }

	public SampleDto(ID id, ID subjectId, ID dataSetId, String name, String type, String tissue,
			String histology, String notes, Set<ID> studyIds) {
		this.id = id;
		this.subjectId = subjectId;
		this.dataSetId = dataSetId;
		this.name = name;
		this.type = type;
		this.tissue = tissue;
		this.histology = histology;
		this.notes = notes;
		this.studyIds = studyIds;
	}

	@Override public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public ID getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(ID subjectId) {
		this.subjectId = subjectId;
	}

	public ID getDataSetId() {
		return dataSetId;
	}

	public void setDataSetId(ID dataSetId) {
		this.dataSetId = dataSetId;
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

	public String getTissue() {
		return tissue;
	}

	public void setTissue(String tissue) {
		this.tissue = tissue;
	}

	public String getHistology() {
		return histology;
	}

	public void setHistology(String histology) {
		this.histology = histology;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Set<ID> getStudyIds() {
		return studyIds;
	}

	public void setStudyIds(Set<ID> studyIds) {
		this.studyIds = studyIds;
	}
}
