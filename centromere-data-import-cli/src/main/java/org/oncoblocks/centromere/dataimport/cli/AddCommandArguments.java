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
import org.oncoblocks.centromere.core.model.support.BasicDataSetMetadata;
import org.oncoblocks.centromere.core.model.support.DataSetMetadata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Command line argument configuration for the ADD command.
 * 
 * @author woemler
 */
public class AddCommandArguments {
	
	@Parameter(description = "Positional arguments.  The first should be the ")
	private List<String> args = new ArrayList<>();

	public List<String> getArgs() {
		return args;
	}

	public void setArgs(List<String> args) {
		this.args = args;
	}

	public String getCategory() {
		return args.size() > 0 ? args.get(0) : null;
	}

	public String getLabel() {
		return args.size() > 1 ? args.get(1) : null;
	}

	public String getBody() {
		return args.size() > 2 ? args.get(2) : null;
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
			metadata = mapper.readValue(this.getBody(), BasicDataSetMetadata.class);
			metadata.setLabel(this.getLabel());
		} catch (IOException e){
			e.printStackTrace();
		}
		return metadata;
	}

	@Override 
	public String toString() {
		return "AddCommandArguments{" +
				"args=" + args +
				", category='" + (this.getCategory() != null ? this.getCategory() : "") + '\'' +
				", label='" + (this.getLabel() != null ? this.getLabel() : "") + '\'' +
				", body='" + (this.getBody() != null ? this.getBody() : "") + '\'' +
				'}';
	}
}
