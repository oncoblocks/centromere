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

import java.util.Date;
import java.util.concurrent.TimeUnit;

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
		Date start = new Date();
		Assert.notNull(addCommandRunner, "AddCommandRunner must not be null!");
		ImportCommandArguments importArguments = new ImportCommandArguments();
		AddCommandArguments addArguments = new AddCommandArguments();
		JCommander jc = new JCommander();
		jc.addCommand("import", importArguments);
		jc.addCommand("add", addArguments);
		jc.parse(args);
		logger.info("[CENTROMERE] Starting command line import utility.");
		switch (jc.getParsedCommand()){
			case "import":
				logger.info(String.format("[CENTROMERE] Running 'import' command with arguments: %s", importArguments.toString()));
				importCommandRunner.run(importArguments);
				break;
			case "add":
				logger.info(String.format("[CENTROMERE] Running 'add' command with arguments: %s", addArguments.toString()));
				addCommandRunner.run(addArguments);
				break;
			default:
				jc.usage();
		}
		Date end = new Date();
		logger.info(String.format("[CENTROMERE] Finished.  Elapsed time: %s", formatInterval(end.getTime() - start.getTime())));
		
	}

	/**
	 * From http://stackoverflow.com/a/6710604/1458983
	 * Converts a long-formatted timespan into a human-readable string that denotes the length of time 
	 *   that has elapsed.
	 * @param l
	 * @return
	 */
	private static String formatInterval(final long l) {
		final long hr = TimeUnit.MILLISECONDS.toHours(l);
		final long min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr));
		final long sec = TimeUnit.MILLISECONDS.toSeconds(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
		final long ms = TimeUnit.MILLISECONDS.toMillis(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec));
		return String.format("%02d:%02d:%02d.%03d", hr, min, sec, ms);
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
