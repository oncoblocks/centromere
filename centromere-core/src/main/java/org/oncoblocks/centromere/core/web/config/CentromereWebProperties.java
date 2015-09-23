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

package org.oncoblocks.centromere.core.web.config;

/**
 * Bean for managing basic application parameters.
 * 
 * @author woemler
 */
public class CentromereWebProperties {
	
	private String apiRootUrl;

	public String getApiRootUrl() {
		return apiRootUrl;
	}
	
	public String getApiUrlRegex() {
		return apiRootUrl + "/.*";
	}

	public String getApiUrlAntMatcher() {
		return apiRootUrl + "/**";
	}

	public void setApiRootUrl(String apiRootUrl) {
		this.apiRootUrl = apiRootUrl;
	}
}
