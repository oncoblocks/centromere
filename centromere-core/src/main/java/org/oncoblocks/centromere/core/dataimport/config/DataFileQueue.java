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

import org.oncoblocks.centromere.core.dataimport.job.DataFileProcessingException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author woemler
 */
public class DataFileQueue {
	
	private List<QueuedFile> queuedFiles;

	public DataFileQueue() {
		queuedFiles = new ArrayList<>();
	}

	public DataFileQueue(String filePath){
		queuedFiles = new ArrayList<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line = reader.readLine();
			if (line.startsWith("path\ttype")){
				line = reader.readLine();
			}
			while (line != null){
				if (!line.trim().equals("")){
					QueuedFile queuedFile = QueuedFile.fromLine(line);
					if (queuedFile != null){
						queuedFiles.add(queuedFile);
					}
				}
				line = reader.readLine();
			}
		} catch (Exception e){
			e.printStackTrace();
			throw new DataFileProcessingException(String.format("There was a problem reading the data file list file: %s", filePath));
		} finally {
			if (reader != null){
				try {
					reader.close();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public void addQueuedFile(QueuedFile queuedFile){
		queuedFiles.add(queuedFile);
	}
	
	public QueuedFile next(){
		try {
			return queuedFiles.remove(0);
		} catch (IndexOutOfBoundsException e){
			return null;
		}
	}
	
	public boolean hasNext(){
		return queuedFiles.size() > 0;
	}
	
}
