/*
 * Copyright 2015 William Oemler, Blueprint Medicines
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

package org.oncoblocks.centromere.core.dataimport.job;

import org.oncoblocks.centromere.core.dataimport.config.DataFileProcessorMapper;
import org.oncoblocks.centromere.core.dataimport.config.DataFileQueue;
import org.oncoblocks.centromere.core.dataimport.config.JobConfiguration;
import org.oncoblocks.centromere.core.dataimport.config.QueuedFile;
import org.oncoblocks.centromere.core.model.impl.DataFileDto;
import org.oncoblocks.centromere.core.model.impl.DataSetDto;
import org.oncoblocks.centromere.core.repository.impl.DataFileRepositoryOperations;
import org.oncoblocks.centromere.core.repository.impl.DataSetRepositoryOperations;
import org.springframework.util.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author woemler
 */
public class DataImportJob {

	private JobConfiguration config;
	private DataFileQueue dataFileQueue;
	private DataFileProcessorMapper processorMapper;
	private DataFileRepositoryOperations dataFileRepository;
	private DataSetRepositoryOperations dataSetRepository;
	private DataSetDto dataSet;

	public DataImportJob(JobConfiguration jobConfiguration, DataFileQueue dataFileQueue, 
			DataFileProcessorMapper processorMapper) {
		this.config = jobConfiguration;
		Assert.notNull(jobConfiguration);
		Assert.notNull(dataFileQueue);
		Assert.notNull(processorMapper);
		Assert.notNull(jobConfiguration.getDataFileRepository());
		Assert.notNull(jobConfiguration.getDataSetRepository());
		this.dataFileQueue = dataFileQueue;
		this.processorMapper = processorMapper;
		this.dataFileRepository = jobConfiguration.getDataFileRepository();
		this.dataSetRepository = jobConfiguration.getDataSetRepository();
		this.dataSet = jobConfiguration.getDataSet();
	}

	public void run() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date startDate = new Date();
		System.out.println("CENTROMERE: Beginning Data Import @ " + dateFormat.format(new Date()));

		if (dataSet != null) {
			System.out.println("CENTROMERE: Creating new data set record: " + dataSet.getName());
			if (dataSetRepository.getByName(dataSet.getName()) != null) {
				if (config.isFailOnExistingDataSet()){
					throw new DataImportException(
							String.format("Data set already exists: %s", dataSet.getName()));
				} else {
					System.out.println("CENTROMERE: Data set already exists.  Aborting import.");
					return;
				}
			} else {
				dataSet = (DataSetDto) dataSetRepository.insert(dataSet);
			}
		}

		while (dataFileQueue.hasNext()) {

			QueuedFile queuedFile = dataFileQueue.next();
			String inputFilePath = queuedFile.getFilePath();
			String tempFilePath = null;
			String fileType = queuedFile.getType();
			String fileNotes = queuedFile.getNotes();
			Object dataFileId = null;
			boolean isValid = true;

			if (dataSet.getId() != null) {
				if (dataFileRepository.getFileByPath(inputFilePath) != null){
					if (config.isFailOnExistingFile()){
						throw new DataImportException("Data file already exists: " + inputFilePath);
					} else {
						isValid = false;
					}
				} else {
					DataFileDto dataFile = new DataFileDto<>(null, dataSet.getId(), inputFilePath,
							fileType, new Date(), fileNotes);
					dataFile = (DataFileDto) dataFileRepository.insert(dataFile);
					dataFileId = dataFile.getId();
				}
			}

			DataFileProcessor<?> processor = processorMapper.getDataFileProcessor(fileType);
			if (processor == null) {
				if (config.isFailOnMissingFileType()) {
					throw new DataImportException(String.format("Invalid data file type, '%s' for file: %s",
							fileType, inputFilePath));
				} else {
					System.out.println("Skipping unsupported file type: " + fileType);
					isValid = false;
				}
			}

			if (isValid) {
				System.out.println("CENTROMERE: Processing file " + inputFilePath);
				Date fileStart = new Date();
				long count = processor.run(inputFilePath, tempFilePath, dataFileId);
				Date fileEnd = new Date();
				System.out.println("CENTROMERE: Done.  Created " + count + " records.  Elapsed time: "
						+ (fileEnd.getTime() - fileStart.getTime()) + " ms");
			}
				
		}

		Date endDate = new Date();
		System.out.println("CENTROMERE: Data Import Complete @ " + dateFormat.format(new Date())
				+ "  Elapsed time:  " + (endDate.getTime() - startDate.getTime()) + " ms");

	}

}
