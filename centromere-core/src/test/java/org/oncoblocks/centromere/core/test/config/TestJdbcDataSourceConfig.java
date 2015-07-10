package org.oncoblocks.centromere.core.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * @author woemler
 */

@Configuration
public class TestJdbcDataSourceConfig {
	
	@Bean(destroyMethod = "shutdown")
	public DataSource dataSource(){
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript("test-schema.sql")
				.build();
	}
	
}
