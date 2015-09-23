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

package org.oncoblocks.centromere.core.web.query;

import org.oncoblocks.centromere.core.repository.QueryCriteria;

import java.util.List;

/**
 * Interface for {@link org.springframework.web.bind.annotation.ModelAttribute} objects to be used
 *   to define web service query parameters.  Query parameters can be mapped in any way, so long as
 *   they are convertable to {@link QueryCriteria} for repository queries.  The {@code QueryParams}
 *   object should also be able to remap any query string parameters whose names differ from their
 *   repository representation.
 * 
 * @author woemler
 */
public interface QueryParameters {

	/**
	 * Returns a list of {@link QueryCriteria} that are created based on request parameters. 
	 * 
	 * @return {@link QueryCriteria}
	 */
	List<QueryCriteria> getQueryCriteria();
	
	/**
	 * Remaps an input parameter name so that it matches the corresponding repository attribute name.
	 *   Useful for converting input parameters from page or sort requests.
	 * 
	 * @param name
	 * @return
	 */
	String remapParameterName(String name);
}
