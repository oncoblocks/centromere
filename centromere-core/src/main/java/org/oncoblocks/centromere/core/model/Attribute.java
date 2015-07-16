package org.oncoblocks.centromere.core.model;

/**
 * Simple class for representing key-value attributes in other entity classes.
 * 
 * @author woemler
 */

public class Attribute {
	
	private String name;
	private String value;

	public Attribute() { }

	public Attribute(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}