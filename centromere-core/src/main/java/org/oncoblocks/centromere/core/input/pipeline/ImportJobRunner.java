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

import org.oncoblocks.centromere.core.input.DataImportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author woemler
 */
public class ImportJobRunner implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	private static final Logger logger = LoggerFactory.getLogger(ImportJobRunner.class);

	public void parseJobDefinition(String inputFile){
		logger.info(String.format("[CENTROMERE] Beginning data import using job configuration file: %s", inputFile));
		Calendar calendar = Calendar.getInstance();
		Date start = calendar.getTime();
		ImportJob job = null;
		// TODO: Allow other input file formats, like YAML
		try {
			job = new JsonJobFileParser().parseJobFile(inputFile);
			
		} catch (DataImportException e){
			e.printStackTrace();
			logger.error("[CENTROMERE] Data import failed: " + e.getMessage());
		}
		Date end = calendar.getTime();
		long elapsed = end.getTime() - start.getTime();
		logger.info(String.format("[CENTROMERE] Data import ended.  Elapsed time: %s", formatInterval(elapsed)));
	}
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override public void setApplicationContext(
			ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * From http://stackoverflow.com/a/6710604/1458983
	 * @param l
	 * @return
	 */
	private static String formatInterval(final long l) {
		final long hr = TimeUnit.MILLISECONDS.toHours(l);
		final long min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr));
		final long sec = TimeUnit.MILLISECONDS.toSeconds(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
		final long ms = TimeUnit.MILLISECONDS.toMillis(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec));
		return String.format("%02d:%02d:%02d.%03d", hr, min, sec, ms);
	}

}
