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

package org.oncoblocks.centromere.dataimport.test;

import org.oncoblocks.centromere.dataimport.processor.GeneralFileProcessor;
import org.oncoblocks.centromere.dataimport.test.models.EntrezGene;
import org.oncoblocks.centromere.dataimport.test.repositories.EntrezGeneRepository;
import org.oncoblocks.centromere.dataimport.writer.RepositoryRecordWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author woemler
 */

@Component
public class GeneInfoProcessor extends GeneralFileProcessor<EntrezGene, Long> {

	private final EntrezGeneRepository geneRepository;
	
	@Autowired
	public GeneInfoProcessor(EntrezGeneRepository geneRepository) {
		super(new GeneInfoReader(), new RepositoryRecordWriter<>(geneRepository),
				new EntrezGeneValidator(), null);
		this.geneRepository = geneRepository;
	}
	
	@Override
	public void doBefore(){
		geneRepository.dropCollection();
	}
	
	@Override
	public void doAfter(){
		geneRepository.createIndex("entrezGeneId");
		geneRepository.createIndex("primaryGeneSymbol");
	}
	
}
