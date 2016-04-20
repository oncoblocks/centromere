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

package org.oncoblocks.centromere.core.model;

import java.lang.annotation.*;

/**
 * Marks a {@link Model} class field as being a foreign key identifier of another class.  Allows for 
 *   automatic generation of hypermedia links in web responses, based upon the inferred relationship 
 *   and query string parameters.
 * 
 * @author woemler
 */

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ForeignKey {

	/**
	 * The {@link Model} class that the foreign key field is referencing.
	 * @return
	 */
	Class<? extends Model<?>> model();

	/**
	 * {@link Relationship} value that represents the entity relationship to the referenced class.
	 * @return
	 */
	Relationship relationship();

	/**
	 * String name of the relationship to be used in hypermedia links.  By default, the field name is used.
	 * @return
	 */
	String rel() default "";

	/**
	 * Query string parameter name to be used in the hypermedia link.  By default, the field name is used.
	 * @return
	 */
	String field() default "";

	enum Relationship {
		ONE_TO_MANY,
		MANY_TO_ONE,
		MANY_TO_MANY
	}
	
}
