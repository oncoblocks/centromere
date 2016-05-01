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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oncoblocks.centromere.core.model.support.BasicDataSetMetadata;
import org.oncoblocks.centromere.dataimport.cli.DataImportManager;
import org.oncoblocks.centromere.dataimport.cli.ImportCommandArguments;
import org.oncoblocks.centromere.dataimport.cli.ImportCommandRunner;
import org.oncoblocks.centromere.dataimport.cli.test.support.DataFileRepository;
import org.oncoblocks.centromere.dataimport.cli.test.support.DataSetRepository;
import org.oncoblocks.centromere.dataimport.cli.test.support.SampleDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

/**
 * @author woemler
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class, TestMongoConfig.class })
public class ImportCommandTests {

	private DataImportManager manager;
	@Autowired private ApplicationContext context;
	@Autowired private DataSetRepository dataSetRepository;
	@Autowired private DataFileRepository dataFileRepository;
	@Autowired private SampleDataRepository sampleDataRepository;

	@Before
	public void setup() throws Exception {
		dataFileRepository.deleteAll();
		dataSetRepository.deleteAll();
		sampleDataRepository.deleteAll();
		manager = new DataImportManager(context, dataSetRepository, dataFileRepository);
	}
	
	@Test
	public void importArgumentsTest() throws Exception {
		ImportCommandArguments arguments = new ImportCommandArguments();
		JCommander commander = new JCommander();
		commander.addCommand("import", arguments);
		String[] args = { "import", "-t", "sample_data", "-i", "test.txt", "--skip-invalid-records", "-d", 
				"{\"label\": \"test\", \"name\": \"Test data\", \"source\": \"internal\"}" };
		commander.parse(args);
		Assert.isTrue("import".equals(commander.getParsedCommand()));
		Assert.isTrue("sample_data".equals(arguments.getDataType()));
		Assert.isTrue("test.txt".equals(arguments.getInputFilePath()));
		Assert.isTrue(arguments.isSkipInvalidRecords());
		Assert.isTrue(!arguments.isSkipInvalidDataSets());
		BasicDataSetMetadata metadata = (BasicDataSetMetadata) arguments.getDataSetMetadata();
		Assert.notNull(metadata);
		Assert.isTrue("internal".equals(metadata.getSource()));
		Assert.isTrue("test".equals(metadata.getLabel()));
	}
	
	@Test
	public void importRunnerTest() throws Exception {
		Assert.isTrue(sampleDataRepository.count() == 0);
		Assert.isTrue(dataFileRepository.count() == 0);
		Assert.isTrue(dataSetRepository.count() == 0);
		String[] args = { "import", "-t", "sample_data", "-i", ClassLoader.getSystemResource("placeholder.txt").getPath(), 
				"--skip-invalid-records", "-d", "{\"label\": \"test\", \"name\": \"Test data\", \"source\": \"internal\"}" };
		ImportCommandArguments arguments = new ImportCommandArguments();
		JCommander commander = new JCommander();
		commander.addCommand("import", arguments);
		commander.parse(args);
		ImportCommandRunner runner = new ImportCommandRunner(manager);
		runner.run(arguments);
		Assert.isTrue(sampleDataRepository.count() == 5);
		Assert.isTrue(dataFileRepository.count() == 1);
		Assert.isTrue(dataSetRepository.count() == 1);
	}
	
	
	
}
