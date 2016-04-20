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

import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.oncoblocks.centromere.core.dataimport.pipeline.BasicDataSetMetadata;
import org.oncoblocks.centromere.core.dataimport.pipeline.DataSetMetadata;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;

/**
 * Command line argument configuration for the ADD command.
 * 
 * @author woemler
 */
public class AddCommandArguments {
	
	@Parameter(description = "Positional arguments.  The first should be the ")
	private List<String> args;
	private String category;
	private String label;
	private String body;

	public List<String> getArgs() {
		return args;
	}

	public void setArgs(List<String> args) {
		Assert.isTrue(args.size() == 3, "Add command requires exactly three arguments!");
		this.args = args;
		this.category = args.get(0);
		this.label = args.get(1);
		this.body = args.get(2);
	}

	public String getCategory() {
		return category;
	}

	public String getLabel() {
		return label;
	}

	public String getBody() {
		return body;
	}

	/**
	 * Converts the {@code body} argument into a {@link DataSetMetadata} instance.  By default, this is
	 *   done by converting the body from JSON, using Jackson's {@link ObjectMapper}.
	 * 
	 * @return
	 */
	public DataSetMetadata getDataSetMetadata(){
		ObjectMapper mapper = new ObjectMapper();
		BasicDataSetMetadata metadata = null;
		try {
			metadata = mapper.readValue(body, BasicDataSetMetadata.class);
		} catch (IOException e){
			e.printStackTrace();
		}
		return metadata;
	}
	
	
}
