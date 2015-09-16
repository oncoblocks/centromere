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

package org.oncoblocks.centromere.core.test.models;

import org.oncoblocks.centromere.core.model.impl.DataSetDto;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author woemler
 */

@Document(collection = "data_sets")
public class DataSet extends DataSetDto<String> {
	public DataSet() {
	}

	public DataSet(String id, String source, String name, String notes) {
		super(id, source, name, notes);
	}
}
