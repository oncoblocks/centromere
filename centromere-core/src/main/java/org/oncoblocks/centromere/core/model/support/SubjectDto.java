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
import org.oncoblocks.centromere.core.web.query.Attribute;
import org.oncoblocks.centromere.core.web.query.SourcedAlias;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

/**
 * @author woemler
 */

public class SubjectDto<ID extends Serializable> implements Model<ID> {

	@Id private ID id;
	private String name;
	private String species;
	private String gender;
	private String type;
	private String notes;
	private List<SourcedAlias> aliases;
	private List<Attribute> attributes;

	public SubjectDto() { }

	public SubjectDto(ID id, String name, String species, String gender, String type, String notes,
			List<SourcedAlias> aliases, List<Attribute> attributes) {
		this.id = id;
		this.name = name;
		this.species = species;
		this.gender = gender;
		this.type = type;
		this.notes = notes;
		this.aliases = aliases;
		this.attributes = attributes;
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

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<SourcedAlias> getAliases() {
		return aliases;
	}

	public void setAliases(List<SourcedAlias> aliases) {
		this.aliases = aliases;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
}
