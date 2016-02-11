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

import org.oncoblocks.centromere.core.repository.Evaluation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows the use of query string parameters, other than the model field's name to be used in web
 *   API query operations.  {@link Evaluation} values, other than the default equality check, can
 *   be assigned tothe alias as well.
 * 
 * @author woemler
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Alias {

	/**
	 * The string name of the parameter to be used.
	 * @return
	 */
	String value();

	/**
	 * The database query evaluation to be used when generating a 
	 *   {@link org.oncoblocks.centromere.core.repository.QueryCriteria}. 
	 * @return
	 */
	Evaluation evaluation() default Evaluation.EQUALS;

	/**
	 * Alternate field name to be used in the {@link org.oncoblocks.centromere.core.repository.QueryCriteria},
	 *   if the default name of the field would be invalid (such as with maps or lists of key-value pairs.
	 * @return
	 */
	String fieldName() default "";
}
