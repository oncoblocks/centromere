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
 * Interface for adding controller query methods to entity classes with key-value aliases.  
 * 
 * @author woemler
 */
public interface SourcedAliases {
	Collection<SourcedAlias> getAliases();
	void setAliases(Collection<SourcedAlias> aliases);
	void setAliasName(String aliasName);
	void setAliasSource(String aliasSource);
	void setAlias(String alias);
	boolean hasAlias(String name);
}
