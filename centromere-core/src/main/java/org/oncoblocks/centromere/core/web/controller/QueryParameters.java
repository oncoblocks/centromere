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

package org.oncoblocks.centromere.core.web.controller;

import java.util.Set;

/**
 * @author woemler
 */
public class QueryParameters {
	
	private Set<String> fields;
	private Set<String> exclude;

	public Set<String> getIncludedFields() {
		return fields;
	}

	public void setFields(Set<String> fields) {
		this.fields = fields;
	}

	public Set<String> getExcludedFields() {
		return exclude;
	}

	public void setExclude(Set<String> exclude) {
		this.exclude = exclude;
	}
	
}
