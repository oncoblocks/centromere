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
import com.beust.jcommander.MissingCommandException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oncoblocks.centromere.core.dataimport.RecordProcessor;
import org.oncoblocks.centromere.core.model.support.BasicDataSetMetadata;
import org.oncoblocks.centromere.dataimport.cli.*;
import org.oncoblocks.centromere.dataimport.cli.test.support.DataFileRepository;
import org.oncoblocks.centromere.dataimport.cli.test.support.DataSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author woemler
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class, TestMongoConfig.class })
public class AddCommandTests {
	
	private DataImportManager manager;
	@Autowired private ApplicationContext context;
	@Autowired private DataSetRepository dataSetRepository;
	@Autowired private DataFileRepository dataFileRepository;
	
	@Before
	public void setup() throws Exception {
		dataFileRepository.deleteAll();
		dataSetRepository.deleteAll();
		manager = new DataImportManager(context, dataSetRepository, dataFileRepository);
	}
	
	@Test
	public void addDataSetArgumentsTest() throws Exception {
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
		BasicDataSetMetadata dataSetMetadata = (BasicDataSetMetadata) addCommandArguments.getDataSetMetadata();
		Assert.notNull(dataSetMetadata);
		Assert.isTrue("internal".equals(dataSetMetadata.getSource()));
		Assert.isTrue("Test Data Set".equals(dataSetMetadata.getName()));
	}

	@Test
	public void addDataTypeArgumentsTest() throws Exception {
		AddCommandArguments addCommandArguments = new AddCommandArguments();
		JCommander jCommander = new JCommander();
		jCommander.addCommand("add", addCommandArguments);
		String[] args = { "add", "data_type", "data", 
				"org.oncoblocks.centromere.dataimport.cli.test.SampleDataProcessor" };
		jCommander.parse(args);
		Assert.isTrue("add".equals(jCommander.getParsedCommand()));
		Assert.isTrue(addCommandArguments.getArgs().size() == 3);
		String label = addCommandArguments.getLabel();
		String category = addCommandArguments.getCategory();
		String body = addCommandArguments.getBody();
		Assert.notNull(label);
		Assert.notNull(category);
		Assert.notNull(body);
		Assert.isTrue("data_type".equals(category));
		Assert.isTrue("data".equals(label));
		RecordProcessor processor = (RecordProcessor) context.getBean(Class.forName(body));
		Assert.notNull(processor);
		Assert.isTrue(processor instanceof SampleDataProcessor);
	}
	
	@Test
	public void dataImportManagerConfigTest() throws Exception {
		Assert.isTrue(manager.getDataSetMap().isEmpty());
		Map<String, RecordProcessor> dataTypeMap = manager.getDataTypeMap();
		Assert.notNull(dataTypeMap);
		Assert.notEmpty(dataTypeMap);
		Assert.isTrue(dataTypeMap.containsKey("sample_data"));
		Assert.isTrue(dataTypeMap.get("sample_data") instanceof SampleDataProcessor);
	}
	
	@Test
	public void addDataSetTest() throws Exception {
		AddCommandArguments addCommandArguments = new AddCommandArguments();
		JCommander jCommander = new JCommander();
		jCommander.addCommand("add", addCommandArguments);
		String[] args = { "add", "data_set", "test", "\"{ \"source\": \"internal\", \"name\": \"Test Data Set\", \"notes\": \"This is a test data set\" }\"" };
		jCommander.parse(args);
		BasicDataSetMetadata dataSetMetadata = (BasicDataSetMetadata) addCommandArguments.getDataSetMetadata();
		Assert.notNull(dataSetMetadata);
		manager.addDataSetMapping(dataSetMetadata);
		Assert.isTrue(!manager.getDataSetMap().isEmpty());
		BasicDataSetMetadata metadata = (BasicDataSetMetadata) manager.getDataSet("test");
		Assert.notNull(metadata);
		Assert.isTrue("internal".equals(metadata.getSource()));
	}
	
	@Test
	public void addDataTypeTest() throws Exception {
		Assert.isTrue(manager.getDataTypeMap().size() == 1);
		AddCommandArguments addCommandArguments = new AddCommandArguments();
		JCommander jCommander = new JCommander();
		jCommander.addCommand("add", addCommandArguments);
		String[] args = { "add", "data_type", "data",
				"org.oncoblocks.centromere.dataimport.cli.test.SampleDataProcessor" };
		jCommander.parse(args);
		manager.addDataTypeMapping(addCommandArguments.getLabel(), addCommandArguments.getBody());
		Assert.isTrue(manager.getDataTypeMap().size() == 2);
		Assert.isTrue(manager.getDataTypeMap().containsKey("data"));
		Assert.notNull(manager.getDataTypeProcessor("data"));
		Assert.isTrue(manager.getDataTypeProcessor("data") instanceof SampleDataProcessor);
	}
	
	@Test
	public void addDataSetRunnerTest() throws Exception {
		JCommander commander = new JCommander();
		AddCommandArguments arguments = new AddCommandArguments();
		commander.addCommand("add", arguments);
		AddCommandRunner runner = new AddCommandRunner(manager);
		String[] args = { "add", "data_set", "test", "\"{ \"source\": \"internal\", \"name\": \"Test Data Set\", \"notes\": \"This is a test data set\" }\"" };
		commander.parse(args);
		Assert.isTrue(manager.getDataSetMap().isEmpty());
		runner.run(arguments);
		Assert.isTrue(manager.getDataSetMap().size() == 1);
		Assert.state(manager.getDataSetMap().containsKey("test"));
	}
	
	@Test
	public void addDataTypeRunnerTest() throws Exception {
		JCommander commander = new JCommander();
		AddCommandArguments arguments = new AddCommandArguments();
		commander.addCommand("add", arguments);
		String[] args = { "add", "data_type", "data",
				"org.oncoblocks.centromere.dataimport.cli.test.SampleDataProcessor" };
		AddCommandRunner runner = new AddCommandRunner(manager);
		Assert.isTrue(manager.getDataTypeMap().size() == 1);
		commander.parse(args);
		runner.run(arguments);
		Assert.isTrue(manager.getDataTypeMap().size() == 2);
		Assert.isTrue(manager.getDataTypeMap().containsKey("data"));
	}
	
	@Test
	public void badCommandTest() throws Exception {
		JCommander commander = new JCommander();
		AddCommandArguments addCommandArguments = new AddCommandArguments();
		ImportCommandArguments importCommandArguments = new ImportCommandArguments();
		commander.addCommand("add", addCommandArguments);
		commander.addCommand("import", importCommandArguments);
		String[] args = {"bad", "command"};
		Exception exception = null;
		try {
			commander.parse(args);
		} catch (Exception e){
			exception = e;
		}
		Assert.notNull(exception);
		Assert.isTrue(exception instanceof MissingCommandException);
	}

	@Test
	public void badAddCategoryTest() throws Exception {
		JCommander commander = new JCommander();
		AddCommandArguments addCommandArguments = new AddCommandArguments();
		commander.addCommand("add", addCommandArguments);
		AddCommandRunner addCommandRunner = new AddCommandRunner(manager);
		String[] args = {"add", "bad"};
		commander.parse(args);
		Exception exception = null;
		try {
			addCommandRunner.run(addCommandArguments);
		} catch (Exception e){
			exception = e;
		}
		Assert.notNull(exception);
		Assert.isTrue(exception instanceof CommandLineRunnerException);
	}
	
	@Test
	public void invalidDataTypeProcessorTest() throws Exception {
		JCommander commander = new JCommander();
		AddCommandArguments addCommandArguments = new AddCommandArguments();
		commander.addCommand("add", addCommandArguments);
		AddCommandRunner addCommandRunner = new AddCommandRunner(manager);
		String[] args = {"add", "data_type", "bad", "org.oncoblocks.fake.Processor"};
		commander.parse(args);
		Exception exception = null;
		try {
			addCommandRunner.run(addCommandArguments);
		} catch (Exception e){
			exception = e;
		}
		Assert.notNull(exception);
		Assert.isTrue(exception instanceof CommandLineRunnerException);
	}

	@Test
	public void invalidDataSetMetadataTest() throws Exception {
		JCommander commander = new JCommander();
		AddCommandArguments addCommandArguments = new AddCommandArguments();
		commander.addCommand("add", addCommandArguments);
		AddCommandRunner addCommandRunner = new AddCommandRunner(manager);
		String[] args = {"add", "data_set", "bad", "{\"bad\": \"invalid\"}"};
		commander.parse(args);
		Exception exception = null;
		try {
			addCommandRunner.run(addCommandArguments);
		} catch (Exception e){
			exception = e;
		}
		Assert.notNull(exception);
		Assert.isTrue(exception instanceof CommandLineRunnerException);
	}
	
}
