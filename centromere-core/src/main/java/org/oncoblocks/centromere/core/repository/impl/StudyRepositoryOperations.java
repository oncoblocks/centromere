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

package org.oncoblocks.centromere.core.repository.impl;

import org.oncoblocks.centromere.core.model.Model;
import org.oncoblocks.centromere.core.repository.RepositoryOperations;

import java.io.Serializable;
import java.util.List;

/**
 * Requires Study repository operations.
 * 
 * @author woemler
 */
public interface StudyRepositoryOperations<T extends Model<ID>, ID extends Serializable>
		extends RepositoryOperations<T, ID> {
	List<T> findByName(String name);
	List<T> findByGroup(String group);
	List<T> findBySampleId(ID sampleId);
}
