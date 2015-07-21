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

package org.oncoblocks.centromere.core.test.web.service.remapping;

import org.oncoblocks.centromere.core.test.models.Subject;
import org.oncoblocks.centromere.core.test.repository.jdbc.SubjectRepository;
import org.oncoblocks.centromere.core.web.service.QueryParameterRemappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author woemler
 */

@Service
public class SubjectService extends QueryParameterRemappingService<Subject, Long> {
	
	@Autowired
	public SubjectService(SubjectRepository repository) {
		super(repository);
	}

	@Override 
	protected Map<String, String> getRemapping() {
		Map<String,String> map = new HashMap<>();
		map.put("subjectId", "subjects.subject_id");
		map.put("name", "subjects.name");
		map.put("gender", "subjects.gender");
		map.put("species", "subjects.species");
		map.put("attributeName", "subject_attributes.name");
		map.put("attributeValue", "subject_attributes.value");
		map.put("aliasName", "subject_alias.name");
		map.put("aliasSource", "subject_alias.source");
		return map;
	}
}
