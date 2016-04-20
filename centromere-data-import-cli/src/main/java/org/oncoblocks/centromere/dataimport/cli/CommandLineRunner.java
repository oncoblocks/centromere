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

package org.oncoblocks.centromere.dataimport.cli;

import com.beust.jcommander.JCommander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author woemler
 */
public class CommandLineRunner {
	
	private AddCommandRunner addCommandRunner;
	private ImportCommandRunner importCommandRunner;
	
	public static final Logger logger = LoggerFactory.getLogger(CommandLineRunner.class);

	public CommandLineRunner() { }

	public CommandLineRunner(AddCommandRunner addCommandRunner,
			ImportCommandRunner importCommandRunner) {
		this.addCommandRunner = addCommandRunner;
		this.importCommandRunner = importCommandRunner;
	}

	public void run(String[] args) throws Exception {
		Assert.notNull(addCommandRunner, "AddCommandRunner must not be null!");
		ImportCommandArguments importArguments = new ImportCommandArguments();
		AddCommandArguments addArguments = new AddCommandArguments();
		JCommander jc = new JCommander();
		jc.addCommand("import", importArguments);
		jc.addCommand("add", addArguments);
		jc.parse(args);
		
		switch (jc.getParsedCommand()){
			case "import":
				importCommandRunner.run(importArguments);
				break;
			case "add":
				addCommandRunner.run(addArguments);
				break;
			default:
				jc.usage();
		}
		
	}

	public AddCommandRunner getAddCommandRunner() {
		return addCommandRunner;
	}

	public void setAddCommandRunner(
			AddCommandRunner addCommandRunner) {
		this.addCommandRunner = addCommandRunner;
	}

	public ImportCommandRunner getImportCommandRunner() {
		return importCommandRunner;
	}

	public void setImportCommandRunner(
			ImportCommandRunner importCommandRunner) {
		this.importCommandRunner = importCommandRunner;
	}
}
