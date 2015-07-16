package org.oncoblocks.centromere.core.test.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author woemler
 */

@Configuration
@PropertySource({ "classpath:test-mongo-data-source.properties" })
public class TestMongoConfig extends AbstractMongoConfiguration {

	@Autowired
	Environment env;

	@Override
	public String getDatabaseName(){
		return env.getRequiredProperty("mongo.name");
	}

	@Override
	@Bean
	public Mongo mongo() throws Exception {
		ServerAddress serverAddress = new ServerAddress(env.getRequiredProperty("mongo.host"));
		List<MongoCredential> credentials = new ArrayList<>();
		return new MongoClient(serverAddress, credentials);
	}
	
}