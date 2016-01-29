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

package org.oncoblocks.centromere.web.test.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.oncoblocks.centromere.core.model.support.Attribute;
import org.oncoblocks.centromere.core.model.support.SourcedAlias;
import org.oncoblocks.centromere.web.util.StringToAttributeConverter;
import org.oncoblocks.centromere.web.util.StringToSourcedAliasConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

/**
 * @author woemler
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { UtilTestConfig.class })
public class ConverterTests {
	
	@Test
	public void attributeConverterTest(){
		StringToAttributeConverter converter = new StringToAttributeConverter();
		Attribute attribute = converter.convert("key:value");
		Assert.isTrue(attribute.getName().equals("key"));
		Assert.isTrue(attribute.getValue().equals("value"));
		attribute = converter.convert("key");
		Assert.isTrue(attribute.getName().equals("key"));
		Assert.isNull(attribute.getValue());
	}
	
	@Test
	public void sourcedAliasConverterTest(){
		StringToSourcedAliasConverter converter = new StringToSourcedAliasConverter();
		SourcedAlias alias = converter.convert("source:alias");
		Assert.isTrue(alias.getSource().equals("source"));
		Assert.isTrue(alias.getName().equals("alias"));
		alias = converter.convert("source");
		Assert.isTrue(alias.getSource().equals("source"));
		Assert.isNull(alias.getName());
	}
	
}
