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

package org.oncoblocks.centromere.core.dataimport.reader.impl;

import org.oncoblocks.centromere.core.dataimport.job.DataFileProcessingException;
import org.oncoblocks.centromere.core.dataimport.reader.DataFileReader;
import org.oncoblocks.centromere.core.model.impl.EntrezGeneDto;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author woemler
 */
public class GeneInfoReader extends DataFileReader<EntrezGeneDto> {

	public GeneInfoReader() {
		super();
	}

	@Override 
	public EntrezGeneDto readRecord() throws DataFileProcessingException {
		EntrezGeneDto gene = null;
		String line;
		try {
			boolean flag = true;
			while(flag) {
				line = this.getReader().readLine();
				if (line == null || !line.startsWith("#Format: tax_id GeneID")) {
					flag = false;
					if (line != null && !line.equals("")){
						gene = getRecordFromLine(line);
					}
				}
				
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		return gene;
		
	}
	
	private EntrezGeneDto getRecordFromLine(String line){
		String[] bits = line.split("\\t");
		EntrezGeneDto gene = new EntrezGeneDto();
		gene.setTaxId(Integer.parseInt(bits[0]));
		gene.setEntrezGeneId(Long.parseLong(bits[1]));
		gene.setPrimaryGeneSymbol(bits[2]);
		gene.setAliases(new HashSet<>(Arrays.asList(bits[3].split("\\|"))));
		Map<String,Object> dbXrefs = new HashMap<>();
		for (String ref: bits[4].split("\\|")){
			String[] r = ref.split(":");
			dbXrefs.put(r[0], r[r.length-1]);
		}
		gene.setDbXrefs(dbXrefs);
		gene.setChromosome(bits[5]);
		gene.setChromosomeLocation(bits[6]);
		gene.setDescription(bits[7]);
		gene.setGeneType(bits[8]);
		return gene;
	}
	
}
