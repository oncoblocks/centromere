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

package org.oncoblocks.centromere.core.repository;

/**
 * List of constants, representing database query evaluations, to be supported by each repository
 *   implementation.  Each operation's implementation will vary by database technology, but should
 *   be expected to behave the same.
 * 
* @author woemler
*/
public enum Evaluation {
	EQUALS,
	IN,
	NOT_EQUALS,
	NOT_IN,
	LIKE,
	NOT_LIKE,
	STARTS_WITH,
	ENDS_WITH,
	GREATER_THAN,
	LESS_THAN,
	GREATER_THAN_EQUALS,
	LESS_THAN_EQUALS,
	BETWEEN,
	BETWEEN_INCLUSIVE,
	OUTSIDE,
	OUTSIDE_INCLUSIVE,
	IS_NULL,
	NOT_NULL,
	IS_TRUE,
	IS_FALSE
}
