package org.oncoblocks.centromere.core.model;

import java.util.Collection;

/**
 * Interface for adding controller query methods to entity classes with key-value attributes.
 * 
 * @author woemler
 */
public interface Attributes {
	Collection<Attribute> getAttributes();
	void setAttributes(Collection<Attribute> attributes);
	void setAttributeName(String attributeName);
	void setAttributeValue(String attributeValue);
	void setAttribute(String attribute);
	boolean hasAttribute(String name);
}
