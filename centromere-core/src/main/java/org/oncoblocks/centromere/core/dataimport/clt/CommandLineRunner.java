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

package org.oncoblocks.centromere.core.dataimport.clt;

import com.beust.jcommander.JCommander;

/**
 * @author woemler
 */
public class CommandLineRunner {
	
	public void main(String[] args) throws Exception {
		
		ImportCommandArguments importArguments = new ImportCommandArguments();
		AddCommandArguments addArguments = new AddCommandArguments();
		JCommander jc = new JCommander();
		jc.addCommand("import", importArguments);
		jc.addCommand("add", addArguments);
		jc.parse(args);
		
		switch (jc.getParsedCommand()){
			case "import":
				// do stuff
				break;
			case "add":
				// do stuff
				break;
			default:
				jc.usage();
		}
		
	}
	
}
