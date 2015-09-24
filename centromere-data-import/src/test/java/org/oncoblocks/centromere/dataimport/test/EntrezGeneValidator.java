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

package org.oncoblocks.centromere.dataimport.test;

import org.oncoblocks.centromere.dataimport.config.DataImportException;
import org.oncoblocks.centromere.dataimport.test.models.EntrezGene;
import org.oncoblocks.centromere.dataimport.validator.EntityValidationException;
import org.oncoblocks.centromere.dataimport.validator.EntityValidator;
import org.springframework.util.Assert;

/**
 * @author woemler
 */
public class EntrezGeneValidator implements EntityValidator<EntrezGene> {

	@Override 
	public boolean validate(EntrezGene entity) throws DataImportException{
		try {
			Assert.notNull(entity.getEntrezGeneId(), "Entrez gene ID must not be null");
			Assert.isTrue(entity.getEntrezGeneId() > 0, "Entrez Gene ID must be greater than 0");
			Assert.notNull(entity.getPrimaryGeneSymbol(), "Primary gene symbol must not be null");
			Assert.isTrue(!entity.getPrimaryGeneSymbol().equals(""), "Primary gene symbol must not be an empty string");
			Assert.isTrue(entity.getTaxId().equals(9606), "Tax ID must be 9606 (Homo sapiens)");
			return true;
		} catch (IllegalArgumentException e){
			e.printStackTrace();
			throw new EntityValidationException(e.getMessage());
		}
	}
}
