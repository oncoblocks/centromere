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

package org.oncoblocks.centromere.core.web.util;

import org.oncoblocks.centromere.core.model.support.SourcedAlias;
import org.springframework.core.convert.converter.Converter;

/**
 * Simple converter for {@link SourcedAlias}
 * 
 * @author woemler
 */
public class StringToSourcedAliasConverter implements Converter<String, SourcedAlias> {
	@Override public SourcedAlias convert(String s) {
		String[] bits = s.split(":");
		SourcedAlias alias = new SourcedAlias();
		alias.setSource(bits[0]);
		if (bits.length > 1){
			alias.setName(bits[1]);
		}
		return alias;
	}
}
