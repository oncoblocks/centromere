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

package org.oncoblocks.centromere.dataimport.support;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;

/**
 * @author woemler
 */

public class MongoDbUtils {
	
	private final MongoTemplate mongoTemplate;

	public MongoDbUtils(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public String convertEntityToJson(Object entity){
		MongoConverter converter = mongoTemplate.getConverter();
		DBObject dbObject = new BasicDBObject();
		converter.write(entity, dbObject);
		if (dbObject.containsField("_id") && dbObject.get("_id") == null){
			dbObject.removeField("_id");
		}
		if (dbObject.containsField("_class")){
			dbObject.removeField("_class");
		}
		return dbObject.toString();
	}
	
}
