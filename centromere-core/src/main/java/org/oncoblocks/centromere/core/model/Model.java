package org.oncoblocks.centromere.core.model;

import java.io.Serializable;

/**
 * Basic entity interface to ensure that model objects have identifiable attributes.  {@code ID} is 
 *   intended to reflect database primary key identifiers, whether they be primitive types, or a 
 *   unique combination of fields.  
 * 
 * @author woemler
 */
public interface Model<ID extends Serializable> {
	ID getId();
}
