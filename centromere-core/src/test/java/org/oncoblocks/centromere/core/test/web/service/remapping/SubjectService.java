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
