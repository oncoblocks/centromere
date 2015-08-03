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

package org.oncoblocks.centromere.core.test.web.swagger;

import org.oncoblocks.centromere.core.test.models.EntrezGene;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author woemler
 */

@Controller
@RequestMapping("/swagger")
public class SwaggerController {
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public HttpEntity findById(@PathVariable Long id){
		EntrezGene gene = new EntrezGene();
		gene.setEntrezGeneId(id);
		gene.setPrimaryGeneSymbol("TEST");
		return new ResponseEntity<EntrezGene>(gene, HttpStatus.OK);
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public HttpEntity find(@RequestParam(value = "symbol", required = false) String geneSymbol){
		List<EntrezGene> genes = new ArrayList<>();
		EntrezGene gene = new EntrezGene();
		gene.setEntrezGeneId(0L);
		gene.setPrimaryGeneSymbol( geneSymbol != null ? geneSymbol : "TEST" );
		genes.add(gene);
		return new ResponseEntity<>(genes, HttpStatus.OK);
	}
	
}
