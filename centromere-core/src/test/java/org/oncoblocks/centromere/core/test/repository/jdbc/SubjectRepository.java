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

package org.oncoblocks.centromere.core.test.repository.jdbc;

import com.nurkiewicz.jdbcrepository.RowUnmapper;
import org.oncoblocks.centromere.core.model.Attribute;
import org.oncoblocks.centromere.core.model.SourcedAlias;
import org.oncoblocks.centromere.core.repository.GenericJdbcRepository;
import org.oncoblocks.centromere.core.test.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author woemler
 */

@Repository
public class SubjectRepository extends GenericJdbcRepository<Subject, Long> {
	
	@Autowired
	public SubjectRepository(DataSource dataSource) {
		super(dataSource, Subject.getSubjectTableDescription(), new SubjectRowMapper(), new SubjectRowUnmapper());
	}
	
	public static class SubjectRowMapper implements RowMapper<Subject> {
		public Subject mapRow(ResultSet resultSet, int i) throws SQLException {
			
			Subject subject = new Subject();
			subject.setSubjectId(resultSet.getLong("subject_id"));
			subject.setName(resultSet.getString("name"));
			subject.setSpecies(resultSet.getString("species"));
			subject.setGender(resultSet.getString("gender"));
			subject.setNotes(resultSet.getString("notes"));

			List<SourcedAlias> aliases = new ArrayList<>();
			for (String alias: resultSet.getString("aliases").split("::")){
				String[] bits = alias.split(":");
				if (bits.length == 2) aliases.add(new SourcedAlias(bits[0], bits[1]));
			}
			subject.setAliases(aliases);
			
			List<Attribute> attributes = new ArrayList<>();
			for (String attribute: resultSet.getString("attributes").split("::")){
				String[] bits = attribute.split(":");
				if (bits.length == 2) attributes.add(new Attribute(bits[0], bits[1]));
			}
			subject.setAttributes(attributes);
			
			return subject;
			
		}
	}
	
	public static class SubjectRowUnmapper implements RowUnmapper<Subject> {
		@Override 
		public Map<String, Object> mapColumns(Subject subject) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("subject_id", subject.getSubjectId());
			map.put("name", subject.getName());
			map.put("species", subject.getSpecies());
			map.put("gender", subject.getGender());
			map.put("notes", subject.getNotes());
			return map;
		}
	}
	
}
