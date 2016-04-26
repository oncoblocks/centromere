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

package org.oncoblocks.centromere.sql;

import org.oncoblocks.centromere.core.dataimport.DataImportException;
import org.oncoblocks.centromere.core.dataimport.AbstractRecordFileWriter;
import org.oncoblocks.centromere.core.model.Model;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Writes entities to a delimited-text file for import via mysqlimport utility.  It is important 
 *   that the order and type of the fields appearing in the model class match the order and type
 *   of the fields in the target database table.
 * 
 * @author woemler
 */
public class MySqlImportTempFileWriter<T extends Model<?>> extends AbstractRecordFileWriter<T> {
	
	private String delimiter = "\t";
	private String enclosedBy = "";
	private String escapedBy = "\\\\";
	private String terminatedBy = "\n";
	private List<String> ignoredFields = new ArrayList<>();

	public MySqlImportTempFileWriter() {
		super();
	}

	@Override 
	public void writeRecord(T record) throws DataImportException {
		FileWriter writer = this.getWriter();
		StringBuilder stringBuilder = new StringBuilder();
		try {
			boolean flag = false;
			for (Field field : record.getClass().getDeclaredFields()) {
				if (!ignoredFields.contains(field.getName())) {
					field.setAccessible(true);
					Object value = field.get(record) != null ? field.get(record) : null;
					if (value == null){
						value = "null";
					} else if (!"".equals(enclosedBy) && value instanceof String) {
						value = ((String) value).replaceAll(enclosedBy, escapedBy + enclosedBy);
					}
					if (flag)
						stringBuilder.append(delimiter);
					stringBuilder.append(enclosedBy)
							.append(value)
							.append(enclosedBy);
					flag = true;
				}
			}
		} catch (IllegalAccessException e){
			e.printStackTrace();
			throw new DataImportException(e.getMessage());
		}
		try {
			writer.write(stringBuilder.toString());
			writer.write(terminatedBy);
		} catch (IOException e){
			e.printStackTrace();
			throw new DataImportException(e.getMessage());
		}
	}

	public String getDelimiter() {
		return delimiter;
	}

	public MySqlImportTempFileWriter setDelimiter(String delimiter) {
		this.delimiter = delimiter;
		return this;
	}

	public String getEnclosedBy() {
		return enclosedBy;
	}

	public MySqlImportTempFileWriter setEnclosedBy(String enclosedBy) {
		this.enclosedBy = enclosedBy;
		return this;
	}

	public String getEscapedBy() {
		return escapedBy;
	}

	public MySqlImportTempFileWriter setEscapedBy(String escapedBy) {
		this.escapedBy = escapedBy;
		return this;
	}

	public String getTerminatedBy() {
		return terminatedBy;
	}

	public MySqlImportTempFileWriter setTerminatedBy(String terminatedBy) {
		this.terminatedBy = terminatedBy;
		return this;
	}

	public List<String> getIgnoredFields() {
		return ignoredFields;
	}

	public MySqlImportTempFileWriter setIgnoredFields(List<String> ignoredFields) {
		this.ignoredFields = ignoredFields;
		return this;
	}
}
