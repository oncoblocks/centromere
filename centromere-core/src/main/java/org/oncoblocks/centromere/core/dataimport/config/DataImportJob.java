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

package org.oncoblocks.centromere.core.dataimport.config;

import org.oncoblocks.centromere.core.dataimport.processor.GeneralFileProcessor;
import org.oncoblocks.centromere.core.model.support.DataFileMetadata;
import org.oncoblocks.centromere.core.model.support.DataSetMetadata;
import org.oncoblocks.centromere.core.repository.support.DataFileRepositoryOperations;
import org.oncoblocks.centromere.core.repository.support.DataSetRepositoryOperations;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author woemler
 */
public class DataImportJob {

	private DataImportOptions options;
	private DataFileQueue dataFileQueue;
	private DataFileRepositoryOperations dataFileRepository;
	private DataSetRepositoryOperations dataSetRepository;

	public DataImportJob(DataImportOptions options,
			DataFileQueue dataFileQueue,
			DataFileRepositoryOperations dataFileRepository,
			DataSetRepositoryOperations dataSetRepository) {
		this.options = options;
		this.dataFileQueue = dataFileQueue;
		this.dataFileRepository = dataFileRepository;
		this.dataSetRepository = dataSetRepository;
	}

	public void run() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date startDate = new Date();
		System.out.println("CENTROMERE: Beginning Data Import @ " + dateFormat.format(new Date()));
		
		while (dataFileQueue.hasNext()) {
			
			QueuedFile queuedFile = dataFileQueue.next();
			DataSetMetadata dataSetMetadata = queuedFile.getDataSet();

			System.out.println("CENTROMERE: Creating new data set record: " + dataSetMetadata.getName());
			if (dataSetMetadata.getId() == null || !dataSetRepository.exists(dataSetMetadata.getId())) {
				dataSetMetadata = (DataSetMetadata) dataSetRepository.insert(dataSetMetadata);
			} else {
				System.out.println("CENTROMERE: Data set already exists.");
			}

			DataFileMetadata dataFileMetadata = queuedFile.getDataFile();
			dataFileMetadata.setDataSetId(dataSetMetadata.getId());

			System.out.println("CENTROMERE: Creating new data file record: " + dataFileMetadata.getFilePath());
			if ((dataFileMetadata.getId() != null && dataFileRepository.exists(dataFileMetadata.getId()))
					|| dataFileRepository.getByFilePath(dataFileMetadata.getFilePath()) != null){
				if (options.isFailOnExistingFile()){
					throw new DataImportException("Data file already exists: " + dataFileMetadata.getFilePath());
				}
			} else {
				
				dataFileMetadata = (DataFileMetadata) dataFileRepository.insert(dataFileMetadata);
				GeneralFileProcessor processor = queuedFile.getProcessor();
				
				File inputFile = new File(dataFileMetadata.getFilePath());
				String tempFileName = inputFile.getName() + ".tmp";
				File tempFile = new File(options.getTempFileDirectory(), tempFileName);
				
				System.out.println("CENTROMERE: Processing file " + dataFileMetadata.getFilePath());
				Date fileStart = new Date();
				long count = processor.run(dataFileMetadata.getFilePath(), tempFile.getAbsolutePath(), 
						dataSetMetadata.getId(), dataFileMetadata.getId()); 
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
