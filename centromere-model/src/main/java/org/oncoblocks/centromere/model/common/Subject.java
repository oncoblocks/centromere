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

import org.oncoblocks.centromere.core.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author woemler
 */

@Filterable
public class Subject<ID extends Serializable> implements Model<ID>, SourcedAliases, Attributes {

	private ID id;
	private String name;
	private String species;
	private String gender;
	private String type;
	private String notes;
	private List<SourcedAlias> aliases;
	private List<Attribute> attributes;

	public Subject() { }

	public Subject(ID id, String name, String species, String gender, String type, String notes,
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

	public String getAttributeByName(String name){
		for (Attribute attribute: attributes){
			if (attribute.getName().equals(name)){
				return attribute.getValue();
			}
		}
		return null;
	}

	public String getAliasBySource(String source){
		for (SourcedAlias alias: aliases){
			if (alias.getSource().equals(source)){
				return alias.getName();
			}
		}
		return null;
	}

	public ID getId() {
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

	public void setAliases(Collection<SourcedAlias> aliases) {
		this.aliases = (List) aliases;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Collection<Attribute> attributes) {
		this.attributes = (List) attributes;
	}

	public void setAttributeName(String attributeName) {
		if (attributes == null) attributes = new ArrayList<>();
		attributes.add(new Attribute(attributeName, null));
	}

	public void setAttributeValue(String attributeValue) {
		if (attributes == null) attributes = new ArrayList<>();
		attributes.add(new Attribute(null, attributeValue));
	}

	public void setAttribute(String attribute) {
		if (attributes == null) attributes = new ArrayList<>();
		attributes.add(new Attribute(attribute.split(":")[0], attribute.split(":")[1]));
	}

	public boolean hasAttribute(String name) {
		for (Attribute attribute: attributes){
			if (attribute.getName().equals(name)) return true;
		}
		return false;
	}

	public void setAliasName(String aliasName) {
		if (aliases == null) aliases = new ArrayList<>();
		aliases.add(new SourcedAlias(null, aliasName));
	}

	public void setAliasSource(String aliasSource) {
		if (aliases == null) aliases = new ArrayList<>();
		aliases.add(new SourcedAlias(aliasSource, null));
	}

	public void setAlias(String alias) {
		if (aliases == null) aliases = new ArrayList<>();
		aliases.add(new SourcedAlias(alias.split(":")[0], alias.split(":")[1]));
	}

	public boolean hasAlias(String name) {
		for (SourcedAlias alias: aliases){
			if (alias.getName().equals(name)) return true;
		}
		return false;
	}
	
}
