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

import org.oncoblocks.centromere.core.input.importer.RecordImporter;
import org.oncoblocks.centromere.core.input.processor.RecordProcessor;
import org.oncoblocks.centromere.core.input.reader.RecordReader;
import org.oncoblocks.centromere.core.input.writer.RecordWriter;
import org.oncoblocks.centromere.core.model.Model;
import org.springframework.validation.Validator;

/**
 * Metadata POJO used to construct a {@link RecordProcessor}.
 * 
 * @author woemler
 */
public class ProcessorConfig {

	private Class<? extends Model> model;
	private Class<? extends RecordReader> reader;
	private Class<? extends Validator> validator;
	private Class<? extends RecordWriter> writer;
	private Class<? extends RecordImporter> importer;
	private Class<? extends RecordProcessor> processor;

	public Class<? extends Model> getModel() {
		return model;
	}

	public void setModel(Class<? extends Model> model) {
		this.model = model;
	}

	public Class<? extends RecordReader> getReader() {
		return reader;
	}

	public void setReader(
			Class<? extends RecordReader> reader) {
		this.reader = reader;
	}

	public Class<? extends Validator> getValidator() {
		return validator;
	}

	public void setValidator(
			Class<? extends Validator> validator) {
		this.validator = validator;
	}

	public Class<? extends RecordWriter> getWriter() {
		return writer;
	}

	public void setWriter(
			Class<? extends RecordWriter> writer) {
		this.writer = writer;
	}

	public Class<? extends RecordImporter> getImporter() {
		return importer;
	}

	public void setImporter(
			Class<? extends RecordImporter> importer) {
		this.importer = importer;
	}

	public Class<? extends RecordProcessor> getProcessor() {
		return processor;
	}

	public void setProcessor(
			Class<? extends RecordProcessor> processor) {
		this.processor = processor;
	}
}
