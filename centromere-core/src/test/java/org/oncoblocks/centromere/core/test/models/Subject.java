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

package org.oncoblocks.centromere.core.test.models;

import org.oncoblocks.centromere.core.model.*;
import org.oncoblocks.centromere.core.repository.sqlbuilder.ComplexTableDescription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author woemler
 */

@Filterable
public class Subject implements Model<Long>, SourcedAliases, Attributes {
	
	@Queryable private Long subjectId;
	@Queryable private String name;
	private String species;
	private String gender;
	
	@Queryable({
			@Parameter(value = "aliasName", type = String.class)
	})
	private List<SourcedAlias> aliases;

	@Queryable({
			@Parameter(value = "attributeName", type = String.class),
			@Parameter(value = "attributeValue", type = String.class)
	})
	private List<Attribute> attributes;
	private String notes;

	public Subject() { }

	public Subject(Long subjectId, String name, String species, String gender,
			List<SourcedAlias> aliases,
			List<Attribute> attributes, String notes) {
		this.subjectId = subjectId;
		this.name = name;
		this.species = species;
		this.gender = gender;
		this.aliases = aliases;
		this.attributes = attributes;
		this.notes = notes;
	}

	public void setAttributeName(String attributeName) {
		if (attributes == null) attributes = new ArrayList<>();
		attributes.add(new Attribute(attributeName, null));
	}

	public void setAttributeValue(String attributeValue) {
		if (attributes == null) attributes = new ArrayList<>();
		attributes.add(new Attribute(null, attributeValue));
	}

	public void setAttribute(Attribute attribute) {
		if (attributes == null) attributes = new ArrayList<>();
		attributes.add(attribute);
	}

	public boolean hasAttribute(String name) {
		for (Attribute attribute: attributes){
			if (attribute.getName().equals(name)) return true;
		}
		return false;
	}

	public Long getId() {
		return subjectId;
	}

	public void setAliasName(String aliasName) {
		if (aliases == null) aliases = new ArrayList<>();
		aliases.add(new SourcedAlias(null, aliasName));
	}

	public void setAliasSource(String aliasSource) {
		if (aliases == null) aliases = new ArrayList<>();
		aliases.add(new SourcedAlias(aliasSource, null));
	}

	public void setAlias(SourcedAlias alias) {
		if (aliases == null) aliases = new ArrayList<>();
		aliases.add(alias);
	}

	public boolean hasAlias(String name) {
		for (SourcedAlias alias: aliases){
			if (alias.getName().equals(name)) return true;
		}
		return false;
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
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

	public List<SourcedAlias> getAliases() {
		return aliases;
	}

	public void setAliases(Collection<SourcedAlias> aliases) {
		this.aliases = (List<SourcedAlias>) aliases;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Collection<Attribute> attributes) {
		this.attributes = (List<Attribute>) attributes;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public static ComplexTableDescription getSubjectTableDescription(){
		return new ComplexTableDescription(
				"subjects",
				Arrays.asList(new String[]{ "subjects.subject_id" }),
				"subjects.*, GROUP_CONCAT(CONCAT(subject_aliases.source, ':', subject_aliases.name) SEPARATOR '::') as aliases, " 
						+ " GROUP_CONCAT(CONCAT(subject_attributes.name, ':', subject_attributes.value) SEPARATOR '::') as attributes ",
				"subjects LEFT JOIN subject_aliases on subjects.subject_id = subject_aliases.subject_id " 
						+ " LEFT JOIN subject_attributes on subjects.subject_id = subject_attributes.subject_id ",
				"subjects.subject_id"
		);
	}
	
}
