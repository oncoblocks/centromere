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

package org.oncoblocks.centromere.core.dataimport.validator.impl;

import org.oncoblocks.centromere.core.dataimport.validator.EntityValidationException;
import org.oncoblocks.centromere.core.dataimport.validator.EntityValidator;
import org.oncoblocks.centromere.core.model.support.DataFileDto;
import org.springframework.util.Assert;

/**
 * @author woemler
 */
public class DataFileValidator<T extends DataFileDto> implements EntityValidator<T> {
	@Override 
	public boolean validate(T entity) {
		try {
			Assert.notNull(entity);
			Assert.notNull(entity.getDataSetId());
			Assert.notNull(entity.getFilePath());
			Assert.isTrue(!entity.getFilePath().equals(""));
			Assert.notNull(entity.getDataType());
			Assert.isTrue(!entity.getDataType().equals(""));
			return true;
		} catch (IllegalArgumentException e){
			e.printStackTrace();
			throw new EntityValidationException(e.getMessage());
		}
	}
}
