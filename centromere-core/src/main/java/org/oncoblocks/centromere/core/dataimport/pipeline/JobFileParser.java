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

package org.oncoblocks.centromere.core.dataimport.pipeline;

import org.oncoblocks.centromere.core.dataimport.component.DataImportException;

/**
 * Interface for classes that parse a job-description file and output an {@link ImportJob} object.
 * 
 * @author woemler
 */
@Deprecated
public interface JobFileParser {
	/**
	 * Reads an input file and converts its contents to an {@link ImportJob} object.
	 * @param inputPath Path to the job description file.
	 * @return {@link ImportJob}
	 * @throws DataImportException
	 */
	ImportJob parseJobFile(String inputPath) throws DataImportException;
}
