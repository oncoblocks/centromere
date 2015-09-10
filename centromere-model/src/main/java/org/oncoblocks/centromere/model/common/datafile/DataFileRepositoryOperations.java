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

package org.oncoblocks.centromere.model.common.datafile;

import org.oncoblocks.centromere.core.model.Model;

import java.io.Serializable;
import java.util.List;

/**
 * @author woemler
 */
public interface DataFileRepositoryOperations<T extends Model<ID>, ID extends Serializable> {
	List<T> findByDataSetId(ID dataSetId);
	List<T> findByFileName(String fileName);
}
