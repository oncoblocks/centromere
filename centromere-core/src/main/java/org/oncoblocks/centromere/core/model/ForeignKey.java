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

import java.lang.annotation.*;

/**
 * Used for annotating {@link Model} fields to denote that it
 *   is a foreign key, referencing a different class, {@code type}.  Used in building hypermedia links
 *   to related entities, where {@code rel} is the relationship identifier, and {@code qsParameter} is
 *   the query string parameter that references the parent class's field in many-to-one relationships..
 * 
 * @author woemler
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.FIELD)
public @interface ForeignKey {
	Class<? extends Model> type();
	String relString() default "";
	String qsParameter() default "";
}
