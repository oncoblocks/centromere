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

import com.beust.jcommander.Parameter;

import java.util.List;

/**
 * @author woemler
 */
public class AddCommandArguments {
	
	@Parameter(description = "List of records to be added")
	private List<String> records;

	@Parameter(names = {"--skip-invalid-records"}, description = "")
	private boolean skipInvalidRecords = false;

	@Parameter(names = {"--skip-invalid-genes"}, description = "")
	private boolean skipInvalidGenes = false;

	@Parameter(names = {"--skip-invalid-samples"}, description = "")
	private boolean skipInvalidSamples = false;

	@Parameter(names = {"--skip-invalid-data-sets"}, description = "")
	private boolean skipInvalidDataSets = false;

	public List<String> getRecords() {
		return records;
	}

	public void setRecords(List<String> records) {
		this.records = records;
	}

	public boolean isSkipInvalidRecords() {
		return skipInvalidRecords;
	}

	public void setSkipInvalidRecords(boolean skipInvalidRecords) {
		this.skipInvalidRecords = skipInvalidRecords;
	}

	public boolean isSkipInvalidGenes() {
		return skipInvalidGenes;
	}

	public void setSkipInvalidGenes(boolean skipInvalidGenes) {
		this.skipInvalidGenes = skipInvalidGenes;
	}

	public boolean isSkipInvalidSamples() {
		return skipInvalidSamples;
	}

	public void setSkipInvalidSamples(boolean skipInvalidSamples) {
		this.skipInvalidSamples = skipInvalidSamples;
	}

	public boolean isSkipInvalidDataSets() {
		return skipInvalidDataSets;
	}

	public void setSkipInvalidDataSets(boolean skipInvalidDataSets) {
		this.skipInvalidDataSets = skipInvalidDataSets;
	}
}
