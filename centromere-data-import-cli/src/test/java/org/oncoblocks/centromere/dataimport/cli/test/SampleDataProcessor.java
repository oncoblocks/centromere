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

import org.oncoblocks.centromere.core.dataimport.*;
import org.oncoblocks.centromere.core.model.support.BasicDataFileMetadata;
import org.oncoblocks.centromere.core.model.support.BasicDataSetMetadata;
import org.oncoblocks.centromere.core.model.support.DataFileMetadata;
import org.oncoblocks.centromere.core.model.support.DataSetMetadata;
import org.oncoblocks.centromere.dataimport.cli.test.support.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * @author woemler
 */
@Component
@DataTypes({"sample_data"})
public class SampleDataProcessor extends GenericRecordProcessor<SampleData> {
	
	private final DataFileRepository dataFileRepository;
	private final DataSetRepository dataSetRepository;
	private final SampleDataRepository sampleDataRepository;

	@Autowired
	public SampleDataProcessor(SampleDataRepository repository, DataFileRepository dataFileRepository,
			DataSetRepository dataSetRepository) {
		super(SampleData.class, new SampleDataReader(), new SampleDataValidator(), 
				new RepositoryRecordWriter<>(repository));
		this.sampleDataRepository = repository;
		this.dataFileRepository = dataFileRepository;
		this.dataSetRepository = dataSetRepository;
	}

	@Override 
	public void doBefore() {
		BasicDataSetMetadata dataSetMetadata = (BasicDataSetMetadata) this.getDataSet();
		BasicDataFileMetadata dataFileMetadata = (BasicDataFileMetadata) this.getDataFile();
		DataSet dataSet = this.getDataSet(dataSetMetadata);
		this.setDataFileMetadata(this.getDataFile(dataFileMetadata, dataSet));
		super.doBefore();
	}
	
	private DataSet getDataSet(DataSetMetadata dataSetMetadata){
		DataSet dataSet = null;
		List<DataSet> dataSets = dataSetRepository.getByLabel(dataSetMetadata.getLabel());
		if (dataSets == null || dataSets.isEmpty()){
			dataSet = new DataSet();
			dataSet.setLabel(dataSetMetadata.getLabel());
			dataSet.setSource(dataSetMetadata.getSource());
			dataSet.setName(dataSetMetadata.getName());
			dataSet = dataSetRepository.insert(dataSet);
		} else {
			dataSet = dataSets.get(0);
		}
		return dataSet;
	}
	
	private DataFile getDataFile(DataFileMetadata dataFileMetadata, DataSet dataSet){
		DataFile dataFile = null;
		List<DataFile> dataFiles = dataFileRepository.getByFilePath(dataFileMetadata.getFilePath());
		if (dataFiles == null || dataFiles.isEmpty()){
			dataFile = new DataFile();
			dataFile.setFilePath(dataFileMetadata.getFilePath());
			dataFile.setDataType(dataFileMetadata.getDataType());
			dataFile.setDataSetId(dataSet.getId());
			dataFile = dataFileRepository.insert(dataFile);
		} else {
			dataFile = dataFiles.get(0);
		}
		return dataFile;
	}

	// Reader
	public static class SampleDataReader extends RecordCollectionReader<SampleData> 
			implements DataFileAware {
		
		private DataFile dataFile;
		
		public SampleDataReader() {
			super(SampleData.createSampleData());
		}

		@Override 
		public SampleData readRecord() throws DataImportException {
			SampleData data = super.readRecord();
			if (data != null){
				data.setDataFileId(dataFile.getId());
			}
			return data;
		}

		public void setDataFileMetadata(DataFileMetadata dataFile) {
			this.dataFile = (DataFile) dataFile;
		}
	}
	
	// Validator
	public static class SampleDataValidator implements Validator {

		public boolean supports(Class<?> aClass) {
			return SampleData.class.equals(aClass);
		}

		public void validate(Object o, Errors errors) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dataFileId", "dataFileId.empty");
		}
	}
	
}
