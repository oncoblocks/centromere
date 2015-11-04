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

package org.oncoblocks.centromere.dataimport.config;

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

	public DataFileQueue(
			List<QueuedFile> queuedFiles) {
		this.queuedFiles = queuedFiles;
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
		return queuedFiles != null && queuedFiles.size() > 0;
	}
	
}
