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

package org.oncoblocks.centromere.core.repository.support;

import org.oncoblocks.centromere.core.model.support.DataSetMetadata;
import org.oncoblocks.centromere.core.repository.RepositoryOperations;

import java.io.Serializable;

/**
 * @author woemler
 */
public interface DataSetMetadataRepository<T extends DataSetMetadata<ID>, ID extends Serializable> 
		extends RepositoryOperations<T, ID> {
	Iterable<T> getByLabel(String label);
	Iterable<T> getByName(String name);
	Iterable<T> getBySource(String source);
}
