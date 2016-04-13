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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.oncoblocks.centromere.core.dataimport.component.DataImportException;
import org.oncoblocks.centromere.core.dataimport.component.GenericRecordProcessor;
import org.oncoblocks.centromere.core.dataimport.component.RecordCollectionReader;
import org.oncoblocks.centromere.core.dataimport.component.RecordProcessor;
import org.oncoblocks.centromere.core.dataimport.pipeline.DataTypeManager;
import org.oncoblocks.centromere.core.model.Model;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author woemler
 */
public class AddCommandRunner {
	
	private DataTypeManager dataTypeManager;
	private ObjectMapper objectMapper;
	private AddCommandArguments arguments;
	
 	public void addRecords(String dataType, List<String> recordStrings) throws DataImportException{
		Assert.notNull(dataTypeManager, "DataTypeManager must not be null!");
		Assert.notNull(objectMapper, "ObjectMapper must not be null!");
		Assert.notNull(arguments, "AddCommandArguments must not be null!");
		if (!dataTypeManager.isSupportedDataType(dataType)){
			throw new DataImportException(String.format("Data type is not supported in the current context: %s", dataType));
		}
		RecordProcessor dataTypeProcessor = dataTypeManager.getProcessorByDataType(dataType);
		List<Model> records = new ArrayList<>();
		for (String s: recordStrings){
			try {
				records.add((Model) objectMapper.readValue(s, dataTypeProcessor.getModel()));
			} catch (IOException e){
				e.printStackTrace();
				if (!arguments.isSkipInvalidRecords()){
					throw new DataImportException(e.getMessage());
				}
			}
		}
		 GenericRecordProcessor processor = new GenericRecordProcessor();
		 processor.setModel(dataTypeProcessor.getModel());
		 processor.setReader(new RecordCollectionReader<>(records));
		 processor.setValidator(dataTypeProcessor.getValidator());
		 processor.setWriter(dataTypeProcessor.getWriter());
		 processor.setImporter(dataTypeProcessor.getImporter());
		 processor.setImportOptions(dataTypeProcessor.getImportOptions());
		 processor.run(null);
	}


	public DataTypeManager getDataTypeManager() {
		return dataTypeManager;
	}

	public void setDataTypeManager(
			DataTypeManager dataTypeManager) {
		this.dataTypeManager = dataTypeManager;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public AddCommandArguments getArguments() {
		return arguments;
	}

	public void setArguments(
			AddCommandArguments arguments) {
		this.arguments = arguments;
	}
}
