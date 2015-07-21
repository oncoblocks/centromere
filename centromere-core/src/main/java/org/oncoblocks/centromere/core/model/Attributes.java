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
