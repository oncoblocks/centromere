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

package org.oncoblocks.centromere.core.input.pipeline;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.oncoblocks.centromere.core.input.DataImportException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author woemler
 */
public class JsonJobFileParser implements JobFileParser {
	
	private ObjectMapper objectMapper;

	public JsonJobFileParser() {
		this.objectMapper = new ObjectMapper();
	}

	public JsonJobFileParser(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public ImportJob parseJobFile(String inputPath) throws DataImportException {
		ImportJob job = null;
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(inputPath));
			String line = reader.readLine();
			while (line != null) {
				builder.append(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new DataImportException(e.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

		}
		try {
			job = objectMapper.readValue(builder.toString(), ImportJob.class);
		} catch (IOException e) {
			e.printStackTrace();
			throw new DataImportException(e.getMessage());
		}
		return job;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
}
