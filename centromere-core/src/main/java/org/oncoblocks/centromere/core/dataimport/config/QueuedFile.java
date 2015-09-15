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

/**
 * @author woemler
 */
public class QueuedFile {
	
	private String filePath;
	private String type;
	private String notes;

	public QueuedFile(String filePath, String type, String notes) {
		this.filePath = filePath;
		this.type = type;
		this.notes = notes;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public static QueuedFile fromLine(String line){
		String[] bits = line.split("\\t");
		QueuedFile queuedFile = null;
		if (bits.length == 3){
			queuedFile = new QueuedFile(bits[0].trim(), bits[1].trim(), bits[2].trim());
		}
		return queuedFile;
	}
	
}
