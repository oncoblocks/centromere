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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * @author woemler
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class DataImportTests {
	
	@Autowired private Validator validator;
	private final String geneInfoPath = ClassLoader.getSystemClassLoader().getResource("Homo_sapiens.gene_info").getPath();

	@Before
	public void setup() throws Exception{ }

	@Test
	public void geneInfoReaderTest() throws Exception{
		GeneInfoReader reader = new GeneInfoReader();
		List<EntrezGene> genes = new ArrayList<>();
		try {
			reader.open(geneInfoPath);
			EntrezGene gene = reader.readRecord();
			while (gene != null) {
				genes.add(gene);
				gene = reader.readRecord();
			}
		} finally {
			reader.close();
		}
		Assert.notEmpty(genes);
		Assert.isTrue(genes.size() == 5);
		Assert.isTrue(genes.get(4).getEntrezGeneId().equals(10L));
	}
	
	@Test
	public void validationTest() throws Exception {
		EntrezGene gene = new EntrezGene();
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(gene, gene.getClass().getName());
		validator.validate(gene, bindingResult);
		if (bindingResult.hasErrors()){
			for (ObjectError error: bindingResult.getAllErrors()){
				System.out.println(error.toString());
			}
		} else {
			fail("Validation did not catch missing field.");
		}
	}
	
}
