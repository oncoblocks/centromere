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

package org.oncoblocks.centromere.core.test;

import org.oncoblocks.centromere.core.dataimport.BasicImportOptions;
import org.oncoblocks.centromere.core.dataimport.GenericRecordProcessor;
import org.oncoblocks.centromere.core.dataimport.RepositoryRecordWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

/**
 * @author woemler
 */
@Component
public class GeneInfoProcessor extends GenericRecordProcessor<EntrezGene> {
	
	private final TestRepository testRepository;

	@Autowired
	public GeneInfoProcessor(Validator validator, TestRepository testRepository, BasicImportOptions importOptions) {
		super(new GeneInfoReader(), validator, new RepositoryRecordWriter<>(testRepository), null, importOptions);
		this.testRepository = testRepository;
	}

	@Override public void doBefore() {
		testRepository.deleteAll();
	}

	@Override public void doAfter() {
		return;
	}
}
