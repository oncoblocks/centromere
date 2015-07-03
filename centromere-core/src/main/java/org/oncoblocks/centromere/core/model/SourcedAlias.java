package org.oncoblocks.centromere.core.model;

/**
 * Simple representation of alias names for embedding in other entities.
 * 
 * @author woemler
 */

public class SourcedAlias {

	private String source;
	private String name;

	public SourcedAlias() { }

	public SourcedAlias(String source, String name) {
		this.source = source;
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
