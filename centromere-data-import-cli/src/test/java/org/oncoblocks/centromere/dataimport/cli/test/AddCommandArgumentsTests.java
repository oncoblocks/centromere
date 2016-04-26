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

package org.oncoblocks.centromere.dataimport.cli.test;

import com.beust.jcommander.JCommander;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oncoblocks.centromere.core.model.support.BasicDataSetMetadata;
import org.oncoblocks.centromere.dataimport.cli.AddCommandArguments;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

/**
 * @author woemler
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class AddCommandArgumentsTests {
	
	@Test
	public void addDataSetTest() throws Exception {
		AddCommandArguments addCommandArguments = new AddCommandArguments();
		JCommander jCommander = new JCommander();
		jCommander.addCommand("add", addCommandArguments);
		String[] args = { "add", "data_set", "test", "\"{ \"source\": \"internal\", \"name\": \"Test Data Set\", \"notes\": \"This is a test data set\" }\"" };
		jCommander.parse(args);
		Assert.isTrue("add".equals(jCommander.getParsedCommand()));
		Assert.isTrue(addCommandArguments.getArgs().size() == 3);
		String label = addCommandArguments.getLabel();
		String category = addCommandArguments.getCategory();
		String body = addCommandArguments.getBody();
		Assert.notNull(label);
		Assert.notNull(category);
		Assert.notNull(body);
		Assert.isTrue("data_set".equals(category));
		Assert.isTrue("test".equals(label));
		ObjectMapper objectMapper = new ObjectMapper();
		BasicDataSetMetadata dataSetMetadata = objectMapper.readValue(body, BasicDataSetMetadata.class);
		Assert.notNull(dataSetMetadata);
		Assert.isTrue("internal".equals(dataSetMetadata.getSource()));
		Assert.isTrue("Test Data Set".equals(dataSetMetadata.getName()));
	}
	
}
