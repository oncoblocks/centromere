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

package org.oncoblocks.centromere.web.test.models;

import org.oncoblocks.centromere.core.model.Filterable;
import org.oncoblocks.centromere.core.model.Model;
import org.oncoblocks.centromere.core.model.support.Attribute;
import org.oncoblocks.centromere.core.model.support.SourcedAlias;
import org.oncoblocks.centromere.sql.sqlbuilder.ComplexTableDescription;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author woemler
 */

@Filterable
public class Subject implements Model<Long> {

	@Id private Long id;
	private String name;
	private String species;
	private String gender;
	private String type;
	private String notes;
	private List<SourcedAlias> aliases;
	private List<Attribute> attributes;

	public Subject() { }

	public Subject(Long id, String name, String species, String gender, String type, String notes,
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

	@Override public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
	
	public static List<Subject> createDummyData(){

		List<Subject> subjects = new ArrayList<>();
		List<SourcedAlias> aliases = new ArrayList<>();
		List<Attribute> attributes = new ArrayList<>();
		
		Subject subject = new Subject(1L, "PersonA", "Homo sapiens", "M", "patient", null, null, null);
		aliases.add(new SourcedAlias("clinic", "patient01"));
		attributes.add(new Attribute("cancerType", "colon"));
		subject.setAliases(aliases);
		subject.setAttributes(attributes);
		subjects.add(subject);
		aliases = new ArrayList<>();
		attributes = new ArrayList<>();

		subject = new Subject(2L, "PersonB", "Homo sapiens", "F", "patient", null, null, null);
		aliases.add(new SourcedAlias("clinic", "patient02"));
		attributes.add(new Attribute("cancerType","breast"));
		subject.setAliases(aliases);
		subject.setAttributes(attributes);
		subjects.add(subject);
		aliases = new ArrayList<>();
		attributes = new ArrayList<>();

		subject = new Subject(3L, "PersonC", "Homo sapiens", "M", "patient", null, null, null);
		aliases.add(new SourcedAlias("clinic","patient03"));
		attributes.add(new Attribute("cancerType","lung"));
		subject.setAliases(aliases);
		subject.setAttributes(attributes);
		subjects.add(subject);
		aliases = new ArrayList<>();
		attributes = new ArrayList<>();

		subject = new Subject(4L, "MCF7", "Homo sapiens", "F", "cell line", null, null, null);
		aliases.add(new SourcedAlias("CCLE","MCF7_BREAST"));
		attributes.add(new Attribute("cancerType","breast"));
		attributes.add(new Attribute("isCellLine","Y"));
		subject.setAliases(aliases);
		subject.setAttributes(attributes);
		subjects.add(subject);
		aliases = new ArrayList<>();
		attributes = new ArrayList<>();

		subject = new Subject(5L, "A375", "Homo sapiens", "U", "cell line", null, null, null);
		aliases.add(new SourcedAlias("CCLE","A375_SKIN"));
		attributes.add(new Attribute("cancerType","skin"));
		attributes.add(new Attribute("isCellLine","Y"));
		subject.setAliases(aliases);
		subject.setAttributes(attributes);
		subjects.add(subject);
		
		return subjects;
		
	}
	
}
